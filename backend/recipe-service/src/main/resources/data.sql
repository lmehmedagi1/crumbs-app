CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO
'
DECLARE
    -- Recipes
    sampita UUID := ''d3cd7d6f-b9c5-40aa-bbd0-487c47411b8a'';
    cheesecake UUID := ''0668b655-97b3-4514-9f65-50cf8087fa46'';
    parmezan_piletina UUID := ''2e0233d2-6e01-455c-8724-2117ad252ced'';
    tjestenina_tuna UUID := ''ac8ff8ff-7193-4c45-90bd-9c662cc0494a'';
    zimska_juha UUID := ''e530846c-9184-497d-923e-dc27ab0c5317'';

  -- categories types
    tezina_id UUID := ''6638b186-c45a-43a6-bf81-51676fc90bc0'';
    grupa_id UUID := ''6f95d94a-e26d-40d2-9c07-1295be65338c'';
    nacin_id UUID := ''e4d5010a-a7a8-4cb1-aa35-2e515bb2652c'';
    sezona_id UUID := ''895c110b-ec2f-4892-9370-34383667a930'';

   -- Categories
    jednostavno_cat UUID := ''7d8757e9-1e01-4227-8bc8-93dc7b60004b'';
    srednje_cat UUID := ''47d25350-16de-4364-a51f-74340ed585e3'';
    slozeno_cat UUID := ''6a1b9c4b-ccc7-4ad1-9920-2e401e2288c6'';
    desert_cat UUID := ''d3f25855-9723-43c2-b29c-34626dd834d2'';
    glavno_jelo_cat UUID := ''249cf0f8-c344-4b56-8eda-01ceb4ebfceb'';
    juhe_cat UUID := ''943c8274-d329-4d78-92b9-e2f3407a91a4'';
    pica_cat UUID := ''bfaed0fd-aa46-4708-8157-83bdb56bf2e6'';
    notermic_cat UUID := ''763129e0-6c42-46d2-8e04-cca05b009487'';
    kuhanje_cat UUID := ''79749c03-94c3-4517-a27c-046c52d673a0'';
    pecenje_cat UUID := ''80787355-6d01-4891-b575-33aebd0b750b'';
    przenje_cat UUID := ''718a8452-22a4-4532-8fb5-dd20af06b782'';
    zima_cat UUID := ''e5ab1102-f081-4940-b437-f381dd1699ec'';


       -- ingredients
    pileca_prsa UUID := ''5438a531-27fe-406a-9e0d-8fd95c1e12e7'';
    bijeli_luk UUID := ''c45e4bda-ae03-4fdf-a230-2473ea6ecfdb'';
    maslac UUID := ''d8dfea6d-238f-4b9e-a67b-76e97b0ac435'';
    jaje UUID := ''89f1cf70-2824-443c-8eda-bfdeb4c2b3a2'';
    so UUID := ''9ec607ca-80ec-4052-bbcd-918209df65f7'';
    biber UUID := ''21666d1e-1278-4f7d-819e-c6b273fbf2c8'';
    secer UUID := ''9fd8f740-7a97-4db9-ae2c-4743b9d27385'';
    parmezan UUID := ''30893d5b-dceb-49f8-9d63-a6c8a870839d'';
    keks UUID := ''ee4d4925-f18e-4ca3-aa3b-cb5e07e78b26'';
    tresnja UUID := ''4e24b29e-85df-4bb2-a283-dfdd675ffc3b'';
    konzerva_tuna UUID := ''caed6d96-0d2a-49bd-bb35-7fba3d632c9d'';
    crveni_luk UUID := ''0d04eba0-3f3b-4826-9339-54de7370387d'';
    persin UUID := ''80696b1c-d7b8-4f29-9b20-6d5fc7bffaeb'';
    tjestenina UUID := ''97654172-f73b-4b0e-a97c-d7a28801f00c'';
    slag UUID := ''80696b1c-f73b-4db9-ae2c-4743b9d27385'';
    maslinovo_ulje UUID := ''743ff69e-1d26-4ca9-85f7-f40c89c88168'';
    sampinjoni UUID := ''8c6f0c23-931a-4c7f-856a-9a6db3bef534'';
    mrkva UUID := ''60083235-6324-4f80-90b1-f58a5162a086'';
    kelj UUID := ''05a4b038-bac0-4a2d-9d0f-94b8064b916f'';


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
    joe_id UUID := ''5da6df9e-1940-4837-882f-ecb1c2b26189'';

        -- Diets
    veget_diet UUID := ''987c0974-0533-485a-aa99-e4bb3939ce10'';
    sport_diet UUID := ''87940ca7-807d-4d1b-af2e-f6a0c22ed2c8'';
    kolac_diet UUID := ''add537e6-12a6-410a-83f3-7c9cc95c2d7a'';

