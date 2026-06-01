# Jira Backend - Sistema Completo

## 🚀 Início Rápido

**👉 Comece aqui:** [QUICK_START.md](QUICK_START.md) - Setup em 4 passos

**📚 Índice completo:** [INDEX.md](INDEX.md) - Todos os documentos

## 🔐 Autenticação JWT

A API agora requer **Bearer Token JWT** para autenticação:

```bash
# 1. Fazer login
POST /api/v1/auth/login
{
  "username": "joao.silva",
  "password": "password123"
}

# 2. Usar token em requisições
GET /api/v1/projects
Authorization: Bearer {seu-token-aqui}
```

**Documentação:** [AUTHENTICATION.md](AUTHENTICATION.md)

## 🌍 Múltiplos Ambientes

A aplicação suporta **3 ambientes** com configurações diferentes:

| Ambiente | Command | Config |
|----------|---------|--------|
| **Development** | `./gradlew bootRun` | Dev (inseguro) |
| **Homologation** | `java -jar app.jar --spring.profiles.active=homolog` | Teste |
| **Production** | `JWT_SECRET=... java -jar app.jar --spring.profiles.active=prod` | Seguro |

**Documentação:** [ENVIRONMENTS.md](ENVIRONMENTS.md)

## Visão Geral

Este é um projeto backend completo para Jira, estruturado em três módulos:

1. **jira** - Aplicação principal de gerenciamento de projetos e issues
2. **integration-lib** - Biblioteca centralizada de integrações de APIs
3. **integration-service** - Serviço dedicado para gerenciar integrações com plataformas externas

## Arquitetura

```
jira/
├── src/main/java/com/project/jira/
│   ├── config/              # Configurações (Swagger, Security, etc)
│   ├── domain/
│   │   ├── entity/          # Entidades (Project, Issue, User)
│   │   └── repository/      # Repositórios MongoDB
│   ├── application/
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── service/         # Lógica de negócio + AuthService
│   │   └── service/         # Lógica de negócio
│   ├── infrastructure/
│   │   ├── security/        # JwtUtil, JwtAuthenticationFilter
│   │   └── exception/       # Handlers de exceção
│   ├── presentation/
│   │   ├── controller/      # REST Controllers
│   │   └── controller/      # AuthController (novo)
│   └── JiraApplication.java # Classe principal
│
├── integration-lib/
│   └── src/main/java/com/project/integration/
│       ├── client/          # Clientes de integração (GitHub, Slack)
│       ├── service/         # Serviço de integração
│       └── config/          # Configurações de integrações
│
└── integration-service/
    ├── src/main/java/com/project/integration/
    │   ├── config/          # Configurações específicas
    │   └── presentation/
    │       └── controller/  # Controllers de integrações
    └── src/main/resources/
        └── application.properties
```

## Tecnologias Utilizadas

- **Java 21** (LTS)
- **Spring Boot 3.5.3**
- **MongoDB** - Banco de dados
- **Spring Data MongoDB** - Acesso a dados
- **Spring Security** - Autenticação e autorização
- **JWT (JJWT)** - Tokens Bearer
- **Swagger/OpenAPI 3.0** - Documentação de API
- **Lombok** - Redução de boilerplate
- **Gradle** - Build tool

## Componentes Principais

### 1. Jira Application (Porto 8080)

#### Autenticação
- **JWT Bearer Token** - Tokens JWT de curta duração
- **3 Ambientes** - Dev, Homolog, Produção
- **Secrets Dinâmicos** - Configuração via variáveis de ambiente

#### Entidades
- **Project** - Projetos com tipos (SOFTWARE, SERVICE_MANAGEMENT, BUSINESS)
- **Issue** - Issues com tipos, prioridades e status
- **User** - Usuários com diferentes papéis
- **Comment** - Comentários em issues

#### Serviços
- **ProjectService** - CRUD de projetos
- **IssueService** - CRUD de issues e filtros
- **UserService** - Gerenciamento de usuários
- **AuthService** - Autenticação e geração de tokens

#### Endpoints de Autenticação
- `POST /api/v1/auth/login` - Login e gerar token
- `POST /api/v1/auth/token/{userId}` - Gerar token para usuário
- `GET /api/v1/auth/validate` - Validar token Bearer

#### Endpoints de Projetos
- `GET /api/v1/projects` - Listar todos (requer token)
- `POST /api/v1/projects` - Criar novo (requer token)
- `GET /api/v1/projects/{id}` - Obter por ID (requer token)
- `PUT /api/v1/projects/{id}` - Atualizar (requer token)
- `DELETE /api/v1/projects/{id}` - Deletar (requer token)

#### Endpoints de Issues
- `GET /api/v1/issues` - Listar issues (requer token)
- `POST /api/v1/issues` - Criar nova (requer token)
- `GET /api/v1/issues/{id}` - Obter por ID (requer token)
- `PUT /api/v1/issues/{id}` - Atualizar (requer token)
- `DELETE /api/v1/issues/{id}` - Deletar (requer token)

