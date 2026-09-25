-- V4__criar_meta_valor.sql
-- Valores mensais, normalizados (uma linha por mês, não uma coluna por
-- mês) — assim o schema não precisa mudar todo ano quando o ciclo
-- orçamentário virar (ver CicloOrcamentario no core do módulo meta).
CREATE TABLE meta_valor (
    id       BIGSERIAL      NOT NULL,
    meta_id  BIGINT         NOT NULL,
    ano      SMALLINT       NOT NULL,
    mes      SMALLINT       NOT NULL,
    tipo     VARCHAR(10)    NOT NULL CHECK (tipo IN ('REAL', 'FORECAST', 'ORCADO')),
    valor    NUMERIC(18,2)  NOT NULL DEFAULT 0,
    CONSTRAINT pk_meta_valor PRIMARY KEY (id),
    CONSTRAINT fk_meta_valor_meta FOREIGN KEY (meta_id) REFERENCES meta (id) ON DELETE CASCADE,
    CONSTRAINT uk_meta_valor_meta_ano_mes UNIQUE (meta_id, ano, mes)
);

CREATE INDEX idx_meta_valor_meta_id ON meta_valor (meta_id);
