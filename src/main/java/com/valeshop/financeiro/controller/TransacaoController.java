package com.valeshop.financeiro.controller;

import com.valeshop.financeiro.controller.dto.TransacaoResponse;
import com.valeshop.financeiro.controller.dto.TransferenciaRequest;
import com.valeshop.financeiro.usecase.RealizarTransferenciaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final RealizarTransferenciaUseCase realizarTransferenciaUseCase;

    public TransacaoController(RealizarTransferenciaUseCase realizarTransferenciaUseCase) {
        this.realizarTransferenciaUseCase = realizarTransferenciaUseCase;
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransacaoResponse> realizarTransferencia(@RequestBody TransferenciaRequest request) {
        TransacaoResponse response = realizarTransferenciaUseCase.executar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponse>> listarPorConta(@PathVariable Long contaId) {
        List<TransacaoResponse> transacoes = realizarTransferenciaUseCase.listarPorConta(contaId);
        return ResponseEntity.ok(transacoes);
    }
}
