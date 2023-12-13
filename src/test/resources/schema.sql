CREATE TABLE IF NOT EXISTS customer (
	id integer NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	tax_code varchar(255) NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_account (
	id bigserial NOT NULL,
	balance float8 NOT NULL,
	customer_id int8 NULL,
	CONSTRAINT bank_account_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "transaction" (
	id bigserial NOT NULL,
	amount float8 NOT NULL,
	date_time timestamp NOT NULL,
	operation_type varchar(255) NOT NULL,
	bank_account_id int8 NOT NULL,
	CONSTRAINT transaction_pkey PRIMARY KEY (id)
);
