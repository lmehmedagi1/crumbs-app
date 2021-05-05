DROP TABLE IF EXISTS system_events;
CREATE TABLE system_events
(
    "id"              UUID,
    "timestamp"       timestamp NOT NULL,
    "service_name"    TEXT      NOT NULL,
    "resource_name"   TEXT      NOT NULL,
    "method"          TEXT      NOT NULL,
    "response_status" TEXT      NOT NULL,
    PRIMARY KEY ("id")
);
