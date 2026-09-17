package com.valeshop.financeiro.controller;

import com.valeshop.financeiro.controller.dto.ContaResponse;
import com.valeshop.financeiro.controller.dto.CriarContaRequest;
import com.valeshop.financeiro.usecase.CriarContaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final CriarContaUseCase criarContaUseCase;

    public ContaController(CriarContaUseCase criarContaUseCase) {
        this.criarContaUseCase = criarContaUseCase;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criarConta(@RequestBody CriarContaRequest request) {
        ContaResponse response = criarContaUseCase.executar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable Long id) {
        ContaResponse response = criarContaUseCase.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ContaResponse>> listarTodas() {
        List<ContaResponse> contas = criarContaUseCase.listarTodas();
        return ResponseEntity.ok(contas);
    }
}
