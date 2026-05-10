package br.com.guifroes1984.api.pagamentos.resources;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaCategoria;
import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaDia;
import br.com.guifroes1984.api.pagamentos.event.RecursoCriadoEvent;
import br.com.guifroes1984.api.pagamentos.exceptionhandler.ExceptionHandler.Erro;
import br.com.guifroes1984.api.pagamentos.model.Anexo;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.LancamentoRepository;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;
import br.com.guifroes1984.api.pagamentos.repository.projection.ResumoLancamento;
import br.com.guifroes1984.api.pagamentos.service.LancamentoService;
import br.com.guifroes1984.api.pagamentos.service.exception.PessoaInexistenteOuInativaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lancamentos")
@Tag(name = "Lançamentos", description = "Operações relacionadas aos lançamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MessageSource messageSource;

	@PostMapping(value = "/com-anexo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Cadastrar lançamento com anexo", description = "Faz upload de um arquivo e salva o lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> criarComAnexo(@RequestPart("lancamento") String lancamentoJson,
			@RequestPart("file") MultipartFile file, HttpServletResponse response) throws IOException {

		Lancamento lancamento = objectMapper.readValue(lancamentoJson, Lancamento.class);

		Lancamento lancamentoSalvo = lancamentoService.salvarComAnexo(lancamento, file);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@GetMapping("/{codigo}/anexo")
	@Operation(summary = "Download do anexo do lançamento")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<byte[]> downloadAnexo(@PathVariable Long codigo) {

		try {

			Anexo anexo = lancamentoService.buscarAnexoDoLancamento(codigo);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(anexo.getTipo()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNome() + "\"")
					.body(anexo.getDados());

		} catch (IllegalArgumentException e) {

			return ResponseEntity.notFound().build();

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/relatorios/por-pessoa")
	@Operation(summary = "Relatório PDF por pessoa", description = "Gera relatório consolidado de lançamentos em PDF")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<byte[]> relatorioPorPessoa(

			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,

			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim

	) throws Exception {

		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf").body(relatorio);
	}

	@GetMapping(value = "/estatisticas/por-dia", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Estatísticas de lançamentos por dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaDia> porDia(

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim

	) {

		LocalDate hoje = LocalDate.now();

		LocalDate inicio = (dataInicio != null) ? dataInicio : hoje.withDayOfMonth(1);

		LocalDate fim = (dataFim != null) ? dataFim : hoje.withDayOfMonth(hoje.lengthOfMonth());

		return this.lancamentoRepository.porDia(inicio, fim);
	}

	@GetMapping(value = "/estatisticas/por-categoria", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Estatísticas de lançamentos por categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public List<LancamentoEstatisticaCategoria> porCategoria(

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim

	) {

		if (dataInicio == null) {
			dataInicio = LocalDate.now().withDayOfMonth(1);
		}

		if (dataFim == null) {
			dataFim = LocalDate.now();
		}

		return this.lancamentoRepository.porCategoria(dataInicio, dataFim);
	}

	@GetMapping
	@Operation(summary = "Pesquisar lançamentos")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {

	    return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@Operation(summary = "Pesquisar lançamentos resumidos")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {

		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	@Operation(summary = "Buscar lançamento por código")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {

		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);

		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@Operation(summary = "Adicionar lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> criar(

			@Valid @RequestBody Lancamento lancamento, HttpServletResponse response

	) {

		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Remover lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public void remover(@PathVariable Long codigo) {

		lancamentoRepository.deleteById(codigo);
	}

	@PutMapping(value = "{codigo}/anexo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Atualizar anexo do lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<?> atualizarAnexo(

			@PathVariable Long codigo, @RequestPart("file") MultipartFile file

	) {

		try {

			Lancamento lancamentoAtualizado = lancamentoService.atualizarAnexo(codigo, file);

			return ResponseEntity.ok(lancamentoAtualizado);

		} catch (IOException e) {

			return ResponseEntity.badRequest().body("Erro ao processar o anexo: " + e.getMessage());
		}
	}

	@PutMapping("/{codigo}")
	@Operation(summary = "Atualizar lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> atualizar(

			@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento

	) {

		lancamento.setCodigo(codigo);

		Lancamento lancamentoAtualizado = lancamentoService.atualizar(lancamento);

		return ResponseEntity.ok(lancamentoAtualizado);
	}

	@DeleteMapping("/{codigo}/anexo")
	@Operation(summary = "Remover anexo do lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Void> deletarAnexo(@PathVariable Long codigo) {

		lancamentoService.removerAnexo(codigo);

		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());

		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return ResponseEntity.badRequest().body(erros);
	}
}
