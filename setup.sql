-- Table: onlineshop.product

-- DROP TABLE onlineshop.product;

CREATE TABLE IF NOT EXISTS onlineshop.product
(
    id integer NOT NULL DEFAULT nextval('onlineshop.product_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    price numeric(15,2) NOT NULL,
    creation_date date NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT name UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE onlineshop.product
    OWNER to jacksparrow;
