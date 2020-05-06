CREATE TABLE communities
(
    id        bigint       not null auto_increment primary key,
    name      varchar(101) not null unique,
    avatar_id int unsigned null references uploads (id)
);

CREATE TABLE community_members
(
    user_id      bigint unsigned not null references users (id),
    community_id bigint unsigned not null references communities (id)
);

CREATE TABLE community_records
(
    community_id bigint not null references communities (id),
    audio_id     bigint not null references audio_records (id)
);