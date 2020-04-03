CREATE TABLE users
(
    id       bigint       not null auto_increment primary key,
    username varchar(101) not null unique,
    password varchar(255) not null,
    avatar   varchar(255) null,
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
