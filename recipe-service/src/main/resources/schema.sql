DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS recipe_categories;
DROP TABLE IF EXISTS recipe_ingredients;
DROP TABLE IF EXISTS diet_recipes;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS diets;
CREATE TABLE recipes (
	"id"	      TEXT,
	"title"       TEXT NOT NULL,
	"description" TEXT,
	"method"      TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE categories (
	"id"	      TEXT,
	"name"        TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE ingredients (
	"id"	      TEXT,
	"name"        TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE recipe_categories (
	"recipe_id"	  TEXT,
	"category_id" TEXT,
	PRIMARY KEY("recipe_id", "category_id"),
	FOREIGN KEY("recipe_id") REFERENCES recipes("id"),
	FOREIGN KEY("category_id") REFERENCES categories("id")
);
CREATE TABLE recipe_ingredients (
	"recipe_id"	    TEXT,
	"ingredient_id" TEXT,
	PRIMARY KEY("recipe_id", "ingredient_id"),
	FOREIGN KEY("recipe_id") REFERENCES recipes("id"),
	FOREIGN KEY("ingredient_id") REFERENCES ingredients("id")
);
CREATE TABLE diets (
	"id"	      TEXT,
	"title"       TEXT NOT NULL,
	"description" TEXT NOT NULL,
	"duration"    INT NOT NULL,
	"is_private"  BOOL NOT NULL,
	"user_id"     TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE diet_recipes (
	"diet_id"	TEXT,
	"recipe_id" TEXT,
	PRIMARY KEY("diet_id", "recipe_id"),
	FOREIGN KEY("diet_id") REFERENCES diets("id"),
	FOREIGN KEY("recipe_id") REFERENCES recipes("id")
);
CREATE TABLE images (
	"id"	    TEXT,
	"image"     bytea NOT NULL,
	"recipe_id" TEXT NOT NULL,
	FOREIGN KEY("recipe_id") REFERENCES recipes("id"),
	PRIMARY KEY("id")
);
