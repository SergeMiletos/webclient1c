-- Table: public.users1c

-- DROP TABLE IF EXISTS public.users1c;

CREATE TABLE IF NOT EXISTS public.users1c
(
    id bigint NOT NULL,
    base64val character varying(255) COLLATE pg_catalog."default",
    last_login timestamp without time zone,
    password character varying(255) COLLATE pg_catalog."default",
    na_cl character varying(255) COLLATE pg_catalog."default",
    user_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users1c_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users1c
    OWNER to hibernateuser;