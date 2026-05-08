package br.com.guifroes1984.api.pagamentos.resources;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.event.RecursoCriadoEvent;
import br.com.guifroes1984.api.pagamentos.model.Categoria;
import br.com.guifroes1984.api.pagamentos.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria", description = "Operações relacionadas às categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@Operation(summary = "Endpoint para listar todas as categorias")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@PostMapping
	@Operation(summary = "Endpoint para adicionar uma nova categoria")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public ResponseEntity<Categoria> adicionar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	@GetMapping("/{codigo}")
	@Operation(summary = "Endpoint para buscar uma categoria pelo código")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{codigo}")
	@Operation(summary = "Endpoint para atualizar uma categoria")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria) {
		Optional<Categoria> categoriaOptional = categoriaRepository.findById(codigo);

		if (!categoriaOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Categoria categoriaSalva = categoriaOptional.get();
		categoriaSalva.setNome(categoria.getNome());
		categoriaRepository.save(categoriaSalva);

		return ResponseEntity.ok(categoriaSalva);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Endpoint para remover uma categoria")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.deleteById(codigo);
	}

}
