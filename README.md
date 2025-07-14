# Bora Estudar - Backend

Sistema backend para plataforma de grupos de estudo com integração Discord, desenvolvido em Spring Boot.

## Sobre o Projeto

O **Bora Estudar** é uma aplicação web que facilita a criação e gerenciamento de grupos de estudo. O sistema permite que usuários se cadastrem, criem grupos de estudo por disciplina e se conectem através de servidores Discord automaticamente criados para cada grupo.

### Principais Funcionalidades

- **Autenticação e Autorização**: Sistema completo de registro, login e confirmação por email
- **Gerenciamento de Usuários**: CRUD completo de usuários com validação
- **Grupos de Estudo**: Criação, busca e gerenciamento de grupos de estudo
- **Integração Discord**: Criação automática de servidores Discord para cada grupo
- **Disciplinas**: Sistema de categorização por disciplinas/matérias
- **Notificações por Email**: Confirmação de conta e outras notificações
- **API RESTful**: Endpoints bem estruturados seguindo padrões REST

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.6**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **PostgreSQL** - Banco de dados (produção)
- **JWT** - Tokens de autenticação
- **Discord4J** - Integração com Discord
- **Maven** - Gerenciamento de dependências
- **ModelMapper** - Mapeamento de objetos
- **Jakarta Validation** - Validação de dados

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Conta Discord (para configuração do bot)
- Servidor SMTP (para envio de emails)

## Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd bora-estudar-back-help
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e configure suas credenciais:

```bash
cp config.example.yaml config.yaml
```

Edite o arquivo `config.yaml` com suas configurações:

```yaml
# Database Configuration
database:
  username: sa
  password: ""
  url: jdbc:h2:file:./data/database_name

# Discord OAuth Configuration
discord:
  client_id: seu_discord_client_id
  client_secret: seu_discord_client_secret
  redirect_uri: https://seu-dominio.com/associate/callback
  token: seu_discord_bot_token
  guild_id: seu_discord_guild_id

# JWT Configuration
jwt:
  secret: sua_chave_jwt_secreta_aqui
  expiration_time: 1440
  cookie_name: access_token

# Email Configuration
mail:
  host: smtp.gmail.com
  port: 587
  username: seu-email@gmail.com
  password: sua_senha_de_app

# Email Token Configuration
email_token:
  expiration_time_minutes: 60

# External Host Configuration
external:
  host: https://seu-dominio.com/
```

### 3. Configuração do Discord Bot

1. Acesse o [Discord Developer Portal](https://discord.com/developers/applications)
2. Crie uma nova aplicação
3. Configure o OAuth2 com as URLs de redirecionamento apropriadas
4. Crie um bot e obtenha o token
5. Adicione o bot ao seu servidor Discord com as permissões necessárias

## Executando a Aplicação

### Desenvolvimento

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Produção

```bash
# Gerar o JAR
mvn clean package

# Executar o JAR
java -jar target/bora-estudar-1.0.0.jar
```

### Docker

```bash
# Construir a imagem
docker build -t bora-estudar-back .

# Executar o container
docker run -p 8080:8080 bora-estudar-back
```

## Estrutura da API

### Autenticação

- `POST /signup` - Registro de usuário
- `POST /signin` - Login de usuário
- `GET /confirm` - Confirmação de email
- `POST /logout` - Logout de usuário

### Usuários

- `GET /users` - Listar todos os usuários
- `GET /users/{id}` - Buscar usuário por ID
- `PUT /users/{id}` - Atualizar usuário
- `DELETE /users/{id}` - Deletar usuário

### Grupos de Estudo

- `POST /study-groups` - Criar grupo de estudo
- `POST /study-groups/criar-aleatorio` - Criar grupo aleatório
- `GET /study-groups` - Listar grupos (com filtros)
- `GET /study-groups/{id}` - Buscar grupo por ID
- `PUT /study-groups/{id}` - Atualizar grupo
- `DELETE /study-groups/{id}` - Deletar grupo
- `POST /study-groups/{id}/join` - Entrar no grupo
- `DELETE /study-groups/{id}/leave` - Sair do grupo

### Disciplinas

- `POST /subjects` - Criar disciplina
- `GET /subjects` - Listar disciplinas
- `GET /subjects/{id}` - Buscar disciplina por ID
- `PUT /subjects/{id}` - Atualizar disciplina
- `DELETE /subjects/{id}` - Deletar disciplina

## Banco de Dados

### Modelo de Dados

O sistema utiliza as seguintes entidades principais:

- **User**: Usuários do sistema
- **StudyGroup**: Grupos de estudo
- **Subject**: Disciplinas/matérias
- **StudyGroupUser**: Relacionamento usuário-grupo
- **StudyGroupWeekday**: Dias da semana dos grupos
- **EmailVerificationToken**: Tokens de verificação de email

### Console H2 (Desenvolvimento)

Acesse `http://localhost:8080/h2` para visualizar o banco de dados em desenvolvimento.

## Segurança

- Autenticação baseada em JWT
- Cookies HTTP-only para tokens
- Validação de entrada em todos os endpoints
- Confirmação de email obrigatória
- Integração com OAuth2 do Discord

## Monitoramento e Logs

A aplicação utiliza SLF4J com Logback para logging. Os logs incluem:

- Operações de autenticação
- Criação de grupos Discord
- Erros de validação
- Operações de banco de dados

## Configuração de Variáveis

### Segurança

**IMPORTANTE**:
- O arquivo `config.yaml` está no `.gitignore` e **NÃO deve ser commitado**
- Use o arquivo `config.example.yaml` como referência para outros desenvolvedores
- Mantenha suas credenciais seguras e nunca as compartilhe

### Ambientes diferentes

Você pode criar diferentes arquivos de configuração para diferentes ambientes:

```bash
config.yaml                # Desenvolvimento local
config.local.yaml          # Configurações locais específicas
config.development.yaml    # Ambiente de desenvolvimento
config.test.yaml           # Ambiente de testes
config.production.yaml     # Ambiente de produção
```

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto é parte de um Trabalho de Conclusão de Curso (TCC) da Universidade Federal Fluminense.