DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS notification;
DROP TYPE IF EXISTS ENTYPE;
CREATE TYPE ENTYPE as ENUM ('recipe', 'diet', 'crumbs_user');

CREATE TABLE notifications
(
    "id"          UUID,
    "user_id"     UUID                    NOT NULL,
    "entity_id"   UUID                    NOT NULL,
    "entity_type" ENTYPE                  NOT NULL,
    "title"       TEXT                    NOT NULL,
    "description" TEXT                    NOT NULL,
    "is_read"     BOOLEAN DEFAULT 'false' NOT NULL,
    "created_at"  timestamp               NOT NULL,
    PRIMARY KEY ("id")
);
