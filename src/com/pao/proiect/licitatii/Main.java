package com.pao.proiect.licitatii;

import com.pao.proiect.licitatii.exception.SumaPreaMicaException;
import com.pao.proiect.licitatii.model.*;
import com.pao.proiect.licitatii.service.LicitatieService;
import com.pao.proiect.licitatii.service.UtilizatorService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        initializeazaBazaDeDate(false);

        UtilizatorService uService = UtilizatorService.getInstance();
        LicitatieService lService = LicitatieService.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("    SISTEM DE LICITATII   ");
        System.out.println("=========================================");

        boolean ruleaza = true;
        while (ruleaza) {
            System.out.println("\n--- MENIU PRINCIPAL ---");
            System.out.println("1. Inregistrare utilizator (Licitator / Vanzator)");
            System.out.println("2. Stergere utilizator");
            System.out.println("3. Adaugare produs (Arta / Electronic)");
            System.out.println("4. Creare licitatie pentru produs");
            System.out.println("5. Inchidere / Finalizare licitatie");
            System.out.println("6. Plasare oferta pe o licitatie");
            System.out.println("7. Filtrare licitatii dupa categorie");
            System.out.println("8. Listare toate licitatiile si oferte");
            System.out.println("9. Vizualizare istoric oferte per licitator");
            System.out.println("10. Stergere licitatie (daca nu are oferte)");
            System.out.println("11. Ordonare produse dupa pretul de pornire");
            System.out.println("12. Listare toti utilizatorii inregistrati");
            System.out.println("13. Resetare completa baza de date (atentie!)");
            System.out.println("0. Iesire");
            System.out.print("Alege o optiune: ");

            String optiuneStr = scanner.nextLine().trim();
            if (optiuneStr.isEmpty()) {
                continue;
            }

            try {
                int optiune = Integer.parseInt(optiuneStr);
                switch (optiune) {
                    case 1:
                        inregistrareUtilizator(uService, scanner);
                        break;
                    case 2:
                        stergereUtilizator(uService, scanner);
                        break;
                    case 3:
                        adaugareProdus(lService, scanner);
                        break;
                    case 4:
                        creareLicitatie(lService, scanner);
                        break;
                    case 5:
                        inchideLicitatie(lService, scanner);
                        break;
                    case 6:
                        plaseazaOferta(lService, uService, scanner);
                        break;
                    case 7:
                        filtrareLicitatii(lService, scanner);
                        break;
                    case 8:
                        listeazaLicitatii(lService);
                        break;
                    case 9:
                        vizualizareIstoric(lService);
                        break;
                    case 10:
                        stergereLicitatie(lService, scanner);
                        break;
                    case 11:
                        ordonareProduse(lService);
                        break;
                    case 12:
                        listeazaUtilizatori(uService);
                        break;
                    case 13:
                        System.out.print("Esti sigur ca vrei sa stergi toate datele? (da/nu): ");
                        String raspuns = scanner.nextLine().trim().toLowerCase();
                        if (raspuns.equals("da") || raspuns.equals("y") || raspuns.equals("d")) {
                            initializeazaBazaDeDate(true);
                            System.out.println("Baza de date a fost resetata si tabelele au fost recreate!");
                        } else {
                            System.out.println("Resetare anulata.");
                        }
                        break;
                    case 0:
                        ruleaza = false;
                        System.out.println("La revedere!");
                        break;
                    default:
                        System.out.println("Optiune invalida! Incearca din nou.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Te rugam sa introduci un numar valid.");
            } catch (Exception e) {
                System.out.println("A aparut o eroare: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void inregistrareUtilizator(UtilizatorService uService, Scanner scanner) {
        System.out.println("\n--- Inregistrare Utilizator ---");
        System.out.println("Alege tipul: 1. Licitator | 2. Vanzator");
        System.out.print("Optiune: ");
        String tipStr = scanner.nextLine().trim();
        if (!tipStr.equals("1") && !tipStr.equals("2")) {
            System.out.println("Tip invalid!");
            return;
        }

        System.out.print("ID (ex. L1, V1): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("ID-ul nu poate fi gol!");
            return;
        }

        System.out.print("Nume: ");
        String nume = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (tipStr.equals("1")) {
            System.out.print("Adresa Livrare: ");
            String adresa = scanner.nextLine().trim();
            Licitator licitator = new Licitator(new Identificator(id), nume, email, adresa);
            uService.adauga(licitator);
            System.out.println("Licitatorul " + nume + " a fost inregistrat cu succes!");
        } else {
            System.out.print("Numar Telefon: ");
            String telefon = scanner.nextLine().trim();
            Vanzator vanzator = new Vanzator(new Identificator(id), nume, email, telefon);
            uService.adauga(vanzator);
            System.out.println("Vanzatorul " + nume + " a fost inregistrat cu succes!");
        }
    }

    private static void stergereUtilizator(UtilizatorService uService, Scanner scanner) {
        System.out.println("\n--- Stergere Utilizator ---");
        System.out.print("ID Utilizator: ");
        String id = scanner.nextLine().trim();
        Utilizator u = uService.cautaDupaId(id);
        if (u == null) {
            System.out.println("Utilizatorul cu ID-ul " + id + " nu a fost gasit!");
            return;
        }
        uService.sterge(id);
        System.out.println("Utilizatorul " + ((Membru) u).getNume() + " a fost sters.");
    }

    private static void adaugareProdus(LicitatieService lService, Scanner scanner) {
        System.out.println("\n--- Adaugare Produs ---");
        System.out.println("Alege categoria: 1. Arta | 2. Electronic");
        System.out.print("Optiune: ");
        String catStr = scanner.nextLine().trim();
        if (!catStr.equals("1") && !catStr.equals("2")) {
            System.out.println("Categorie invalida!");
            return;
        }

        System.out.print("ID Produs (ex. P1): ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("ID-ul nu poate fi gol!");
            return;
        }

        System.out.print("Nume Produs: ");
        String nume = scanner.nextLine().trim();
        System.out.print("Descriere: ");
        String descriere = scanner.nextLine().trim();
        
        System.out.print("Pret pornire (RON): ");
        double pret = 0;
        try {
            pret = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Pret invalid!");
            return;
        }

        if (catStr.equals("1")) {
            System.out.print("Artist: ");
            String artist = scanner.nextLine().trim();
            System.out.print("An Creare: ");
            int an = 0;
            try {
                an = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("An invalid!");
                return;
            }
            ProdusArta arta = new ProdusArta(new Identificator(id), nume, descriere, pret, artist, an);
            lService.adaugaProdus(arta);
            System.out.println("Produsul de arta " + nume + " a fost adaugat cu succes!");
        } else {
            System.out.print("Garantie (luni): ");
            int garantie = 0;
            try {
                garantie = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Garantie invalida!");
                return;
            }
            ProdusElectronic el = new ProdusElectronic(new Identificator(id), nume, descriere, pret, garantie);
            lService.adaugaProdus(el);
            System.out.println("Produsul electronic " + nume + " a fost adaugat cu succes!");
        }
    }

    private static void creareLicitatie(LicitatieService lService, Scanner scanner) {
        System.out.println("\n--- Creare Licitatie ---");
        System.out.print("ID Licitatie (ex. LIC1): ");
        String idLicitatie = scanner.nextLine().trim();
        if (idLicitatie.isEmpty()) {
            System.out.println("ID-ul nu poate fi gol!");
            return;
        }

        System.out.print("ID Produs existent: ");
        String idProdus = scanner.nextLine().trim();
        
        // Cautam produsul
        Produs produs = null;
        List<Produs> produse = lService.ordonareProduseDupaPret();
        for (Produs p : produse) {
            if (p.getId().getValoare().equals(idProdus)) {
                produs = p;
                break;
            }
        }

        if (produs == null) {
            System.out.println("Produsul cu ID-ul " + idProdus + " nu exista in sistem! Adauga-l mai intai.");
            return;
        }

        System.out.print("Numar zile valabilitate licitatie (ex. 2): ");
        int zile = 0;
        try {
            zile = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Valoare invalida!");
            return;
        }

        Licitatie licitatie = new Licitatie(new Identificator(idLicitatie), produs, LocalDateTime.now().plusDays(zile));
        lService.creeazaLicitatie(licitatie);
        System.out.println("Licitatia " + idLicitatie + " pentru produsul " + produs.getNume() + " a fost creata!");
    }

    private static void inchideLicitatie(LicitatieService lService, Scanner scanner) {
        System.out.println("\n--- Inchidere Licitatie ---");
        System.out.print("ID Licitatie de inchis: ");
        String id = scanner.nextLine().trim();
        Licitatie l = lService.cautaLicitatieDupaId(id);
        if (l == null) {
            System.out.println("Licitatia nu exista!");
            return;
        }
        if (!l.isEsteActiva()) {
            System.out.println("Licitatia este deja inchisa!");
            return;
        }
        l.setEsteActiva(false);
        lService.actualizeazaLicitatie(l);
        System.out.println("Licitatia " + id + " a fost declarata INCHISA.");
        
        List<Oferta> oferte = lService.obtineOferteLicitatieCuJoin(id);
        if (!oferte.isEmpty()) {
            // Sortam descrescator
            oferte.sort((o1, o2) -> Double.compare(o2.getSuma(), o1.getSuma()));
            Oferta castigatoare = oferte.get(0);
            System.out.println("Castigatorul licitatiei este " + castigatoare.getLicitator().getNume() + " cu oferta de " + castigatoare.getSuma() + " RON!");
        } else {
            System.out.println("Licitatia s-a incheiat fara nicio oferta.");
        }
    }

    private static void plaseazaOferta(LicitatieService lService, UtilizatorService uService, Scanner scanner) {
        System.out.println("\n--- Plasare Oferta ---");
        System.out.print("ID Licitatie (ex. LIC1): ");
        String idLicitatie = scanner.nextLine().trim();
        Licitatie licitatie = lService.cautaLicitatieDupaId(idLicitatie);
        if (licitatie == null) {
            System.out.println("Licitatia " + idLicitatie + " nu exista!");
            return;
        }

        if (!licitatie.isEsteActiva()) {
            System.out.println("Licitatia " + idLicitatie + " este inchisa!");
            return;
        }

        System.out.print("ID Licitator existent (ex. L1): ");
        String idLicitator = scanner.nextLine().trim();
        Utilizator u = uService.cautaDupaId(idLicitator);
        if (u == null) {
            System.out.println("Utilizatorul nu exista!");
            return;
        }
        if (!(u instanceof Licitator)) {
            System.out.println("Doar utilizatorii de tip Licitator pot plasa oferte!");
            return;
        }

        System.out.print("Suma oferita (RON): ");
        double suma = 0;
        try {
            suma = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Suma invalida!");
            return;
        }

        try {
            lService.plaseazaOferta(idLicitatie, (Licitator) u, suma);
            System.out.println("Oferta de " + suma + " RON a fost plasata cu succes de " + ((Membru) u).getNume() + "!");
        } catch (SumaPreaMicaException e) {
            System.out.println("Eroare la plasare oferta: " + e.getMessage());
        }
    }

    private static void filtrareLicitatii(LicitatieService lService, Scanner scanner) {
        System.out.println("\n--- Filtrare Licitatii dupa Categorie ---");
        System.out.print("Introdu categoria (Arta / Electronic): ");
        String categorie = scanner.nextLine().trim();
        List<Licitatie> rezultate = lService.filtrareDupaCategorie(categorie);
        if (rezultate.isEmpty()) {
            System.out.println("Nu s-au gasit licitatii pentru categoria '" + categorie + "'.");
        } else {
            System.out.println("Licitatii in categoria '" + categorie + "':");
            rezultate.forEach(l -> {
                System.out.println(" - ID: " + l.getId().getValoare() + " | Produs: " + l.getProdus().getNume() + " | Pret pornire: " + l.getProdus().getPretPornire() + " RON | Activa: " + (l.isEsteActiva() ? "DA" : "NU"));
            });
        }
    }

    private static void listeazaLicitatii(LicitatieService lService) {
        System.out.println("\n--- Toate Licitatiile din Sistem ---");
        List<Licitatie> licitatii = lService.listeazaToateLicitatiile();
        if (licitatii.isEmpty()) {
            System.out.println("Nu exista licitatii inregistrate.");
            return;
        }

        for (Licitatie l : licitatii) {
            System.out.println("ID Licitatie: " + l.getId().getValoare());
            System.out.println("  Produs: " + l.getProdus().getNume() + " (" + l.getProdus().getCategorie() + ")");
            System.out.println("  Pret pornire: " + l.getProdus().getPretPornire() + " RON");
            System.out.println("  Status: " + (l.isEsteActiva() ? "ACTIVA" : "INCHISA"));
            System.out.println("  Data incheiere: " + l.getDataIncheiere());
            
            List<Oferta> oferte = lService.obtineOferteLicitatieCuJoin(l.getId().getValoare());
            if (oferte.isEmpty()) {
                System.out.println("  Oferte: Fara oferte depuse.");
            } else {
                System.out.println("  Oferte (sortate descrescator dupa suma):");
                oferte.sort((o1, o2) -> Double.compare(o2.getSuma(), o1.getSuma()));
                for (Oferta o : oferte) {
                    System.out.println("    > " + o.getLicitator().getNume() + ": " + o.getSuma() + " RON la data " + o.getMomentTimp());
                }
                Oferta castigatoare = oferte.get(0);
                System.out.println("  [Lider actual / Castigator: " + castigatoare.getLicitator().getNume() + " - " + castigatoare.getSuma() + " RON]");
            }
            System.out.println();
        }
    }

    private static void vizualizareIstoric(LicitatieService lService) {
        System.out.println("\n--- Istoric Oferte per Licitator ---");
        Map<String, List<Oferta>> istoric = lService.obtineIstoricOfertePerLicitator();
        if (istoric.isEmpty()) {
            System.out.println("Nu exista oferte in sistem.");
            return;
        }
        istoric.forEach((nume, lista) -> {
            System.out.println("Utilizator: " + nume);
            lista.forEach(o -> System.out.println("  > " + o.getSuma() + " RON la momentul " + o.getMomentTimp()));
        });
    }

    private static void stergereLicitatie(LicitatieService lService, Scanner scanner) {
        System.out.println("\n--- Stergere Licitatie ---");
        System.out.print("ID Licitatie de sters (doar daca nu are oferte depuse): ");
        String id = scanner.nextLine().trim();
        Licitatie l = lService.cautaLicitatieDupaId(id);
        if (l == null) {
            System.out.println("Licitatia cu ID-ul " + id + " nu exista!");
            return;
        }

        List<Oferta> oferte = lService.obtineOferteLicitatieCuJoin(id);
        if (!oferte.isEmpty()) {
            System.out.println("Nu se poate sterge o licitatie care are deja oferte depuse!");
            return;
        }

        lService.stergeLicitatie(id);
        System.out.println("Licitatia " + id + " a fost stearsa.");
    }

    private static void ordonareProduse(LicitatieService lService) {
        System.out.println("\n--- Produse in Sistem (Ordonate dupa pret de pornire) ---");
        List<Produs> produse = lService.ordonareProduseDupaPret();
        if (produse.isEmpty()) {
            System.out.println("Nu exista produse inregistrate.");
            return;
        }
        produse.forEach(p -> {
            System.out.println(" - " + p.getNume() + " | ID: " + p.getId().getValoare() + " | Categorie: " + p.getCategorie() + " | Pret pornire: " + p.getPretPornire() + " RON");
        });
    }

    private static void listeazaUtilizatori(UtilizatorService uService) {
        System.out.println("\n--- Utilizatori Inregistrati ---");
        List<Utilizator> utilizatori = uService.listeazaToti();
        if (utilizatori.isEmpty()) {
            System.out.println("Nu exista utilizatori inregistrati.");
            return;
        }
        utilizatori.forEach(u -> {
            System.out.print(" - ID: " + u.getId().getValoare() + " | Tip: " + u.getTipUtilizator() + " | Nume: " + ((Membru) u).getNume() + " | Email: " + ((Membru) u).getEmail());
            if (u instanceof Licitator) {
                System.out.println(" | Adresa: " + ((Licitator) u).getAdresaLivrare());
            } else if (u instanceof Vanzator) {
                System.out.println(" | Telefon: " + ((Vanzator) u).getNumarTelefon());
            }
        });
    }

    private static void initializeazaBazaDeDate(boolean fortaReset) {
        try {
            Connection conexiune = com.pao.proiect.licitatii.util.DatabaseConnection.getInstanta().getConexiune();
            if (fortaReset) {
                try (Statement stmt = conexiune.createStatement()) {
                    stmt.execute("DROP TABLE IF EXISTS oferte");
                    stmt.execute("DROP TABLE IF EXISTS licitatii");
                    stmt.execute("DROP TABLE IF EXISTS produse");
                    stmt.execute("DROP TABLE IF EXISTS utilizatori");
                }
            }
            File fisierSql = gasesteFisier("schema.sql");
            String continutSql = "";
            try (BufferedReader cititor = new BufferedReader(new FileReader(fisierSql))) {
                StringBuilder sb = new StringBuilder();
                String linie;
                while ((linie = cititor.readLine()) != null) {
                    sb.append(linie).append("\n");
                }
                continutSql = sb.toString();
            }
            String[] instructiuni = continutSql.split(";");
            try (Statement stmt = conexiune.createStatement()) {
                for (String sql : instructiuni) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql.trim());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File gasesteFisier(String numeFisier) {
        File dir = new File(".").getAbsoluteFile();
        while (dir != null) {
            File f = new File(dir, numeFisier);
            if (f.exists()) {
                return f;
            }
            File fRes = new File(dir, "resources/" + numeFisier);
            if (fRes.exists()) {
                return fRes;
            }
            dir = dir.getParentFile();
        }
        return new File(numeFisier);
    }
}
