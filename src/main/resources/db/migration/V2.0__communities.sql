CREATE TABLE communities
(
    id   bigint       not null auto_increment primary key,
    name varchar(101) not null unique
);

CREATE TABLE community_members
(
    id           bigint not null auto_increment primary key,
    user_id      bigint unsigned not null references users(id),
    community_id bigint unsigned not null references communities(id)
);