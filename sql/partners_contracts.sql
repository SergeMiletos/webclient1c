-- Table: public.partners_contracts

-- DROP TABLE IF EXISTS public.partners_contracts;

CREATE TABLE IF NOT EXISTS public.partners_contracts
(
    uuid character varying(255) COLLATE pg_catalog."default" NOT NULL,
    contract_code character varying(255) COLLATE pg_catalog."default",
    contract_date character varying(255) COLLATE pg_catalog."default",
    contract_name character varying(255) COLLATE pg_catalog."default",
    contract_number character varying(255) COLLATE pg_catalog."default",
    contract_type character varying(255) COLLATE pg_catalog."default",
    companies_uuid character varying(255) COLLATE pg_catalog."default",
    partners_uuid character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT partners_contracts_pkey PRIMARY KEY (uuid),
    CONSTRAINT fk5heldupx8twsma586y6l0hy14 FOREIGN KEY (partners_uuid)
        REFERENCES public.partners (uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkgnekw2ox5hibj74eh58fruq85 FOREIGN KEY (companies_uuid)
        REFERENCES public.companies (uuid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.partners_contracts
    OWNER to hibernateuser;