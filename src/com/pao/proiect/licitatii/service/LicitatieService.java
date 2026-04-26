package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.exception.SumaPreaMicaException;
import com.pao.proiect.licitatii.model.*;
import java.util.*;
import java.util.stream.Collectors;


public class LicitatieService {
    private static LicitatieService instance;
    private List<Licitatie> licitatii;
    private List<Produs> produseInSistem;

    private LicitatieService() {
        licitatii = new ArrayList<>();
        produseInSistem = new ArrayList<>();
    }

    public static LicitatieService getInstance() {
        if (instance == null) {
            instance = new LicitatieService();
        }
        return instance;
    }

    public void adaugaProdus(Produs p) {
        produseInSistem.add(p);
    }

    public void creeazaLicitatie(Licitatie l) {
        licitatii.add(l);
    }

    public void plaseazaOferta(String idLicitatie, Licitator licitator, double suma) throws SumaPreaMicaException {
        Licitatie l = cautaLicitatieDupaId(idLicitatie);
        if (l == null) return;
        if (!l.isEsteActiva()) throw new RuntimeException("Licitatia este inchisa!");

        double pretCurent = l.getOfertaCastigatoare() != null ? 
                             l.getOfertaCastigatoare().getSuma() : 
                             l.getProdus().getPretPornire();

        if (suma <= pretCurent) {
            throw new SumaPreaMicaException("Suma de " + suma + " este prea mica! Pretul actual este " + pretCurent);
        }

        l.adaugaOferta(new Oferta(licitator, suma));
    }

    public List<Licitatie> filtrareDupaCategorie(String categorie) {
        return licitatii.stream()
                .filter(l -> l.getProdus().getCategorie().equalsIgnoreCase(categorie))
                .collect(Collectors.toList());
    }

    public List<Produs> ordonareProduseDupaPret() {
        List<Produs> sorteate = new ArrayList<>(produseInSistem);
        Collections.sort(sorteate);
        return sorteate;
    }

    public Licitatie cautaLicitatieDupaId(String id) {
        return licitatii.stream()
                .filter(l -> l.getId().getValoare().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void stergeLicitatie(String id) {
        licitatii.removeIf(l -> l.getId().getValoare().equals(id) && l.getOferte().isEmpty());
    }

    public List<Licitatie> listeazaToateLicitatiile() {
        return new ArrayList<>(licitatii);
    }

    public Map<String, List<Oferta>> obtineIstoricOfertePerLicitator() {
        Map<String, List<Oferta>> istoric = new HashMap<>();
        for (Licitatie l : licitatii) {
            for (Oferta o : l.getOferte()) {
                String nume = o.getLicitator().getNume();
                istoric.computeIfAbsent(nume, k -> new ArrayList<>()).add(o);
            }
        }
        return istoric;
    }
}

