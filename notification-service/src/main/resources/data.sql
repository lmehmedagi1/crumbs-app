CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
BEGIN
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), current_timestamp, ''Lajk od usera 1'', false);
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), current_timestamp, ''Lajk od usera 2'', false);
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), current_timestamp, ''Komentar od usera 3'', true);
END;
' LANGUAGE PLPGSQL;