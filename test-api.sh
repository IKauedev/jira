#!/bin/bash
# Script de Testes - Exemplos cURL para API Jira Backend
# Use este script para testar a API via linha de comando

BASE_URL="http://localhost:8080"
INTEGRATION_URL="http://localhost:8081"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}Testes cURL - Jira Backend API${NC}"
echo -e "${YELLOW}========================================${NC}\n"

# ============================================
# HEALTH CHECK
# ============================================
echo -e "${GREEN}[1] Health Check - Jira App${NC}"
curl -X GET "${BASE_URL}/actuator/health" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# ============================================
# USUÁRIOS
# ============================================
echo -e "${GREEN}[2] Listar todos os usuários${NC}"
curl -X GET "${BASE_URL}/api/v1/users" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[3] Criar novo usuário${NC}"
curl -X POST "${BASE_URL}/api/v1/users" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "novo.usuario",
    "email": "novo.usuario@example.com",
    "fullName": "Novo Usuario",
    "avatarUrl": "https://i.pravatar.cc/150?u=novo@example.com",
    "role": "DEVELOPER",
    "active": true
  }' \
  -w "\nStatus: %{http_code}\n\n"

# ============================================
# PROJETOS
# ============================================
echo -e "${GREEN}[4] Listar todos os projetos${NC}"
curl -X GET "${BASE_URL}/api/v1/projects" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[5] Criar novo projeto${NC}"
curl -X POST "${BASE_URL}/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "TEST",
    "name": "Projeto Teste",
    "description": "Projeto criado via cURL",
    "lead": "66666666666666666666662",
    "category": "Testes",
    "projectType": "SOFTWARE"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# ============================================
# ISSUES
# ============================================
echo -e "${GREEN}[6] Listar todas as issues${NC}"
curl -X GET "${BASE_URL}/api/v1/issues" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[7] Criar nova issue${NC}"
curl -X POST "${BASE_URL}/api/v1/issues" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "TEST-1",
    "projectId": "66666666666666666666671",
    "summary": "Issue de teste via cURL",
    "description": "Descrição da issue de teste",
    "issueType": "TASK",
    "priority": "MEDIUM",
    "status": "TO_DO",
    "assignee": "66666666666666666666662",
    "labels": ["teste", "api"],
    "components": ["test"]
  }' \
  -w "\nStatus: %{http_code}\n\n"

# ============================================
# GRAPHQL
# ============================================
echo -e "${GREEN}[8] GraphQL - Query Projetos${NC}"
curl -X POST "${BASE_URL}/graphql" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { projects { id key name description lead projectType createdAt } }"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[9] GraphQL - Query Issues${NC}"
curl -X POST "${BASE_URL}/graphql" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { issues { id key summary issueType priority status assignee createdAt } }"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# ============================================
# INTEGRAÇÕES
# ============================================
echo -e "${GREEN}[10] Health Check - Integration Service${NC}"
curl -X GET "${INTEGRATION_URL}/actuator/health" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[11] GitHub Integration - Obter usuário${NC}"
curl -X GET "${INTEGRATION_URL}/api/v1/integrations/github/users/torvalds" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${GREEN}[12] GitHub Integration - Obter repositório${NC}"
curl -X GET "${INTEGRATION_URL}/api/v1/integrations/github/repos/torvalds/linux" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}Testes Concluídos!${NC}"
echo -e "${YELLOW}========================================${NC}"
