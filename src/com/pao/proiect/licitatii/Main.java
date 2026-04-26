package com.pao.proiect.licitatii;

import com.pao.proiect.licitatii.exception.SumaPreaMicaException;
import com.pao.proiect.licitatii.model.*;
import com.pao.proiect.licitatii.service.LicitatieService;
import com.pao.proiect.licitatii.service.UtilizatorService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        UtilizatorService uService = UtilizatorService.getInstance();
        LicitatieService lService = LicitatieService.getInstance();

        System.out.println("--- DEMONSTRATIE SISTEM LICITATII (ETAPA 1) ---\n");


        System.out.println("Actiunea 1: Inregistrare utilizatori...");
        Vanzator v1 = new Vanzator(new Identificator("V1"), "Ion Popescu", "ion@yahoo.com", "0722111222");
        Licitator l1 = new Licitator(new Identificator("L1"), "Andrei Vasile", "andrei@gmail.com", "Strada Florilor 10");
        Licitator l2 = new Licitator(new Identificator("L2"), "Maria Ionescu", "maria@gmail.com", "Bulevardul Unirii 5");
        uService.adauga(v1);
        uService.adauga(l1);
        uService.adauga(l2);
        System.out.println("Utilizatori inregistrati: " + uService.listeazaToti().size());


        System.out.println("\nActiunea 2: Adaugare produse de catre vanzator...");
        Produs p1 = new ProdusArta(new Identificator("P1"), "Tablou Rasarit", "Ulei pe panza", 500.0, "Nicolae Grigorescu", 1890);
        Produs p2 = new ProdusElectronic(new Identificator("P2"), "Laptop Gaming", "RTX 4060, 16GB RAM", 3500.0, 24);
        Produs p3 = new ProdusArta(new Identificator("P3"), "Sculptura Pasarea", "Bronz", 1000.0, "Constantin Brancusi", 1910);
        lService.adaugaProdus(p1);
        lService.adaugaProdus(p2);
        lService.adaugaProdus(p3);


        System.out.println("\nActiunea 3: Creare licitatii pentru produse...");
        lService.creeazaLicitatie(new Licitatie(new Identificator("LIC1"), p1, LocalDateTime.now().plusDays(2)));
        lService.creeazaLicitatie(new Licitatie(new Identificator("LIC2"), p2, LocalDateTime.now().plusHours(5)));
        System.out.println("Licitatii active: " + lService.listeazaToateLicitatiile().size());


        System.out.println("\nActiunea 4: Plasare oferte...");
        try {
            lService.plaseazaOferta("LIC1", l1, 550.0);
            lService.plaseazaOferta("LIC1", l2, 600.0);
            lService.plaseazaOferta("LIC1", l1, 700.0);
            System.out.println("Oferte plasate cu succes pentru LIC1.");
            
            lService.plaseazaOferta("LIC1", l2, 650.0);
        } catch (SumaPreaMicaException e) {
            System.out.println("Eroare asteptata: " + e.getMessage());
        }


        System.out.println("\nActiunea 5: Filtrare licitatii CATEGORIE 'Arta'...");
        List<Licitatie> arta = lService.filtrareDupaCategorie("Arta");
        arta.forEach(l -> System.out.println(" - " + l.getProdus().getNume()));


        System.out.println("\nActiunea 6: Listare oferte pentru LIC1 (sortate descrescator)...");
        Licitatie lic1 = lService.cautaLicitatieDupaId("LIC1");
        lic1.getOferte().forEach(o -> System.out.println(" > " + o));


        System.out.println("\nActiunea 7: Finalizare licitatie LIC1...");
        lic1.setEsteActiva(false);
        if (castigatoare != null) {
            System.out.println("Castigator: " + castigatoare.getLicitator().getNume() + " cu suma " + castigatoare.getSuma());
        }


        System.out.println("\nActiunea 8: Istoric oferte per utilizator (folosind Map)...");
        Map<String, List<Oferta>> istoric = lService.obtineIstoricOfertePerLicitator();
        istoric.forEach((nume, lista) -> {
            System.out.println("Utilizator: " + nume + " a plasat " + lista.size() + " oferte.");
        });


        System.out.println("\nActiunea 9: Incercare stergere licitatie LIC2...");
        System.out.println("Licitatii inainte: " + lService.listeazaToateLicitatiile().size());
        lService.stergeLicitatie("LIC2");


        System.out.println("\nActiunea 10: Produse ordonate dupa pret pornire (folosind Comparable)...");
        lService.ordonareProduseDupaPret().forEach(p -> 
            System.out.println(" - " + p.getNume() + ": " + p.getPretPornire() + " RON"));

        System.out.println("\n--- DEMONSTRATIE FINALIZATA ---");
    }
}

