DROP TABLE IF EXISTS "public"."SourcePreVerification" CASCADE;

CREATE TABLE "public"."SourcePreVerification"
(
	id bigserial NOT NULL,
	"imageId" BIGINT NOT NULL,
	"imageCreatedAt" timestamp with time zone NULL,
	"lat" float NULL,
	"lon" float NULL,
	"isCorrect" BOOLEAN NULL,
	"taskId" VARCHAR(255) NULL,
	"status" VARCHAR(16) NOT NULL,
	"desc" VARCHAR(1000) NULL,
	"algorithmName" VARCHAR(200) NOT NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."SourcePreVerification" ADD CONSTRAINT "PK_SourcePreVerification"	PRIMARY KEY (id);
