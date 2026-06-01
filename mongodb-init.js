// MongoDB Collections Setup
// Copie e execute estes comandos no MongoDB para inicializar as coleções com dados de exemplo

// ============================================
// Criar Database
// ============================================
use jira_db;

// ============================================
// Collection: users
// ============================================
// NOTA: Todas as senhas são "Password123!" (BCrypt hash)
// Em produção, os usuários devem ser criados via API POST /api/v1/users
db.users.insertMany([
  {
    "_id": ObjectId("66666666666666666666661"),
    "username": "admin.user",
    "email": "admin@example.com",
    "fullName": "Administrador do Sistema",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl1baWRC3Su",
    "avatarUrl": "https://i.pravatar.cc/150?u=admin@example.com",
    "role": "ADMIN",
    "active": true,
    "createdAt": new Date("2026-05-01T10:00:00Z"),
    "updatedAt": new Date("2026-05-31T10:00:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666662"),
    "username": "joao.silva",
    "email": "joao.silva@example.com",
    "fullName": "João Silva",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl1baWRC3Su",
    "avatarUrl": "https://i.pravatar.cc/150?u=joao@example.com",
    "role": "DEVELOPER",
    "active": true,
    "createdAt": new Date("2026-05-05T09:30:00Z"),
    "updatedAt": new Date("2026-05-31T14:20:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666663"),
    "username": "maria.santos",
    "email": "maria.santos@example.com",
    "fullName": "Maria Santos",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl1baWRC3Su",
    "avatarUrl": "https://i.pravatar.cc/150?u=maria@example.com",
    "role": "MANAGER",
    "active": true,
    "createdAt": new Date("2026-05-10T08:00:00Z"),
    "updatedAt": new Date("2026-05-30T16:45:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666664"),
    "username": "carlos.oliveira",
    "email": "carlos.oliveira@example.com",
    "fullName": "Carlos Oliveira",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl1baWRC3Su",
    "avatarUrl": "https://i.pravatar.cc/150?u=carlos@example.com",
    "role": "DEVELOPER",
    "active": true,
    "createdAt": new Date("2026-05-15T11:15:00Z"),
    "updatedAt": new Date("2026-05-28T13:30:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666665"),
    "username": "ana.costa",
    "email": "ana.costa@example.com",
    "fullName": "Ana Costa",
    "password": "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl1baWRC3Su",
    "avatarUrl": "https://i.pravatar.cc/150?u=ana@example.com",
    "role": "VIEWER",
    "active": true,
    "createdAt": new Date("2026-05-20T10:00:00Z"),
    "updatedAt": new Date("2026-05-29T15:00:00Z")
  }
]);

// ============================================
// Collection: projects
// ============================================
db.projects.insertMany([
  {
    "_id": ObjectId("66666666666666666666671"),
    "key": "PROJ",
    "name": "Plataforma Principal",
    "description": "Sistema principal de gerenciamento de projetos",
    "lead": "66666666666666666666663",
    "category": "Desenvolvimento",
    "projectType": "SOFTWARE",
    "createdAt": new Date("2026-05-01T10:00:00Z"),
    "updatedAt": new Date("2026-05-31T10:00:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666672"),
    "key": "INFRA",
    "name": "Infraestrutura",
    "description": "Projeto de infraestrutura e DevOps",
    "lead": "66666666666666666666662",
    "category": "Operações",
    "projectType": "SERVICE_MANAGEMENT",
    "createdAt": new Date("2026-05-05T09:30:00Z"),
    "updatedAt": new Date("2026-05-25T14:20:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666673"),
    "key": "MOBILE",
    "name": "Aplicativo Mobile",
    "description": "Aplicação mobile para iOS e Android",
    "lead": "66666666666666666666664",
    "category": "Desenvolvimento Mobile",
    "projectType": "SOFTWARE",
    "createdAt": new Date("2026-05-10T08:00:00Z"),
    "updatedAt": new Date("2026-05-30T16:45:00Z")
  }
]);

