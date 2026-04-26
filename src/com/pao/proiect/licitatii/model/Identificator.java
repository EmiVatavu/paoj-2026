package com.pao.proiect.licitatii.model;

import java.util.Objects;


public final class Identificator {
    private final String valoare;

    public Identificator(String valoare) {
        this.valoare = valoare;
    }

    public String getValoare() {
        return valoare;
    }

    @Override
    public String toString() {
        return valoare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identificator that = (Identificator) o;
        return Objects.equals(valoare, that.valoare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare);
    }
}

