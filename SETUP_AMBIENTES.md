# 📚 Guia Completo - Ambientes e Configurações Dinâmicas

## 🎯 Resumo Executivo

A aplicação Jira Backend foi configurada para suportar **3 ambientes independentes** com secrets, credenciais e configurações dinâmicas:

| Aspecto | Dev | Homolog | Prod |
|--------|-----|---------|------|
| 🔑 JWT Secret | Fixo (dev) | Fixo (teste) | **Variável ENV** |
| ⏰ JWT Expiration | 24h | 7 dias | 1h |
| 📊 Banco de Dados | Local | Remoto | Remoto + Auth |
| 📝 Logs | DEBUG | INFO | WARN |
| 🔍 Swagger | ✅ Ativo | ✅ Ativo | ❌ Inativo |
| 🛠️ DevTools | ✅ Ativo | ❌ Inativo | ❌ Inativo |

---

## 📁 Arquivos de Configuração Criados

### Configurações por Profile

```
src/main/resources/
├── application.properties           # Base (fallback)
├── application.yml                  # Base YAML
├── application-dev.properties       # Dev - Properties
├── application-dev.yml              # Dev - YAML
├── application-homolog.properties   # Homolog - Properties
├── application-homolog.yml          # Homolog - YAML
├── application-prod.properties      # Prod - Properties
└── application-prod.yml             # Prod - YAML
```

### Arquivo de Exemplo

```
.env.example                         # Template de variáveis
```

---

## 🔐 JWT Secret Dinâmico

### Como Funciona

**Em Development/Homolog:**
```yaml
jwt.secret: "valor-fixo-no-properties"
```

**Em Production:**
```yaml
jwt.secret: ${JWT_SECRET}  # Lê da variável de ambiente
```

### Configurar em Produção

**Opção 1: Variável de Ambiente**
```bash
export JWT_SECRET="gX5kL7mN9oP2qR4sT6uV8wX0yZ1aB3cD4eF6gH8iJ"
java -jar app.jar --spring.profiles.active=prod
```

**Opção 2: Docker**
```bash
docker run \
  -e JWT_SECRET="gX5kL7mN9oP2qR4sT6uV8wX0yZ1aB3cD4eF6gH8iJ" \
  -e SPRING_PROFILES_ACTIVE=prod \
  jira-backend:latest
```

**Opção 3: Docker Compose**
```yaml
services:
  jira-app:
    image: jira-backend:latest
    environment:
      SPRING_PROFILES_ACTIVE: prod
      JWT_SECRET: ${JWT_SECRET}  # Vem do .env
```

**Opção 4: Kubernetes Secret**
```bash
kubectl create secret generic jira-jwt \
  --from-literal=secret="gX5kL7mN9oP2qR4sT6uV8wX0yZ1aB3cD4eF6gH8iJ"

# No deployment:
env:
  - name: JWT_SECRET
    valueFrom:
      secretKeyRef:
        name: jira-jwt
        key: secret
```

---

## 🗄️ MongoDB Dinâmico

### Development
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/jira_db
```

### Homolog
```properties
spring.data.mongodb.uri=mongodb://mongodb-homolog:27017/jira_db_homolog
```

### Production
```properties
spring.data.mongodb.uri=${MONGODB_URI}
```

**Exemplo de URI com autenticação:**
```
mongodb://admin:senha123@mongodb-prod:27017/jira_db_prod?authSource=admin&replicaSet=rs0
```

---

## 🚀 Como Iniciar em Cada Ambiente

### Development
```bash
# Padrão (dev é o profile padrão)
./gradlew bootRun

# Ou explicitamente
./gradlew bootRun --args='--spring.profiles.active=dev'

# Verificar
curl http://localhost:8080/api/v1/health
# Resposta: environment: "development"
```

### Homologation
```bash
# Via aplicação empacotada
java -jar jira-1.0.0-SNAPSHOT.jar --spring.profiles.active=homolog

# Via Docker
docker run \
  -e SPRING_PROFILES_ACTIVE=homolog \
  -p 8080:8080 \
  jira-backend:latest

# Verificar
curl http://localhost:8080/api/v1/health
# Resposta: environment: "homologation"
```

### Production
```bash
# Com variáveis obrigatórias
export JWT_SECRET="sua-chave-segura-aqui"
export MONGODB_URI="mongodb://admin:senha@mongo-prod:27017/jira_db_prod?authSource=admin"

java -jar jira-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod

# Via Docker Compose
docker-compose -f docker-compose-prod.yml up

