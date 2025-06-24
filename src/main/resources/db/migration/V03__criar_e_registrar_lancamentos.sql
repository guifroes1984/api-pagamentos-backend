CREATE TABLE lancamento (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	codigo_categoria BIGINT(20) NOT NULL,
	codigo_pessoa BIGINT(20) NOT NULL,
	codigo_anexo BIGINT(20),
	
	
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo), 
	FOREIGN KEY (codigo_anexo) REFERENCES anexo(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Salário mensal', '2025-01-27', null, 6500.00, 'Distribuição de lucros', 'RECEITA', 2, 1, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Supermercado', '2025-05-12', null, 3500.00, null, 'RECEITA', 1, 2, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Academia', '2025-05-15', null, 120, null, 'DESPESA', 4, 3, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Conta de luz', '2025-06-20', null, 110.44, null, 'DESPESA', 2, 4, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Conta de água', '2025-05-25', null, 200.30, 'Copel', 'DESPESA', 5, 5, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Restaurante', '2025-06-14', '2025-11-01', 1010.32, null, 'DESPESA', 4, 6, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Venda vídeo game', '2025-01-01', null, 500, null, 'RECEITA', 1, 7, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Clube', '2025-03-07', '2025-11-01', 400.32, null, 'DESPESA', 4, 8, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Impostos', '2025-04-10', null, 123.64, 'Multas', 'DESPESA', 3, 9, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Multa', '2025-04-10', null, 665.33, null, 'DESPESA', 5, 10, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Padaria', '2025-02-28', '2025-02-28', 8.32, null, 'DESPESA', 1, 5, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Papelaria', '2025-02-10', '2025-04-10', 2100.32, null, 'DESPESA', 5, 4, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Almoço', '2025-03-09', '2025-11-01', 1040.32, null, 'DESPESA', 4, 3, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Café', '2025-02-20', '2025-02-18', 4.32, null, 'DESPESA', 4, 2, LAST_INSERT_ID());
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa, codigo_anexo) values ('Lanche', '2025-04-10', null, 10.20, null, 'DESPESA', 4, 1, LAST_INSERT_ID());