BEGIN

INSERT INTO recipes VALUES (sampita, lela_id, ''Šampita'', ''Najgori kolac u istoriji'', ''1. Kupite sampitu$2. Bacite je'', null, 2, current_timestamp, current_timestamp + (10 * interval ''1 minute''));
INSERT INTO recipes VALUES (cheesecake, aki_id, ''Cheesecake sa trešnjom'', ''Po receptu iz Tuzle :)'', ''1. Kupite gotovu i ne smarajte se$2. Vrijeme = novac'', null, 1, current_timestamp, current_timestamp + (20 * interval ''1 minute''));

INSERT INTO recipes VALUES (parmezan_piletina, medo_id, ''Paremezan piletina'', ''Danas nešto na brzinu za ručak! Ovo je samo još jedan način pripreme piletine s parmezanom. Jednostavno, a tako fino.'',
''1. Zagrijte pećnicu na 200C. Umutite jedno jaje i dodajte mu otopljeni maslac, protisnuti češnjak i sjeckani peršin. U drugoj zdjeli pomiješajte krušne mrvice, parmezan, talijanski mix, sol i papar$
2. Pileća prsa prerežite na pola po dužini, a zatim svaku polovicu po širini, tako da od jednog komada pilećih prsa dobite 4 filea(možda i 5 ako su prsa deblja). Svaki komad umočite u maslac s češnjakom, a zatim u mješavinu od krušnih mrvica i parmezana.$
3. Meso poslažite u posudu za pečenje i prelite s ostatkom otopljenog maslaca. Pecite 20-30 minuta, tj. dok piletina ne dobije lijepu zlatnu boju i hrskavu koricu.'',
''Piletinu poslužite toplu s prilogom po želji. Ja najviše volim uz rižu ili pečeni krumpir. Dobar tek!'', 45, current_timestamp, current_timestamp + (30 * interval ''1 minute''));

INSERT INTO recipes VALUES (tjestenina_tuna, lela_id, ''Tjestenina s tunom'', null, ''1. Staviti kuhati tjesteninu po želji. Osobno, kuham pene.$2. Crveni luk i češnjak usitniti. Otvoriti tri konzerve tune (po 80 g). Malo maslinovog ulja iz konzerve uliti na tavu, ostatak ukloniti. Ne preporučam staviti puno ulja jer će to previše razrijediti umak.;
$3. Na maslinovom ulju kratko prepeći crveni luk. Stavljam ga vrlo malo, otprilike jednu čajnu žličicu usitnjenog luka.$4. Dodati parmezan i promiješati. Vrhnje se ne smije dugo držati na plamenu - od pola do maksimalno jedne minute - stoga je potrebno imati sve sastojke na dohvat ruke.$
5. U bijeli umak dodati ocijeđenu tunu iz konzerve na način da se previše ne izmrvi, te lagano promiješati.$6. Tjesteninu staviti na tanjur, preko nje preliti bijeli umak i posuti s malo prešina.'',
''Priprema jela traje zapravo onoliko koliko je potrebno tjestenini da se skuha. Umak je brzo gotov. :) Osobno, ne stavljam nikakve začine (osim peršina), pogotovo ne sol s obzirom na to da mi je parmezan dovoljno slan.'', 20, current_timestamp, current_timestamp + (40 * interval ''1 minute'') );

