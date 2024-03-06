-- Table: public.stock_names

-- DROP TABLE IF EXISTS public.stock_names;

CREATE TABLE IF NOT EXISTS public.stock_names
(
    stock_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    stock_name character varying(255) COLLATE pg_catalog."default",
    company_or_human character varying(255) COLLATE pg_catalog."default",
    inn character varying(255) COLLATE pg_catalog."default",
    kpp character varying(255) COLLATE pg_catalog."default",
    partner_code character varying(255) COLLATE pg_catalog."default",
    artner_full_name character varying(255) COLLATE pg_catalog."default",
    partner_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT stock_names_pkey PRIMARY KEY (stock_code)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.stock_names
    OWNER to hibernateuser;