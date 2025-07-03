package br.com.guifroes1984.api.pagamentos.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.guifroes1984.api.pagamentos.model.Usuario;
import br.com.guifroes1984.api.pagamentos.repository.UsuarioRepository;
import br.com.guifroes1984.api.pagamentos.service.exception.EmailJaCadastradoException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("E-mail já cadastrado.");
        }

        Long novoCodigo = usuarioRepository.findMaxCodigo().orElse(0L) + 1;
        usuario.setCodigo(novoCodigo);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario usuarioBase = usuarioRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Usuário base não encontrado"));
        usuario.setPermissoes(new ArrayList<>(usuarioBase.getPermissoes()));

        return usuarioRepository.save(usuario);
    }
}

