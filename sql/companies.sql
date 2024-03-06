-- Table: public.companies

-- DROP TABLE IF EXISTS public.companies;

CREATE TABLE IF NOT EXISTS public.companies
(
    company_code character varying(255) COLLATE pg_catalog."default",
    hash1c character varying(255) COLLATE pg_catalog."default",
    inn character varying(255) COLLATE pg_catalog."default",
    kpp character varying(255) COLLATE pg_catalog."default",
    company_name character varying(255) COLLATE pg_catalog."default",
    uuid character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT companies_pkey PRIMARY KEY (uuid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.companies
    OWNER to hibernateuser;