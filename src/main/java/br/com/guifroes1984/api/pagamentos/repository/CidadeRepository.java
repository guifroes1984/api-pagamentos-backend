package br.com.guifroes1984.api.pagamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
	List<Cidade> findByEstadoCodigo(Long estadoCodigo);
	
}
