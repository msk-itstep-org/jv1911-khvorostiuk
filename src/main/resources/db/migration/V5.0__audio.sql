CREATE TABLE audio_records
(
    id                int unsigned not null auto_increment primary key,
    filename          varchar(255) not null,
    original_filename varchar(255) not null,
    content_type      varchar(255) not null
);
