package com.valeshop.financeiro.controller.dto;

import java.math.BigDecimal;

public record ContaResponse(
    Long id,
    String nome,
    String tipo,
    BigDecimal limite,
    BigDecimal saldo
) {}
