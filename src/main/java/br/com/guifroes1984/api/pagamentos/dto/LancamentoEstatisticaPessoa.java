package br.com.guifroes1984.api.pagamentos.dto;

import java.math.BigDecimal;

import br.com.guifroes1984.api.pagamentos.model.Pessoa;
import br.com.guifroes1984.api.pagamentos.model.TipoLancamento;

public class LancamentoEstatisticaPessoa {

	private TipoLancamento tipo;

	private Pessoa pessoa;

	private BigDecimal total;

	public LancamentoEstatisticaPessoa(TipoLancamento tipo, Pessoa pessoa, BigDecimal total) {
		this.tipo = tipo;
		this.pessoa = pessoa;
		this.total = total;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
