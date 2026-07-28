-- Usuário padrão: admin / admin123 (BCrypt) — trocar a senha no primeiro acesso em produção.
INSERT INTO usuario (nome, username, senha, perfil) VALUES
    ('Gerente Comercial', 'admin', '$2b$10$79O6Vg0CtiF7S1V6OYYUOO.bM/zc/G3xTNTsgiKX5p4MpLGtTNfH6', 'GERENTE');

INSERT INTO vendedor (nome, email, regiao) VALUES
    ('Marcos Teixeira', 'marcos.teixeira@bbts.com.br', 'Sul do Ceará'),
    ('Janaína Souza', 'janaina.souza@bbts.com.br', 'Sertão Central'),
    ('Roberto Lima', 'roberto.lima@bbts.com.br', 'Litoral Oeste');

INSERT INTO cliente (nome, propriedade, cultura, municipio, telefone, status, qualificado, vendedor_id) VALUES
    ('Fazenda Boa Vista', 'Sítio Boa Vista', 'Milho', 'Quixadá', '(88) 99811-2233', 'CONTATADO', TRUE, 1),
    ('Agropecuária Serra Verde', 'Fazenda Serra Verde', 'Soja', 'Iguatu', '(88) 99744-5566', 'VENDIDO', TRUE, 1),
    ('José Carlos Pereira', 'Sítio Riacho Fundo', 'Feijão', 'Tauá', '(88) 99677-8899', 'INDISPONIVEL', TRUE, 2),
    ('Fazenda Três Irmãos', 'Fazenda Três Irmãos', 'Algodão', 'Crateús', '(88) 99622-3344', 'PENDENTE', FALSE, NULL),
    ('Cooperativa Vale Fértil', NULL, 'Milho / Soja', 'Sobral', '(88) 99511-7788', 'CONTATADO', TRUE, 3);
