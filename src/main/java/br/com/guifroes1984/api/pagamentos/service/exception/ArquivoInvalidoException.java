package br.com.guifroes1984.api.pagamentos.service.exception;

public class ArquivoInvalidoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ArquivoInvalidoException(String mensagem) {
		super(mensagem);
	}
}