// ============================================
// Collection: issues
// ============================================
db.issues.insertMany([
  {
    "_id": ObjectId("66666666666666666666681"),
    "key": "PROJ-1",
    "projectId": "66666666666666666666671",
    "summary": "Implementar autenticação OAuth2",
    "description": "Adicionar suporte a autenticação OAuth2 para integração com provedores terceiros",
    "issueType": "FEATURE",
    "priority": "HIGH",
    "status": "IN_PROGRESS",
    "assignee": "66666666666666666666662",
    "reporter": "66666666666666666666663",
    "labels": ["backend", "auth", "security"],
    "components": ["authentication"],
    "createdAt": new Date("2026-05-10T10:00:00Z"),
    "updatedAt": new Date("2026-05-28T14:30:00Z"),
    "dueDate": new Date("2026-06-15T23:59:59Z")
  },
  {
    "_id": ObjectId("66666666666666666666682"),
    "key": "PROJ-2",
    "projectId": "66666666666666666666671",
    "summary": "Corrigir bug na validação de email",
    "description": "Emails válidos estão sendo rejeitados pelo validador",
    "issueType": "BUG",
    "priority": "HIGHEST",
    "status": "IN_REVIEW",
    "assignee": "66666666666666666666664",
    "reporter": "66666666666666666666665",
    "labels": ["backend", "bug"],
    "components": ["validation"],
    "createdAt": new Date("2026-05-15T09:15:00Z"),
    "updatedAt": new Date("2026-05-30T11:45:00Z"),
    "dueDate": new Date("2026-06-05T23:59:59Z")
  },
  {
    "_id": ObjectId("66666666666666666666683"),
    "key": "PROJ-3",
    "projectId": "66666666666666666666671",
    "summary": "Melhorar performance de consultas MongoDB",
    "description": "Otimizar índices e queries para reduzir tempo de resposta",
    "issueType": "IMPROVEMENT",
    "priority": "MEDIUM",
    "status": "TO_DO",
    "assignee": "66666666666666666666662",
    "reporter": "66666666666666666666663",
    "labels": ["backend", "performance", "database"],
    "components": ["database"],
    "createdAt": new Date("2026-05-18T13:00:00Z"),
    "updatedAt": new Date("2026-05-25T10:30:00Z"),
    "dueDate": new Date("2026-07-01T23:59:59Z")
  },
  {
    "_id": ObjectId("66666666666666666666684"),
    "key": "INFRA-1",
    "projectId": "66666666666666666666672",
    "summary": "Configurar CI/CD pipeline",
    "description": "Implementar pipeline automatizado de integração contínua",
    "issueType": "TASK",
    "priority": "HIGH",
    "status": "IN_PROGRESS",
    "assignee": "66666666666666666666662",
    "reporter": "66666666666666666666663",
    "labels": ["devops", "ci-cd"],
    "components": ["pipeline"],
    "createdAt": new Date("2026-05-12T11:00:00Z"),
    "updatedAt": new Date("2026-05-29T15:20:00Z"),
    "dueDate": new Date("2026-06-10T23:59:59Z")
  },
  {
    "_id": ObjectId("66666666666666666666685"),
    "key": "MOBILE-1",
    "projectId": "66666666666666666666673",
    "summary": "Design da tela de login",
    "description": "Criar design da interface de login para aplicativo mobile",
    "issueType": "TASK",
    "priority": "MEDIUM",
    "status": "DONE",
    "assignee": "66666666666666666666664",
    "reporter": "66666666666666666666663",
    "labels": ["frontend", "ui"],
    "components": ["ui"],
    "createdAt": new Date("2026-05-01T10:00:00Z"),
    "updatedAt": new Date("2026-05-20T16:00:00Z"),
    "dueDate": new Date("2026-05-25T23:59:59Z")
  },
  {
    "_id": ObjectId("66666666666666666666686"),
    "key": "PROJ-4",
    "projectId": "66666666666666666666671",
    "summary": "Epic: Sistema de Notificações",
    "description": "Implementar sistema completo de notificações em tempo real",
    "issueType": "EPIC",
    "priority": "HIGH",
    "status": "TO_DO",
    "assignee": null,
    "reporter": "66666666666666666666663",
    "labels": ["notifications", "feature"],
    "components": ["notification-service"],
    "createdAt": new Date("2026-05-22T14:00:00Z"),
    "updatedAt": new Date("2026-05-31T10:00:00Z"),
    "dueDate": new Date("2026-08-31T23:59:59Z")
  }
]);

