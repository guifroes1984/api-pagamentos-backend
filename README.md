# 💳 API de Pagamentos

API REST desenvolvida para gerenciamento de pagamentos com autenticação JWT.

---

## 🚀 Tecnologias

- Java 17
- Spring Boot 2.7
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- MySQL
- Flyway
- Maven
- Swagger / OpenAPI

---

## 🎯 Objetivo

Simular uma API de pagamentos com:

- Cadastro de usuários
- Autenticação com JWT
- Controle de acesso por perfil
- Cadastro de categorias
- Cadastro de pessoas
- Controle de lançamentos financeiros
- Upload de anexos
- Relatórios PDF

---

## 🔐 Autenticação

A API utiliza autenticação via JWT.

Após realizar login, o token deve ser enviado no header:

Authorization: Bearer {token}

---

## ▶️ Como rodar o projeto

1. Clonar repositório:
git clone https://github.com/guifroes1984/api-pagamentos-backend.git


2. Entrar na pasta:
cd api-pagamentos-backend


3. Rodar:
mvn spring-boot:run


A aplicação estará disponível em:
http://localhost:8080


Swagger:
http://localhost:8080/swagger-ui/index.html


---

## 👨‍💻 Autor

Guilherme Froes