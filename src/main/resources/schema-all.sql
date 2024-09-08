DROP TABLE IF EXISTS PRECO;

CREATE TABLE Preco
(
    preco_id             BIGINT         NOT NULL AUTO_INCREMENT,
    preco_normal         DECIMAL(19, 2) NOT NULL,
    preco_promocional    DECIMAL(19, 2),
    data_inicio_promocao DATE,
    data_fim_promocao    DATE,
    item_id              BIGINT         NOT NULL,
    PRIMARY KEY (preco_id)
);

-- Definindo AUTO_INCREMENT começando 21 para tabela Preco
ALTER TABLE PRECO
    ALTER COLUMN preco_id RESTART WITH 21;
