# CRUMBS APP

CRUMBS APP je aplikacija koja se koristi za pronalaženje recepata za hranu. Možete odabrati kategoriju; želite li doručak, ručak, večeru? Tražite desert, vegetarijanski izbor, hranu za djecu i još mnogo toga? Na osnovu kategorije koju odaberete prikazat će vam se adekvatni recepti. Korisnici će moći ostaviti povratne informacije i ocjene za svaki recept. Svakog dana svim korisnicima je globalno dostupan drugačiji „Recept dana“, kao i drugi najbolje ocijenjeni recepti. 

## Članovi tima

- [Anel Mandal](https://github.com/mand0ne)
- [Lejla Mehmedagić](https://github.com/lmehmedagi1)
- [Medin Paldum](https://github.com/mpaldum1)
- [Arslan Turkušić](https://github.com/aturkusic)

## Demo aplikacije

- [Video](https://drive.google.com/drive/folders/1ZvSa7iVrfdQpD5iSKFoIk_lkG3-shmrJ?usp=sharing)

## Konfiguracijske datoteke

- [Repozitorij](https://github.com/lmehmedagi1/crumbs-app-configuration)

## Uputstvo za pokretanje

```
docker compose build
docker compose up
```

## Uputstvo za pokretanje selenium IDE testova
- Instalirati Selenium IDE ekstenziju za browser
- Učitati projekat za testove, Crumbs-app.side, koji se nalazi u root folderu aplikacije
- Ponovo pokrenuti servise kako bi se baza restartovala
- Odlogovati se sa aplikacije na frontendu
- Pokrenuti testove u prozoru Selenium IDE ekstenzije 

## Funkcionalnosti

Autentikacija:
- Prijava i registracija
- Verifikacija računa putem email-a
- Odjava
- Zaboravljena lozinka

Upravljanje profilom
- Promjena ličnih podataka
- Promjena avatara

Upravljanje receptima
- Dodavanje recepata
- Brisanje vlastitih recepata
- Izmjena vlastitih recepata

Pregled recepata
- Pregled svih recepata
- Detaljan pregled odabranog recepta: sastojci, galerija i upute 
- Pretraživanje recepata na osnovu naziva
- Filtriranje recepata, npr. na osnovu osnovu tipa ishrane
- Pregled najbolje ocijenjenih recepata na dnevnoj i mjesečnoj bazi
- Pregled omiljenih recepata

Upravljanje planovima ishrane
- Kreiranje plana ishrane na određeni period
- Označavanje plana kao globalno dostupan
- Pregled svih globalno dostupnih planova
- Pregled liste sastojaka potrebnih za kreirani plan ishrane

Recenzija recepata
- Ocjenjivanje recepata ocjenom od 1 do 5
- Komentarisanje recepata
- Označavanje recepta kao jedan od omiljenih
- Primanje notifikacije o novoj recenziji na vaš recept

Pretplata na autore
- Pretplata na omiljene autore
- Poništavanje pretplate na pretplaćene autore
- Pregled autora na koji ste pretplaćeni
- Primanje notifikacije o novoj pretplati
- Primanje notifikacije o novom receptu omiljenih autora

## Pristup ponovnoj izradi projekta

Ukoliko bismo projekat započeli iznova pristup realizaciji bi zasigurno bio drugačiji. Primarno, mnogo više vremena i truda bi bilo uloženo u fazi planiranja i dizajna. Smatramo da bi time efektivno uklonili neželjene ispravke implementacije koje su bile nužne u kasnijoj fazi projekta. Također, uvođenje sofvera za rukovođenje projekta (Jira) u kasnoj fazi projekta pokazalo se vrlo korisno te je poboljšalo cjelokupan rad i organizaciju na projektu. Ovakav pristup bi sigurno bio korišten od prvog dana projekta. 

