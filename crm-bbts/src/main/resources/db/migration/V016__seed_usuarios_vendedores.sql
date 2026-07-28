-- Cria contas de acesso para os três vendedores do seed (senha padrão: vendedor123)
-- Hash BCrypt de "vendedor123"
INSERT INTO usuario (nome, username, senha, perfil, vendedor_id)
SELECT 'Marcos Teixeira', 'marcos.teixeira', '$2b$10$Z4Y8CktYIZjEC5pSjfEIF.jR./po5ImQK5zKPEI/BBMJF8x2bp3sK', 'VENDEDOR', id
FROM vendedor WHERE email = 'marcos.teixeira@bbts.com.br';

INSERT INTO usuario (nome, username, senha, perfil, vendedor_id)
SELECT 'Janaína Souza', 'janaina.souza', '$2b$10$Z4Y8CktYIZjEC5pSjfEIF.jR./po5ImQK5zKPEI/BBMJF8x2bp3sK', 'VENDEDOR', id
FROM vendedor WHERE email = 'janaina.souza@bbts.com.br';

INSERT INTO usuario (nome, username, senha, perfil, vendedor_id)
SELECT 'Roberto Lima', 'roberto.lima', '$2b$10$Z4Y8CktYIZjEC5pSjfEIF.jR./po5ImQK5zKPEI/BBMJF8x2bp3sK', 'VENDEDOR', id
FROM vendedor WHERE email = 'roberto.lima@bbts.com.br';
