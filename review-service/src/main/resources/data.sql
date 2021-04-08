CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
' DECLARE
    vise UUID := uuid_generate_v4();
    vise2 UUID := uuid_generate_v4();
BEGIN
INSERT INTO reviews VALUES (''5ccafc30-b1b3-4f74-ba3c-79583a3129c5'', ''fb244360-88cb-11eb-8dcd-0242ac130003'', ''d913320a-baf1-43e0-b8b7-25f748e574ee'', true, 4, ''Dobar pravo'');
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), uuid_generate_v4(), false, 3, ''Meh bezze'');
INSERT INTO reviews VALUES (uuid_generate_v4(), vise2, uuid_generate_v4(), true, 5, ''Prejako fakat'');
INSERT INTO reviews VALUES (uuid_generate_v4(), vise2, uuid_generate_v4(), true, 5, ''Najbolji kolac'');
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), vise, false, 1, ''Jadno'');
END;
' LANGUAGE PLPGSQL;
