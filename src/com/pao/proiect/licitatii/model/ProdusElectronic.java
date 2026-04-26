package com.pao.proiect.licitatii.model;


public class ProdusElectronic extends Produs {
    private int garantieLuni;

    public ProdusElectronic(Identificator id, String nume, String descriere, double pretPornire, int garantieLuni) {
        super(id, nume, descriere, pretPornire);
        this.garantieLuni = garantieLuni;
    }

    public int getGarantieLuni() {
        return garantieLuni;
    }

    @Override
    public String getCategorie() {
        return "Electronic";
    }

    @Override
    public String toString() {
        return "ProdusElectronic{" +
                "nume='" + getNume() + '\'' +
                ", garantie=" + garantieLuni + " luni" +
                ", pret=" + getPretPornire() +
                '}';
    }
}

