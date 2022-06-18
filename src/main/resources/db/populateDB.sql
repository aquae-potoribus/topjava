DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time,user_id, description, calories)
VALUES (now(),100000, 'завтрак', 500),
       (current_date,100001, 'обед', 900),
       (to_timestamp(200120400),100001, 'обед', 900),
       (to_timestamp(200120401),100001, 'обед', 900);
