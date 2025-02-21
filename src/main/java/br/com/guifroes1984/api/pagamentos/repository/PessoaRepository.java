package br.com.guifroes1984.api.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
