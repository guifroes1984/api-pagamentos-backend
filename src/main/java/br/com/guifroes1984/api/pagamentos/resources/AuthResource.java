package br.com.guifroes1984.api.pagamentos.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.dto.EsqueciSenhaDTO;
import br.com.guifroes1984.api.pagamentos.mail.Mailer;

@RestController
@RequestMapping("/auth")
public class AuthResource {
	
	@Autowired
	private Mailer mailer;
	
	@PostMapping("/esqueci-senha")
	public ResponseEntity<Void> esqueciSenha(@RequestBody EsqueciSenhaDTO esqueciSenhaDTO) {
		mailer.solicitarResetDeSenha(esqueciSenhaDTO.getEmail());
		return ResponseEntity.noContent().build();
	}

}
