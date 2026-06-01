# Script de Testes - Exemplos PowerShell para API Jira Backend
# Use este script para testar a API via PowerShell no Windows

$BASE_URL = "http://localhost:8080"
$INTEGRATION_URL = "http://localhost:8081"

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Testes PowerShell - Jira Backend API" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

# ============================================
# HEALTH CHECK
# ============================================
Write-Host "[1] Health Check - Jira App" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/actuator/health" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Status: OK" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# USUÁRIOS
# ============================================
Write-Host "[2] Listar todos os usuários" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/users" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Usuários encontrados:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[3] Criar novo usuário" -ForegroundColor Green
$novoUsuario = @{
    username = "novo.usuario"
    email = "novo.usuario@example.com"
    fullName = "Novo Usuario"
    avatarUrl = "https://i.pravatar.cc/150?u=novo@example.com"
    role = "DEVELOPER"
    active = $true
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/users" `
        -Method Post `
        -Headers @{ "Content-Type" = "application/json" } `
        -Body $novoUsuario
    Write-Host "Usuário criado com sucesso!" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# PROJETOS
# ============================================
Write-Host "[4] Listar todos os projetos" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/projects" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Projetos encontrados:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[5] Criar novo projeto" -ForegroundColor Green
$novoProjeto = @{
    key = "TEST"
    name = "Projeto Teste"
    description = "Projeto criado via PowerShell"
    lead = "66666666666666666666662"
    category = "Testes"
    projectType = "SOFTWARE"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/projects" `
        -Method Post `
        -Headers @{ "Content-Type" = "application/json" } `
        -Body $novoProjeto
    Write-Host "Projeto criado com sucesso!" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# ISSUES
# ============================================
Write-Host "[6] Listar todas as issues" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/issues" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Issues encontradas:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[7] Criar nova issue" -ForegroundColor Green
$novaIssue = @{
    key = "TEST-1"
    projectId = "66666666666666666666671"
    summary = "Issue de teste via PowerShell"
    description = "Descrição da issue de teste"
    issueType = "TASK"
    priority = "MEDIUM"
    status = "TO_DO"
    assignee = "66666666666666666666662"
    labels = @("teste", "api")
    components = @("test")
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/v1/issues" `
        -Method Post `
        -Headers @{ "Content-Type" = "application/json" } `
        -Body $novaIssue
    Write-Host "Issue criada com sucesso!" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# GRAPHQL
# ============================================
Write-Host "[8] GraphQL - Query Projetos" -ForegroundColor Green
$graphqlQuery1 = @{
    query = "query { projects { id key name description lead projectType createdAt } }"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/graphql" `
        -Method Post `
        -Headers @{ "Content-Type" = "application/json" } `
        -Body $graphqlQuery1
    Write-Host "Resultado GraphQL:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[9] GraphQL - Query Issues" -ForegroundColor Green
$graphqlQuery2 = @{
    query = "query { issues { id key summary issueType priority status assignee createdAt } }"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/graphql" `
        -Method Post `
        -Headers @{ "Content-Type" = "application/json" } `
        -Body $graphqlQuery2
    Write-Host "Resultado GraphQL:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# INTEGRAÇÕES
# ============================================
Write-Host "[10] Health Check - Integration Service" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$INTEGRATION_URL/actuator/health" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Status: OK" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[11] GitHub Integration - Obter usuário" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$INTEGRATION_URL/api/v1/integrations/github/users/torvalds" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Dados do usuário GitHub:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "[12] GitHub Integration - Obter repositório" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$INTEGRATION_URL/api/v1/integrations/github/repos/torvalds/linux" `
        -Method Get `
        -Headers @{ "Accept" = "application/json" }
    Write-Host "Dados do repositório GitHub:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
} catch {
    Write-Host "Erro: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Testes Concluídos!" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
