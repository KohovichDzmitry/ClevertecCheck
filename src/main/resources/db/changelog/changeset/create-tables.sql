-- liquibase formatted sql

-- changeset Kohovich Dmitry:1
CREATE TABLE IF NOT EXISTS public.products
(
    product_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    product_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) DEFAULT 0,
    stock integer,
    CONSTRAINT products_pkey PRIMARY KEY (product_id),
    CONSTRAINT check_stock CHECK (stock = 0 OR stock = 1)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.products
    OWNER to postgres;

-- changeset Kohovich Dmitry:2
CREATE TABLE IF NOT EXISTS public.cards
(
    card_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    card_number integer NOT NULL,
    discount integer NOT NULL,
    CONSTRAINT cards_pkey PRIMARY KEY (card_id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cards
    OWNER to postgres;

-- changeset Kohovich Dmitry:3
CREATE TABLE IF NOT EXISTS public.custom_order
(
    order_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    id_product integer NOT NULL,
    quantity integer NOT NULL,
    CONSTRAINT custom_order_pkey PRIMARY KEY (order_id),
    CONSTRAINT fk_product_id_order FOREIGN KEY (id_product)
    REFERENCES public.products (product_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT check_quantity CHECK (quantity > 0)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.custom_order
    OWNER to postgres;