package com.valeshop.financeiro.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
    Long id,
    Long contaOrigemId,
    Long contaDestinoId,
    BigDecimal valor,
    LocalDateTime dataHora,
    String tipo
) {}
