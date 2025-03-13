package br.com.guifroes1984.api.pagamentos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	public Page<Pessoa> findByNomeContaining(String nome, Pageable pePageable);

}
