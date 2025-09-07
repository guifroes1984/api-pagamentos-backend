package br.com.guifroes1984.api.pagamentos.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaCategoria;
import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaDia;
import br.com.guifroes1984.api.pagamentos.dto.LancamentoEstatisticaPessoa;
import br.com.guifroes1984.api.pagamentos.model.Categoria_;
import br.com.guifroes1984.api.pagamentos.model.Lancamento;
import br.com.guifroes1984.api.pagamentos.model.Lancamento_;
import br.com.guifroes1984.api.pagamentos.model.Pessoa_;
import br.com.guifroes1984.api.pagamentos.repository.filter.LancamentoFilter;
import br.com.guifroes1984.api.pagamentos.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaPessoa.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoa.class,
				root.get(Lancamento_.tipo), 
				root.get(Lancamento_.pessoa),
				criteriaBuilder.sum(root.get(Lancamento_.valor))));


		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						inicio),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						fim));

		criteriaQuery.groupBy(root.get(Lancamento_.tipo), 
				root.get(Lancamento_.pessoa));

		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate dataInicio, LocalDate dataFim) {
	    CriteriaBuilder cb = manager.getCriteriaBuilder();

	    CriteriaQuery<LancamentoEstatisticaDia> cq = cb.createQuery(LancamentoEstatisticaDia.class);
	    Root<Lancamento> root = cq.from(Lancamento.class);

	    cq.select(cb.construct(
	            LancamentoEstatisticaDia.class,
	            root.get(Lancamento_.tipo),
	            root.get(Lancamento_.dataVencimento),
	            cb.sum(root.get(Lancamento_.valor))
	    ));

	    Predicate restricoes = cb.conjunction();

	    if (dataInicio != null) {
	        restricoes = cb.and(restricoes,
	                cb.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataInicio));
	    }

	    if (dataFim != null) {
	        restricoes = cb.and(restricoes,
	                cb.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataFim));
	    }

	    cq.where(restricoes);
	    cq.groupBy(root.get(Lancamento_.tipo), root.get(Lancamento_.dataVencimento));

	    return manager.createQuery(cq).getResultList();
	}

	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate dataInicio, LocalDate dataFim) {
	    CriteriaBuilder cb = manager.getCriteriaBuilder();

	    CriteriaQuery<LancamentoEstatisticaCategoria> cq = cb.createQuery(LancamentoEstatisticaCategoria.class);
	    Root<Lancamento> root = cq.from(Lancamento.class);

	    cq.select(cb.construct(
	            LancamentoEstatisticaCategoria.class,
	            root.get(Lancamento_.categoria),
	            cb.sum(root.get(Lancamento_.valor))
	    ));

	    Predicate restricoes = cb.conjunction();

	    if (dataInicio != null) {
	        restricoes = cb.and(restricoes,
	                cb.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataInicio));
	    }

	    if (dataFim != null) {
	        restricoes = cb.and(restricoes,
	                cb.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataFim));
	    }

	    cq.where(restricoes);
	    cq.groupBy(root.get(Lancamento_.categoria));

	    return manager.createQuery(cq).getResultList();
	}


	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		// Criar as restrições
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarResttricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo),
				root.get(Lancamento_.descricao), root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento), root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicionarResttricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarResttricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
