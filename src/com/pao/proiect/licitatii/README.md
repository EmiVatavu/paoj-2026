# Sistem de Licitații - Proiect PAO

Acest proiect reprezintă un sistem de gestionare a licitațiilor online, implementând funcționalități de înregistrare utilizatori, adăugare produse, plasare oferte și gestionare a procesului de licitare.

## 1.1 — Lista de acțiuni și interogări

1. Inregistrare utilizator(Adăugarea unui nou Participant (Licitator) sau Vânzător în sistem)
2. Adăugare produs(Un vânzător înregistrat adaugă un produs nou (Artă sau Electronic) pentru a fi licitat)
3. Creare licitație(Inițierea unei licitații pentru un produs existent, cu o dată de finalizare stabilită)
4. Plasare ofertă(Un licitator propune o sumă pentru o licitație activă (trebuie să fie mai mare decât oferta curentă))
5. Căutare licitații după categorie(Filtrarea licitațiilor active în funcție de tipul produsului (ex: doar produse de artă))
6. Listare oferte licitație(Afișarea tuturor ofertelor pentru o licitație, sortate descrescător după sumă)
7. Finalizare licitație(Închiderea oficială a unei licitații și desemnarea câștigătorului (oferta cu suma cea mai mare))
8. Istoric utilizator(Afișarea tuturor ofertelor plasate de un anumit utilizator în sistem)
9. Ștergere licitație(Eliminarea unei licitații din sistem, condiționată de absența ofertelor)
10. Ordonare produse după preț(Listarea tuturor produselor din sistem sortate crescător după prețul de pornire)

## 1.2 — Lista tipurilor de obiecte

1. `Utilizator` (Abstract)
2. `Membru` (Abstract)
3. `Licitator`
4. `Vanzator`
5. `Produs` (Abstract)
6. `ProdusArta`
7. `ProdusElectronic`
8. `Licitatie`
9. `Oferta`
10. `Identificator` (Imutabil)
