package br.com.guifroes1984.api.pagamentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
	
	Optional<Permissao> findByDescricao(String descricao);
	
	List<Permissao> findByDescricao(List<String> descricoes);

}
