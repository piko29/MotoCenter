DROP TABLE IF EXISTS application_user;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS moto_product;

CREATE TABLE application_user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL
);

CREATE TABLE user_role
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        varchar(30)  NOT NULL,
    description varchar(200) NOT NULL
);

CREATE TABLE user_roles
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES application_user(id),
    FOREIGN KEY (role_id) REFERENCES user_role(id)
);

CREATE TABLE moto_product
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(100) NOT NULL,
    description     VARCHAR(200) NOT NULL,
    image           VARCHAR(200) NOT NULL,
    price           INT NOT NULL,
    contact_info    VARCHAR(200) NOT NULL,
    user_id         BIGINT,
    owner           VARCHAR(200) NOT NULL,
    sold            BOOLEAN,
    buyer           VARCHAR(100) NOT NULL
);
