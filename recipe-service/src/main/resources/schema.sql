DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS recipe_categories;
DROP TABLE IF EXISTS recipe_ingredients;
DROP TABLE IF EXISTS diet_recipes;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS diets;

CREATE TABLE recipes
(
    "id"          uuid,
    "user_id"     uuid,
    "title"       TEXT NOT NULL,
    "description" TEXT,
    "method"      TEXT NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE categories
(
    "id"   uuid,
    "name" TEXT NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE ingredients
(
    "id"   uuid,
    "name" TEXT NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE recipe_categories
(
    "recipe_id"   uuid,
    "category_id" uuid,
    PRIMARY KEY ("recipe_id", "category_id"),
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id") ON DELETE CASCADE,
    FOREIGN KEY ("category_id") REFERENCES categories ("id") ON DELETE CASCADE
);

CREATE TABLE recipe_ingredients
(
    "recipe_id"     uuid,
    "ingredient_id" uuid,
    PRIMARY KEY ("recipe_id", "ingredient_id"),
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id") ON DELETE CASCADE,
    FOREIGN KEY ("ingredient_id") REFERENCES ingredients ("id") ON DELETE CASCADE
);

CREATE TABLE diets
(
    "id"          uuid,
    "title"       TEXT    NOT NULL,
    "description" TEXT    NOT NULL,
    "duration"    INT     NOT NULL,
    "is_private"  BOOLEAN NOT NULL DEFAULT 'false',
    "user_id"     uuid,
    PRIMARY KEY ("id")
);

CREATE TABLE diet_recipes
(
    "diet_id"   uuid,
    "recipe_id" uuid,
    PRIMARY KEY ("diet_id", "recipe_id"),
    FOREIGN KEY ("diet_id") REFERENCES diets ("id") ON DELETE CASCADE,
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id") ON DELETE CASCADE
);

CREATE TABLE images
(
    "id"        uuid,
    "image"     bytea NOT NULL,
    "recipe_id" uuid  NOT NULL,
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id"),
    PRIMARY KEY ("id")
);
