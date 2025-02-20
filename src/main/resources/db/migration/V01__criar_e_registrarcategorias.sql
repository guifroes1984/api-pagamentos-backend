CREATE TABLE TB_CATEGORIA (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO TB_CATEGORIA (nome) values ('Lazer');
INSERT INTO TB_CATEGORIA (nome) values ('Alimentação');
INSERT INTO TB_CATEGORIA (nome) values ('Supermercado');
INSERT INTO TB_CATEGORIA (nome) values ('Farmácia');
INSERT INTO TB_CATEGORIA (nome) values ('Outros');