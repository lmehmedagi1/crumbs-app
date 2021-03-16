DROP TABLE IF EXISTS users;
CREATE TABLE users (
	"id"	TEXT,
	"first_name" TEXT NOT NULL,
	"last_name" TEXT NOT NULL,
	"email" TEXT NOT NULL,
	"password" TEXT NOT NULL,
	PRIMARY KEY("id")
);
