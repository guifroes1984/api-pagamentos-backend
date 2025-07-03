package br.com.guifroes1984.api.pagamentos.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.model.Usuario;
import br.com.guifroes1984.api.pagamentos.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
@Api(value = "Usuario", description = "Operações relacionadas aos cadastro de usuários")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService usuarioService;

	
	@PostMapping
	@ApiOperation(value = "Cadastra um novo usuário")
	public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
		Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}

}
