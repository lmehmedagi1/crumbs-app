CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    sampita_id UUID := ''fb244360-88cb-11eb-8dcd-0242ac130003'';
    user_id UUID := ''bb244360-88cb-11eb-8dcd-0242ac130003'';
    monchay_id UUID := uuid_generate_v4();
    monchay_id1 UUID := uuid_generate_v4();
    kolac_cat_id UUID := ''fb244361-88cb-14eb-8ecd-0242ac130003'';
    dorucak_cat_id UUID := uuid_generate_v4();
    sir_ing_id UUID := uuid_generate_v4();
    visnja_ing_id UUID := uuid_generate_v4();
    slag_ing_id UUID := uuid_generate_v4();
    sporcka_diet_id UUID := ''bb244361-88cb-14eb-8ecd-0242ac130003'';
BEGIN
INSERT INTO recipes VALUES (sampita_id,''bb244360-88cb-11eb-8dcd-0242ac130003'', ''Šampita'', ''Najgori kolac u istoriji'', ''1. Kupite sampitu; 2. Bacite je'');
INSERT INTO recipes VALUES (monchay_id,''bb244360-88cb-11eb-8dcd-0242ac130003'', ''Mon Chay'', ''Najjaci kolac u istoriji'', ''1. Kupite Mon Chay u Metropolisu; 2. Dajte Anelu'');
INSERT INTO recipes VALUES (monchay_id1,''bb244360-88cb-11eb-8dcd-0242ac130003'', ''Mon Chay'', ''Najjaci kolac u istoriji'', ''1. Kupite Mon Chay u Metropolisu; 2. Dajte Anelu'');

INSERT INTO categories VALUES (kolac_cat_id, ''Kolac'');
INSERT INTO categories VALUES (dorucak_cat_id, ''Dorucak'');

INSERT INTO ingredients VALUES (sir_ing_id, ''Sir'');
INSERT INTO ingredients VALUES (visnja_ing_id, ''Visnja'');
INSERT INTO ingredients VALUES (slag_ing_id, ''Slag'');

INSERT INTO recipe_categories VALUES (sampita_id, kolac_cat_id);
INSERT INTO recipe_categories VALUES (monchay_id, kolac_cat_id);
INSERT INTO recipe_categories VALUES (monchay_id, dorucak_cat_id);

INSERT INTO recipe_ingredients VALUES (sampita_id, slag_ing_id);
INSERT INTO recipe_ingredients VALUES (monchay_id, sir_ing_id);
INSERT INTO recipe_ingredients VALUES (monchay_id, visnja_ing_id);

INSERT INTO diets VALUES (sporcka_diet_id, ''Sporcka ishrana'', ''Idealna dijeta preko vikenda'', 3, ''f'', uuid_generate_v4());

INSERT INTO diet_recipes VALUES (sporcka_diet_id, monchay_id);
INSERT INTO diet_recipes VALUES (sporcka_diet_id, sampita_id);

INSERT INTO images VALUES (uuid_generate_v4(),
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), sampita_id);
INSERT INTO images VALUES (uuid_generate_v4(),
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), sampita_id);
INSERT INTO images VALUES (uuid_generate_v4(),
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), monchay_id);
INSERT INTO images VALUES (uuid_generate_v4(),
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), monchay_id);
END;
' LANGUAGE PLPGSQL;
