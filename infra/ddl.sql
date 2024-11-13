CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE proposals (
	"id" VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4 (),
    "status" VARCHAR(50),
	"createdat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"updatedat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"accountid" VARCHAR(36)
);
