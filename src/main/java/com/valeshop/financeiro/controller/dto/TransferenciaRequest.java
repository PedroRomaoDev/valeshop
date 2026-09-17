package com.valeshop.financeiro.controller.dto;

import java.math.BigDecimal;

public record TransferenciaRequest(
    Long contaOrigemId,
    Long contaDestinoId,
    BigDecimal valor
) {}
