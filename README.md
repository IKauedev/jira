# Jira Backend

Backend em Java 21 com Spring Boot para gerenciamento de projetos, issues, usuários, comentários, autenticação JWT, GraphQL e serviços de integração.

## Serviços

- `jira-app`: API principal na porta `8080`.
- `integration-service`: API de integrações na porta `8081`.
- `mongodb`: banco de dados usado pelos serviços.

## Tecnologias

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Security
- Spring Data MongoDB
- Spring GraphQL
- SpringDoc OpenAPI / Swagger UI
- JWT com JJWT
- Lombok
- Gradle
- Docker Compose

## Executar

```bash
docker-compose -f docker/docker-compose.override.yml up --build
```

Se estiver dentro da pasta `docker`, use:

```bash
docker-compose -f docker-compose.override.yml up --build
```

Ou localmente:

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

## URLs

- Jira API: `http://localhost:8080`
- Jira Swagger: `http://localhost:8080/swagger-ui.html`
- Jira OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- GraphiQL: `http://localhost:8080/graphiql`
- GraphQL endpoint: `POST http://localhost:8080/graphql`
- Integration Swagger: `http://localhost:8081/swagger-ui.html`
- Integration OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Autenticação

A maioria das rotas da API principal usa Bearer token JWT.

Usuários de seed em `mongodb-init.js` usam a senha:

```text
Password123!
```

Login:

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin.user",
  "password": "Password123!"
}
```

Use o token retornado nas rotas protegidas:

```http
Authorization: Bearer <token>
```

No Swagger, clique em **Authorize** e informe:

```text
Bearer <token>
```

## Rotas Públicas

- `GET /api/v1/health`
- `GET /api/v1/health/info`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `GET /api/v1/auth/reset-password/validate`
- `POST /api/v1/users`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/graphiql`

## Rotas Protegidas

Requerem `Authorization: Bearer <token>`:

- `GET /api/v1/auth/validate`
- `POST /api/v1/auth/change-password`
- `GET /api/v1/auth/me`
- `GET /api/v1/users/**`
- `PUT /api/v1/users/**`
- `GET|POST|PUT|DELETE /api/v1/projects/**`
- `GET|POST|PUT|DELETE /api/v1/issues/**`
- `GET|POST|PUT|DELETE /api/v1/comments/**`
- `POST /graphql`

Requerem usuário `ADMIN`:

- `POST /api/v1/auth/token/{userId}`
- `POST /api/v1/users/{id}/activate`
- `POST /api/v1/users/{id}/deactivate`
- `DELETE /api/v1/users/{id}`

## Endpoints Principais

### Autenticação

- `POST /api/v1/auth/login`
- `GET /api/v1/auth/validate`
- `GET /api/v1/auth/me`
- `POST /api/v1/auth/change-password`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `GET /api/v1/auth/reset-password/validate?token=...`
- `POST /api/v1/auth/token/{userId}` (ADMIN)

### Usuários

- `POST /api/v1/users`
- `GET /api/v1/users`
- `GET /api/v1/users/{id}`
- `GET /api/v1/users/username/{username}`
- `GET /api/v1/users/email/{email}`
- `GET /api/v1/users/role/{role}`
- `PUT /api/v1/users/{id}`
- `DELETE /api/v1/users/{id}` (ADMIN)
- `POST /api/v1/users/{id}/activate` (ADMIN)
- `POST /api/v1/users/{id}/deactivate` (ADMIN)

### Projetos

- `GET /api/v1/projects`
- `POST /api/v1/projects`
- `GET /api/v1/projects/{id}`
- `GET /api/v1/projects/key/{key}`
- `GET /api/v1/projects/lead/{lead}`
- `PUT /api/v1/projects/{id}`
- `DELETE /api/v1/projects/{id}`

### Issues

- `POST /api/v1/issues`
- `GET /api/v1/issues/{id}`
- `GET /api/v1/issues/key/{key}`
- `GET /api/v1/issues/project/{projectId}`
- `GET /api/v1/issues/assignee/{assignee}`
- `GET /api/v1/issues/status/{status}`
- `GET /api/v1/issues/project/{projectId}/status/{status}`
- `PUT /api/v1/issues/{id}`
- `DELETE /api/v1/issues/{id}`

### Comentários

- `POST /api/v1/comments`
- `GET /api/v1/comments/{id}`
- `GET /api/v1/comments/issue/{issueId}`
- `GET /api/v1/comments/author/{author}`
- `PUT /api/v1/comments/{id}`
- `DELETE /api/v1/comments/{id}`

## GraphQL

O endpoint GraphQL fica em:

```text
POST /graphql
```

Ele requer Bearer token. Para testar no navegador, use:

```text
http://localhost:8080/graphiql
```

Exemplo:

```graphql
query {
  projects {
    id
    key
    name
    description
    lead
    projectType
    createdAt
  }
}
```

Exemplo com filtro:

```graphql
query {
  issuesByStatus(status: IN_PROGRESS) {
    id
    key
    summary
    priority
    status
    assignee
    createdAt
  }
}
```

Consultas disponíveis:

- `projects`, `project`, `projectByKey`, `projectsByLead`
- `issues`, `issue`, `issueByKey`, `issuesByProjectId`, `issuesByAssignee`, `issuesByStatus`, `issuesByProjectAndStatus`
- `users`, `user`, `userByUsername`, `userByEmail`, `usersByRole`

## Integration Service

Swagger:

```text
http://localhost:8081/swagger-ui.html
```

Rotas:

- `GET /api/v1/github/users/{username}`
- `GET /api/v1/github/repos/{owner}/{repo}`
- `GET /api/v1/github/repos/{owner}/{repo}/issues`
- `POST /api/v1/slack/messages?channel={channel}&message={message}`
- `GET /api/v1/slack/users/{userId}`
- `GET /api/v1/slack/channels/{channelId}`

## Collection

A collection Postman/Insomnia fica em:

```text
collection.json
```

Fluxo recomendado:

1. Execute **Autenticação / Login e Gerar Token**.
2. A collection salva o token em `bearer_token`.
3. Execute as rotas protegidas normalmente.

## Build e Testes

```bash
./gradlew build
./gradlew test
```

No Windows:

```powershell
.\gradlew.bat build
.\gradlew.bat test
```

## AWS

O projeto pode ser publicado na AWS usando ECR + App Runner, Secrets Manager/SSM, SES e CloudWatch Logs. O guia de variáveis, segredos e comandos de deploy está em [`AWS_DEPLOYMENT.md`](AWS_DEPLOYMENT.md).
