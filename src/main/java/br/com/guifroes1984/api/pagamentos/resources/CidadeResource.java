package br.com.guifroes1984.api.pagamentos.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.model.Cidade;
import br.com.guifroes1984.api.pagamentos.repository.CidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cidades")
@Tag(name = "Estado", description = "Operações relacionadas aos cidades")
public class CidadeResource {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	@Operation(summary = "Endpoint que retorna a lista de todos as cidades")
	public List<Cidade> pesquisar(@RequestParam Long estado) {
		return cidadeRepository.findByEstadoCodigo(estado);
	}

}
