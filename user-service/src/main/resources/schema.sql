DROP TABLE IF EXISTS user_details;
DROP TABLE IF EXISTS users;
CREATE TABLE users (
	"id"	TEXT,
	"username" TEXT NOT NULL,
	"email" TEXT NOT NULL,
	"password" TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE user_details (
	"id"	TEXT,
	"first_name" TEXT NOT NULL,
	"last_name" TEXT NOT NULL,
	"gender" TEXT NOT NULL,
	"phone_number" TEXT NOT NULL,
	"avatar" bytea NOT NULL,
	"user_id" TEXT NOT NULL,
	FOREIGN KEY("user_id") REFERENCES users("id"),
	PRIMARY KEY("id")
);
