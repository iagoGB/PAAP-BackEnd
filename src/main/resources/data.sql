INSERT INTO public.role(role_name) VALUES ('ROLE_ADMIN');
INSERT INTO public.role(role_name) VALUES ('ROLE_USER');

INSERT INTO public.categoria(id, name) VALUES (1, 'Ciência e Tecnologia');
INSERT INTO public.categoria(id, name) VALUES (2, 'Cultura e Arte');
INSERT INTO public.categoria(id, name) VALUES (3, 'Educação e Pesquisa');

--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Departamento de Tecnologia', '/palestrante/numerodafotox.png', 'João Ribeiro');
--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Professor da Universidade do Texas', '/palestrante/numerodafotoy.png', 'Luce Hasenberg');
--INSERT INTO public.palestrante(descricao, foto, nome) VALUES ('Socióloga do Departamento de Cidadania da Alemanha', '/palestrante/numerodafotoz.png', 'Ashley Monrow');



INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone
)
VALUES ( 
    '2019-12-25 18:26:29.124',
    '../assets/images/default_avatar.png',
    120,
    '99999',
    '2019-12-25 10:57:29.067', 
    '2019-12-20',
    'STI',
    'admin',
    'Administrador',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '32139929'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone
)
VALUES ( 
    '2016-09-15 14:26:29.124',
    'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png',
    1500,
    '5555',
    '2019-12-25 08:27:59.067', 
    '2016-12-20',
    'ICA',
    'usuario@teste.com',
    'Usuario Dois',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '83229080'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone
)
VALUES ( 
    '2017-02-02 16:31:29.124',
    'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png',
    600,
    '6565',
    '2017-12-25 08:27:59.067', 
    '2017-10-20',
    'UFC Virtual',
    'tres@usuario.com',
    'Maria de Fátima Souza',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '3498873'
);

INSERT INTO public.usuario(
	updated_at, avatar,workload, cpf, created_at, entry_date, departament, email, name, password, phone
)
VALUES ( 
    '2017-02-02 16:31:29.124',
    'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png',
    2550,
    '223344',
    '2018-12-25 08:27:59.067', 
    '2018-10-20',
    'UFC Virtual',
    'usuario',
    'Usuário',
    '$2a$10$uB6.WzxcB4dflWRxBZDuZuPrLeJtdastzakqbAeP9PTdk1ZlubOum',
    '853498873'
);

INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (2, 'ROLE_USER');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (3, 'ROLE_USER');
INSERT INTO public.usuario_role(usuario_id, role_id) VALUES (4, 'ROLE_USER');

INSERT INTO public.event(
	id, workload, date_time, picture, local, title, fk_categoria_id, is_open, keyword)
	VALUES (10,2*60, '2020-04-12 09:00:00.067', 'caminho', 'Rua Juscelino Kubicheck','Evento Spring Boot Test', 1, true,'CODeXXYYZ20-10');

INSERT INTO public.event(
	id, workload, date_time, picture, local, title, fk_categoria_id, is_open, keyword)
	VALUES (20,4*60, '2022-04-21 14:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Rua Oscar Freire 1720, Centro','Treinamento de Educação a Distância Para Professores', 3, true,'YYZZ15-20');

INSERT INTO public.event(
	id, workload, date_time, picture, local, title, fk_categoria_id, is_open, keyword)
	VALUES (33,3*60, '2020-07-22 15:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Rua Mentor de Alencar 2020, Centro','Conhecendo a Faculdade de Direito', 2, true,'AABc-33');

INSERT INTO public.event(
	id, workload, date_time, picture, local, title, fk_categoria_id, is_open, keyword)
	VALUES (50,3*60, '2020-09-22 18:00:00.067', 'https://fcdocente-teste.s3.sa-east-1.amazonaws.com/eventos/paap.png', 'Campus do Pici','Evento de Teste', 1, true,'ZZRot-50');

 --                 O usuario dois está cadastrado em 2 eventos ao iniciar o banco
    INSERT INTO public.event_user(
        fk_evento_id, fk_usuario_id, is_user_present)
        VALUES (20, 2, false);

    INSERT INTO public.event_user(
        fk_evento_id, fk_usuario_id, is_user_present)
        VALUES (33, 2, false);

    -- Usuario 3 Já possui 2 eventos cadastrados,1 com  presença e carga horaria computadas
    INSERT INTO public.event_user(
        fk_evento_id, fk_usuario_id, is_user_present)
        VALUES (50, 3, true);

     INSERT INTO public.event_user(
        fk_evento_id, fk_usuario_id, is_user_present)
        VALUES (10, 3, false);
-- 
        
INSERT INTO public.event_speakers(event_id, speakers)
	VALUES (20, 'Professora do EAD');
INSERT INTO public.event_speakers(event_id, speakers)
	VALUES (20, 'Professor top do EAD2');