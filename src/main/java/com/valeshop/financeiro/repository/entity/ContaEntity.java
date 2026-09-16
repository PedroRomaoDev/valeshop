package com.valeshop.financeiro.repository.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contas") // Define o nome da tabela no banco de dados
@Data // O Lombok gera automaticamente getters, setters, toString, equals e hashCode
public class ContaEntity {

    @Id // Diz ao Hibernate que este campo é a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Faz o banco gerar o ID automaticamente
    private Long id;

    @Column(name = "nome", length = 50, nullable = false) // Define o nome da coluna e restrições
    private String nome;

    @Column(name = "tipo", length = 20, nullable = false) // Ex: 'RECARGA', 'PAGAMENTO'
    private String tipo;

    @Column(name = "limite", nullable = false, precision = 10, scale = 2) // Para valores monetários
    private BigDecimal limite; // Use BigDecimal para dinheiro!

    @Column(name = "saldo", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;
}
