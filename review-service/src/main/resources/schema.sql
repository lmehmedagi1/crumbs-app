DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
    "id"            UUID,
    "recipe_id"     UUID NOT NULL,
    "user_id"       UUID NOT NULL,
    "is_liked"      BOOLEAN DEFAULT 'false' NOT NULL,
    "rating"        INT,
    "comment"       TEXT,
    PRIMARY KEY ("id")
);
