#!/bin/bash

# Script de startup para Jira Backend - Linux/Mac

echo "========================================"
echo "Jira Backend - Startup Script"
echo "========================================"

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "[ERRO] Docker não está instalado"
    echo "Instale Docker: https://docs.docker.com/get-docker/"
    exit 1
fi

# Verificar se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "[ERRO] Docker Compose não está instalado"
    exit 1
fi

echo ""
echo "[INFO] Iniciando serviços com Docker Compose..."
docker-compose -f docker-compose.override.yml up -d

if [ $? -ne 0 ]; then
    echo "[ERRO] Falha ao iniciar Docker Compose"
    exit 1
fi

# Aguardar os serviços iniciarem
echo ""
echo "[INFO] Aguardando serviços iniciarem..."
sleep 10

# Verificar status dos containers
echo ""
echo "[INFO] Status dos containers:"
docker-compose ps

# Verificar conectividade
echo ""
echo "[INFO] Testando conectividade dos serviços..."

echo "[TESTE] MongoDB..."
docker exec jira-mongodb mongosh --eval "db.runCommand({ping: 1})" --quiet >/dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "[OK] MongoDB está respondendo"
else
    echo "[AVISO] MongoDB pode estar inicializando..."
fi

echo ""
echo "[INFO] Testando endpoints..."

echo "[TESTE] Jira Health Check..."
curl -s http://localhost:8080/api/v1/health >/dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "[OK] Jira está respondendo em http://localhost:8080"
    echo "     Swagger: http://localhost:8080/swagger-ui.html"
else
    echo "[AVISO] Jira ainda pode estar inicializando..."
fi

echo "[TESTE] Integration Service Health Check..."
curl -s http://localhost:8081/api/v1/health >/dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "[OK] Integration Service está respondendo em http://localhost:8081"
    echo "     Swagger: http://localhost:8081/swagger-ui.html"
else
    echo "[AVISO] Integration Service ainda pode estar inicializando..."
fi

echo ""
echo "========================================"
echo "Startup completo!"
echo "========================================"
echo ""
echo "URLs Importantes:"
echo "- Jira Swagger:         http://localhost:8080/swagger-ui.html"
echo "- Integration Swagger:  http://localhost:8081/swagger-ui.html"
echo "- MongoDB:              localhost:27017"
echo ""
echo "Comandos úteis:"
echo "- Ver logs:             docker-compose logs -f"
echo "- Parar serviços:       docker-compose down"
echo "- Limpar volumes:       docker-compose down -v"
echo ""
