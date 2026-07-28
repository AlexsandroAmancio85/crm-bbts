-- Campos adicionais do processo de venda (item 4.1 do fluxo do vendedor)
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS cpf VARCHAR(14);
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS convenio VARCHAR(100);
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS prazo INT;
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS valor_vendido DECIMAL(14,2);
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS observacao VARCHAR(1000);

CREATE INDEX IF NOT EXISTS idx_cliente_cpf ON cliente(cpf);
