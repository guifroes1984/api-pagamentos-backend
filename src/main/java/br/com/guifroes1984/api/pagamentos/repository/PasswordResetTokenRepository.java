package br.com.guifroes1984.api.pagamentos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.PasswordResetToken;
import br.com.guifroes1984.api.pagamentos.model.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	Optional<PasswordResetToken> findByToken(String token);
	
	void deleteByUsuario(Usuario usuario);

}