// ============================================
// Collection: comments
// ============================================
db.comments.insertMany([
  {
    "_id": ObjectId("66666666666666666666691"),
    "issueId": "66666666666666666666681",
    "author": "66666666666666666666662",
    "body": "Já comecei a implementação. Será necessário ajustar algumas configurações de segurança.",
    "createdAt": new Date("2026-05-20T10:30:00Z"),
    "updatedAt": new Date("2026-05-20T10:30:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666692"),
    "issueId": "66666666666666666666681",
    "author": "66666666666666666666663",
    "body": "Ótimo! Por favor, documente todas as mudanças no arquivo SECURITY.md",
    "createdAt": new Date("2026-05-21T09:15:00Z"),
    "updatedAt": new Date("2026-05-21T09:15:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666693"),
    "issueId": "66666666666666666666682",
    "author": "66666666666666666666664",
    "body": "Encontrei o problema. Era uma regex inválida no validador de email.",
    "createdAt": new Date("2026-05-25T14:00:00Z"),
    "updatedAt": new Date("2026-05-25T14:00:00Z")
  },
  {
    "_id": ObjectId("66666666666666666666694"),
    "issueId": "66666666666666666666684",
    "author": "66666666666666666666662",
    "body": "Vou usar GitHub Actions para o pipeline. Já tenho uma base pronta.",
    "createdAt": new Date("2026-05-23T11:45:00Z"),
    "updatedAt": new Date("2026-05-23T11:45:00Z")
  }
]);

// ============================================
// Criar Índices
// ============================================

// Índices para Users
db.users.createIndex({ "username": 1 }, { unique: true });
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "role": 1 });
db.users.createIndex({ "active": 1 });

// Índices para Projects
db.projects.createIndex({ "key": 1 }, { unique: true });
db.projects.createIndex({ "lead": 1 });
db.projects.createIndex({ "projectType": 1 });

// Índices para Issues
db.issues.createIndex({ "key": 1 }, { unique: true });
db.issues.createIndex({ "projectId": 1 });
db.issues.createIndex({ "assignee": 1 });
db.issues.createIndex({ "reporter": 1 });
db.issues.createIndex({ "status": 1 });
db.issues.createIndex({ "priority": 1 });
db.issues.createIndex({ "createdAt": 1 });
db.issues.createIndex({ "dueDate": 1 });

// Índices para Comments
db.comments.createIndex({ "issueId": 1 });
db.comments.createIndex({ "author": 1 });
db.comments.createIndex({ "createdAt": 1 });

// Índices para Password Reset Tokens
db.password_reset_tokens.createIndex({ "token": 1 }, { unique: true });
db.password_reset_tokens.createIndex({ "userId": 1 });
db.password_reset_tokens.createIndex({ "email": 1 });
db.password_reset_tokens.createIndex({ "expiresAt": 1 }, { expireAfterSeconds: 0 });

// ============================================
// Validação
// ============================================

print("\n=== Contagem de Documentos ===");
print("Users: " + db.users.countDocuments());
print("Projects: " + db.projects.countDocuments());
print("Issues: " + db.issues.countDocuments());
print("Comments: " + db.comments.countDocuments());

print("\n=== Coleções Criadas com Sucesso ===");
print("Disponibilize este script para inicializar o banco de dados MongoDB");
