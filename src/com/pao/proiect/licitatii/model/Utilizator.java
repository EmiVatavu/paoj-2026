package com.pao.proiect.licitatii.model;


public abstract class Utilizator {
    protected Identificator id;

    public Utilizator(Identificator id) {
        this.id = id;
    }

    public Identificator getId() {
        return id;
    }

    public void setId(Identificator id) {
        this.id = id;
    }

    public abstract String getTipUtilizator();

    @Override
    public String toString() {
        return "Utilizator{" +
                "id=" + id +
                ", tip='" + getTipUtilizator() + '\'' +
                '}';
    }
}

