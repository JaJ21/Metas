-- V6__seed_admin.sql
-- Usuário admin inicial pra não ficar sem nenhum acesso após o deploy.
-- Senha: "admin123" — hash BCrypt gerado e válido (confirmado com a
-- biblioteca bcrypt). TROQUE essa senha em produção assim que possível
-- (ver README.md, seção "Primeiro acesso").
INSERT INTO usuario (cpf, nome, cargo, senha_hash, roles, data_cadastro)
VALUES (
    '12345678900',
    'Administrador Inicial',
    'TI',
    '$2b$10$2BPOZ0cOtPDgTtAT8PPk8eD.kyMjxaZAgvRzyM2aidf6i8hxYfZmW',
    'ADMIN',
    now()
);