#### Endpoints de Saúde
- `GET /api/v1/health` - Health check com info de ambiente
- `GET /api/v1/health/info` - Informações detalhadas da aplicação

### 2. Integration Lib

Biblioteca reutilizável com clients para integrações:

#### GitHubIntegrationClient
```java
- getUser(username) - Obter dados do usuário GitHub
- getRepository(owner, repo) - Obter repositório
- getIssues(owner, repo) - Listar issues do repositório
```

#### SlackIntegrationClient
```java
- sendMessage(channel, message) - Enviar mensagem
- getUser(userId) - Obter usuário
- getChannel(channelId) - Obter canal
```

### 3. Integration Service (Porto 8081)

#### Controllers
- `GET /api/v1/github/users/{username}` - Dados do usuário GitHub
- `GET /api/v1/github/repos/{owner}/{repo}` - Informações do repositório
- `GET /api/v1/github/repos/{owner}/{repo}/issues` - Issues do repositório

- `POST /api/v1/slack/messages` - Enviar mensagem ao Slack
- `GET /api/v1/slack/users/{userId}` - Obter usuário do Slack
- `GET /api/v1/slack/channels/{channelId}` - Obter canal do Slack

## Configuração

### 1. MongoDB

Certifique-se de que MongoDB está rodando:

```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### 2. Docker Compose

Ou use o `compose.yaml` fornecido:

```bash
docker-compose up -d
```

### 3. Variáveis de Ambiente

Configure em `application.properties`:

```properties
# Jira
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/jira_db

# Integration Service
server.port=8081
integration.github.token=seu-token-github
integration.slack.token=xoxb-seu-token-slack
```

## Build e Execução

### Build do projeto
```bash
./gradlew build
```

### Executar Jira Application
```bash
./gradlew :bootRun --args='--spring.profiles.active=jira'
```

### Executar Integration Service
```bash
./gradlew integration-service:bootRun
```

### Acessar APIs

- **Jira Swagger**: http://localhost:8080/swagger-ui.html
- **Jira API Docs**: http://localhost:8080/api-docs

- **Integration Swagger**: http://localhost:8081/swagger-ui.html
- **Integration API Docs**: http://localhost:8081/api-docs

## Exemplos de Uso

### Criar um Projeto
```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Content-Type: application/json" \
  -d '{
    "key": "PROJ",
    "name": "Meu Projeto",
    "description": "Descrição do projeto",
    "lead": "usuario@example.com",
    "projectType": "SOFTWARE"
  }'
```

### Criar uma Issue
```bash
curl -X POST http://localhost:8080/api/v1/issues \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "id-do-projeto",
    "summary": "Corrigir bug de login",
    "description": "O login está falhando",
    "issueType": "BUG",
    "priority": "HIGH",
    "status": "TO_DO",
    "assignee": "usuario@example.com"
  }'
```

### Consultar GitHub
```bash
curl http://localhost:8081/api/v1/github/users/torvalds
```

### Integração com Slack
```bash
curl -X POST http://localhost:8081/api/v1/slack/messages \
  -H "Content-Type: application/json" \
  -d '{
    "channel": "#geral",
    "message": "Nova issue criada no Jira"
  }'
```

## Estrutura de Pasta

```
workspace/jira/
├── build.gradle                    # Build do projeto principal
├── settings.gradle                 # Configuração de módulos
├── compose.yaml                    # Docker Compose
├── gradlew / gradlew.bat          # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/com/project/jira/
│   │   └── resources/
│   └── test/
├── integration-lib/
│   ├── build.gradle
│   ├── src/main/java/com/project/integration/
│   └── src/test/
├── integration-service/
│   ├── build.gradle
│   ├── src/main/java/com/project/integration/
│   ├── src/main/resources/
│   └── src/test/
└── build/                          # Output de build
```

## Testes

### Executar todos os testes
```bash
./gradlew test
```

### Testar módulo específico
```bash
./gradlew :integration-lib:test
./gradlew :integration-service:test
```

## Segurança

### Autenticação
- JWT Bearer tokens para APIs
- Configurado no Swagger

### CORS
- Configurar em caso de consumo por frontend

### Validação
- Validação de entrada em DTOs
- Exception handlers para erros

## Próximas Melhorias

- [ ] Autenticação JWT completa
- [ ] Integração com mais plataformas (Jira Cloud, GitLab, etc)
- [ ] Rate limiting
- [ ] Cache com Redis
- [ ] Testes de integração
- [ ] CI/CD com GitHub Actions
- [ ] Containerização com Docker
- [ ] Kubernetes deployment

## Contribuindo

Este projeto segue as melhores práticas de desenvolvimento Spring Boot.

## Licença

Apache 2.0
