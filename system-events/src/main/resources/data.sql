CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
' DECLARE
    -- Sys events
    sysevent_1_id UUID := ''53ebc440-297a-4960-90a3-468a7f129755'';
    sysevent_2_id UUID := ''d4b4d54d-160a-413b-9766-eb7ec42f992f'';
    sysevent_3_id UUID := ''9f7c3afb-b6d1-46ae-bc7c-51a8772a1330'';
    sysevent_4_id UUID := ''2aba109c-87c9-4dbd-8276-f160dfafd32c'';
    sysevent_5_id UUID := ''39490cb7-2a4c-4326-9b7f-8ee32de2aab6'';

BEGIN
    INSERT INTO system_events VALUES (sysevent_1_id, current_timestamp, ''Recipe service'', ''GET'', ''R1'', ''OK'');
    INSERT INTO system_events VALUES (sysevent_2_id, current_timestamp, ''Review service'', ''DELETE'', ''R2'', ''Neutral'');
    INSERT INTO system_events VALUES (sysevent_3_id, current_timestamp, ''Notification service'', ''GET'', ''R3'', ''Fail'');
    INSERT INTO system_events VALUES (sysevent_4_id, current_timestamp, ''User service'', ''POST'', ''R4'', ''Error'');
    INSERT INTO system_events VALUES (sysevent_5_id, current_timestamp, ''Recipe service'', ''POST'', ''R5'', ''OK'');
END;
' LANGUAGE PLPGSQL;
