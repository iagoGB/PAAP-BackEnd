INSERT INTO public.role(role_name) VALUES ('ROLE_ADMIN');
INSERT INTO public.role(role_name) VALUES ('ROLE_USER');

INSERT INTO public.category(name, url_image) VALUES ( 'Ciência e Tecnologia','https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png');
INSERT INTO public.category(name, url_image) VALUES ('Cultura e Arte','https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png');
INSERT INTO public.category(name, url_image) VALUES ('Educação e Pesquisa','https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png');

--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Departamento de Tecnologia', '/palestrante/numerodafotox.png', 'João Ribeiro');
--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Professor da Universidade do Texas', '/palestrante/numerodafotoy.png', 'Luce Hasenberg');
--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Socióloga do Departamento de Cidadania da Alemanha', '/palestrante/numerodafotoz.png', 'Ashley Monrow');



INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone,siape
)
VALUES ( 
    '2019-12-25 18:26:29.124',
    'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png',
    0,
    '368.628.750-12',
    '2019-12-25 10:57:29.067', 
    '2019-12-20',
    'EIDEIA',
    'admin',
    'Administrador',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '(85) 9 3213-9929',
    '1234567'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone,siape
)
VALUES ( 
    '2016-09-15 14:26:29.124',
    'https://i1.wp.com/www.jeanmktdigital.com.br/wp-content/uploads/2020/11/Pessoas-pretas-Medio.png?w=1365&ssl=1',
    8,
    '555.555.555-22',
    '2019-12-25 08:27:59.067', 
    '2018-12-20',
    'ICA',
    'usuario@teste.com',
    'Karen Bravo dos Santos',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '(85) 9 9999-9999',
    '7777777'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone, siape
)
VALUES ( 
    '2017-02-02 16:31:29.124',
    'https://images.nappy.co/uploads/large/215977137818ldtd4ht2optqolh8r4rqxahagl4hcrhfaluxdgh3dsqcy6pdurdo2yu2pjqln8yjqskbrbl0smszkprx9bl8b3kpcdnpgi4wtwn.jpg?auto=format&fm=jpg&w=1280&q=75',
    30,
    '656.565.656-56',
    '2017-12-25 08:27:59.067', 
    '2019-10-20',
    'UFC Virtual',
    'usuario2@teste.com',
    'Elizabeth Rios Damas Albuquerque Feitosa da Costa e Silva',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '(85) 9 8873-8888',
    '5234567'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone,siape
)
VALUES ( 
    '2017-02-02 16:31:29.124',
    'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80',
    12,
    '223.344.111-50',
    '2018-12-25 08:27:59.067', 
    '2020-02-01',
    'UFC Virtual',
    'usuario3@teste.com',
    'João Bezerra Duarte',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '(85) 9 3213-9087',
    '9439867'
);

INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (2, 'ROLE_USER');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (3, 'ROLE_USER');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (4, 'ROLE_USER');

-- INSERT INTO public.event(
-- 	id, workload, date_time, picture, local, title, fk_categoria_id,  keyword)
-- 	VALUES (10,2*60, '2020-04-12 09:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Rua Juscelino Kubicheck','Ferramenta de Ensino Gameficado', 1, 'CODeXXYYZ20-10');

-- INSERT INTO public.event(
-- 	id, workload, date_time, picture, local, title, fk_categoria_id,  keyword)
-- 	VALUES (20,4*60, '2022-04-21 14:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Rua Oscar Freire 1720, Centro','Treinamento de Educação a Distância Para Professores', 3, 'YYZZ15-20');

-- INSERT INTO public.event(
-- 	id, workload, date_time, picture, local, title, fk_categoria_id,  keyword)
-- 	VALUES (33,3*60, '2020-07-22 15:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Rua Mentor de Alencar 2020, Centro','Conhecendo a Faculdade de Direito', 2, 'AABc-33');

-- INSERT INTO public.event(
-- 	id, workload, date_time, picture, local, title, fk_categoria_id,  keyword)
-- 	VALUES (50,3*60, '2020-09-22 18:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Campus do Pici','Evento de Teste', 1, 'ZZRot-50');

--  --                 O usuario dois está cadastrado em 2 eventos ao iniciar o banco
--     INSERT INTO public.event_user(
--         fk_evento_id, fk_usuario_id, is_user_present)
--         VALUES (20, 2, false);

--     INSERT INTO public.event_user(
--         fk_evento_id, fk_usuario_id, is_user_present)
--         VALUES (33, 2, false);

--     -- Usuario 3 Já possui 2 eventos cadastrados,1 com  presença e carga horaria computadas
--     INSERT INTO public.event_user(
--         fk_evento_id, fk_usuario_id, is_user_present)
--         VALUES (50, 3, true);

--      INSERT INTO public.event_user(
--         fk_evento_id, fk_usuario_id, is_user_present)
--         VALUES (10, 3, false);
-- -- 
        
-- INSERT INTO public.event_speakers(event_id, speakers)
-- 	VALUES (20, 'Professora do EAD');
-- INSERT INTO public.event_speakers(event_id, speakers)
-- 	VALUES (20, 'Professor  do Ensino a distância');