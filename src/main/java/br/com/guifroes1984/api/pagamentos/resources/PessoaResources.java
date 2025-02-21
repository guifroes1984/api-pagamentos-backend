package br.com.guifroes1984.api.pagamentos.resources;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.guifroes1984.api.pagamentos.model.Categoria;
import br.com.guifroes1984.api.pagamentos.model.Pessoa;
import br.com.guifroes1984.api.pagamentos.repository.PessoaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pessoas")
@Api(value = "Pessoa", description = "Operações relacionadas às pessoas")
public class PessoaResources {

	@Autowired
	private PessoaRepository pessoaRepository;

	@GetMapping
	@ApiOperation(value = "Lista todas as pessoas", response = List.class)
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	@PostMapping
	@ApiOperation(value = "Adiciona uma nova pessoa")
	public ResponseEntity<Pessoa> adicionar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(pessoaSalva.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());

		return ResponseEntity.created(uri).body(pessoaSalva);
	}
	
	@GetMapping("/{codigo}")
	@ApiOperation(value = "Busca uma pessoa pelo código", response = Categoria.class)
	public Pessoa buscarPeloCodigo(@PathVariable Long codigo) {
		return pessoaRepository.findOne(codigo);
	}

}
