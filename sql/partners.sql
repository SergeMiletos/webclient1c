-- Table: public.partners

-- DROP TABLE IF EXISTS public.partners;

CREATE TABLE IF NOT EXISTS public.partners
(
    uuid character varying(255) COLLATE pg_catalog."default" NOT NULL,
    company_or_human character varying(255) COLLATE pg_catalog."default",
    hash1c character varying(255) COLLATE pg_catalog."default",
    inn character varying(255) COLLATE pg_catalog."default",
    kpp character varying(255) COLLATE pg_catalog."default",
    partner_code character varying(255) COLLATE pg_catalog."default",
    partner_full_name character varying(255) COLLATE pg_catalog."default",
    partner_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT partners_pkey PRIMARY KEY (uuid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.partners
    OWNER to hibernateuser;