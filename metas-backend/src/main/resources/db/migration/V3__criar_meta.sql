-- V3__criar_meta.sql
-- Cabeçalho de cada linha de meta (Centro de Custo + Cod Conta é a chave
-- que decide substituição/inserção no upsert — ver MetaGatewayImpl).
CREATE TABLE meta (
    id                     BIGSERIAL    NOT NULL,
    centro_custo           VARCHAR(50)  NOT NULL,
    cod_conta              VARCHAR(50)  NOT NULL,
    justificativa          VARCHAR(1000),
    cpf_ultima_alteracao   VARCHAR(11),
    data_ultima_alteracao  TIMESTAMP,
    CONSTRAINT pk_meta PRIMARY KEY (id),
    CONSTRAINT uk_meta_centro_custo_cod_conta UNIQUE (centro_custo, cod_conta)
);
