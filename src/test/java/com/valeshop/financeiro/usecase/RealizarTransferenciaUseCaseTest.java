package com.valeshop.financeiro.usecase;

import com.valeshop.financeiro.controller.dto.TransacaoResponse;
import com.valeshop.financeiro.controller.dto.TransferenciaRequest;
import com.valeshop.financeiro.exception.BusinessException;
import com.valeshop.financeiro.repository.ContaRepository;
import com.valeshop.financeiro.repository.TransacaoRepository;
import com.valeshop.financeiro.repository.entity.ContaEntity;
import com.valeshop.financeiro.repository.entity.TransacaoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealizarTransferenciaUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private RealizarTransferenciaUseCase realizarTransferenciaUseCase;

    private ContaEntity contaOrigem;
    private ContaEntity contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new ContaEntity();
        contaOrigem.setId(1L);
        contaOrigem.setNome("Conta Origem");
        contaOrigem.setTipo("RECARGA");
        contaOrigem.setSaldo(new BigDecimal("500.00"));
        contaOrigem.setLimite(new BigDecimal("200.00"));

        contaDestino = new ContaEntity();
        contaDestino.setId(2L);
        contaDestino.setNome("Conta Destino");
        contaDestino.setTipo("PAGAMENTO");
        contaDestino.setSaldo(new BigDecimal("100.00"));
        contaDestino.setLimite(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso quando houver saldo suficiente")
    void deveRealizarTransferenciaComSucesso() {
        TransferenciaRequest request = new TransferenciaRequest(1L, 2L, new BigDecimal("300.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        TransacaoEntity transacaoSalva = TransacaoEntity.builder()
            .id(10L)
            .contaOrigemId(1L)
            .contaDestinoId(2L)
            .valor(new BigDecimal("300.00"))
            .dataHora(LocalDateTime.now())
            .tipo("TRANSFERENCIA")
            .build();

        when(transacaoRepository.save(any(TransacaoEntity.class))).thenReturn(transacaoSalva);

        TransacaoResponse response = realizarTransferenciaUseCase.executar(request);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(new BigDecimal("200.00"), contaOrigem.getSaldo()); // 500 - 300
        assertEquals(new BigDecimal("400.00"), contaDestino.getSaldo()); // 100 + 300

        verify(contaRepository, times(1)).save(contaOrigem);
        verify(contaRepository, times(1)).save(contaDestino);
        verify(transacaoRepository, times(1)).save(any(TransacaoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o saldo + limite for insuficiente")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        TransferenciaRequest request = new TransferenciaRequest(1L, 2L, new BigDecimal("1000.00")); // Saldo + Limite = 700

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        BusinessException exception = assertThrows(BusinessException.class, () ->
            realizarTransferenciaUseCase.executar(request)
        );

        assertEquals("Saldo insuficiente para realizar a transferência.", exception.getMessage());
        verify(transacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar transferir para a mesma conta")
    void deveLancarExcecaoMesmaConta() {
        TransferenciaRequest request = new TransferenciaRequest(1L, 1L, new BigDecimal("100.00"));

        BusinessException exception = assertThrows(BusinessException.class, () ->
            realizarTransferenciaUseCase.executar(request)
        );

        assertEquals("A conta de origem e destino não podem ser iguais.", exception.getMessage());
        verify(contaRepository, never()).findById(any());
    }
}
