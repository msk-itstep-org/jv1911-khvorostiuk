CREATE TABLE users
(
    id              bigint       not null auto_increment primary key,
    username        varchar(101) not null unique,
    password        varchar(255) not null,
    active          boolean      not null,
    email           varchar(101) not null,
    photo_id        bigint unsigned null references photos(id),
    avatar_id       int unsigned null references uploads (id),
    activation_code varchar(255) null
);

CREATE TABLE user_roles
(
    user_id bigint       not null references users (id),
    role    varchar(100) not null,
    primary key (user_id, role)
);

create table user_records
(
    id       int unsigned not null auto_increment primary key,
    user_id  bigint not null references users (id),
    audio_id bigint not null references audio_records (id)
);
