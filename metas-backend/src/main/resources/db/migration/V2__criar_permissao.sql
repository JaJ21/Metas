-- V2__criar_permissao.sql
-- Read/Write/Delete por CPF + Centro de Custo + Cod Conta.
CREATE TABLE permissao (
    id            BIGSERIAL    NOT NULL,
    cpf           VARCHAR(11)  NOT NULL,
    centro_custo  VARCHAR(50)  NOT NULL,
    cod_conta     VARCHAR(50)  NOT NULL,
    leitura       BOOLEAN      NOT NULL DEFAULT FALSE,
    escrita       BOOLEAN      NOT NULL DEFAULT FALSE,
    exclusao      BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_permissao PRIMARY KEY (id),
    CONSTRAINT uk_permissao_cpf_centro_conta UNIQUE (cpf, centro_custo, cod_conta)
);

CREATE INDEX idx_permissao_cpf ON permissao (cpf);
