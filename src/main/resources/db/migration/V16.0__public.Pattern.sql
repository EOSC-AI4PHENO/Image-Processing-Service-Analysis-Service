DROP TABLE IF EXISTS "public"."Pattern" CASCADE;

CREATE TABLE "public"."Pattern"
(
    id bigserial NOT NULL,
    name varchar(200) NULL,
    "desc" varchar(500) NULL,
    "realm" varchar(50) NOT NULL,
    "userId" VARCHAR(255) NULL,
    "formula" VARCHAR(255) NOT NULL,
    "isActive" boolean NOT NULL DEFAULT TRUE,
    "createdAt" timestamp with time zone NOT NULL,
    "modifiedAt" timestamp with time zone NOT NULL
);

ALTER TABLE "public"."Pattern" ADD CONSTRAINT "PK_Pattern"	PRIMARY KEY (id);