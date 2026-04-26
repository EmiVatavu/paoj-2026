package com.pao.proiect.licitatii.model;

import java.util.Objects;


public abstract class Membru extends Utilizator {
    private String nume;
    private String email;

    public Membru(Identificator id, String nume, String email) {
        super(id);
        this.nume = nume;
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Membru{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", tip='" + getTipUtilizator() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membru membru = (Membru) o;
        return Objects.equals(id, membru.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

