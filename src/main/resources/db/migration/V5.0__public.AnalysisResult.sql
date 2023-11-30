DROP TABLE IF EXISTS "public"."AnalysisResult" CASCADE;

CREATE TABLE "public"."AnalysisResult"
(
	id bigserial NOT NULL,
	"detailsId" BIGINT NOT NULL,
	"parameterId" BIGINT NOT NULL,
	"value" NUMERIC(16,5) NOT NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."AnalysisResult" ADD CONSTRAINT "PK_AnalysisResult"	PRIMARY KEY (id);

ALTER TABLE "public"."AnalysisResult" ADD CONSTRAINT "FK_AnalysisResult_AnalysisDetail"
	FOREIGN KEY ("detailsId") REFERENCES "public"."AnalysisDetail" (id) ON DELETE No Action ON UPDATE No Action;

ALTER TABLE "public"."AnalysisResult" ADD CONSTRAINT "FK_AnalysisResult_AnalysisParameter"
	FOREIGN KEY ("parameterId") REFERENCES "public"."AnalysisParameter" (id) ON DELETE No Action ON UPDATE No Action;