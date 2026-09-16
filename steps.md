Guia de Desenvolvimento: API de Carteira Digital (Clean Architecture)
Este documento serve como o plano mestre e guia arquitetural para o desenvolvimento do seu projeto pessoal em Java (Spring Boot), alinhado com as tecnologias exigidas no mercado (Java 17/21, Spring Boot, Spring Data JPA, PostgreSQL, Docker e JUnit).

1. Arquitetura da Aplicacao (Clean Architecture por Camadas)
A aplicacao esta dividida em camadas bem definidas para garantir que as regras de negocio fiquem isoladas do framework web e do banco de dados:

Controller (Camada de Apresentacao): Recebe as requisicoes HTTP, valida os dados de entrada (DTOs) e chama os Use Cases.

Use Case (Casos de Uso / Regras de Negocio): O coracao da aplicacao. Contem a logica de negocio pura (ex: validar saldo, calcular taxas, efetivar transferencias). Nao conhece o Spring Web e nem o Banco de Dados diretamente.

Repository / Gateway (Camada de Persistencia): Comunicacao com o PostgreSQL usando Spring Data JPA / Hibernate.

2. Estrutura de Pacotes Sugerida
Organize o seu projeto dentro do pacote principal (ex: com.valeshop.financeiro) com a seguinte estrutura de diretorios:

Plaintext
src/main/java/com/valeshop/financeiro/
│
├── controller/                # Camada Web (REST Controllers, DTOs de Request/Response)
│   ├── ContaController.java
│   └── TransacaoController.java
│
├── usecase/                   # Casos de Uso (Regras de Negocio da aplicacao)
│   ├── CriarContaUseCase.java
│   └── RealizarTransferenciaUseCase.java
│
├── repository/                # Camada de Persistencia (Spring Data JPA Repositories & Entities)
│   ├── ContaRepository.java
│   ├── TransacaoRepository.java
│   └── entity/
│       ├── ContaEntity.java
│       └── TransacaoEntity.java
│
└── exception/                 # Tratamento global de erros e excecoes de negocio
    └── BusinessException.java
3. Stack Tecnologica
Linguagem: Java 17 ou 21

Framework: Spring Boot 3.x

Persistencia: Spring Data JPA / Hibernate

Banco de Dados: PostgreSQL (rodando via Docker)

Testes: JUnit 5 + Mockito

4. Configuracao do Ambiente (Docker Compose)
Crie um arquivo docker-compose.yml na raiz do seu projeto para subir o banco de dados PostgreSQL rapidamente:

YAML
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: valeshop_postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: rootpassword
      POSTGRES_DB: valeshop_financeiro
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
5. Configuracao do Banco (application.yml)
No arquivo src/main/resources/application.yml, configure a conexao com o banco e o Hibernate:

YAML
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/valeshop_financeiro
    username: postgres
    password: rootpassword
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8080
6. Passo a Passo de Desenvolvimento
Passo 1: Configurar o projeto base via Spring Initializr com as dependencias: Spring Web, Spring Data JPA, PostgreSQL Driver e Lombok.

Passo 2: Subir o banco de dados utilizando o comando docker-compose up -d.

Passo 3: Criar as Entidades JPA na camada de repository/entity.

Passo 4: Criar as interfaces de repositorio estendendo JpaRepository.

Passo 5: Implementar as regras de negocio nos Use Cases.

Passo 6: Expor os endpoints REST na camada de controller.

Passo 7: Escrever testes unitarios com JUnit 5 para validar os casos de uso.