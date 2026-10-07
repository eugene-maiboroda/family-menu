CREATE TABLE shopping_lists (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    inbox BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    menu_ids JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(menu_ids) = 'array'),
    dish_ids JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(dish_ids) = 'array'),
    manual_products JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(manual_products) = 'array')
);
CREATE UNIQUE INDEX uq_shopping_lists_inbox ON shopping_lists(inbox) WHERE inbox;
INSERT INTO shopping_lists(id, name, inbox) VALUES ('inbox', 'Inbox', TRUE);
