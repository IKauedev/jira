#!/bin/bash
# Script de Setup - Jira Backend com Múltiplos Ambientes

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║       Jira Backend API - Setup Múltiplos Ambientes          ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
echo ""

# 1. Selecionar ambiente
echo -e "${YELLOW}[1/5] Selecione o ambiente:${NC}"
echo "1) Development (dev)"
echo "2) Homologation (homolog)"
echo "3) Production (prod)"
echo ""
read -p "Escolha (1-3): " ENVIRONMENT_CHOICE

case $ENVIRONMENT_CHOICE in
  1)
    PROFILE="dev"
    PROFILE_NAME="Development"
    ;;
  2)
    PROFILE="homolog"
    PROFILE_NAME="Homologation"
    ;;
  3)
    PROFILE="prod"
    PROFILE_NAME="Production"
    ;;
  *)
    echo -e "${RED}❌ Opção inválida!${NC}"
    exit 1
    ;;
esac

echo -e "${GREEN}✅ Ambiente selecionado: ${PROFILE_NAME}${NC}"
echo ""

# 2. Preparar .env em produção
if [ "$PROFILE" = "prod" ]; then
  echo -e "${YELLOW}[2/5] Configurar variáveis de ambiente${NC}"

  if [ ! -f .env ]; then
    echo -e "${YELLOW}Criando arquivo .env...${NC}"
    cp .env.example .env
  fi

  echo ""
  read -p "Digite o JWT_SECRET (ou pressione Enter para gerar um novo): " JWT_SECRET_INPUT

  if [ -z "$JWT_SECRET_INPUT" ]; then
    JWT_SECRET=$(openssl rand -base64 32)
    echo -e "${GREEN}✅ JWT Secret gerado: ${YELLOW}${JWT_SECRET}${NC}"
  else
    JWT_SECRET=$JWT_SECRET_INPUT
  fi

  echo ""
  read -p "Digite a MONGODB_URI: " MONGODB_URI

  if [ -z "$MONGODB_URI" ]; then
    echo -e "${RED}❌ MONGODB_URI é obrigatório em produção!${NC}"
    exit 1
  fi

  # Atualizar .env
  sed -i "s|JWT_SECRET=.*|JWT_SECRET=${JWT_SECRET}|" .env
  sed -i "s|MONGODB_URI=.*|MONGODB_URI=${MONGODB_URI}|" .env
  sed -i "s|SPRING_PROFILES_ACTIVE=.*|SPRING_PROFILES_ACTIVE=${PROFILE}|" .env

  export JWT_SECRET
  export MONGODB_URI
  export SPRING_PROFILES_ACTIVE=$PROFILE

  echo -e "${GREEN}✅ Variáveis de ambiente carregadas${NC}"
else
  echo -e "${YELLOW}[2/5] Configurar variáveis de ambiente${NC}"
  export SPRING_PROFILES_ACTIVE=$PROFILE
  echo -e "${GREEN}✅ Ambiente ${PROFILE_NAME} selecionado${NC}"
fi

echo ""

# 3. Verificar Docker
if command -v docker &> /dev/null; then
  echo -e "${YELLOW}[3/5] Docker encontrado ✅${NC}"
  DOCKER_AVAILABLE=true
else
  echo -e "${YELLOW}[3/5] Docker não encontrado${NC}"
  DOCKER_AVAILABLE=false
fi

echo ""

# 4. Iniciar MongoDB (se Docker disponível)
if [ "$DOCKER_AVAILABLE" = true ]; then
  echo -e "${YELLOW}[4/5] Iniciar MongoDB?${NC}"
  read -p "Deseja iniciar MongoDB via Docker? (s/n): " MONGO_CHOICE

  if [ "$MONGO_CHOICE" = "s" ] || [ "$MONGO_CHOICE" = "S" ]; then
    echo -e "${YELLOW}Iniciando MongoDB...${NC}"

    if [ "$PROFILE" = "prod" ]; then
      echo -e "${YELLOW}Pulando MongoDB em produção (usar servidor remoto)${NC}"
    else
      docker-compose -f docker/docker-compose.override.yml up -d mongodb 2>/dev/null || \
      docker run -d -p 27017:27017 --name jira-mongodb mongo:6

      echo -e "${GREEN}✅ MongoDB iniciado${NC}"
      sleep 3
    fi
  fi
fi

echo ""

# 5. Compilar e iniciar aplicação
echo -e "${YELLOW}[5/5] Compilar e iniciar aplicação${NC}"
echo ""

if [ "$PROFILE" = "prod" ]; then
  echo -e "${YELLOW}Compilando para produção...${NC}"
  ./gradlew clean build -x test --no-daemon

  echo -e "${GREEN}✅ Build concluído${NC}"
  echo ""
  echo -e "${YELLOW}Para iniciar em produção, execute:${NC}"
  echo -e "${BLUE}java -jar build/libs/jira-*.jar --spring.profiles.active=prod${NC}"
else
  echo -e "${YELLOW}Iniciando aplicação em ${PROFILE_NAME}...${NC}"
  ./gradlew bootRun --args="--spring.profiles.active=${PROFILE}" --no-daemon
fi

echo ""
echo -e "${GREEN}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                    Setup Concluído! ✅                       ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════════════════╝${NC}"
