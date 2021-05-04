DROP TABLE IF EXISTS system_events;
CREATE TABLE system_events
(
    "id"            UUID,
    "timestamp"     timestamp NOT NULL,
    "service_name"  TEXT      NOT NULL,
    "action_type"   TEXT      NOT NULL,
    "resource_name" TEXT      NOT NULL,
    "response_type" TEXT      NOT NULL,
    "message"       TEXT,
    PRIMARY KEY ("id")
);
