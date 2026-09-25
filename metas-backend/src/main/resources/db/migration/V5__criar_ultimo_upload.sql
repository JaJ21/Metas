-- V5__criar_ultimo_upload.sql
-- Cache do último upload confirmado de cada usuário (não acumula
-- histórico — cada novo upload substitui o anterior desse mesmo CPF).
CREATE TABLE ultimo_upload (
    cpf                     VARCHAR(11)  NOT NULL,
    nome_arquivo_original   VARCHAR(255),
    data_upload             TIMESTAMP    NOT NULL,
    linhas                  INT          NOT NULL,
    substituidas            INT          NOT NULL,
    novas                   INT          NOT NULL,
    CONSTRAINT pk_ultimo_upload PRIMARY KEY (cpf)
);

CREATE TABLE ultimo_upload_chave (
    id            BIGSERIAL    NOT NULL,
    cpf           VARCHAR(11)  NOT NULL,
    centro_custo  VARCHAR(50)  NOT NULL,
    cod_conta     VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_ultimo_upload_chave PRIMARY KEY (id),
    CONSTRAINT fk_ultimo_upload_chave_upload FOREIGN KEY (cpf) REFERENCES ultimo_upload (cpf) ON DELETE CASCADE
);

CREATE INDEX idx_ultimo_upload_chave_cpf ON ultimo_upload_chave (cpf);