INSERT INTO recipes VALUES (zimska_juha, manda_id, ''Zimska juha'', null, ''1. Naruci iz restorana'', null, 1, current_timestamp, current_timestamp + (50 * interval ''1 minute''));


INSERT INTO category_type VALUES (tezina_id, ''Tezina pripreme'');
INSERT INTO category_type VALUES (nacin_id, ''Nacin pripreme'');
INSERT INTO category_type VALUES (grupa_id, ''Grupa jela'');
INSERT INTO category_type VALUES (sezona_id, ''Sezona'');

INSERT INTO categories VALUES (jednostavno_cat, tezina_id, ''Jednostavno'');
INSERT INTO categories VALUES (srednje_cat, tezina_id, ''Srednje'');
INSERT INTO categories VALUES (slozeno_cat, tezina_id, ''Slozeno'');

INSERT INTO categories VALUES (desert_cat, grupa_id, ''Desert'');
INSERT INTO categories VALUES (glavno_jelo_cat, grupa_id, ''Glavno jelo'');
INSERT INTO categories VALUES (juhe_cat, grupa_id, ''Juhe'');
INSERT INTO categories VALUES (pica_cat, grupa_id, ''Pića'');
INSERT INTO categories VALUES (notermic_cat, nacin_id, ''Bez termičke obrade'');
INSERT INTO categories VALUES (kuhanje_cat, nacin_id, ''Kuhanje'');
INSERT INTO categories VALUES (pecenje_cat, nacin_id, ''Pecenje'');
INSERT INTO categories VALUES (przenje_cat, nacin_id, ''Przenje'');
INSERT INTO categories VALUES (zima_cat, sezona_id, ''Zima'');


INSERT INTO ingredients VALUES (pileca_prsa, ''Pileća prsa'');
INSERT INTO ingredients VALUES (bijeli_luk, ''Bijeli luk'');
INSERT INTO ingredients VALUES (maslac, ''Maslac'');
INSERT INTO ingredients VALUES (jaje, ''Jaja'');
INSERT INTO ingredients VALUES (so, ''So'');
INSERT INTO ingredients VALUES (biber, ''Biber'');
INSERT INTO ingredients VALUES (secer, ''Šećer'');
INSERT INTO ingredients VALUES (parmezan, ''Parmezan sir'');
INSERT INTO ingredients VALUES (keks, ''Keks'');
INSERT INTO ingredients VALUES (tresnja, ''Trešnja'');
INSERT INTO ingredients VALUES (konzerva_tuna, ''Tuna u konzervi'');
INSERT INTO ingredients VALUES (crveni_luk, ''Crveni luk'');
INSERT INTO ingredients VALUES (persin, ''Peršun'');
INSERT INTO ingredients VALUES (tjestenina, ''Tjestenina'');
INSERT INTO ingredients VALUES (slag, ''Šlag'');
INSERT INTO ingredients VALUES (maslinovo_ulje, ''Maslinovo ulje'');
INSERT INTO ingredients VALUES (sampinjoni, ''Šampinjoni'');
INSERT INTO ingredients VALUES (mrkva, ''Mrkva'');
INSERT INTO ingredients VALUES (kelj, ''Kelj'');

