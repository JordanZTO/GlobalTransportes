# 🚚 Global Transportes - Sistema de Gerenciamento de Fretes

Este projeto foi desenvolvido como parte da disciplina do **3º Período do curso de Análise e Desenvolvimento de Sistemas**, com o objetivo de criar uma aplicação backend e frontend para gerenciamento de transportes de cargas e fretes.

## 📜 Descrição do Projeto

O sistema permite o cadastro, gestão e acompanhamento de fretes, motoristas e clientes. Possui funcionalidades tanto para usuários comuns, clientes e motoristas quanto para administradores do sistema.

O backend foi desenvolvido em **Java Spring Boot**, utilizando padrões de arquitetura REST e documentado através de **Swagger/OpenAPI**. O frontend é construído com **HTML, CSS e JavaScript puro**, com foco em simplicidade e funcionamento direto com a API.

Além disso, o projeto contém:

- 🔹 **Diagrama de Casos de Uso** — Representa as interações dos usuários e administradores com o sistema.
- 🔹 **Diagrama de Classes** — Representa a estrutura das entidades, atributos e os relacionamentos entre elas.
- 🔹 **Arquivo SQL** — Script de criação do banco de dados, disponível no repositório.
- 🔹 **Testes implementados** — O projeto possui os testes necessarios, garantindo a qualidade e o funcionamento das principais funcionalidades.

## 🚀 Funcionalidades Principais

- ✅ Cadastro e login de usuários, clientes e motoristas
- ✅ Recuperação e redefinição de senha
- ✅ Gestão de perfis (clientes e motoristas)
- ✅ Cadastro e acompanhamento de fretes
- ✅ Acompanhamento de status e checkpoints dos fretes
- ✅ Dashboard administrativo para monitoramento do sistema
- ✅ Validação e gestão de motoristas e clientes (Admin)
- ✅ Envio de mensagens para suporte

## 🔗 Documentação da API

A documentação completa da API está disponível através do Swagger:

👉 **[Acessar Documentação da API](http://localhost:8080/swagger-ui/index.html#/)**  
*(Substituir `localhost` pelo IP ou domínio onde o backend estiver rodando)*

## 🏗️ Tecnologias Utilizadas

### 🔙 Backend
- ✔️ Java 17+
- ✔️ Spring Boot
- ✔️ Spring MVC (REST)
- ✔️ Spring Data JPA
- ✔️ Swagger (OpenAPI) - Documentação da API
- ✔️ Banco de Dados Relacional Mysql
- ✔️ Maven
- ✔️ JUnit e Spring Boot Test (Testes automatizados)

### 🌐 Frontend
- ✔️ HTML
- ✔️ CSS
- ✔️ JavaScript

## ⚙️ Configurações Necessárias Antes de Rodar

### 🔑 Banco de Dados
- É necessário configurar o acesso ao banco de dados no arquivo:

```
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:jdbc:mysql://localhost:3306/globaltransportes?useSSL=false&serverTimezone=UTC
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

- O script SQL para criação da estrutura do banco está disponível na pasta `/database` do repositório.

### 🖼️ Armazenamento de Fotos
- O projeto salva imagens (CNH, fotofrente e fotoPlaca) em um diretório local.  
- Configure o caminho de armazenamento no serviço:

```
ArquivoService.java
```

Exemplo de configuração no código:

```java
private final String uploadDir = "C:\\Users\\Jordan\\Desktop\\GlobalTransportes\\uploads\\imagens";
```

📌 Certifique-se de criar esse diretório no seu sistema e garantir permissões de escrita.

## 🏃‍♂️ Como Executar o Projeto


Acesse no navegador:
```
http://localhost:8080/login
```
