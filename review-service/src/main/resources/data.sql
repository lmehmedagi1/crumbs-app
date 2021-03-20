CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
BEGIN
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), uuid_generate_v4(), true, 4, ''Dobar pravo'');
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), uuid_generate_v4(), false, 3, ''Meh bezze'');
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), uuid_generate_v4(), true, 5, ''Prejako fakat'');
INSERT INTO reviews VALUES (uuid_generate_v4(), uuid_generate_v4(), uuid_generate_v4(), true, 5, ''Najbolji kolac'');
END;
' LANGUAGE PLPGSQL;