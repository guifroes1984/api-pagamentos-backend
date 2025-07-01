package br.com.guifroes1984.api.pagamentos.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.guifroes1984.api.pagamentos.model.Anexo;
import br.com.guifroes1984.api.pagamentos.repository.AnexoRepository;

@Service
public class AnexoService {

	@Autowired
	private AnexoRepository anexoRepository;

	public AnexoService(AnexoRepository anexoRepository) {
		this.anexoRepository = anexoRepository;
	}

	public Anexo salvar(MultipartFile file) throws IOException {
		Anexo anexo = new Anexo();
		anexo.setNome(file.getOriginalFilename());
		anexo.setTipo(file.getContentType());
		anexo.setDados(file.getBytes());
		return anexoRepository.save(anexo);
	}

	public void remover(Long id) {
		if (!anexoRepository.existsById(id)) {
			throw new IllegalArgumentException("Anexo não encontrado");
		}
		anexoRepository.deleteById(id);
	}

}
