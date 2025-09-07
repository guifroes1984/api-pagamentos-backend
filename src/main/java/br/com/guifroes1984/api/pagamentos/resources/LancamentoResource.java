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
import br.com.guifroes1984.api.pagamentos.model.Categoria;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.LancamentoRepository;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;
import br.com.guifroes1984.api.pagamentos.repository.projection.ResumoLancamento;
import br.com.guifroes1984.api.pagamentos.service.LancamentoService;
import br.com.guifroes1984.api.pagamentos.service.exception.PessoaInexistenteOuInativaException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lancamentos")
@Api(value = "Lançamentos", description = "Operações relacionadas de lançamentos financeiros")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping(value = "/com-anexo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(value = "Endpoint para cadastrar lançamento com anexo", notes = "Faz o upload de um arquivo (ex: comprovante) e retorna o objeto salvo com informações do anexo.")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criarComAnexo(@RequestPart("lancamento") String lancamentoJson,
			@RequestPart("file") MultipartFile file, HttpServletResponse response) throws IOException {

		Lancamento lancamento = objectMapper.readValue(lancamentoJson, Lancamento.class);

		Lancamento lancamentoSalvo = lancamentoService.salvarComAnexo(lancamento, file);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@GetMapping("/{codigo}/anexo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
	@ApiOperation(value = "Endpoint que faz download do anexo de um lançamento")
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
	@ApiOperation(value = "Endpoint que gera relatório em PDF dos lançamentos por pessoa", notes = "Gera um relatório consolidado de lançamentos financeiros agrupados por pessoa dentro de um intervalo de datas. O retorno é um arquivo PDF.", produces = MediaType.APPLICATION_PDF_VALUE)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {

		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf").body(relatorio);
	}

	@GetMapping(value = "/estatisticas/por-dia", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Endpoint para obter estatísticas de lançamentos por dia", response = List.class)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaDia> porDia(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

		LocalDate hoje = LocalDate.now();
		LocalDate inicio = (dataInicio != null) ? dataInicio : hoje.withDayOfMonth(1);
		LocalDate fim = (dataFim != null) ? dataFim : hoje.withDayOfMonth(hoje.lengthOfMonth());

		return this.lancamentoRepository.porDia(inicio, fim);
	}

	@GetMapping(value = "/estatisticas/por-categoria", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Endpoint para obter estatísticas de lançamentos por categoria", response = List.class)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<LancamentoEstatisticaCategoria> porCategoria(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,

			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

		if (dataInicio == null) {
			dataInicio = LocalDate.now().withDayOfMonth(1);
		}
		if (dataFim == null) {
			dataFim = LocalDate.now();
		}

		return this.lancamentoRepository.porCategoria(dataInicio, dataFim);
	}

	@GetMapping
	@ApiOperation(value = "Endpoint para pesquisar todos os lançamentos", response = List.class)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@ApiOperation(value = "Endpoint para pesquisar todos os lançamentos (Resumo)", response = List.class)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	@ApiOperation(value = "Endpoint para buscar um lançamento pelo código", response = Categoria.class)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@ApiOperation(value = "Endpoint para adicionar um novo lançamento")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Endpoint para remover um lançamento pelo código", notes = "Exclui o lançamento do banco de dados pelo código informado.")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}

	@PutMapping(value = "{codigo}/anexo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	@ApiOperation(value = "Endpoint para atualiza apenas o anexo de um lançamento")
	public ResponseEntity<?> atualizarAnexo(@PathVariable Long codigo, @RequestPart("file") MultipartFile file) {

		try {
			Lancamento lancamentoAtualizado = lancamentoService.atualizarAnexo(codigo, file);
			return ResponseEntity.ok(lancamentoAtualizado);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Erro ao processar o anexo: " + e.getMessage());
		}
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	@ApiOperation(value = "Atualiza os dados de um lançamento")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		lancamento.setCodigo(codigo);
		Lancamento lancamentoAtualizado = lancamentoService.atualizar(lancamento);
		return ResponseEntity.ok(lancamentoAtualizado);
	}

	@DeleteMapping("/{codigo}/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	@ApiOperation(value = "Endpoint que remove o anexo de um lançamento")
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
