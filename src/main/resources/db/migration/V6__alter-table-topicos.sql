-- 1. Remove a coluna de texto antiga
ALTER TABLE topicos DROP COLUMN autor;

-- 2. Adiciona a coluna que o Hibernate está procurando (autor_id)
-- Certifique-se de que existe um usuário com ID 1 antes de rodar com o DEFAULT
ALTER TABLE topicos ADD COLUMN autor_id BIGINT NOT NULL DEFAULT 1;

-- 3. Cria a restrição de chave estrangeira
ALTER TABLE topicos
ADD CONSTRAINT fk_topicos_autor
FOREIGN KEY (autor_id) REFERENCES usuarios(id);