# AWS Deployment

Este projeto pode rodar na AWS sem alterar sua arquitetura principal: dois containers Spring Boot, MongoDB externo/gerenciado, segredos injetados no runtime e envio de e-mail via Amazon SES.

## Servicos Recomendados

- Amazon ECR para armazenar as imagens `jira-app` e `integration-service`.
- AWS App Runner para publicar os containers com menor complexidade operacional.
- AWS Secrets Manager ou SSM Parameter Store para valores sensiveis.
- Amazon SES via SMTP para envio de redefinicao de senha.
- CloudWatch Logs para centralizar logs dos containers e acompanhar falhas.

## Segredos e Parametros

Crie os valores abaixo no Secrets Manager ou no SSM Parameter Store e injete-os como variaveis de ambiente no App Runner.

## Perfis do Integration Service

O `integration-service` possui perfis proprios para controlar seguranca e configuracao por ambiente:

- `local`: usa MongoDB local e permite `INTEGRATION_API_KEY` vazia para facilitar testes.
- `dev`: usa MongoDB local por padrao e define a chave de desenvolvimento `dev-integration-api-key-change-me`, que pode ser sobrescrita por variavel de ambiente.
- `homolog`: exige `INTEGRATION_MONGODB_URI` e `INTEGRATION_API_KEY`.
- `prod`: exige `INTEGRATION_MONGODB_URI` e `INTEGRATION_API_KEY`, desliga Swagger por padrao e reduz verbosidade dos logs.

Para gerar uma chave forte no PowerShell:

```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

Com OpenSSL:

```bash
openssl rand -base64 32
```

Use uma chave diferente para cada ambiente. Em chamadas HTTP para `/api/v1/**`, envie:

```http
X-Integration-Api-Key: <valor-de-INTEGRATION_API_KEY>
```

Para `jira-app`:

- `SPRING_PROFILES_ACTIVE=prod`
- `MONGODB_URI`
- `JWT_SECRET`
- `EMAIL_ENABLED=true`
- `SES_SMTP_HOST`, por exemplo `email-smtp.us-east-1.amazonaws.com`
- `SES_SMTP_PORT=587`
- `SES_SMTP_USERNAME`
- `SES_SMTP_PASSWORD`
- `SES_FROM_EMAIL`
- `PASSWORD_RESET_BASE_URL`
- `PASSWORD_RESET_INCLUDE_TOKEN_IN_RESPONSE=false`
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info`

Para `integration-service`:

- `SPRING_PROFILES_ACTIVE=prod`
- `INTEGRATION_MONGODB_URI`
- `GITHUB_TOKEN`
- `SLACK_TOKEN` ou `SLACK_BOT_TOKEN`
- `INTEGRATION_API_KEY`
- `INTEGRATION_API_KEY_HEADER=X-Integration-Api-Key`
- `SWAGGER_ENABLED=false`
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info`

## Build e Push no ECR

Substitua os placeholders de conta, regiao e repositorio conforme seu ambiente.

```powershell
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

docker build -f docker/Dockerfile.jira -t jira-app .
docker tag jira-app:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/jira-app:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/jira-app:latest

docker build -f docker/Dockerfile.integration -t integration-service .
docker tag integration-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/integration-service:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/integration-service:latest
```

## App Runner

Crie um servico App Runner para cada imagem do ECR.

Para `jira-app`:

- Porta: `8080`
- Health check HTTP: `/actuator/health`
- Variaveis e segredos: use os nomes listados na secao de `jira-app`.

Para `integration-service`:

- Porta: `8081`
- Health check HTTP: `/actuator/health`
- Variaveis e segredos: use os nomes listados na secao de `integration-service`.
- Ao chamar rotas `/api/v1/**`, envie o header `X-Integration-Api-Key` com o valor de `INTEGRATION_API_KEY`.

## Amazon SES

Configure um dominio ou remetente verificado no SES e gere credenciais SMTP. Em sandbox, o destinatario tambem precisa ser verificado.

O fluxo de redefinicao usa `SES_SMTP_*`, `SES_FROM_EMAIL` e `PASSWORD_RESET_BASE_URL`. Em producao, o token nao deve ser retornado na resposta HTTP e `PASSWORD_RESET_INCLUDE_TOKEN_IN_RESPONSE` deve permanecer `false`.

O app tambem aceita as variaveis antigas `MAIL_*` como fallback, mas para AWS prefira os nomes `SES_*`.

## Observabilidade

App Runner envia stdout/stderr para CloudWatch Logs. O projeto tambem expoe `/actuator/health` para health checks e readiness/liveness probes.

Alarmes iniciais recomendados:

- `5xx` alto no App Runner.
- Instancias reiniciando ou deploy com falha.
- Logs contendo `ERROR`.
- Latencia ou falha no envio SMTP do SES.
