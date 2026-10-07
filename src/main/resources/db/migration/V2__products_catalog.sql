-- Preserve product IDs and all dish quantities when upgrading the original schema.
ALTER TABLE ingredients RENAME TO products;
ALTER TABLE dish_ingredients RENAME COLUMN ingredient_id TO product_id;
UPDATE products SET name = btrim(regexp_replace(name, '\s+', ' ', 'g'));
-- Stop transactionally rather than guessing how historical duplicates should merge.
DO $$ BEGIN
IF EXISTS (SELECT 1 FROM products GROUP BY lower(name) HAVING count(*) > 1) THEN
RAISE EXCEPTION 'Duplicate normalized product names: merge these ingredients before upgrading';
END IF;
END $$;
CREATE UNIQUE INDEX uq_products_normalized_name ON products (lower(name));
