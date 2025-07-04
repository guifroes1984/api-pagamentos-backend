package br.com.guifroes1984.api.pagamentos.security.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.guifroes1984.api.pagamentos.model.Usuario;

public class UsuarioSistema extends User {
	
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> permissoes) {
        super(usuario.getEmail(), usuario.getSenha(), permissoes);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
