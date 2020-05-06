CREATE TABLE likes
(
    id      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (user_id)
);

CREATE TABLE post_likes
(
    id      BIGINT NOT NULL PRIMARY KEY REFERENCES likes (id),
    post_id BIGINT NOT NULL REFERENCES posts (id)
);
