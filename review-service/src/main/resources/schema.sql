DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
    "id"     TEXT,
    "recipe_id"     TEXT NOT NULL,
    "user_id"       TEXT NOT NULL,
    "is_liked"      BOOL DEFAULT 'false' NOT NULL,
    "rating"        INT,
    "comment"       TEXT,
    PRIMARY KEY ("id")
);
