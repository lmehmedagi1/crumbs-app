CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    -- Notifications
    notification_1_id UUID := ''d35ef65f-6a62-41de-8b97-9a0e7c5edc04'';
    notification_2_id UUID := ''3e4c8055-79cd-4211-ab73-3225c3400005'';
    notification_3_id UUID := ''4b31b1df-ee90-463c-8d62-0f68bbcb444b'';
    notification_4_id UUID := ''aa6d5f06-c4d1-4ade-a776-bf46b6592414'';

    -- Users
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';

    -- Recipes
    cheesecake UUID := ''0668b655-97b3-4514-9f65-50cf8087fa46'';
    zimska_juha UUID := ''e530846c-9184-497d-923e-dc27ab0c5317'';

BEGIN
INSERT INTO notifications VALUES (notification_1_id, lela_id, manda_id, ''crumbs_user'', ''Subscription'', ''Anel Mandal has subscribed'', true, current_timestamp);
INSERT INTO notifications VALUES (notification_2_id, manda_id, medo_id, ''crumbs_user'', ''Subscription'', ''Medin Paldum has subscribed'', false, current_timestamp);
INSERT INTO notifications VALUES (notification_3_id, aki_id, cheesecake, ''recipe'', ''New comment'', ''Anel Mandal left a comment on your Cheesecake recipe'', true, current_timestamp);
INSERT INTO notifications VALUES (notification_4_id, manda_id, zimska_juha, ''recipe'', ''New comment'', ''Medin Paldum left a comment on your Zimska juha recipe'', false, current_timestamp);

END;
' LANGUAGE PLPGSQL;
