INSERT INTO public.product(nom, prix, prix_achat) VALUES('Ordinateur portable' , 350, 120) ON CONFLICT DO NOTHING;
INSERT INTO public.product(nom, prix, prix_achat) VALUES('Aspirateur Robot' , 500, 200) ON CONFLICT DO NOTHING;
INSERT INTO public.product(nom, prix, prix_achat) VALUES('Table de Ping Pong' , 750, 400) ON CONFLICT DO NOTHING;
