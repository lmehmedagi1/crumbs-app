CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
    DECLARE
        lela_id         UUID := uuid_generate_v4();
        manda_id        UUID := uuid_generate_v4();
        medo_id         UUID := uuid_generate_v4();
        aki_id          UUID := uuid_generate_v4();
    BEGIN
        INSERT INTO users
        VALUES (lela_id, ''lmehmedagi'', ''lmehmedagi1@etf.unsa.ba'',
                ''$2a$10$QTyNy5czbfGeid0fJIGFTO4Fb4Pa4c6XeO4Y1YXRxUQzv4uSo6w92'');
        INSERT INTO users
        VALUES (manda_id, ''amandal'', ''amandal1@etf.unsa.ba'',
                ''$2a$10$QTyNy5czbfGeid0fJIGFTO4Fb4Pa4c6XeO4Y1YXRxUQzv4uSo6w92'');
        INSERT INTO users
        VALUES (medo_id, ''mpaldum'', ''mpaldum1@etf.unsa.ba'',
                ''$2a$10$QTyNy5czbfGeid0fJIGFTO4Fb4Pa4c6XeO4Y1YXRxUQzv4uSo6w92'');
        INSERT INTO users
        VALUES (aki_id, ''aturkusic'', ''aturkusic1@etf.unsa.ba'',
                ''$2a$10$QTyNy5czbfGeid0fJIGFTO4Fb4Pa4c6XeO4Y1YXRxUQzv4uSo6w92'');

        INSERT INTO user_details
        VALUES (uuid_generate_v4(), ''Lejla'', ''Mehmedagic'', ''Female'', ''062123123'',
                decode(
                        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
                        ''base64''), lela_id);
        INSERT INTO user_details
        VALUES (uuid_generate_v4(), ''Anel'', ''Mandal'', ''Male'', ''062123123'',
                decode(
                        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
                        ''base64''), manda_id);
        INSERT INTO user_details
        VALUES (uuid_generate_v4(), ''Medin'', ''Paldum'', ''Male'', ''062123123'',
                decode(
                        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
                        ''base64''), medo_id);
        INSERT INTO user_details
        VALUES (uuid_generate_v4(), ''Arslan'', ''Turkusic'', ''Non-Binary'', ''062123123'',
                decode(
                        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
                        ''base64''), aki_id);

        INSERT INTO subscriptions VALUES (manda_id, lela_id);
        INSERT INTO subscriptions VALUES (manda_id, aki_id);
        INSERT INTO subscriptions VALUES (aki_id, medo_id);
        INSERT INTO subscriptions VALUES (medo_id, manda_id);
    END;
' LANGUAGE PLPGSQL;







