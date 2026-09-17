package com.valeshop.financeiro.repository;

import com.valeshop.financeiro.repository.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {
    List<TransacaoEntity> findByContaOrigemIdOrContaDestinoIdOrderByDataHoraDesc(Long contaOrigemId, Long contaDestinoId);
}
