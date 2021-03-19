CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
BEGIN
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), ''Lajk'', ''User user_id ti je '' ||
    ''upravo lajkovo nesto'', false);
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), ''Lajk'', ''User user_id ti je '' ||
    ''upravo lajkovo nesto'', false);
    INSERT INTO notifications VALUES (uuid_generate_v4(), uuid_generate_v4(), ''Koment'', ''User user_id ti je '' ||
    ''upravo komentovo nesto'', false);
END;
' LANGUAGE PLPGSQL;