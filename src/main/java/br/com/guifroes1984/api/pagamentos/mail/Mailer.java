package br.com.guifroes1984.api.pagamentos.mail;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.model.PasswordResetToken;
import br.com.guifroes1984.api.pagamentos.model.Usuario;
import br.com.guifroes1984.api.pagamentos.repository.PasswordResetTokenRepository;
import br.com.guifroes1984.api.pagamentos.repository.UsuarioRepository;

@Component
public class Mailer {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine thymeleaf;
	
	@Value("${frontend.reset-password-url}")
    private String resetPasswordUrl;

//	@Autowired
//	private LancamentoRepository repo;
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-lancamentos-vencidos";
//		
//		List<Lancamento> lista = repo.findAll();
//		
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("lancamentos", lista);
//		
//		this.enviarEmail("guilhermefroestestedeemail@gmail.com", 
//				Arrays.asList("guilhermefroes26@gmail.com"), 
//				"Testando", template, variaveis);
//		System.out.println("Terminado o envio de e-mail.");
//	}

	public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos, List<Usuario> destinatarios) {
		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("lancamentos", vencidos);

		List<String> emails = destinatarios.stream().map(u -> u.getEmail()).collect(Collectors.toList());

		this.enviarEmail("guilhermefroestestedeemail@gmail.com", emails, "Lançamentos vencidos",
				"mail/aviso-lancamentos-vencidos", variaveis);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template,
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));

		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

		String mensagem = thymeleaf.process(template, context);

		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}

	public void solicitarResetDeSenha(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com este e-mail."));

		String token = UUID.randomUUID().toString();

		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUsuario(usuario);
		resetToken.setExpiracao(LocalDateTime.now().plusHours(1));

		tokenRepository.save(resetToken);

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("nome", usuario.getNome());
		variaveis.put("link", resetPasswordUrl + "?token=" + token);

		this.enviarEmail("guilhermefroestestedeemail@gmail.com", Arrays.asList(usuario.getEmail()),
				"Recuperação de senha", "mail/recuperar-senha", variaveis);
	}

}
