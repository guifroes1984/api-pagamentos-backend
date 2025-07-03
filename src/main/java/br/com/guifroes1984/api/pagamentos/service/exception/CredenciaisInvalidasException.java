package br.com.guifroes1984.api.pagamentos.service.exception;

public class CredenciaisInvalidasException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CredenciaisInvalidasException(String message) {
        super(message);
    }
}
