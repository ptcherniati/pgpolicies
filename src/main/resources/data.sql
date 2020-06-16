--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1 (Debian 12.1-1.pgdg100+1)
-- Dumped by pg_dump version 12.2

-- Started on 2020-06-16 15:30:50 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 168438)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 3023 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;


--
-- TOC entry 205 (class 1259 OID 168336)
-- Name: authorities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.authorities (
    username character varying(50) NOT NULL,
    roles_nom character varying(20) NOT NULL,
    authority character varying(20) GENERATED ALWAYS AS (roles_nom) STORED
);


--
-- TOC entry 203 (class 1259 OID 168264)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 212 (class 1259 OID 267622)
-- Name: policies; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.policies (
    nom_nom character varying(20) NOT NULL,
    description text DEFAULT '.*'::text,
    for_field character varying(255),
    check_field character varying(255),
    using_field character varying(255),
    id integer NOT NULL
);


--
-- TOC entry 3024 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN policies.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.policies.description IS 'Description de rôle et de l''effet de l’expression régulière.';


--
-- TOC entry 211 (class 1259 OID 267620)
-- Name: policies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.policies_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3025 (class 0 OID 0)
-- Dependencies: 211
-- Name: policies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.policies_id_seq OWNED BY public.policies.id;


--
-- TOC entry 210 (class 1259 OID 184658)
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    nom character varying(20) NOT NULL
);


--
-- TOC entry 204 (class 1259 OID 168331)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    enabled boolean NOT NULL
);


--
-- TOC entry 2850 (class 2604 OID 267629)
-- Name: policies id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.policies ALTER COLUMN id SET DEFAULT nextval('public.policies_id_seq'::regclass);


--
-- TOC entry 209 (class 1259 OID 176505)
-- Name: category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    nom character varying(20)
);

--
-- TOC entry 208 (class 1259 OID 176500)
-- Name: aliment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aliment (
    id bigint NOT NULL,
    nom character varying(20) NOT NULL,
    category_id bigint
);


--
-- TOC entry 3014 (class 0 OID 176505)
-- Dependencies: 209
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.category (id, nom) FROM stdin;
19	Fruits
21	Légumes
\.


--
-- TOC entry 3013 (class 0 OID 176500)
-- Dependencies: 208
-- Data for Name: aliment; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.aliment (id, nom, category_id) FROM stdin;
20	Ananas	19
24	Artichaud	21
25	Abricot	19
26	Amande	19
27	Brugnon	19
29	Cerise	19
30	Clémentine	19
32	Citron	19
33	Datte	19
34	Marron	19
35	Melon	19
36	Avocat	21
37	Brocoli	21
38	Blette	21
39	Betterave	21
40	Chou	21
41	Citrouille	21
42	Cornichon	21
43	Cresson	21
44	Concombre	21
45	Épinard	21
47	Maïs	21
28	bananes	19
1	banane	19
\.


--
-- TOC entry 3017 (class 0 OID 267622)
-- Dependencies: 212
-- Data for Name: policies; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.policies (nom_nom, description, for_field, check_field, using_field, id) FROM stdin;
policy_commence_a	commence par a 	SELECT		^a.*$	20
policy_finit_e	finit par e 	SELECT		^.*e$	21
policy_banane	juste banane	SELECT		banane	22
policy_contient_ne	contient ne	SELECT		.*ne.*	23
policy_update_banane	update banane	UPDATE	^banane.*	banane	24
policy_commence_b	commence par b	SELECT		^b.*$	25
\.


--
-- TOC entry 3009 (class 0 OID 168331)
-- Dependencies: 204
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.users (username, password, enabled) FROM stdin;
user;	$2a$10$1wotulIe.zWt0ae.jToV.e/zi4nULf/8r00trYkoxLjxj1aJT3TtO	t
admin	$2a$10$ztRbtFUt0QFhbxggQjE9s.6D3cgloifZ5FOIwdgINJpyGWKMxLttC	t
toto	$2a$11$px6g.nVHLeLNczeCs7LHseyd44gvnF2hHC.nYfVQNzkPavi5b9hyi	t
\.

--créer les rôles associés
create role "user";
create role "admin";
create role "toto";


--
-- TOC entry 3015 (class 0 OID 184658)
-- Dependencies: 210
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.roles (nom) FROM stdin;
ROLE_ADMIN
ROLE_USER
policy_commence_a
policy_finit_e
policy_banane
policy_contient_ne
policy_update_banane
policy_commence_b
\.


--
-- TOC entry 3010 (class 0 OID 168336)
-- Dependencies: 205
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.authorities (username, roles_nom) FROM stdin;
user	ROLE_USER
admin	ROLE_ADMIN
admin	ROLE_USER
toto	ROLE_USER
toto	policy_banane
user	policy_banane
toto	policy_commence_a
toto	policy_commence_b
\.

--créer les rôles associés
create role "ROLE_ADMIN";
grant ROLE_ADMIN to "admin";
create role "ROLE_USER";
grant ROLE_USER to "user";
grant ROLE_USER to "admin";
grant ROLE_USER to "toto";