# Verificar
curl http://localhost:8080/api/v1/health
# Resposta: environment: "production"
```

---

## 📝 Gerar Secret Forte

### Opção 1: OpenSSL (Recomendado)
```bash
openssl rand -base64 32
# Resultado: kL9mN2oP5qR8sT1uV4wX7yZ0aB3cD6eF9gH2iJ
```

### Opção 2: Node.js
```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

### Opção 3: Python
```bash
python3 -c "import secrets; print(secrets.token_urlsafe(32))"
```

### Opção 4: Java
```bash
java -jar -cp /path/to/spring-boot-app.jar org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
```

### Opção 5: PowerShell
```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RNGCryptoServiceProvider]::new()
$rng.GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

---

## ⚙️ Configurações por Ambiente

### Development (application-dev.yml)
```yaml
spring:
  profiles:
    active: dev
  data:
    mongodb:
      uri: mongodb://localhost:27017/jira_db
  devtools:
    restart:
      enabled: true

jwt:
  secret: dev-secret-key-only-for-development-not-secure-at-all
  expiration: 86400000  # 24 horas

logging:
  level:
    root: INFO
    com.project.jira: DEBUG
    org.springframework.security: DEBUG

springdoc:
  swagger-ui:
    enabled: true

app:
  environment: development
```

### Homolog (application-homolog.yml)
```yaml
spring:
  profiles:
    active: homolog
  data:
    mongodb:
      uri: mongodb://mongodb-homolog:27017/jira_db_homolog
  devtools:
    restart:
      enabled: false

jwt:
  secret: homolog-secret-key-for-testing-environment-should-be-strong
  expiration: 604800000  # 7 dias

logging:
  level:
    root: WARN
    com.project.jira: INFO
    org.springframework.security: WARN

springdoc:
  swagger-ui:
    enabled: true

app:
  environment: homologation
```

### Production (application-prod.yml)
```yaml
spring:
  profiles:
    active: prod
  data:
    mongodb:
      uri: ${MONGODB_URI}  # Variável de ambiente
  devtools:
    restart:
      enabled: false

jwt:
  secret: ${JWT_SECRET}  # Variável de ambiente
  expiration: 3600000    # 1 hora

logging:
  level:
    root: WARN
    com.project.jira: WARN
    org.springframework.security: ERROR

springdoc:
  swagger-ui:
    enabled: false

server:
  compression:
    enabled: true

app:
  environment: production
```

---

## 🔄 Trocar Ambiente em Runtime

### Via Linha de Comando
```bash
# Dev para Homolog
java -jar app.jar --spring.profiles.active=homolog

# Homolog para Prod
java -jar app.jar --spring.profiles.active=prod --JWT_SECRET=xyz
```

### Via Variável de Ambiente
```bash
# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET="sua-chave-aqui"
java -jar app.jar

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:JWT_SECRET = "sua-chave-aqui"
java -jar app.jar
```

### Via Docker
```bash
docker run -e SPRING_PROFILES_ACTIVE=prod -e JWT_SECRET=xyz jira:latest
```

---

## 📊 Verificar Ambiente Ativo

### Endpoint Health
```bash
curl http://localhost:8080/api/v1/health

# Resposta:
{
  "status": "UP",
  "service": "Jira Backend Application",
  "version": "1.0.0",
  "environment": "development",
  "profile": "dev",
  "timestamp": 1716594000000
}
```

### Endpoint Info
```bash
curl http://localhost:8080/api/v1/health/info

# Resposta:
{
  "application": "Jira Backend Application",
  "version": "1.0.0",
  "environment": "development",
  "profile": "dev",
  "runtime": "512MB",
  "javaVersion": "21.0.0",
  "osName": "Linux"
}
```

### Via Logs
```
[main] INFO o.s.b.w.e.t.TomcatWebServer : Tomcat initialized with port(s): 8080 (http)
[main] INFO c.p.j.c.SecurityConfig : JWT configuration loaded for profile: dev
```

---

## 🛡️ Boas Práticas de Segurança

### ✅ SIM - Faça Assim
```bash
# 1. Gere uma chave forte
export JWT_SECRET=$(openssl rand -base64 32)

# 2. Use variáveis de ambiente
java -jar app.jar --spring.profiles.active=prod

# 3. Não deixe secrets nos logs
logging.level.security=ERROR

# 4. Use HTTPS em produção
server.ssl.key-store=/path/to/keystore.jks
server.ssl.key-store-password=${KEYSTORE_PASSWORD}

# 5. Implemente rate limiting
spring.security.ratelimit.enabled=true

