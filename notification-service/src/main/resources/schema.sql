DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
    "id"            TEXT,
    "user_id"       TEXT NOT NULL,
    "title"         TEXT NOT NULL,
    "description"   TEXT NOT NULL,
    "is_read"       BOOL DEFAULT 'false' NOT NULL,
    PRIMARY KEY("id")
);


