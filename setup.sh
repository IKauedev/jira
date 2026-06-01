#!/bin/bash
# Script de Setup - Inicializa o ambiente completo da API Jira

echo "=========================================="
echo "Setup Completo - Jira Backend API"
echo "=========================================="
echo ""

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker não está instalado${NC}"
    exit 1
fi

# Verificar se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose não está instalado${NC}"
    exit 1
fi

echo -e "${YELLOW}[1/4] Iniciando containers Docker...${NC}"
docker-compose -f docker/docker-compose.override.yml up -d

echo -e "${YELLOW}Aguardando MongoDB iniciar...${NC}"
sleep 10

echo -e "${YELLOW}[2/4] Populando banco de dados MongoDB...${NC}"
# Tentar conectar e executar o script
mongosh --host localhost:27017 -u admin -p password < mongodb-init.js > /dev/null 2>&1 || \
  docker exec jira-mongodb mongosh -u admin -p password < mongodb-init.js || \
  echo -e "${YELLOW}Nota: Execute manualmente: mongosh < mongodb-init.js${NC}"

echo -e "${YELLOW}[3/4] Compilando aplicação...${NC}"
./gradlew build -x test -q

echo -e "${YELLOW}[4/4] Iniciando aplicação...${NC}"
./gradlew bootRun &

echo ""
echo -e "${GREEN}=========================================="
echo -e "Setup Concluído com Sucesso!"
echo -e "==========================================${NC}"
echo ""
echo "URLs de Acesso:"
echo -e "  Jira App: ${YELLOW}http://localhost:8080${NC}"
echo -e "  Swagger UI: ${YELLOW}http://localhost:8080/swagger-ui.html${NC}"
echo -e "  Integration Service: ${YELLOW}http://localhost:8081${NC}"
echo -e "  MongoDB: ${YELLOW}localhost:27017${NC}"
echo ""
echo "Próximos Passos:"
echo "  1. Importe collection.json no Postman/Insomnia"
echo "  2. Use os endpoints pré-configurados para testar a API"
echo "  3. Verifique os logs em tempo real"
echo ""
echo "Para parar os containers:"
echo "  docker-compose -f docker/docker-compose.override.yml down"
echo ""
