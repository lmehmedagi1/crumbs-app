CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
BEGIN
INSERT INTO notifications VALUES (''382fc8b8-349b-42d9-85f0-c2a47d2bca5f'', uuid_generate_v4(), current_timestamp, ''Lajk od usera 1'', false);
INSERT INTO notifications VALUES (''2eb4c3bb-569a-4af7-b763-5dab250d8bfe'', ''9c76205a-afa0-4699-9ec3-0ce0f10c515e'', current_timestamp, ''Lajk od usera 2'', false);
INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), current_timestamp, ''Komentar od usera 3'', true);
END;
' LANGUAGE PLPGSQL;