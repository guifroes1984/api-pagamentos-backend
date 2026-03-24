package com.guifroes1984.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guifroes1984.pagamentos.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
