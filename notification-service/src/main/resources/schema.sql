DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
    "id"            UUID,
    "user_id"       UUID NOT NULL,
    "title"         TEXT NOT NULL,
    "description"   TEXT NOT NULL,
    "is_read"       BOOLEAN DEFAULT 'false' NOT NULL,
    PRIMARY KEY("id")
);


