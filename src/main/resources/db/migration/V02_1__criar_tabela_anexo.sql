CREATE TABLE anexo (
    codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    tipo VARCHAR(100),
    dados LONGBLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (1).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (2).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (3).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (4).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (5).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (6).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (7).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (8).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (9).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (10).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (11).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (12).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (13).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (14).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));
INSERT INTO anexo (nome, tipo, dados) VALUES ('relatorio (15).pdf', 'application/pdf', LOAD_FILE('C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'));