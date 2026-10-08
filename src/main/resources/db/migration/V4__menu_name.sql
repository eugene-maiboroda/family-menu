ALTER TABLE menus ADD COLUMN name VARCHAR(255);

UPDATE menus SET name = 'Меню ' || id;

ALTER TABLE menus ALTER COLUMN name SET NOT NULL;
