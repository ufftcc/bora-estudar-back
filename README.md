# Configuração de Variáveis de Ambiente (.config.yaml)

Este projeto agora suporta o uso de arquivos `.config.yaml` para gerenciar variáveis de ambiente de forma segura e flexível.

## Como usar

### 1. Configuração inicial

1. Copie o arquivo `config.example.yaml` para `config.yaml`:
   ```bash
   cp config.example.yaml config.yaml
   ```

2. Edite o arquivo `config.yaml` com suas configurações específicas:
   ```bash
   # Exemplo de configuração

   # Database Configuration
   database:
     username: seu_usuario
     password: sua_senha
     url: jdbc:h2:file:./data/boraestudar

   # Discord OAuth Configuration
   discord:
     client_id: seu_client_id
     client_secret: seu_client_secret
     redirect_uri: seu_redirect_uri
     token: seu_token
     guild_id: seu_guild_id
   ```

### 2. Segurança

**IMPORTANTE**: 
- O arquivo `config.yaml` está no `.gitignore` e **NÃO deve ser commitado**
- Use o arquivo `config.example.yaml` como referência para outros desenvolvedores
- Mantenha suas credenciais seguras e nunca as compartilhe

### 3. Ambientes diferentes

Você pode criar diferentes arquivos `.config.yaml` para diferentes ambientes:

```bash
.config.yaml                # Desenvolvimento local
.config.local.yaml          # Configurações locais específicas
.config.development.yaml    # Ambiente de desenvolvimento
.config.test.yaml           # Ambiente de testes
.config.production.yaml     # Ambiente de produção
```

### 5. Exemplo de uso

```yaml
# Antes (application-local.yaml)
spring:
  datasource:
    username: sa
    password: ''

# Depois (application-local.yaml)
spring:
  datasource:
    username: ${database.username}
    password: ${database.password}
```

```bash
# Arquivo .config.yaml
database:
  username: seu_usuario
  password: sua_senha
```

## Executando a aplicação

Após configurar o arquivo `config.yaml`, execute a aplicação normalmente:

```bash
mvn spring-boot:run
```

## Troubleshooting

- Verifique se o arquivo `config.yaml` está na raiz do projeto
- Certifique-se de que não há espaços extras nas variáveis do `config.yaml`
- Use aspas apenas quando necessário (ex: valores com espaços)
