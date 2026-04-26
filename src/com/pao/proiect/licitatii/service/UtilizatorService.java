package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.Utilizator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UtilizatorService {
    private static UtilizatorService instance;
    private List<Utilizator> utilizatori;
    private Map<String, Utilizator> indexDupaEmail;

    private UtilizatorService() {
        utilizatori = new ArrayList<>();
        indexDupaEmail = new HashMap<>();
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public void adauga(Utilizator u) {
        utilizatori.add(u);
        if (u instanceof com.pao.proiect.licitatii.model.Membru) {
            indexDupaEmail.put(((com.pao.proiect.licitatii.model.Membru) u).getEmail(), u);
        }
    }

    public void sterge(String id) {
        utilizatori.removeIf(u -> u.getId().getValoare().equals(id));
    }

    public Utilizator cautaDupaId(String id) {
        return utilizatori.stream()
                .filter(u -> u.getId().getValoare().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Utilizator cautaDupaEmail(String email) {
        return indexDupaEmail.get(email);
    }

    public List<Utilizator> listeazaToti() {
        return new ArrayList<>(utilizatori);
    }
}

