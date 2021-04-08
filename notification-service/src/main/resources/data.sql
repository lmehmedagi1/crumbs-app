CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    notification_1_id UUID := ''d35ef65f-6a62-41de-8b97-9a0e7c5edc04'';
    notification_2_id UUID := ''3e4c8055-79cd-4211-ab73-3225c3400005'';
    notification_3_id UUID := ''4b31b1df-ee90-463c-8d62-0f68bbcb444b'';
    notification_4_id UUID := ''aa6d5f06-c4d1-4ade-a776-bf46b6592414'';
    notification_5_id UUID := ''e87dad64-0023-483e-b2d0-35ad8c7cbfd4'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';
BEGIN
INSERT INTO notifications VALUES (''382fc8b8-349b-42d9-85f0-c2a47d2bca5f'', ''d913320a-baf1-43e0-b8b7-25f748e574ee'', current_timestamp, ''Lajk od usera 1'', false);
INSERT INTO notifications VALUES (''2eb4c3bb-569a-4af7-b763-5dab250d8bfe'', ''9c76205a-afa0-4699-9ec3-0ce0f10c515e'', current_timestamp, ''Lajk od usera 2'', false);
INSERT INTO notifications VALUES (uuid_generate_v4(), ''9c76205a-afa0-4699-9ec3-0ce0f10c515e'', current_timestamp, ''Komentar od usera 3'', false);
INSERT INTO notifications VALUES (uuid_generate_v4(), ''9c76205a-afa0-4699-9ec3-0ce0f10c515e'', current_timestamp, ''Komentar od usera 4'', false);
INSERT INTO notifications VALUES (uuid_generate_v4(), ''9c76205a-afa0-4699-9ec3-0ce0f10c515e'', current_timestamp, ''Komentar od usera 5'', false);
END;
' LANGUAGE PLPGSQL;