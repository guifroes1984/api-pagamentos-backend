package br.com.guifroes1984.api.pagamentos.repository.lancamento;

import java.util.List;

import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);

}
