package br.com.guifroes1984.api.pagamentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.guifroes1984.api.pagamentos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByEmail(String email);
	
	public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
	
	@Query("SELECT MAX(u.codigo) FROM Usuario u")
	Optional<Long> findMaxCodigo();

}
