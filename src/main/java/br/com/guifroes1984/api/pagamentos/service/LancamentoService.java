package br.com.guifroes1984.api.pagamentos.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaPessoa;
import br.com.guifroes1984.api.pagamentos.mail.Mailer;
import br.com.guifroes1984.api.pagamentos.model.Anexo;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.model.Pessoa;
import br.com.guifroes1984.api.pagamentos.model.Usuario;
import br.com.guifroes1984.api.pagamentos.repository.AnexoRepository;
import br.com.guifroes1984.api.pagamentos.repository.LancamentoRepository;
import br.com.guifroes1984.api.pagamentos.repository.PessoaRepository;
import br.com.guifroes1984.api.pagamentos.repository.UsuarioRepository;
import br.com.guifroes1984.api.pagamentos.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Autowired
	private AnexoService anexoService;
	
	@Autowired
	private AnexoRepository anexoRepository;

	@Scheduled(cron = "0 0 6 * * *") /*
										 * Metodo de agendamento para execução. No caso aqui está todos os dia a 6h da
										 * manhã.
										 */
	// @Scheduled(fixedDelay = 1000 * 60 * 30) /*Metodo que nvia nesse caso aqui a
	// cada 30 minutos um e-mail. Somente para testar*/
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de " + "e-mails de aviso de lançamentos vencidos.");
		}

		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para aviso.");

			return;
		}

		logger.info("Existem {} lançamentos vencidos.", vencidos.size());

		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

		if (destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos, mas o " + "sistema não encontrou destinatários.");

			return;
		}

		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

		logger.info("Envio de e-mail de aviso concluído.");
	}

	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public Lancamento salvar(Lancamento lancamento) {
		validarPessoaAtiva(lancamento.getPessoa().getCodigo());
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento salvarComAnexo(Lancamento lancamento, MultipartFile file) throws IOException {
		validarPessoaAtiva(lancamento.getPessoa().getCodigo());

		if (file != null && !file.isEmpty()) {
			Anexo anexo = anexoService.salvar(file);
			lancamento.setAnexo(anexo);
		}

		return lancamentoRepository.save(lancamento);
	}

	private void validarPessoaAtiva(Long pessoaCodigo) {
		Pessoa pessoa = pessoaRepository.findOne(pessoaCodigo);
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	public Lancamento atualizarAnexo(Long codigo, MultipartFile file) throws IOException {
	    Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);

	    if (file != null && !file.isEmpty()) {
	        Anexo novoAnexo = anexoService.salvar(file);
	        Anexo anexoAntigo = lancamentoSalvo.getAnexo();
	        lancamentoSalvo.setAnexo(novoAnexo);
	        Lancamento salvo = lancamentoRepository.save(lancamentoSalvo);

	        if (anexoAntigo != null && anexoAntigo.getCodigo() != null
	            && !anexoAntigo.getCodigo().equals(novoAnexo.getCodigo())) {
	            anexoRepository.delete(anexoAntigo);
	        }

	        return salvo;
	    }

	    return lancamentoSalvo;
	}
	
	public void removerAnexo(Long codigo) {
	    Lancamento lancamento = buscarLancamentoExistente(codigo);
	    Anexo anexo = lancamento.getAnexo();
	    
	    if (anexo != null) {
	        lancamento.setAnexo(null);
	        lancamentoRepository.save(lancamento);
	        anexoRepository.delete(anexo);
	    }
	}
	
	public Anexo buscarAnexoDoLancamento(Long codigoLancamento) {
		Lancamento lancamento = lancamentoRepository.findOne(codigoLancamento);
		if (lancamento == null) {
		    throw new IllegalArgumentException("Lançamento não encontrado");
		}

	    Anexo anexo = lancamento.getAnexo(); // Supondo que existe getAnexo()
	    if (anexo == null) {
	        throw new IllegalArgumentException("Lançamento não possui anexo");
	    }

	    return anexo;
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvo;
	}

}
