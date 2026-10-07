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

CREATE TABLE ingredients (
                             id VARCHAR(36) PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- DISH INGREDIENTS
-- зв'язок Dish <-> Ingredient
-- =====================================================

CREATE TABLE dish_ingredients (
                                  id VARCHAR(36) PRIMARY KEY,

                                  dish_id VARCHAR(36) NOT NULL,
                                  ingredient_id VARCHAR(36) NOT NULL,

                                  amount NUMERIC(10, 2) NOT NULL,
                                  unit VARCHAR(50) NOT NULL,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_dish_ingredients_dish
                                      FOREIGN KEY (dish_id)
                                          REFERENCES dishes(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_dish_ingredients_ingredient
                                      FOREIGN KEY (ingredient_id)
                                          REFERENCES ingredients(id),

                                  CONSTRAINT uq_dish_ingredient
                                      UNIQUE (dish_id, ingredient_id),

                                  CONSTRAINT chk_dish_ingredient_amount
                                      CHECK (amount > 0)
);


-- =====================================================
-- MENUS
-- =====================================================

CREATE TABLE menus (
                       id VARCHAR(36) PRIMARY KEY,

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