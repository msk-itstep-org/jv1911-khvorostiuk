create table goods (
    id int unsigned not null auto_increment primary key,
    name varchar(255) not null,
    manufacturer varchar (255) not null,
    unique index (manufacturer),
    price int not null
);
