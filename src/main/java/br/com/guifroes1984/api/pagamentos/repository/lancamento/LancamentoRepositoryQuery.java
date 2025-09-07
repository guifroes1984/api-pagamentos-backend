package br.com.guifroes1984.api.pagamentos.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaCategoria;
import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaDia;
import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaPessoa;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;
import br.com.guifroes1984.api.pagamentos.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
    
    public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
    
    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate dataInicio, LocalDate dataFim);
    
    public List<LancamentoEstatisticaDia> porDia(LocalDate dataInicio, LocalDate dataFim);
    
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}

