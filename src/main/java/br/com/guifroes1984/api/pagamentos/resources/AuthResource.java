package br.com.guifroes1984.api.pagamentos.resources;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.guifroes1984.api.pagamentos.dto.EsqueciSenhaDTO;
import br.com.guifroes1984.api.pagamentos.dto.ResetarSenhaDTO;
import br.com.guifroes1984.api.pagamentos.mail.Mailer;
import br.com.guifroes1984.api.pagamentos.model.PasswordResetToken;
import br.com.guifroes1984.api.pagamentos.model.Usuario;
import br.com.guifroes1984.api.pagamentos.repository.PasswordResetTokenRepository;
import br.com.guifroes1984.api.pagamentos.repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthResource {
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping("/esqueci-senha")
	public ResponseEntity<Void> esqueciSenha(@RequestBody EsqueciSenhaDTO esqueciSenhaDTO) {
		mailer.solicitarResetDeSenha(esqueciSenhaDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/resetar-senha")
	public ResponseEntity<Void> resetarSenha(@RequestBody ResetarSenhaDTO resetarSenhaDTO) {
		PasswordResetToken token = tokenRepository.findByToken(resetarSenhaDTO.getToken())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido."));
		
		if (token.getExpiracao().isBefore(LocalDateTime.now())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado.");
		}
		
		Usuario usuario = token.getUsuario();
		usuario.setSenha(passwordEncoder.encode(resetarSenhaDTO.getNovaSenha()));
		
		usuarioRepository.save(usuario);
		tokenRepository.delete(token);
		
		return ResponseEntity.noContent().build();
	}

}
