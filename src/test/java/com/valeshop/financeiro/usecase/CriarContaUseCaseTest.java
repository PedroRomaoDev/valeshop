package com.valeshop.financeiro.usecase;

import com.valeshop.financeiro.controller.dto.ContaResponse;
import com.valeshop.financeiro.controller.dto.CriarContaRequest;
import com.valeshop.financeiro.exception.BusinessException;
import com.valeshop.financeiro.repository.ContaRepository;
import com.valeshop.financeiro.repository.entity.ContaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarContaUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private CriarContaUseCase criarContaUseCase;

    private CriarContaRequest requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new CriarContaRequest(
            "Pedro Romao",
            "RECARGA",
            new BigDecimal("1000.00"),
            new BigDecimal("500.00")
        );
    }

    @Test
    @DisplayName("Deve criar conta com sucesso quando os dados forem válidos")
    void deveCriarContaComSucesso() {
        ContaEntity entitySalva = new ContaEntity();
        entitySalva.setId(1L);
        entitySalva.setNome("Pedro Romao");
        entitySalva.setTipo("RECARGA");
        entitySalva.setLimite(new BigDecimal("1000.00"));
        entitySalva.setSaldo(new BigDecimal("500.00"));

        when(contaRepository.save(any(ContaEntity.class))).thenReturn(entitySalva);

        ContaResponse response = criarContaUseCase.executar(requestValido);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Pedro Romao", response.nome());
        assertEquals("RECARGA", response.tipo());
        assertEquals(new BigDecimal("1000.00"), response.limite());
        assertEquals(new BigDecimal("500.00"), response.saldo());

        verify(contaRepository, times(1)).save(any(ContaEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do titular for inválido")
    void deveLancarExcecaoQuandoNomeForInvalido() {
        CriarContaRequest requestInvalido = new CriarContaRequest(
            "",
            "RECARGA",
            new BigDecimal("1000.00"),
            new BigDecimal("500.00")
        );

        BusinessException exception = assertThrows(BusinessException.class, () -> 
            criarContaUseCase.executar(requestInvalido)
        );

        assertEquals("O nome do titular da conta é obrigatório.", exception.getMessage());
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o saldo inicial for negativo")
    void deveLancarExcecaoQuandoSaldoInicialForNegativo() {
        CriarContaRequest requestInvalido = new CriarContaRequest(
            "Pedro Romao",
            "RECARGA",
            new BigDecimal("1000.00"),
            new BigDecimal("-50.00")
        );

        BusinessException exception = assertThrows(BusinessException.class, () -> 
            criarContaUseCase.executar(requestInvalido)
        );

        assertEquals("O saldo inicial não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any());
    }
}
