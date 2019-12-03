CREATE TABLE users(
    id int unsigned not null auto_increment primary key,
    username varchar(101) not null,
    password  varchar (101) not null,
    phone varchar (12) not null,
    unique index (username)
);
