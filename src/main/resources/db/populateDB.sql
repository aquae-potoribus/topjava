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

INSERT INTO meals (date_time, user_id, description, calories)
VALUES ('2020-10-05 14:01:10', 100000, 'user: завтрак', 500),
       ('2020-10-06 14:02:10', 100000, 'user: обед', 700),
       ('2020-10-07 14:03:10', 100000, 'user: полдник', 300),
       ('2020-10-07 14:04:10', 100001, 'admin: завтрак', 400),
       ('2020-10-06 14:05:10', 100001, 'admin: обед', 900),
       ('2020-10-05 14:06:10', 100001, 'admin: полдник', 300),
       ('2020-10-07 14:07:10', 100001, 'admin: ужин', 400);
