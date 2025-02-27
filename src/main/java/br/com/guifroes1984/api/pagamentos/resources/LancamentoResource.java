package br.com.guifroes1984.api.pagamentos.resources;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.guifroes1984.api.pagamentos.event.RecursoCriadoEvent;
import br.com.guifroes1984.api.pagamentos.model.Categoria;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.repository.LancamentoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/lancamentos")
@Api(value = "Lançamentos", description = "Operações relacionadas de lançamentos financeiros")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@ApiOperation(value = "Lista todos os lançamentos", response = List.class)
	public List<Lancamento> listar() {
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	@ApiOperation(value = "Busca um lançamento pelo código", response = Categoria.class)
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ApiOperation(value = "Adiciona um novo lançamento")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

}
