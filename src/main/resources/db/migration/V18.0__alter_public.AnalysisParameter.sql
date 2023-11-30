ALTER TABLE public."AnalysisParameter"
    ADD COLUMN IF NOT EXISTS "orderNumber" INTEGER DEFAULT 0;

