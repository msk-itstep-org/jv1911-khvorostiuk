ALTER TABLE goods
ADD COLUMN manufacturer VARCHAR (255) NOT NULL,
ADD UNIQUE INDEX (manufacturer);