package com.pao.proiect.licitatii.model;

import java.util.Objects;


public abstract class Produs implements Comparable<Produs> {
    private Identificator id;
    private String nume;
    private String descriere;
    private double pretPornire;

    public Produs(Identificator id, String nume, String descriere, double pretPornire) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
        this.pretPornire = pretPornire;
    }

    public Identificator getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public double getPretPornire() {
        return pretPornire;
    }

    public abstract String getCategorie();

    @Override
    public int compareTo(Produs o) {
        return Double.compare(this.pretPornire, o.pretPornire);
    }

    @Override
    public String toString() {
        return "Produs{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", pretPornire=" + pretPornire +
                ", categorie='" + getCategorie() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produs produs = (Produs) o;
        return Objects.equals(id, produs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

