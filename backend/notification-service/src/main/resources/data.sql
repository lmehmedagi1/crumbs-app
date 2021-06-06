CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    -- Notifications
    notification_1_id UUID := ''d35ef65f-6a62-41de-8b97-9a0e7c5edc04'';
    notification_2_id UUID := ''3e4c8055-79cd-4211-ab73-3225c3400005'';

    -- Users
    lela_id UUID := ''3e8ec94c-3edf-49e0-b548-425088881f60'';
    manda_id UUID := ''75a8f34b-2539-452a-9325-b432dbe3b995'';
    medo_id UUID := ''d913320a-baf1-43e0-b8b7-25f748e574ee'';
    aki_id UUID := ''fc98612b-c0c2-4792-b2ac-acb7dd6555c1'';

BEGIN
INSERT INTO notifications VALUES (notification_1_id, lela_id, manda_id, ''crumbs_user'', ''Subscription'', ''Anel Mandal has subscribed'', true, current_timestamp);
INSERT INTO notifications VALUES (notification_2_id, manda_id, medo_id, ''crumbs_user'', ''Subscription'', ''Medin Paldum has subscribed'', false, current_timestamp);

END;
' LANGUAGE PLPGSQL;
