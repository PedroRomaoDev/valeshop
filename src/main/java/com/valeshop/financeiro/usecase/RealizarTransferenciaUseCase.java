package com.valeshop.financeiro.usecase;

import com.valeshop.financeiro.controller.dto.TransacaoResponse;
import com.valeshop.financeiro.controller.dto.TransferenciaRequest;
import com.valeshop.financeiro.exception.BusinessException;
import com.valeshop.financeiro.repository.ContaRepository;
import com.valeshop.financeiro.repository.TransacaoRepository;
import com.valeshop.financeiro.repository.entity.ContaEntity;
import com.valeshop.financeiro.repository.entity.TransacaoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RealizarTransferenciaUseCase {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public RealizarTransferenciaUseCase(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public TransacaoResponse executar(TransferenciaRequest request) {
        if (request.contaOrigemId() == null || request.contaDestinoId() == null) {
            throw new BusinessException("As contas de origem e destino devem ser especificadas.");
        }

        if (request.contaOrigemId().equals(request.contaDestinoId())) {
            throw new BusinessException("A conta de origem e destino não podem ser iguais.");
        }

        if (request.valor() == null || request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da transferência deve ser maior que zero.");
        }

        ContaEntity contaOrigem = contaRepository.findById(request.contaOrigemId())
            .orElseThrow(() -> new BusinessException("Conta de origem não encontrada: ID " + request.contaOrigemId()));

        ContaEntity contaDestino = contaRepository.findById(request.contaDestinoId())
            .orElseThrow(() -> new BusinessException("Conta de destino não encontrada: ID " + request.contaDestinoId()));

        BigDecimal saldoDisponivel = contaOrigem.getSaldo().add(contaOrigem.getLimite());
        if (saldoDisponivel.compareTo(request.valor()) < 0) {
            throw new BusinessException("Saldo insuficiente para realizar a transferência.");
        }

        // Atualização de saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Registro da transação
        TransacaoEntity transacao = TransacaoEntity.builder()
            .contaOrigemId(contaOrigem.getId())
            .contaDestinoId(contaDestino.getId())
            .valor(request.valor())
            .dataHora(LocalDateTime.now())
            .tipo("TRANSFERENCIA")
            .build();

        TransacaoEntity transacaoSalva = transacaoRepository.save(transacao);

        return new TransacaoResponse(
            transacaoSalva.getId(),
            transacaoSalva.getContaOrigemId(),
            transacaoSalva.getContaDestinoId(),
            transacaoSalva.getValor(),
            transacaoSalva.getDataHora(),
            transacaoSalva.getTipo()
        );
    }

    public List<TransacaoResponse> listarPorConta(Long contaId) {
        if (!contaRepository.existsById(contaId)) {
            throw new BusinessException("Conta não encontrada para o ID: " + contaId);
        }

        return transacaoRepository.findByContaOrigemIdOrContaDestinoIdOrderByDataHoraDesc(contaId, contaId)
            .stream()
            .map(t -> new TransacaoResponse(
                t.getId(),
                t.getContaOrigemId(),
                t.getContaDestinoId(),
                t.getValor(),
                t.getDataHora(),
                t.getTipo()
            ))
            .toList();
    }
}
