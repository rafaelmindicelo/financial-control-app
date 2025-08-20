# Financial Control App

Aplicação Java Spring Boot para controle financeiro, com persistência em MySQL e endpoints REST para gerenciamento de contas e despesas.

## Tecnologias

- Java 17+
- Spring Boot
- Maven
- MySQL (via Docker)
- Lombok

## Como rodar o projeto

1. **Suba o banco de dados MySQL com Docker:**

   ```sh
   docker-compose up -d

O banco estará disponível em `localhost:3306` com:

- **Usuário:** `root`
- **Senha:** `mysql`
- **Banco:** `financial_control`

## Configure o `application.properties` do Spring Boot (exemplo):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/financial_control
spring.datasource.username=root
spring.datasource.password=mysql
spring.jpa.hibernate.ddl-auto=update 
```

## Compile e execute a aplicação: ##

``` mvn spring-boot:run ```

# Endpoints principais

## Conta (/account)

### Criar conta
**POST** `/account`  
**Body:** `{ "nome": "...", ... }`

### Depositar
**POST** `/account/{accountId}/deposit?value=100.0`

### Consultar saldo
**GET** `/account/{accountId}/balance`

### Listar todas as contas
**GET** `/account/all`

## Despesas (/expenses)

### Listar despesas por conta
**GET** `/expenses/account/{accountId}`

### Adicionar despesa
**POST** `/expenses`  
**Body:** `{ "accountId": 1, "valor": 50.0, ... }`

### Filtrar despesas por ano
**GET** `/expenses/{accountId}/year/{year}`

### Filtrar despesas por data e categoria
**GET** `/expenses/{accountId}/date-range?startDate=2024-01-01T00:00:00&endDate=2024-06-30T23:59:59&categoryId=2`

---

## Observações
- Datas podem estar no formato `yyyy-MM-ddTHH:mm:ss` ou `yyyy-MM-dd`;
- As respostas de erro seguem o padrão `{ "message": "Descrição do erro" }`.

---

## Docker Compose
Veja o arquivo `docker-compose.yml` para detalhes do serviço MySQL.
