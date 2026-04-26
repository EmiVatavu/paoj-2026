package com.pao.proiect.licitatii.model;


public class Vanzator extends Membru {
    private String numarTelefon;

    public Vanzator(Identificator id, String nume, String email, String numarTelefon) {
        super(id, nume, email);
        this.numarTelefon = numarTelefon;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    @Override
    public String getTipUtilizator() {
        return "Vanzator";
    }

    @Override
    public String toString() {
        return "Vanzator{" +
                "nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", numarTelefon='" + numarTelefon + '\'' +
                '}';
    }
}

