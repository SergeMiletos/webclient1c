-- Table: public.products_stock_levels

-- DROP TABLE IF EXISTS public.products_stock_levels;

CREATE TABLE IF NOT EXISTS public.products_stock_levels
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    product_code character varying(255) COLLATE pg_catalog."default",
    product_name character varying(255) COLLATE pg_catalog."default",
    stock_quantity_values jsonb,
    uom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT products_stock_levels_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.products_stock_levels
    OWNER to hibernateuser;