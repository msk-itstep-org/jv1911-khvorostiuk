CREATE TABLE categories(
  id int unsigned not null auto_increment primary key,
  name varchar(100) not null,
  parent_id int null references categories(id)
)