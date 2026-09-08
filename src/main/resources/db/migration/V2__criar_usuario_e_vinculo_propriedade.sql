CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(100) NOT NULL
);

ALTER TABLE conta ADD COLUMN usuario_id UUID NOT NULL REFERENCES usuario (id);
ALTER TABLE cartao_credito ADD COLUMN usuario_id UUID NOT NULL REFERENCES usuario (id);

CREATE INDEX idx_conta_usuario_id ON conta (usuario_id);
CREATE INDEX idx_cartao_credito_usuario_id ON cartao_credito (usuario_id);
