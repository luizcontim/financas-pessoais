CREATE TABLE conta (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL DEFAULT 0,
    moeda VARCHAR(3) NOT NULL DEFAULT 'BRL'
);

CREATE TABLE movimentacao (
    id UUID PRIMARY KEY,
    conta_id UUID NOT NULL REFERENCES conta (id),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    data_movimentacao DATE NOT NULL
);

CREATE INDEX idx_movimentacao_conta_id ON movimentacao (conta_id);

CREATE TABLE cartao_credito (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    bandeira VARCHAR(30),
    limite NUMERIC(19, 2) NOT NULL,
    dia_fechamento INT NOT NULL CHECK (dia_fechamento BETWEEN 1 AND 28),
    dia_vencimento INT NOT NULL CHECK (dia_vencimento BETWEEN 1 AND 28)
);

CREATE TABLE fatura (
    id UUID PRIMARY KEY,
    cartao_id UUID NOT NULL REFERENCES cartao_credito (id),
    mes_referencia INT NOT NULL CHECK (mes_referencia BETWEEN 1 AND 12),
    ano_referencia INT NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('ABERTA', 'FECHADA', 'PAGA')),
    UNIQUE (cartao_id, mes_referencia, ano_referencia)
);

CREATE INDEX idx_fatura_cartao_id ON fatura (cartao_id);

CREATE TABLE compra (
    id UUID PRIMARY KEY,
    cartao_id UUID NOT NULL REFERENCES cartao_credito (id),
    descricao VARCHAR(255) NOT NULL,
    valor_total NUMERIC(19, 2) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    data_compra DATE NOT NULL,
    quantidade_parcelas INT NOT NULL DEFAULT 1
);

CREATE INDEX idx_compra_cartao_id ON compra (cartao_id);

CREATE TABLE parcela (
    id UUID PRIMARY KEY,
    fatura_id UUID NOT NULL REFERENCES fatura (id),
    compra_id UUID NOT NULL REFERENCES compra (id),
    descricao VARCHAR(255) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    numero INT NOT NULL,
    total_parcelas INT NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    UNIQUE (compra_id, numero)
);

CREATE INDEX idx_parcela_fatura_id ON parcela (fatura_id);
