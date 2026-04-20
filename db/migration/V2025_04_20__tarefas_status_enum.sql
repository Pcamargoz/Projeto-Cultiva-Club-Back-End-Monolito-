-- =====================================================================
-- Migração: trocar coluna `concluida` (boolean) por `status` (enum text)
-- =====================================================================
-- Contexto:
--   A entidade `Tarefas` passou a usar o enum STATUS_TAREFAS
--   (PENDENTE, CONCLUIDO, EXCLUIDO). A coluna `concluida` booleana foi
--   substituída por `status` em texto (EnumType.STRING).
--
--   EXCLUIDO é usado como soft delete — a tarefa permanece no banco mas
--   não aparece nas respostas da API.
--
-- Como executar:
--   Rode este script no banco de produção (Neon/Supabase) ANTES de
--   subir a nova versão do backend. Spring Boot está com
--   `ddl-auto: validate`, então se o schema estiver desalinhado a
--   aplicação falha no boot.
--
--   Rodar em UMA transação para não deixar o banco num estado parcial.
-- =====================================================================

BEGIN;

-- 1) Adiciona a nova coluna `status` (inicialmente nullable para permitir backfill).
ALTER TABLE public."Tarefas"
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);

-- 2) Backfill: converte o booleano existente no enum correspondente.
--    TRUE  -> CONCLUIDO
--    FALSE -> PENDENTE
UPDATE public."Tarefas"
SET status = CASE
    WHEN concluida IS TRUE  THEN 'CONCLUIDO'
    WHEN concluida IS FALSE THEN 'PENDENTE'
    ELSE 'PENDENTE'
END
WHERE status IS NULL;

-- 3) Tornamos a coluna NOT NULL agora que todas as linhas foram preenchidas
--    e colocamos o default de PENDENTE para inserts futuros feitos fora da JPA.
ALTER TABLE public."Tarefas"
    ALTER COLUMN status SET NOT NULL,
    ALTER COLUMN status SET DEFAULT 'PENDENTE';

-- 4) Constraint de integridade: só aceita valores do enum.
ALTER TABLE public."Tarefas"
    DROP CONSTRAINT IF EXISTS tarefas_status_check;
ALTER TABLE public."Tarefas"
    ADD CONSTRAINT tarefas_status_check
    CHECK (status IN ('PENDENTE', 'CONCLUIDO', 'EXCLUIDO'));

-- 5) Remove a coluna antiga `concluida`.
ALTER TABLE public."Tarefas"
    DROP COLUMN IF EXISTS concluida;

COMMIT;

-- =====================================================================
-- Verificação pós-migração (rodar manualmente após o COMMIT):
--
--   SELECT status, COUNT(*) FROM public."Tarefas" GROUP BY status;
--
-- Esperado: apenas PENDENTE / CONCLUIDO (EXCLUIDO só aparece conforme
-- usuários deletam tarefas pela API após o deploy).
-- =====================================================================
