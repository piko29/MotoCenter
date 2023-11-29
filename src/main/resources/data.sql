INSERT INTO
    application_user (first_name, last_name, email, password)
VALUES
    -- superadmin@example.com / admin
    ('John', 'Taylor', 'superadmin@email.com', '{bcrypt}$2a$10$fjJ92utGZVSINQWeC6PAeuZTGgaEAn4gEKCnXTjm0TCC.JyQYGMLC'),
    -- jack@email.com / dogcat
    ('Jack', 'Blue', 'jack@email.com', '{MD5}4b57d50cd514088317d7f46176f3d57b'),
    -- andrew@email.com / super
    ('Andrew', 'Jones', 'andrew@email.com', '{argon2}$argon2i$v=19$m=16,t=2,p=1$TUE5YnVQeE80aHlqTkk2bQ$w2kRqH5fjYgOBz0BDxwp+Q');

INSERT INTO
    user_role (name, description)
VALUES
    ('ADMIN', 'Access to everything'),
    ('USER', 'Limited access');

INSERT INTO
    user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2);
