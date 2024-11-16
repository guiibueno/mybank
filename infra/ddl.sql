CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE proposals (
	"id" VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4 (),
    "status" VARCHAR(50),
	"createdat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"updatedat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"accountid" VARCHAR(36),
	"additionalinfos" TEXT
);

CREATE TABLE customers (
	"id" VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4 (),
	"createdat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"name" VARCHAR(50),
    "birthdate" DATE 
);

CREATE TABLE customerdocuments (
	"id" VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4 (),
    "customerid" VARCHAR(36) REFERENCES customers (id),
	"createdat" TIMESTAMP NOT NULL DEFAULT NOW(),
    "type" VARCHAR(50),
    "number" VARCHAR(50)
);

CREATE TABLE accounts (
	"id" VARCHAR(36) PRIMARY KEY DEFAULT uuid_generate_v4 (),
    "customerid" VARCHAR(36) REFERENCES customers (id),
    "active" VARCHAR(50),
	"createdat" TIMESTAMP NOT NULL DEFAULT NOW(),
	"balance" DECIMAL(12,2)
);

CREATE OR REPLACE FUNCTION update_balance(
	accountid VARCHAR(36),
	transactiontype CHAR(1),
	transactionvalue DECIMAL(12,2)
) RETURNS TABLE (
	"success" BOOL,
	"balance" DECIMAL(12,2)
)
LANGUAGE plpgsql
AS $$
DECLARE
	currentbalance DECIMAL(12,2);
	transactionsuccess BOOL;
BEGIN
	transactionsuccess = FALSE::bool;

	SELECT 
		a.balance
	INTO 
		currentbalance
	FROM 
		accounts a
	WHERE 
		a.id = accountid
	FOR UPDATE;
	
	IF transactiontype = 'C' THEN
		UPDATE 
			accounts
		SET 
			balance = currentbalance + transactionvalue
		WHERE 
			id = accountid;
		
		transactionsuccess = TRUE::bool;
	ELSEIF currentbalance - transactionvalue >= 0 THEN
		UPDATE 
			accounts
		SET 
			balance = currentbalance - transactionvalue
		WHERE 
			id = accountid;
			
		transactionsuccess = TRUE::bool;
	END IF;

	RETURN QUERY 
		SELECT 
			transactionsuccess::bool,
			a.balance
		FROM 
			accounts a
		WHERE 
			a.id = accountid;
END;
$$;