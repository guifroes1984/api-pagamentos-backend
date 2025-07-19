package br.com.guifroes1984.api.pagamentos.dto;

public class ResetarSenhaDTO {

	private String token;

	private String novaSenha;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

}