create role "policy_commence_a";
grant policy_commence_a to "toto";
create role "policy_finit_e";
create role "policy_banane";
grant policy_banane to "toto";
grant policy_banane to "user";
create role "policy_contient_ne";
create role "policy_commence_b";
grant policy_commence_b to "toto";
create role "policy_update_banane";

--
-- TOC entry 3027 (class 0 OID 0)
-- Dependencies: 203
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hibernate_sequence', 48, true);


--
-- TOC entry 3028 (class 0 OID 0)
-- Dependencies: 211
-- Name: policies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.policies_id_seq', 25, true);


--
-- TOC entry 3029 (class 0 OID 0)
-- Dependencies: 206
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_id_seq', 1, false);


--
-- TOC entry 2858 (class 2606 OID 176504)
-- Name: aliment aliment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aliment
    ADD CONSTRAINT aliment_pkey PRIMARY KEY (id);


--
-- TOC entry 2854 (class 2606 OID 184670)
-- Name: authorities authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (roles_nom, username);


--
-- TOC entry 2864 (class 2606 OID 176509)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2860 (class 2606 OID 176516)
-- Name: aliment fk_name_category; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aliment
    ADD CONSTRAINT fk_name_category UNIQUE (nom, category_id);


--
-- TOC entry 2868 (class 2606 OID 267634)
-- Name: policies policies_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.policies
    ADD CONSTRAINT policies_pkey PRIMARY KEY (id);


--
-- TOC entry 2856 (class 2606 OID 168431)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2866 (class 2606 OID 184662)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (nom);


--
-- TOC entry 2852 (class 2606 OID 168408)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- TOC entry 2862 (class 1259 OID 300821)
-- Name: idx_nom2; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_nom2 ON public.aliment USING btree (nom);


--
-- TOC entry 2872 (class 2606 OID 267635)
-- Name: policies FK_NOM; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.policies
    ADD CONSTRAINT "FK_NOM" FOREIGN KEY (nom_nom) REFERENCES public.roles(nom);


--
-- TOC entry 2871 (class 2606 OID 176510)
-- Name: aliment fk4yugmjkiw7qc1o9ha89y9qfh2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aliment
    ADD CONSTRAINT fk4yugmjkiw7qc1o9ha89y9qfh2 FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 2869 (class 2606 OID 168409)
-- Name: authorities fk_authorities_users; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES public.users(username);


--
-- TOC entry 2870 (class 2606 OID 184671)
-- Name: authorities fknf6ngq324f76kslo09v8mohak; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT fknf6ngq324f76kslo09v8mohak FOREIGN KEY (roles_nom) REFERENCES public.roles(nom);


--
-- TOC entry 3000 (class 0 OID 176500)
-- Dependencies: 208
-- Name: aliment; Type: ROW SECURITY; Schema: public; Owner: -
--

ALTER TABLE public.aliment ENABLE ROW LEVEL SECURITY;

--
-- TOC entry 3004 (class 3256 OID 300810)
-- Name: aliment pa_policy_banane; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_banane ON public.aliment FOR SELECT TO policy_banane USING (((nom)::text ~* 'banane'::text));


--
-- TOC entry 3002 (class 3256 OID 300806)
-- Name: aliment pa_policy_commence_a; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_commence_a ON public.aliment FOR SELECT TO policy_commence_a USING (((nom)::text ~* '^a.*$'::text));


--
-- TOC entry 3007 (class 3256 OID 300819)
-- Name: aliment pa_policy_commence_b; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_commence_b ON public.aliment FOR SELECT TO policy_commence_b USING (((nom)::text ~* '^b.*$'::text));


--
-- TOC entry 3005 (class 3256 OID 300812)
-- Name: aliment pa_policy_contient_ne; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_contient_ne ON public.aliment FOR SELECT TO policy_contient_ne USING (((nom)::text ~* '.*ne.*'::text));


--
-- TOC entry 3003 (class 3256 OID 300808)
-- Name: aliment pa_policy_finit_e; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_finit_e ON public.aliment FOR SELECT TO policy_finit_e USING (((nom)::text ~* '^.*e$'::text));


--
-- TOC entry 3006 (class 3256 OID 300815)
-- Name: aliment pa_policy_update_banane; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY pa_policy_update_banane ON public.aliment FOR UPDATE TO policy_update_banane USING (((nom)::text ~* 'banane'::text));


--
-- TOC entry 2999 (class 0 OID 168425)
-- Dependencies: 207
-- Name: product; Type: ROW SECURITY; Schema: public; Owner: -
--

ALTER TABLE public.product ENABLE ROW LEVEL SECURITY;

--
-- TOC entry 3001 (class 3256 OID 267653)
-- Name: aliment role_admin; Type: POLICY; Schema: public; Owner: -
--

CREATE POLICY role_admin ON public.aliment TO role_admin USING (true);


-- Completed on 2020-06-16 15:30:50 UTC

--
-- PostgreSQL database dump complete
--