INSERT INTO recipe_categories VALUES (sampita, desert_cat);
INSERT INTO recipe_categories VALUES (sampita, jednostavno_cat);
INSERT INTO recipe_categories VALUES (cheesecake, desert_cat);
INSERT INTO recipe_categories VALUES (cheesecake, slozeno_cat);
INSERT INTO recipe_categories VALUES (parmezan_piletina, glavno_jelo_cat);
INSERT INTO recipe_categories VALUES (parmezan_piletina, srednje_cat);
INSERT INTO recipe_categories VALUES (parmezan_piletina, pecenje_cat);
INSERT INTO recipe_categories VALUES (tjestenina_tuna, glavno_jelo_cat);
INSERT INTO recipe_categories VALUES (tjestenina_tuna, srednje_cat);
INSERT INTO recipe_categories VALUES (tjestenina_tuna, kuhanje_cat);
INSERT INTO recipe_categories VALUES (zimska_juha, juhe_cat);
INSERT INTO recipe_categories VALUES (zimska_juha, kuhanje_cat);
INSERT INTO recipe_categories VALUES (zimska_juha, zima_cat);

INSERT INTO recipe_ingredients VALUES (sampita, slag, 250, ''g'');
INSERT INTO recipe_ingredients VALUES (sampita, maslac, 50, ''g'');
INSERT INTO recipe_ingredients VALUES (sampita, jaje, 1, ''kom'');
INSERT INTO recipe_ingredients VALUES (cheesecake, slag, 250, ''g'');
INSERT INTO recipe_ingredients VALUES (cheesecake, maslac, 50, ''g'');
INSERT INTO recipe_ingredients VALUES (cheesecake, jaje, 1, ''kom'');
INSERT INTO recipe_ingredients VALUES (cheesecake, tresnja, 150, ''g'');

INSERT INTO diets VALUES (sport_diet, aki_id, ''Sporcka ishrana'', ''Idealna dijeta preko vikenda'', 3, ''true'', current_timestamp, current_timestamp + (10 * interval ''1 minute''));
INSERT INTO diets VALUES (veget_diet, medo_id, ''Vegetarijanska ishrana'', ''Full vegetarijanski vikend'', 7, ''false'', current_timestamp, current_timestamp + (15 * interval ''1 minute''));
INSERT INTO diets VALUES (kolac_diet, lela_id, ''Kolac ishrana'', ''Samo kolaci fakat'', 14, ''false'', current_timestamp, current_timestamp + (20 * interval ''1 minute''));


INSERT INTO diet_recipes VALUES (sport_diet, parmezan_piletina);
INSERT INTO diet_recipes VALUES (veget_diet, cheesecake);
INSERT INTO diet_recipes VALUES (veget_diet, zimska_juha);
INSERT INTO diet_recipes VALUES (kolac_diet, sampita);
INSERT INTO diet_recipes VALUES (kolac_diet, cheesecake);


INSERT INTO images VALUES (sampita_slika1, ''id:IJb-yHir50sAAAAAAAAAFA'', sampita);
INSERT INTO images VALUES (sampita_slika2, ''id:IJb-yHir50sAAAAAAAAAFA'', sampita);
INSERT INTO images VALUES (monchay_slika1, ''id:IJb-yHir50sAAAAAAAAAFA'', cheesecake);
INSERT INTO images VALUES (monchay_slika2, ''id:IJb-yHir50sAAAAAAAAAFA'', cheesecake);
INSERT INTO images VALUES (krempita_slika1, ''id:IJb-yHir50sAAAAAAAAAFA'', sampita);
INSERT INTO images VALUES (omlet_slika1, ''id:IJb-yHir50sAAAAAAAAAFA'', zimska_juha);
INSERT INTO images VALUES (gen_random_uuid(), ''id:IJb-yHir50sAAAAAAAAAFA'', zimska_juha);
INSERT INTO images VALUES (gen_random_uuid(), ''id:IJb-yHir50sAAAAAAAAAFA'', zimska_juha);

INSERT INTO images VALUES (gen_random_uuid(), ''id:IJb-yHir50sAAAAAAAAAFA'', parmezan_piletina);
INSERT INTO images VALUES (gen_random_uuid(), ''id:IJb-yHir50sAAAAAAAAAFA'', parmezan_piletina);
INSERT INTO images VALUES (gen_random_uuid(), ''id:IJb-yHir50sAAAAAAAAAFA'', parmezan_piletina);
END;
' LANGUAGE PLPGSQL;
