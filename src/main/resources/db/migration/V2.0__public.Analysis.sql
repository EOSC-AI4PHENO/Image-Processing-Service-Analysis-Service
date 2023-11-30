DROP TABLE IF EXISTS "public"."Analysis" CASCADE;

CREATE TABLE "public"."Analysis"
(
	id bigserial NOT NULL,
	name varchar(200) NULL,
	"desc" varchar(500) NULL,
    "realm" varchar(50) NOT NULL,
	"userId" VARCHAR(255) NULL,
	"type" VARCHAR(255),
	"sourceId" BIGINT NOT NULL,
	"toProcess" BOOLEAN NOT NULL DEFAULT TRUE,
	"algorithmName" VARCHAR(255) NOT NULL,
	"dateFrom" DATE NULL,
	"dateTo" DATE NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL,
	"modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."Analysis" ADD CONSTRAINT "PK_Analysis"	PRIMARY KEY (id);