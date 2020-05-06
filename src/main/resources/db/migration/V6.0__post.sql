CREATE TABLE posts
(
    id          bigint unsigned not null auto_increment primary key,
    post_message varchar(255) null,
    user_id     bigint       null references user (id)

);

CREATE TABLE post_picture
(
    post_id    bigint not null references posts (id),
    picture_id bigint not null references avatars (id)
);

CREATE TABLE post_audio
(
    post_id  bigint not null references posts (id),
    audio_id bigint not null references audio_records (id)
);