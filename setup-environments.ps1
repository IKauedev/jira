# Script de Setup - Jira Backend com Múltiplos Ambientes (PowerShell)

Write-Host ""
Write-Host "╔══════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║       Jira Backend API - Setup Múltiplos Ambientes          ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# 1. Selecionar ambiente
Write-Host "[1/5] Selecione o ambiente:" -ForegroundColor Yellow
Write-Host "1) Development (dev)"
Write-Host "2) Homologation (homolog)"
Write-Host "3) Production (prod)"
Write-Host ""
$ENVIRONMENT_CHOICE = Read-Host "Escolha (1-3)"

switch ($ENVIRONMENT_CHOICE) {
  "1" {
    $PROFILE = "dev"
    $PROFILE_NAME = "Development"
  }
  "2" {
    $PROFILE = "homolog"
    $PROFILE_NAME = "Homologation"
  }
  "3" {
    $PROFILE = "prod"
    $PROFILE_NAME = "Production"
  }
  default {
    Write-Host "❌ Opção inválida!" -ForegroundColor Red
    exit 1
  }
}

Write-Host "✅ Ambiente selecionado: $PROFILE_NAME" -ForegroundColor Green
Write-Host ""

# 2. Preparar .env em produção
if ($PROFILE -eq "prod") {
  Write-Host "[2/5] Configurar variáveis de ambiente" -ForegroundColor Yellow

  if (-not (Test-Path ".env")) {
    Write-Host "Criando arquivo .env..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
  }

  Write-Host ""
  $JWT_SECRET_INPUT = Read-Host "Digite o JWT_SECRET (ou pressione Enter para gerar um novo)"

  if ([string]::IsNullOrWhiteSpace($JWT_SECRET_INPUT)) {
    # Gerar chave aleatória
    $bytes = New-Object byte[] 32
    $rng = [System.Security.Cryptography.RNGCryptoServiceProvider]::new()
    $rng.GetBytes($bytes)
    $JWT_SECRET = [Convert]::ToBase64String($bytes)
    Write-Host "✅ JWT Secret gerado: $JWT_SECRET" -ForegroundColor Green
  }
  else {
    $JWT_SECRET = $JWT_SECRET_INPUT
  }

  Write-Host ""
  $MONGODB_URI = Read-Host "Digite a MONGODB_URI"

  if ([string]::IsNullOrWhiteSpace($MONGODB_URI)) {
    Write-Host "❌ MONGODB_URI é obrigatório em produção!" -ForegroundColor Red
    exit 1
  }

  # Atualizar .env
  (Get-Content ".env") -replace 'JWT_SECRET=.*', "JWT_SECRET=$JWT_SECRET" | Set-Content ".env"
  (Get-Content ".env") -replace 'MONGODB_URI=.*', "MONGODB_URI=$MONGODB_URI" | Set-Content ".env"
  (Get-Content ".env") -replace 'SPRING_PROFILES_ACTIVE=.*', "SPRING_PROFILES_ACTIVE=$PROFILE" | Set-Content ".env"

  $env:JWT_SECRET = $JWT_SECRET
  $env:MONGODB_URI = $MONGODB_URI
  $env:SPRING_PROFILES_ACTIVE = $PROFILE

  Write-Host "✅ Variáveis de ambiente carregadas" -ForegroundColor Green
}
else {
  Write-Host "[2/5] Configurar variáveis de ambiente" -ForegroundColor Yellow
  $env:SPRING_PROFILES_ACTIVE = $PROFILE
  Write-Host "✅ Ambiente $PROFILE_NAME selecionado" -ForegroundColor Green
}

Write-Host ""

# 3. Verificar Docker
Write-Host "[3/5] Verificar Docker" -ForegroundColor Yellow

try {
  $null = docker --version
  Write-Host "Docker encontrado ✅" -ForegroundColor Green
  $DOCKER_AVAILABLE = $true
}
catch {
  Write-Host "Docker não encontrado" -ForegroundColor Yellow
  $DOCKER_AVAILABLE = $false
}

Write-Host ""

# 4. Iniciar MongoDB (se Docker disponível)
if ($DOCKER_AVAILABLE) {
  Write-Host "[4/5] Iniciar MongoDB?" -ForegroundColor Yellow
  $MONGO_CHOICE = Read-Host "Deseja iniciar MongoDB via Docker? (s/n)"

  if ($MONGO_CHOICE -eq "s" -or $MONGO_CHOICE -eq "S") {
    Write-Host "Iniciando MongoDB..." -ForegroundColor Yellow

    if ($PROFILE -ne "prod") {
      try {
        docker-compose -f docker-compose.override.yml up -d mongodb
      }
      catch {
        docker run -d -p 27017:27017 --name jira-mongodb mongo:6
      }

      Write-Host "✅ MongoDB iniciado" -ForegroundColor Green
      Start-Sleep -Seconds 3
    }
    else {
      Write-Host "Pulando MongoDB em produção (usar servidor remoto)" -ForegroundColor Yellow
    }
  }
}

Write-Host ""

# 5. Compilar e iniciar aplicação
Write-Host "[5/5] Compilar e iniciar aplicação" -ForegroundColor Yellow
Write-Host ""

if ($PROFILE -eq "prod") {
  Write-Host "Compilando para produção..." -ForegroundColor Yellow
  & ".\gradlew.bat" clean build -x test --no-daemon

  Write-Host "✅ Build concluído" -ForegroundColor Green
  Write-Host ""
  Write-Host "Para iniciar em produção, execute:" -ForegroundColor Yellow
  Write-Host "java -jar build/libs/jira-*.jar --spring.profiles.active=prod" -ForegroundColor Cyan
}
else {
  Write-Host "Iniciando aplicação em $PROFILE_NAME..." -ForegroundColor Yellow
  & ".\gradlew.bat" bootRun --args="--spring.profiles.active=$PROFILE" --no-daemon
}

Write-Host ""
Write-Host "╔══════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                    Setup Concluído! ✅                       ║" -ForegroundColor Green
Write-Host "╚══════════════════════════════════════════════════════════════╝" -ForegroundColor Green
