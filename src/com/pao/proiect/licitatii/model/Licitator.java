package com.pao.proiect.licitatii.model;


public class Licitator extends Membru {
    private String adresaLivrare;

    public Licitator(Identificator id, String nume, String email, String adresaLivrare) {
        super(id, nume, email);
        this.adresaLivrare = adresaLivrare;
    }

    public String getAdresaLivrare() {
        return adresaLivrare;
    }

    public void setAdresaLivrare(String adresaLivrare) {
        this.adresaLivrare = adresaLivrare;
    }

    @Override
    public String getTipUtilizator() {
        return "Licitator";
    }

    @Override
    public String toString() {
        return "Licitator{" +
                "nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", adresaLivrare='" + adresaLivrare + '\'' +
                '}';
    }
}

