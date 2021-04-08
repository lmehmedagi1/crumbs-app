CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    -- Recipes
    sampita_id UUID := ''d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a'';
    monchay_id UUID := ''0668b655-97b3-4514-9f65-50cf8087fa46'';
    krempita_id UUID := ''2e0233d2-6e01-455c-8724-2117ad252ced'';
    omlet_id UUID := ''ac8ff8ff-7193-4c45-90bd-9c662cc0494a'';

    -- Categories
    kolac_cat UUID := ''d3f25855-9723-43c2-b29c-34626dd834d2'';
    dorucak_cat UUID := ''249cf0f8-c344-4b56-8eda-01ceb4ebfceb'';
    rucak_cat UUID := ''943c8274-d329-4d78-92b9-e2f3407a91a4'';
    veget_cat UUID := ''bfaed0fd-aa46-4708-8157-83bdb56bf2e6'';

    -- Ingredients
    slag_ing UUID := ''5438a531-27fe-406a-9e0d-8fd95c1e12e7'';
    visnja_ing UUID := ''c45e4bda-ae03-4fdf-a230-2473ea6ecfdb'';
    sir_ing UUID := ''d8dfea6d-238f-4b9e-a67b-76e97b0ac435'';
    jaja_ing UUID := ''89f1cf70-2824-443c-8eda-bfdeb4c2b3a2'';

    -- Diets
    veget_diet UUID := ''987c0974-0533-485a-aa99-e4bb3939ce10'';
    sport_diet UUID := ''87940ca7-807d-4d1b-af2e-f6a0c22ed2c8'';
    kolac_diet UUID := ''add537e6-12a6-410a-83f3-7c9cc95c2d7a'';

    -- Images
    sampita_slika1 UUID := ''aa5a6cdc-76f3-4eed-a175-e5843effd010'';
    sampita_slika2 UUID := ''8b5fe1a5-12d1-430f-b871-48ebd041affa'';
    krempita_slika1 UUID := ''aecd82e0-089c-4e64-b806-85837f553937'';
    monchay_slika1 UUID := ''bee3ff2b-92ec-42c3-b000-0018223fb622'';
    monchay_slika2 UUID := ''289937b5-829b-4fd2-afa1-0dd9c4e63e36'';
    omlet_slika1 UUID := ''049d907d-1a72-4091-89df-3ab088d26980'';

    -- Users
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';
BEGIN
INSERT INTO recipes VALUES (sampita_id, lela_id, ''Šampita'', ''Najgori kolac u istoriji'', ''1. Kupite sampitu; 2. Bacite je'');
INSERT INTO recipes VALUES (krempita_id, manda_id, ''Krempita'', ''Malo bolji od Šampite'', ''1. Kupite Krempitu; 2. Probajte malo; 3. Bacite je'');
INSERT INTO recipes VALUES (monchay_id, aki_id, ''Mon Chay'', ''Najjaci kolac u istoriji'', ''1. Kupite Mon Chay u Metropolisu; 2. Dajte Anelu'');
INSERT INTO recipes VALUES (omlet_id, medo_id, ''Omlet s gljivama'', ''Energetican dorucak'', ''1. Kupite jaja; 2. Kupite gljive; 3. Ispecite; 4. Profit?'');

INSERT INTO categories VALUES (kolac_cat, ''Kolac'');
INSERT INTO categories VALUES (dorucak_cat, ''Dorucak'');
INSERT INTO categories VALUES (rucak_cat, ''Rucak'');
INSERT INTO categories VALUES (veget_cat, ''Vegetarijanska'');

INSERT INTO ingredients VALUES (slag_ing, ''Šlag'');
INSERT INTO ingredients VALUES (visnja_ing, ''Visnja'');
INSERT INTO ingredients VALUES (sir_ing, ''Sir'');
INSERT INTO ingredients VALUES (jaja_ing, ''Jaja'');

INSERT INTO recipe_categories VALUES (sampita_id, kolac_cat);
INSERT INTO recipe_categories VALUES (monchay_id, kolac_cat);
INSERT INTO recipe_categories VALUES (krempita_id, kolac_cat);
INSERT INTO recipe_categories VALUES (krempita_id, dorucak_cat);
INSERT INTO recipe_categories VALUES (omlet_id, dorucak_cat);

INSERT INTO recipe_ingredients VALUES (sampita_id, slag_ing);
INSERT INTO recipe_ingredients VALUES (krempita_id, visnja_ing);
INSERT INTO recipe_ingredients VALUES (monchay_id, sir_ing);
INSERT INTO recipe_ingredients VALUES (monchay_id, visnja_ing);
INSERT INTO recipe_ingredients VALUES (omlet_id, jaja_ing);

INSERT INTO diets VALUES (sport_diet, ''Sporcka ishrana'', ''Idealna dijeta preko vikenda'', 3, ''t'', aki_id);
INSERT INTO diets VALUES (veget_diet, ''Vegetarijanska ishrana'', ''Full vegetarijanski vikend'', 7, ''f'', medo_id);
INSERT INTO diets VALUES (kolac_diet, ''Kolac ishrana'', ''Samo kolaci fakat'', 14, ''f'', lela_id);

INSERT INTO diet_recipes VALUES (sport_diet, omlet_id);
INSERT INTO diet_recipes VALUES (veget_diet, omlet_id);
INSERT INTO diet_recipes VALUES (veget_diet, krempita_id);

INSERT INTO images VALUES (sampita_slika1,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), sampita_id);
INSERT INTO images VALUES (sampita_slika2,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), sampita_id);
INSERT INTO images VALUES (monchay_slika1,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), monchay_id);
INSERT INTO images VALUES (monchay_slika2,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), monchay_id);
INSERT INTO images VALUES (krempita_slika1,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), krempita_id);
INSERT INTO images VALUES (omlet_slika1,
    decode(''iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/HgAGgwJ/lK3Q6wAAAABJRU5ErkJggg=='',
        ''base64''), omlet_id);
END;
' LANGUAGE PLPGSQL;
