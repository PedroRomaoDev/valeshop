package com.valeshop.financeiro.controller.dto;

import java.math.BigDecimal;

public record CriarContaRequest(
    String nome,
    String tipo,
    BigDecimal limite,
    BigDecimal saldoInicial
) {}
