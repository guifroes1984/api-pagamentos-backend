package br.com.guifroes1984.api.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.guifroes1984.api.pagamentos.model.Anexo;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {

}
