CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';
BEGIN
INSERT INTO users
VALUES (lela_id, ''lmehmedagi'', ''lmehmedagi1@etf.unsa.ba'',
    ''$2y$10$IPqxD1wMn6kmnRI1rwT3ZeHqamUL46ZNsDtovq01Y.3Eh7lXAYQaW'');
INSERT INTO users
VALUES (manda_id, ''amandal'', ''amandal1@etf.unsa.ba'',
    ''$2y$10$IPqxD1wMn6kmnRI1rwT3ZeHqamUL46ZNsDtovq01Y.3Eh7lXAYQaW'');
INSERT INTO users
VALUES (medo_id, ''mpaldum'', ''mpaldum1@etf.unsa.ba'',
    ''$2y$10$IPqxD1wMn6kmnRI1rwT3ZeHqamUL46ZNsDtovq01Y.3Eh7lXAYQaW'');
INSERT INTO users
VALUES (aki_id, ''aturkusic'', ''aturkusic1@etf.unsa.ba'',
    ''$2y$10$IPqxD1wMn6kmnRI1rwT3ZeHqamUL46ZNsDtovq01Y.3Eh7lXAYQaW'');

INSERT INTO user_profile
VALUES (''Lejla'', ''Mehmedagic'', ''female'', ''062123123'',
    decode(
        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), true, lela_id);
INSERT INTO user_profile
VALUES (''Anel'', ''Mandal'', ''male'', ''062123123'',
    decode(
        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), true, manda_id);
INSERT INTO user_profile
VALUES (''Medin'', ''Paldum'', ''male'', ''062123123'',
    decode(
        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), true, medo_id);
INSERT INTO user_profile
VALUES (''Arslan'', ''Turkusic'', ''other'', ''062123123'',
    decode(
        ''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), true, aki_id);

INSERT INTO subscriptions VALUES (manda_id, lela_id);
INSERT INTO subscriptions VALUES (manda_id, aki_id);
INSERT INTO subscriptions VALUES (aki_id, medo_id);
INSERT INTO subscriptions VALUES (medo_id, manda_id);
INSERT INTO subscriptions VALUES (medo_id, lela_id);
INSERT INTO subscriptions VALUES (lela_id, manda_id);
INSERT INTO subscriptions VALUES (lela_id, aki_id);
INSERT INTO subscriptions VALUES (lela_id, medo_id);
END;
' LANGUAGE PLPGSQL;







