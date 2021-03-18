DROP TABLE IF EXISTS subscriptions;
DROP TABLE IF EXISTS user_details;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    "id"       UUID,
    "username" TEXT NOT NULL,
    "email"    TEXT NOT NULL,
    "password" TEXT NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE user_details
(
    "id"           UUID,
    "first_name"   TEXT  NOT NULL,
    "last_name"    TEXT  NOT NULL,
    "gender"       TEXT  NOT NULL,
    "phone_number" TEXT  NOT NULL,
    "avatar"       bytea NOT NULL,
    "user_id"      UUID  NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES users ("id"),
    PRIMARY KEY ("id")
);

CREATE TABLE subscriptions
(
    "subscriber_id"     UUID,
    "author_id"         UUID,
    PRIMARY KEY("subscriber_id", "author_id"),
    FOREIGN KEY ("subscriber_id") REFERENCES users ("id"),
    FOREIGN KEY ("author_id") REFERENCES users ("id")
);
