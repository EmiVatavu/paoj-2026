package com.pao.proiect.licitatii.model;


public class ProdusArta extends Produs {
    private String artist;
    private int anCreare;

    public ProdusArta(Identificator id, String nume, String descriere, double pretPornire, String artist, int anCreare) {
        super(id, nume, descriere, pretPornire);
        this.artist = artist;
        this.anCreare = anCreare;
    }

    public String getArtist() {
        return artist;
    }

    public int getAnCreare() {
        return anCreare;
    }

    @Override
    public String getCategorie() {
        return "Arta";
    }

    @Override
    public String toString() {
        return "ProdusArta{" +
                "nume='" + getNume() + '\'' +
                ", artist='" + artist + '\'' +
                ", an=" + anCreare +
                ", pret=" + getPretPornire() +
                '}';
    }
}

