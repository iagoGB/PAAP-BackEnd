DELETE  FROM public.usuario_role;
DELETE  FROM public.usuario;
DELETE  FROM public.categoria;
DELETE  FROM public.role;

SELECT setval(usuario_id_seq, 0);