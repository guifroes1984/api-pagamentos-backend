package br.com.guifroes1984.api.pagamentos.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.model.Estado;
import br.com.guifroes1984.api.pagamentos.repository.EstadoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/estados")
@Api(value = "Estado", description = "Operações relacionadas aos estados")
public class EstadoResource {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "Endpoint que retorna a lista de todos os estados", response = List.class)
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}

}
