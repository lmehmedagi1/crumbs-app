DROP TABLE IF EXISTS images CASCADE;
DROP TABLE IF EXISTS recipe_ingredients CASCADE;
DROP TABLE IF EXISTS recipe_categories CASCADE;
DROP TABLE IF EXISTS diet_recipes CASCADE;
DROP TABLE IF EXISTS recipes CASCADE;

DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS category_type CASCADE;

DROP TABLE IF EXISTS ingredients CASCADE;
DROP TABLE IF EXISTS diets CASCADE;


CREATE TABLE recipes
(
    "id"          uuid,
    "user_id"     uuid,
    "title"       TEXT NOT NULL,
    "description" TEXT,
    "method"      TEXT NOT NULL,
    "advice" TEXT,
    "preparation_time" INT NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "last_modify" TIMESTAMP NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE category_type (
       "id" UUID,
       "name" TEXT NOT NULL UNIQUE,
       PRIMARY KEY ("id")
);

CREATE TABLE categories
(
    "id" UUID,
    "type_id" UUID NOT NULL,
    "name" TEXT NOT NULL UNIQUE,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("type_id") REFERENCES category_type (id) ON DELETE CASCADE
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
    "quantity" REAL NULL,
    "measuring_unit" TEXT NULL,
    PRIMARY KEY ("recipe_id", "ingredient_id"),
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id") ON DELETE CASCADE,
    FOREIGN KEY ("ingredient_id") REFERENCES ingredients ("id") ON DELETE CASCADE
);

CREATE TABLE diets
(
    "id"          uuid,
    "user_id"     uuid,
    "title"       TEXT    NOT NULL,
    "description" TEXT    NOT NULL,
    "duration"    INT     NOT NULL,
    "is_private"  BOOLEAN NOT NULL DEFAULT 'false',
    "created_at" TIMESTAMP NOT NULL,
    "last_modify" TIMESTAMP NOT NULL,
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
    "image"     TEXT NOT NULL,
    "recipe_id" uuid  NOT NULL,
    FOREIGN KEY ("recipe_id") REFERENCES recipes ("id"),
    PRIMARY KEY ("id")
);
