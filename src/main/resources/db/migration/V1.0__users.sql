CREATE TABLE users
(
    id       bigint       not null auto_increment primary key,
    username varchar(101) not null unique,
    password varchar(255) not null,
    avatar   varchar(255),
    active   boolean      not null,
    email varchar(101) not null,
    activation_code varchar(255) null
);

CREATE TABLE user_roles
(
    user_id bigint       not null references users (id),
    role    varchar(100) not null,
    primary key (user_id, role)
);

insert into users(username, password, active, email)
values ("admin", "$2a$10$n3DHKPbyYjzo2i4gtRCk7ObGb/iiZoU.2k8VBIB6N10Ybuu.Dpp72", true, "adminopochta");

INSERT INTO user_roles (user_id, role)
SELECT id, "ROLE_USER"
FROM users
WHERE username = "admin";

insert into user_roles(user_id, role)
select id, "ROLE_ADMIN"
from users
where username = "admin";
