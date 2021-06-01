DROP TABLE IF EXISTS review;
DROP TYPE IF EXISTS ENTYPE;
CREATE TYPE ENTYPE as ENUM ('recipe', 'diet');

CREATE TABLE review
(
    "id"          UUID,
    "user_id"     UUID      NOT NULL,
    "entity_id"   UUID      NOT NULL,
    "entity_type" ENTYPE    NOT NULL,
    "is_liked"    BOOLEAN,
    "rating"      INT,
    "comment"     TEXT,
    "created_at"  TIMESTAMP NOT NULL,
    "last_modify" TIMESTAMP NOT NULL,
    PRIMARY KEY ("id")
);