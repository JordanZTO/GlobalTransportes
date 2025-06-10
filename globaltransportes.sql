drop database globaltransportes;
create database globaltransportes;
use globaltransportes;

CREATE TABLE tipo_usuario (
    tipo_usuario_id TINYINT PRIMARY KEY,
    nome VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE frete_status(
	frete_status_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR (20) UNIQUE NOT NULL
);

CREATE TABLE endereco_frete (
    endereco_frete_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cidade VARCHAR(50) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    pais VARCHAR(50) DEFAULT 'Brasil'
);

CREATE TABLE endereco (
    endereco_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cidade VARCHAR(50) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    pais VARCHAR(50) DEFAULT 'Brasil'
);

CREATE TABLE usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT FALSE,
    tipo_usuario_id TINYINT,
    CONSTRAINT fk_usuario_tipo_usuario FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(tipo_usuario_id)
);

CREATE TABLE caminhao (
    caminhao_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_crlv VARCHAR(20) NOT NULL,
    placa_veiculo VARCHAR(20) NOT NULL,
    ano INT NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    cor VARCHAR(100) NOT NULL,
    quantidade_eixo INT NOT NULL,
    foto_frente VARCHAR(255) NOT NULL,
    foto_placa VARCHAR(255) NOT NULL
);

CREATE TABLE cliente (
    cliente_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    cnpj VARCHAR(100) NOT NULL,
    inscricao_estadual VARCHAR(20) NOT NULL,
    telefone VARCHAR(50) NOT NULL,
    email_comercial VARCHAR(100) NOT NULL,
    valido BOOLEAN DEFAULT FALSE,
    endereco_id BIGINT,
    usuario_id BIGINT UNIQUE,
    CONSTRAINT fk_cliente_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(endereco_id) ON DELETE SET NULL,
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
);

CREATE TABLE motorista (
    motorista_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    cpf VARCHAR (30) NOT NULL,
    numero_cnh VARCHAR (30) NOT NULL,
    numero_antt VARCHAR (30) NOT NULL,
    telefone_pessoal VARCHAR (30) NOT NULL,
	telefone_seguranca_1 VARCHAR(50),
    telefone_seguranca_2 VARCHAR(50),
    telefone_seguranca_3 VARCHAR(50),
    telefone_referencia_1 VARCHAR(50),
    telefone_referencia_2 VARCHAR(50),
    telefone_referencia_3 VARCHAR(50),
    email_comercial VARCHAR (100) NOT NULL,
    foto_cnh VARCHAR (255),
    valido BOOLEAN DEFAULT FALSE,
    endereco_id BIGINT,
    caminhao_id BIGINT,
    usuario_id BIGINT UNIQUE,
    CONSTRAINT fk_motorista_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(endereco_id),
    CONSTRAINT fk_motorista_caminhao FOREIGN KEY (caminhao_id) REFERENCES caminhao(caminhao_id),
    CONSTRAINT fk_motorista_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id)
);

CREATE TABLE frete(
	frete_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_carga VARCHAR (50) NOT NULL,
    valor_frete DECIMAL (10,2),
    peso DOUBLE NOT NULL,
    comprimento DOUBLE NOT NULL,
    largura DOUBLE NOT NULL,
    altura DOUBLE NOT NULL,
    valor_nota_fiscal DECIMAL(10,2) NOT NULL,
    data_criacao DATE NOT NULL,
    data_atualizacao DATE NOT NULL,
	frete_status_id BIGINT,
	endereco_origem_id BIGINT NOT NULL,
    endereco_destino_id BIGINT NOT NULL,
    motorista_id BIGINT,
    cliente_id BIGINT,
    FOREIGN KEY (frete_status_id) REFERENCES frete_status(frete_status_id),
    FOREIGN KEY (endereco_origem_id) REFERENCES endereco_frete(endereco_frete_id),
    FOREIGN KEY (endereco_destino_id) REFERENCES endereco_frete(endereco_frete_id),
	FOREIGN KEY (motorista_id) REFERENCES motorista(motorista_id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
);

CREATE TABLE frete_checkpoint (
    checkpoint_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(2) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    observacoes TEXT,
    frete_status_id BIGINT NOT NULL,
    frete_id BIGINT NOT NULL,
    FOREIGN KEY (frete_status_id) REFERENCES frete_status(frete_status_id),
    FOREIGN KEY (frete_id) REFERENCES frete(frete_id)
);

INSERT INTO tipo_usuario (tipo_usuario_id, nome) VALUES
(1, 'ADMIN'),
(2, 'CLIENTE'),
(3, 'MOTORISTA');

INSERT INTO frete_status (frete_status_id, nome) VALUES
(1, 'PENDENTE'),
(2, 'ACEITO'),
(3, 'ENTREGUE'),
(4, 'CANCELADO'),
(5, 'COLETADO'),
(6, 'EM TRANSITO'),
(7, 'EXTRAVIADO');

INSERT INTO usuario (email, senha, ativo, tipo_usuario_id)
VALUES (
  'admin@gmail.com',
  '$2a$10$GFf0rEKokN6JnQr4MNg3ReKjNH3hMMUm6uozF8BPg8NlRdy.SS2SS',
  TRUE,
  1
);


select * from frete_checkpoint;
select * from frete_status;
select * from cliente;
select * from usuario;
select * from tipo_usuario;
select * from endereco;
select * from motorista;
select * from caminhao;
select * from frete;




