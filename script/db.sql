-- =====================================================
-- DISHES
-- =====================================================

CREATE TABLE dishes (
                        id VARCHAR(36) PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,

                        recipe_steps JSONB,

                        category VARCHAR(50) NOT NULL,

                        created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- INGREDIENTS
-- =====================================================

CREATE TABLE products (
                             id VARCHAR(36) PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- DISH INGREDIENTS
-- зв'язок Dish <-> Ingredient
-- =====================================================

CREATE TABLE dish_products (
                                  id VARCHAR(36) PRIMARY KEY,

                                  dish_id VARCHAR(36) NOT NULL,
                                  product_id VARCHAR(36) NOT NULL,

                                  amount NUMERIC(10, 2) NOT NULL,
                                  unit VARCHAR(50) NOT NULL,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_dish_ingredients_dish
                                      FOREIGN KEY (dish_id)
                                          REFERENCES dishes(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_dish_ingredients_ingredient
                                      FOREIGN KEY (product_id)
                                          REFERENCES products(id),

                                  CONSTRAINT uq_dish_ingredient
                                      UNIQUE (dish_id, product_id),

                                  CONSTRAINT chk_dish_ingredient_amount
                                      CHECK (amount > 0)
);


-- =====================================================
-- MENUS
-- =====================================================

CREATE TABLE menus (
                       id VARCHAR(36) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       created_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- MENU DISHES
-- зв'язок Menu <-> Dish
-- =====================================================

CREATE TABLE menu_dishes (
                             id VARCHAR(36) PRIMARY KEY,

                             menu_id VARCHAR(36) NOT NULL,
                             dish_id VARCHAR(36) NOT NULL,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_menu_dishes_menu
                                 FOREIGN KEY (menu_id)
                                     REFERENCES menus(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_menu_dishes_dish
                                 FOREIGN KEY (dish_id)
                                     REFERENCES dishes(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT uq_menu_dish
                                 UNIQUE (menu_id, dish_id)
);
CREATE UNIQUE INDEX uq_products_normalized_name ON products (lower(name));

-- Shopping lists own their menu, dish and manual-product sources as JSONB.
CREATE TABLE shopping_lists (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    inbox BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    menu_ids JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(menu_ids) = 'array'),
    dish_ids JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(dish_ids) = 'array'),
    manual_products JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(manual_products) = 'array'),
    purchased_items JSONB NOT NULL DEFAULT '[]'::jsonb CHECK(jsonb_typeof(purchased_items) = 'array')
);
CREATE UNIQUE INDEX uq_shopping_lists_inbox ON shopping_lists(inbox) WHERE inbox;
INSERT INTO shopping_lists(id, name, inbox) VALUES ('inbox', 'Inbox', TRUE);
