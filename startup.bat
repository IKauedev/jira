@echo off
REM Script de startup para Jira Backend - Windows

echo ========================================
echo Jira Backend - Startup Script
echo ========================================

REM Verificar se Docker está instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Docker não está instalado ou não está no PATH
    echo Instale Docker Desktop: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Verificar se Docker Compose está instalado
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] Docker Compose não está instalado
    pause
    exit /b 1
)

echo.
echo [INFO] Iniciando serviços com Docker Compose...
docker-compose -f docker-compose.override.yml up -d

if %errorlevel% neq 0 (
    echo [ERRO] Falha ao iniciar Docker Compose
    pause
    exit /b 1
)

REM Aguardar os serviços iniciarem
echo.
echo [INFO] Aguardando serviços iniciarem...
timeout /t 10 /nobreak

REM Verificar status dos containers
echo.
echo [INFO] Status dos containers:
docker-compose ps

REM Verificar conectividade
echo.
echo [INFO] Testando conectividade dos serviços...

echo [TESTE] MongoDB...
docker exec jira-mongodb mongosh --eval "db.runCommand({ping: 1})" --quiet >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] MongoDB está respondendo
) else (
    echo [AVISO] MongoDB pode estar inicializando...
)

echo.
echo [INFO] Testando endpoints...

echo [TESTE] Jira Health Check...
curl -s http://localhost:8080/api/v1/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Jira está respondendo em http://localhost:8080
    echo      Swagger: http://localhost:8080/swagger-ui.html
) else (
    echo [AVISO] Jira ainda pode estar inicializando...
)

echo [TESTE] Integration Service Health Check...
curl -s http://localhost:8081/api/v1/health >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Integration Service está respondendo em http://localhost:8081
    echo      Swagger: http://localhost:8081/swagger-ui.html
) else (
    echo [AVISO] Integration Service ainda pode estar inicializando...
)

echo.
echo ========================================
echo Startup completo!
echo ========================================
echo.
echo URLs Importantes:
echo - Jira Swagger:         http://localhost:8080/swagger-ui.html
echo - Integration Swagger:  http://localhost:8081/swagger-ui.html
echo - MongoDB:              localhost:27017
echo.
echo Comandos úteis:
echo - Ver logs:             docker-compose logs -f
echo - Parar serviços:       docker-compose down
echo - Limpar volumes:       docker-compose down -v
echo.
pause
