# API de Pagamentos

## Descrição

Esta API gerencia categorias, lançamentos financeiros e pessoas. Ela fornece endpoints para CRUD (Create, Read, Update, Delete) dessas entidades e implementa segurança com OAuth2.

## Tecnologias Utilizadas

- **Spring Boot**
- **Spring Security com OAuth2**
- **Spring Data JPA**
- **Hibernate**
- **Swagger para documentação**

## Endpoints Disponíveis

### Categorias

| Método | Endpoint               | Descrição                       |
| ------ | ---------------------- | ------------------------------- |
| `GET`  | `/categorias`          | Lista todas as categorias       |
| `POST` | `/categorias`          | Adiciona uma nova categoria     |
| `GET`  | `/categorias/{codigo}` | Busca uma categoria pelo código |

### Lançamentos

| Método   | Endpoint                | Descrição                             |
| -------- | ----------------------- | ------------------------------------- |
| `GET`    | `/lancamentos`          | Lista todos os lançamentos com filtro |
| `POST`   | `/lancamentos`          | Adiciona um novo lançamento           |
| `GET`    | `/lancamentos/{codigo}` | Busca um lançamento pelo código       |
| `DELETE` | `/lancamentos/{codigo}` | Remove um lançamento pelo código      |

### Pessoas

| Método   | Endpoint                  | Descrição                            |
| -------- | ------------------------- | ------------------------------------ |
| `GET`    | `/pessoas`                | Lista todas as pessoas               |
| `POST`   | `/pessoas`                | Adiciona uma nova pessoa             |
| `GET`    | `/pessoas/{codigo}`       | Busca uma pessoa pelo código         |
| `DELETE` | `/pessoas/{codigo}`       | Remove uma pessoa pelo código        |
| `PUT`    | `/pessoas/{codigo}`       | Atualiza os dados de uma pessoa      |
| `PUT`    | `/pessoas/{codigo}/ativo` | Atualiza o status de ativo da pessoa |

## Segurança

A API implementa autenticação baseada em **OAuth2**. Certifique-se de enviar um token válido no cabeçalho das requisições para endpoints protegidos.

## Instalação e Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/api-pagamentos.git
   ```
2. Configure o banco de dados no `application.properties` ou `application.yml`.
3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

## Documentação

A documentação Swagger pode ser acessada via:

```
http://localhost:8080/swagger-ui.html
```