# 6. Use secrets do Docker/Kubernetes
docker secret create jwt_secret -
```

### ❌ NÃO - Nunca Faça Assim
```bash
# ❌ Não coloque secrets no código
jwt.secret=minha-chave-super-segura-123456789

# ❌ Não versionem .env
git add .env

# ❌ Não coloque secrets em comments
# JWT_SECRET=minha-chave-aqui

# ❌ Não use hardcoded em aplicação-prod.properties
jwt.secret=senha-fixa-em-produção

# ❌ Não deixe DevTools em produção
spring.devtools.restart.enabled=true

# ❌ Não deixe Swagger ativo em produção
springdoc.swagger-ui.enabled=true
```

---

## 📋 Checklist de Implantação

### Development ✅
- [ ] Profile: dev
- [ ] MongoDB: localhost:27017
- [ ] Logs: DEBUG
- [ ] Swagger: Habilitado
- [ ] DevTools: Habilitado
- [ ] JWT Secret: Fixo (inseguro OK para dev)

### Homolog ✅
- [ ] Profile: homolog
- [ ] MongoDB: mongodb-homolog:27017
- [ ] Logs: INFO/WARN
- [ ] Swagger: Habilitado
- [ ] DevTools: Desabilitado
- [ ] JWT Secret: Alterado
- [ ] Teste: Fazer login e gerar token

### Production ✅
- [ ] Profile: prod
- [ ] `JWT_SECRET` definido em variável de ambiente
- [ ] `MONGODB_URI` com credenciais
- [ ] Logs: WARN/ERROR
- [ ] Swagger: Desabilitado
- [ ] DevTools: Desabilitado
- [ ] Compressão: Habilitada
- [ ] HTTPS: Configurado
- [ ] Firewall: Configurado
- [ ] Monitoramento: Ativado
- [ ] Backups: Programados
- [ ] Testes: Health check OK

---

## 🔧 Gerenciamento Avançado

### Multi-Profile em um Único Servidor
```bash
# Executar dev e prod em portas diferentes
java -jar app.jar --spring.profiles.active=dev --server.port=8080 &
java -jar app.jar --spring.profiles.active=prod --server.port=8081 &
```

### Docker Swarm / Kubernetes
```yaml
services:
  jira-dev:
    image: jira:latest
    environment:
      SPRING_PROFILES_ACTIVE: dev
    ports:
      - "8080:8080"

  jira-prod:
    image: jira:latest
    environment:
      SPRING_PROFILES_ACTIVE: prod
      JWT_SECRET: ${JWT_SECRET}
    ports:
      - "443:8080"
    secrets:
      - jwt_secret
```

### Gradual Rollout (Blue-Green Deployment)
```bash
# 1. Deploy Blue (atual - prod)
# 2. Deploy Green (novo - prod com nova config)
# 3. Teste Green
# 4. Switch traffic: Blue → Green
# 5. Rollback se necessário: Green → Blue
```

---

## 📞 Suporte e Troubleshooting

| Problema | Cause | Solução |
|----------|-------|---------|
| JWT token inválido em prod | Secret diferente | Sincronize `JWT_SECRET` |
| MongoDB não conecta homolog | Credenciais erradas | Verificar `MONGODB_URI` |
| Swagger desaparece | Profile prod ativo | Mudar para dev/homolog |
| Logs vazios | Level muito alto | Reduzir nível em prod |
| App não inicia prod | Faltam variáveis ENV | Definir `JWT_SECRET` e `MONGODB_URI` |
| Token expira rápido | Expiration baixo | Verificar `jwt.expiration` |

---

## 📚 Arquivos de Referência

- 📄 `AUTHENTICATION.md` - Autenticação JWT completa
- 📄 `ENVIRONMENTS.md` - Detalhes dos ambientes
- 📄 `.env.example` - Template de variáveis
- 📄 `application.yml` - Configuração base
- 📄 `application-dev.yml` - Config development
- 📄 `application-homolog.yml` - Config homolog
- 📄 `application-prod.yml` - Config production

---

## ✨ Resumo das Novidades

✅ 3 ambientes independentes (dev, homolog, prod)  
✅ JWT Secret dinâmico via variáveis de ambiente  
✅ MongoDB com credenciais em produção  
✅ Configurações diferentes por ambiente  
✅ Health endpoint mostra ambiente ativo  
✅ Arquivo .env.example para referência  
✅ Documentação completa de setup  

**Agora sua aplicação está pronta para múltiplos ambientes! 🚀**

