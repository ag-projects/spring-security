-- Roles & privileges
insert into Privilege (name, id) values ('READ_PRIVILEGE', 1);
insert into Privilege (name, id) values ('WRITE_PRIVILEGE', 2);
insert into Role (name, id) values ('ROLE_ADMIN', 3);
insert into Role (name, id) values ('ROLE_USER', 4);
insert into roles_privileges (role_id, privilege_id) values (3, 1);
insert into roles_privileges (role_id, privilege_id) values (3, 2);
insert into roles_privileges (role_id, privilege_id) values (4, 1);
insert into User (id, created, email, password) values (1, NOW(), 'test@email.com',1, 'Secured123!');
insert into users_roles (user_id, role_id) values (1, 3);

-- Security questions
insert into security_question_definition (id, text) values (1, 'What is the last name of the teacher who gave you your first failing grade?');
insert into security_question_definition (id, text) values (2, 'What is the first name of the person you first kissed?');
insert into security_question_definition (id, text) values (3, 'What is the name of the place your wedding reception was held?');
insert into security_question_definition (id, text) values (4, 'When you were young, what did you want to be when you grew up?');
insert into security_question_definition (id, text) values (5, 'Where were you New Year''s 2000?');
insert into security_question_definition (id, text) values (6, 'Who was your childhood hero?');

-- Test User
insert into User (id, email, password, enabled, created) values (99, 'test@email.com', '{noop}pass', true, '2018-10-06 00:00:00');

-- Persistent Login
create table persistent_logins (
  username varchar(64) not null,
  series varchar(64) primary key,
  token varchar(64) not null,
  last_used timestamp not null);

