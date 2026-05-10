ALTER TABLE lancamento
ADD COLUMN codigo_usuario BIGINT;

ALTER TABLE lancamento
ADD CONSTRAINT fk_lancamento_usuario
FOREIGN KEY (codigo_usuario)
REFERENCES usuario(codigo);