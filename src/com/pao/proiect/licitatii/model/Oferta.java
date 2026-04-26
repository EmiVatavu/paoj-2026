package com.pao.proiect.licitatii.model;

import java.time.LocalDateTime;


public class Oferta implements Comparable<Oferta> {
    private Licitator licitator;
    private double suma;
    private LocalDateTime momentTimp;

    public Oferta(Licitator licitator, double suma) {
        this.licitator = licitator;
        this.suma = suma;
        this.momentTimp = LocalDateTime.now();
    }

    public Licitator getLicitator() {
        return licitator;
    }

    public double getSuma() {
        return suma;
    }

    public LocalDateTime getMomentTimp() {
        return momentTimp;
    }

    @Override
    public int compareTo(Oferta o) {

        return Double.compare(o.suma, this.suma);
    }

    @Override
    public String toString() {
        return String.format("Oferta de %.2f RON de la %s la data %s", 
                suma, licitator.getNume(), momentTimp.toString());
    }
}

