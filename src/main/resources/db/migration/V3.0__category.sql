CREATE TABLE categories(
    id int unsigned not null auto_increment primary key,
    name varchar(100) not null,
    parent_id int null references categories(id),
    image varchar (255) null
);

INSERT INTO categories (name, parent_id, image ) VALUES ('Одежда', null, 'images/inosuke.jpg');
INSERT INTO categories (name, parent_id, image ) VALUES ('Мужская', '1', 'images/muzhik.jpg');
INSERT INTO categories (name, parent_id, image ) VALUES ('Женская', '1', 'images/nyaa.jpg');
INSERT INTO categories (name, parent_id, image ) VALUES ('Футболки', '2', null);
INSERT INTO categories (name, parent_id, image ) VALUES ('Толстовки', '2', null);
INSERT INTO categories (name, parent_id, image ) VALUES ('Рубашки', '2', null);
INSERT INTO categories (name, parent_id, image ) VALUES ('Футболки', '3', null);
INSERT INTO categories (name, parent_id, image ) VALUES ('Леггинсы', '3', null);
INSERT INTO categories (name, parent_id, image ) VALUES ('Толстовки', '3', null);
