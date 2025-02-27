package br.com.guifroes1984.api.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
