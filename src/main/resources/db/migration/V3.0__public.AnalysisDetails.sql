DROP TABLE IF EXISTS "public"."AnalysisDetail" CASCADE;

CREATE TABLE "public"."AnalysisDetail"
(
	id bigserial NOT NULL,
	"analysisId" BIGINT NOT NULL,
	"imageId" BIGINT NOT NULL,
	"roiId" BIGINT NULL,
	"status" VARCHAR(16) NOT NULL,
	"taskId" VARCHAR(255) NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."AnalysisDetail" ADD CONSTRAINT "PK_AnalysisDetail"	PRIMARY KEY (id);

ALTER TABLE "public"."AnalysisDetail" ADD CONSTRAINT "FK_AnalysisDetail_Analysis"
	FOREIGN KEY ("analysisId") REFERENCES "public"."Analysis" (id) ON DELETE No Action ON UPDATE No Action;