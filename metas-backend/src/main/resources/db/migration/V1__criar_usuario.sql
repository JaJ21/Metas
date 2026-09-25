-- V1__criar_usuario.sql
-- Cadastro de usuários (CPF, Nome, Cargo, senha com hash, papéis/roles).
CREATE TABLE usuario (
    cpf             VARCHAR(11)  NOT NULL,
    nome            VARCHAR(255) NOT NULL,
    cargo           VARCHAR(255),
    senha_hash      VARCHAR(255),
    roles           VARCHAR(255),
    data_cadastro   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (cpf)
);
