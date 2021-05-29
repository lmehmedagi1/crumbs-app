DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS verification_token;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS subscriptions;
DROP TABLE IF EXISTS user_profile;

CREATE TABLE users
(
    "id"       UUID,
    "username" TEXT NOT NULL UNIQUE,
    "email"    TEXT NOT NULL UNIQUE,
    "password" TEXT NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE user_profile
(
    "first_name"   TEXT NOT NULL,
    "last_name"    TEXT NOT NULL,
    "gender"       TEXT NOT NULL,
    "phone_number" TEXT NOT NULL,
    "avatar"       TEXT,
    "email_verified" BOOLEAN NOT NULL,
    "user_id"      UUID,
    FOREIGN KEY ("user_id") REFERENCES users ("id") ON DELETE CASCADE
);

CREATE TABLE refresh_token (
   "id" UUID,
   "value" TEXT NOT NULL,
   "valid_until" DATE NOT NULL,
   "user_id" UUID NOT NULL,
   PRIMARY KEY("id"),
   FOREIGN KEY("user_id") REFERENCES users("id")
);

CREATE TABLE verification_token (
   "id" UUID,
   "value" TEXT NOT NULL,
   "valid_until" DATE NOT NULL,
   "user_id" UUID NOT NULL,
   PRIMARY KEY("id"),
   FOREIGN KEY("user_id") REFERENCES users("id")
);

CREATE TABLE subscriptions
(
    "subscriber_id" UUID,
    "author_id"     UUID,
    "created_at" TIMESTAMP,
    PRIMARY KEY ("subscriber_id", "author_id"),
    FOREIGN KEY ("subscriber_id") REFERENCES users ("id") ON DELETE CASCADE,
    FOREIGN KEY ("author_id") REFERENCES users ("id") ON DELETE CASCADE
);
