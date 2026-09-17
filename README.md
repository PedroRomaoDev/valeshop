# ValeShop Financeiro - API de Carteira Digital

## Visão Geral

O **ValeShop Financeiro** é uma API REST desenvolvida em Java com Spring Boot para gerenciamento de carteira digital. A arquitetura segue os princípios de **Clean Architecture**, estabelecendo desacoplamento estrito entre a lógica de negócio principal, a camada de persistência e os adaptadores de interface.

---

## Arquitetura e Tecnologias

- **Linguagem:** Java 21 (LTS)
- **Framework:** Spring Boot 3.4.3 (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`)
- **Persistência:** PostgreSQL 15, Hibernate ORM, Spring Data JPA
- **Gerenciamento de Dependências e Build:** Apache Maven 3.8+
- **Utilitários de Código:** Project Lombok
- **Testes:** JUnit 5, Mockito, Spring Boot Test

---

## Requisitos de Sistema

- **JDK 21** ou superior configurado no `JAVA_HOME`
- **Apache Maven 3.8.0** ou superior
- **Docker Engine** e **Docker Compose**
- **Git Client**

---

## Instruções de Compilação e Execução

### 1. Obtenção do Código Fonte

```bash
git clone <URL_DO_REPOSITORIO>
cd valeshop
```

### 2. Provisionamento da Infraestrutura Local (PostgreSQL)

Inicialize o container de banco de dados através do Docker Compose:

```bash
docker-compose up -d
```

O container alocará o serviço PostgreSQL na porta host `5432` com o banco `valeshop_financeiro`.

### 3. Resolução de Dependências e Build

Para compilar o código fonte e gerar o artefato empacotado sem executar a suíte de testes:

```bash
mvn clean package -DskipTests
```

### 4. Inicialização da Aplicação

#### Execução via Plugin Maven:
```bash
mvn spring-boot:run
```

#### Execução do Artefato Jar:
```bash
java -jar target/financeiro-0.0.1-SNAPSHOT.jar
```

A API estará disponível na porta `8080` (`http://localhost:8080`).

---

## Execução de Testes Automáticos

Para disparar a suíte de testes unitários e de integração:

```bash
mvn test
```

---

## Configurações de Banco de Dados

As propriedades de conexão com a base relacional estão mapeadas em `src/main/resources/application.yml`:

| Parâmetro | Valor Padrão |
| :--- | :--- |
| **JDBC URL** | `jdbc:postgresql://localhost:5432/valeshop_financeiro` |
| **Usuário** | `postgres` |
| **Senha** | `rootpassword` |
| **DDL Auto** | `update` |
