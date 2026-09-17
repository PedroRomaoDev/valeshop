package com.valeshop.financeiro.controller.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
    String mensagem,
    int status,
    LocalDateTime timestamp
) {}
