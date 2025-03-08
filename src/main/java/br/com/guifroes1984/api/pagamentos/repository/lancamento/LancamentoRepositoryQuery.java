package br.com.guifroes1984.api.pagamentos.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;
import br.com.guifroes1984.api.pagamentos.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

}
