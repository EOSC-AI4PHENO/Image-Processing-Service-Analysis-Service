DROP TABLE IF EXISTS "public"."AnalysisParameter" CASCADE;

CREATE TABLE "public"."AnalysisParameter"
(
	id bigserial NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"desc" VARCHAR(255) NULL,
	"value" VARCHAR(255) NOT NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."AnalysisParameter" ADD CONSTRAINT "PK_AnalysisParameter"	PRIMARY KEY (id);