package com.guifroes1984.pagamentos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.pagamentos.dto.CategoriaRequest;
import com.guifroes1984.pagamentos.entities.Categoria;
import com.guifroes1984.pagamentos.repository.CategoriaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	@Operation(summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica pelo seu ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada") })
	public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		return categoria.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Cadastrar nova categoria", description = "Adiciona uma nova categoria ao sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<Categoria> adicionar(@RequestBody CategoriaRequest request) {
		Categoria categoria = new Categoria();
		categoria.setNome(request.getNome());
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		return ResponseEntity.ok(categoriaSalva);
	}

}
