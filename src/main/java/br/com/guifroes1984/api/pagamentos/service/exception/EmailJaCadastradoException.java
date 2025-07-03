package br.com.guifroes1984.api.pagamentos.service.exception;

public class EmailJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmailJaCadastradoException(String message) {
        super(message);
    }

}
