package com.valeshop.financeiro.usecase;

import com.valeshop.financeiro.controller.dto.ContaResponse;
import com.valeshop.financeiro.controller.dto.CriarContaRequest;
import com.valeshop.financeiro.exception.BusinessException;
import com.valeshop.financeiro.repository.ContaRepository;
import com.valeshop.financeiro.repository.entity.ContaEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CriarContaUseCase {

    private final ContaRepository contaRepository;

    public CriarContaUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public ContaResponse executar(CriarContaRequest request) {
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new BusinessException("O nome do titular da conta é obrigatório.");
        }
        if (request.tipo() == null || request.tipo().trim().isEmpty()) {
            throw new BusinessException("O tipo da conta é obrigatório.");
        }

        BigDecimal saldoInicial = request.saldoInicial() != null ? request.saldoInicial() : BigDecimal.ZERO;
        BigDecimal limite = request.limite() != null ? request.limite() : BigDecimal.ZERO;

        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O saldo inicial não pode ser negativo.");
        }
        if (limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O limite da conta não pode ser negativo.");
        }

        ContaEntity entity = new ContaEntity();
        entity.setNome(request.nome().trim());
        entity.setTipo(request.tipo().trim().toUpperCase());
        entity.setSaldo(saldoInicial);
        entity.setLimite(limite);

        ContaEntity contaSalva = contaRepository.save(entity);

        return new ContaResponse(
            contaSalva.getId(),
            contaSalva.getNome(),
            contaSalva.getTipo(),
            contaSalva.getLimite(),
            contaSalva.getSaldo()
        );
    }

    public ContaResponse buscarPorId(Long id) {
        ContaEntity entity = contaRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Conta não encontrada para o ID: " + id));

        return new ContaResponse(
            entity.getId(),
            entity.getNome(),
            entity.getTipo(),
            entity.getLimite(),
            entity.getSaldo()
        );
    }

    public List<ContaResponse> listarTodas() {
        return contaRepository.findAll().stream()
            .map(entity -> new ContaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getLimite(),
                entity.getSaldo()
            ))
            .toList();
    }
}
