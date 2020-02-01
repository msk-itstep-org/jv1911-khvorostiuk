create table roles(
    id int unsigned not null auto_increment primary key,
    role varchar (255) not null,
    unique index (role)
);

create table user_roles(
    user_id int not null references users (id),
    role_id int not null references roles (id),
    primary key (role_id, user_id)
);

insert into roles (role) values ("ROLE_USER"), ("ROLE_ADMIN");

insert into users (username, password, phone) values ("admin", "admin", "+79876543210"),
                                                     ("admin2","123", "");
