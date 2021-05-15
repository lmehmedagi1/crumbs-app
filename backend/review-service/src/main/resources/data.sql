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

    -- Recipes
    sampita_id UUID := ''d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a'';
    monchay_id UUID := ''0668b655-97b3-4514-9f65-50cf8087fa46'';
    krempita_id UUID := ''2e0233d2-6e01-455c-8724-2117ad252ced'';

    -- Users
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';
BEGIN
INSERT INTO reviews VALUES (review_1_id, sampita_id, medo_id, true, 4, ''Dobar pravo'');
INSERT INTO reviews VALUES (review_2_id, sampita_id, medo_id, false, 3, ''Meh bezze'');
INSERT INTO reviews VALUES (review_3_id, sampita_id, manda_id, false, 1, ''Fuj :)'');
INSERT INTO reviews VALUES (review_4_id, monchay_id, aki_id, true, 5, ''Prejako fakat'');
INSERT INTO reviews VALUES (review_5_id, monchay_id, aki_id, true, 5, ''Najbolji kolac'');
INSERT INTO reviews VALUES (review_6_id, krempita_id, lela_id, false, 1, ''Jadno'');
END;
' LANGUAGE PLPGSQL;
