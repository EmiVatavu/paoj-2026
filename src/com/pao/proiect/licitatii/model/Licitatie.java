package com.pao.proiect.licitatii.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Licitatie {
    private Identificator id;
    private Produs produs;
    private List<Oferta> oferte;
    private LocalDateTime dataIncheiere;
    private boolean esteActiva;

    public Licitatie(Identificator id, Produs produs, LocalDateTime dataIncheiere) {
        this.id = id;
        this.produs = produs;
        this.dataIncheiere = dataIncheiere;
        this.oferte = new ArrayList<>();
        this.esteActiva = true;
    }

    public Identificator getId() {
        return id;
    }

    public Produs getProdus() {
        return produs;
    }

    public List<Oferta> getOferte() {
        return Collections.unmodifiableList(oferte);
    }

    public void adaugaOferta(Oferta oferta) {
        this.oferte.add(oferta);

        Collections.sort(this.oferte);
    }

    public boolean isEsteActiva() {
        return esteActiva;
    }

    public void setEsteActiva(boolean esteActiva) {
        this.esteActiva = esteActiva;
    }

    public LocalDateTime getDataIncheiere() {
        return dataIncheiere;
    }

    public Oferta getOfertaCastigatoare() {
        if (oferte.isEmpty()) return null;
        return oferte.get(0); 
    }

    @Override
    public String toString() {
        return "Licitatie{" +
                "id=" + id +
                ", produs=" + produs.getNume() +
                ", nrOferte=" + oferte.size() +
                ", status=" + (esteActiva ? "Activa" : "Inchisa") +
                '}';
    }
}

