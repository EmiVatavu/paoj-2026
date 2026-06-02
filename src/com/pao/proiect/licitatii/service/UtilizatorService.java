package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.Utilizator;
import com.pao.proiect.licitatii.repository.UtilizatorRepository;

import java.util.List;

public class UtilizatorService {
    private static UtilizatorService instance;
    private final UtilizatorRepository utilizatorRepository;
    private final AuditService auditService;

    private UtilizatorService() {
        this.utilizatorRepository = new UtilizatorRepository();
        this.auditService = AuditService.getInstanta();
    }

    public static synchronized UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public void adauga(Utilizator u) {
        utilizatorRepository.salveaza(u);
        auditService.scrieActiune("inregistrare_utilizator");
    }

    public void sterge(String id) {
        utilizatorRepository.sterge(id);
        auditService.scrieActiune("stergere_utilizator");
    }

    public Utilizator cautaDupaId(String id) {
        auditService.scrieActiune("cautare_utilizator_id");
        return utilizatorRepository.cautaDupaId(id).orElse(null);
    }

    public Utilizator cautaDupaEmail(String email) {
        auditService.scrieActiune("cautare_utilizator_email");
        return utilizatorRepository.cautaDupaEmail(email).orElse(null);
    }

    public List<Utilizator> listeazaToti() {
        auditService.scrieActiune("listare_utilizatori");
        return utilizatorRepository.gasesteToate();
    }
}
