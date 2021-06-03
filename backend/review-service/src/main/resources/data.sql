CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DO
' DECLARE
    -- Reviews
    review_1_id UUID := ''9469a486-1e50-4aaf-a760-1daf770a2147'';
    review_2_id UUID := ''22b01334-417b-4a8f-8fb6-79bf003fdf4d'';
    review_3_id UUID := ''53f5e7d3-1942-4ff4-85cf-401a4b7b1334'';
    review_4_id UUID := ''b2f65fd8-b3df-4f8b-9280-5356d65eef19'';
    review_5_id UUID := ''0ca22bda-fe5d-46c1-8ea9-0fcc5c97ef0b'';
    review_6_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170844a4245'';
    review_7_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170845a2245'';
    review_8_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170845a2246'';
    review_9_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170845a2247'';
    review_10_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170845a2248'';
    review_11_id UUID := ''df41bb67-1ae7-49e4-b4b3-c170845a2249'';
    -- Recipes
    sampita UUID := ''d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a'';
    cheesecake UUID := ''0668b655-97b3-4514-9f65-50cf8087fa46'';
    parmezan_piletina UUID := ''2e0233d2-6e01-455c-8724-2117ad252ced'';
    tjestenina_tuna UUID := ''ac8ff8ff-7193-4c45-90bd-9c662cc0494a'';
    zimska_juha UUID := ''e530846c-9184-497d-923e-dc27ab0c5317'';
    krempita UUID := ''2e0233d2-6e01-455c-8724-2117ad252ced'';
    -- Users
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';
    joe_id UUID := ''5da6df9e-1940-4837-882f-ecb1c2b26189'';
    -- Diets
    veget_diet UUID := ''987c0974-0533-485a-aa99-e4bb3939ce10'';
    sport_diet UUID := ''87940ca7-807d-4d1b-af2e-f6a0c22ed2c8'';
    kolac_diet UUID := ''add537e6-12a6-410a-83f3-7c9cc95c2d7a'';
BEGIN
    INSERT INTO review VALUES (review_1_id, medo_id, veget_diet, ''diet'', false, 1, ''SAMO MESO BAJO v1'', current_timestamp, current_timestamp + (10 * interval ''1 minute''));
    INSERT INTO review VALUES (review_2_id, medo_id, parmezan_piletina, ''recipe'', true, 5, ''SAMO MESO BAJO v2'', current_timestamp, current_timestamp + (15 * interval ''1 minute''));
    INSERT INTO review VALUES (review_3_id, manda_id, zimska_juha, ''recipe'', true, 4, ''Solidno iskreno :)'', current_timestamp, current_timestamp + (10 * interval ''1 minute''));
    INSERT INTO review VALUES (review_4_id, aki_id, parmezan_piletina, ''recipe'', true, 5, ''Prejako fakat'', current_timestamp, current_timestamp + (16 * interval ''1 minute''));
    INSERT INTO review VALUES (review_11_id, lela_id, cheesecake, ''recipe'', true, 5, ''Prejako fakat24242'', current_timestamp, current_timestamp + (16 * interval ''1 minute''));
    INSERT INTO review VALUES (review_5_id, aki_id, sampita, ''recipe'', false, 2, ''Meh'', current_timestamp, current_timestamp + (14 * interval ''1 minute''));
    INSERT INTO review VALUES (review_6_id, lela_id, kolac_diet, ''diet'', true, 5, ''Yessir boi'', current_timestamp, current_timestamp + (7 * interval ''1 minute''));
    INSERT INTO review VALUES (review_8_id, lela_id, krempita, ''recipe'', true, 4, ''Yessir boiii1'', current_timestamp, current_timestamp + (7 * interval ''1 minute''));
    INSERT INTO review VALUES (review_9_id, medo_id, krempita, ''recipe'', true, 4, ''Yessir boiii2'', current_timestamp, current_timestamp + (7 * interval ''1 minute''));
    INSERT INTO review VALUES (review_10_id, aki_id, krempita, ''recipe'', true, 4, ''Yessir boiii3'', current_timestamp, current_timestamp + (7 * interval ''1 minute''));
END;
' LANGUAGE PLPGSQL;