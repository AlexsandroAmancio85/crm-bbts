-- Vincula conta de usuário ao registro de vendedor (perfil VENDEDOR)
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS vendedor_id BIGINT REFERENCES vendedor(id);
CREATE INDEX IF NOT EXISTS idx_usuario_vendedor ON usuario(vendedor_id);
