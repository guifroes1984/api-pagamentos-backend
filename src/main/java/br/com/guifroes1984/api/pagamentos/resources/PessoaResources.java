package br.com.guifroes1984.api.pagamentos.resources;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.event.RecursoCriadoEvent;
import br.com.guifroes1984.api.pagamentos.model.Pessoa;
import br.com.guifroes1984.api.pagamentos.repository.PessoaRepository;
import br.com.guifroes1984.api.pagamentos.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoa", description = "Operações relacionadas às pessoas")
public class PessoaResources {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	@Operation(summary = "Endpoint para listar todas as pessoas com filtro opcional por nome")
	public Page<Pessoa> pequisar(@RequestParam(required = false, defaultValue = "%") String nome, Pageable pePageable) {
		return pessoaRepository.findByNomeContaining(nome, pePageable);
	}

	@PostMapping
	@Operation(summary = "Endpoint para adicionar uma nova pessoa")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public ResponseEntity<Pessoa> adicionar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaService.salvar(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

	@GetMapping("/{codigo}")
	@Operation(summary = "Endpoint para buscar uma pessoa pelo código")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
		return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Endpoint para remover uma pessoa pelo código", description = "Exclui a pessoa do banco de dados pelo código informado.")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	@Operation(summary = "Endpoint para atualizar uma pessoa pelo código", description = "Atualiza os dados de uma pessoa no banco de dados.")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Endpoint para atualizar a propriedade 'ativo' de uma pessoa")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}

}
