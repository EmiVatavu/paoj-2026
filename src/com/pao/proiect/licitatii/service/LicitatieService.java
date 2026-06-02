package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.exception.SumaPreaMicaException;
import com.pao.proiect.licitatii.model.Licitatie;
import com.pao.proiect.licitatii.model.Licitator;
import com.pao.proiect.licitatii.model.Oferta;
import com.pao.proiect.licitatii.model.Produs;
import com.pao.proiect.licitatii.repository.LicitatieRepository;
import com.pao.proiect.licitatii.repository.OfertaRepository;
import com.pao.proiect.licitatii.repository.ProdusRepository;
import com.pao.proiect.licitatii.repository.UtilizatorRepository;
import com.pao.proiect.licitatii.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicitatieService {
    private static LicitatieService instance;
    private final LicitatieRepository licitatieRepository;
    private final ProdusRepository produsRepository;
    private final OfertaRepository ofertaRepository;
    private final AuditService auditService;
    private final Connection conexiune;

    private LicitatieService() {
        this.licitatieRepository = new LicitatieRepository();
        this.produsRepository = new ProdusRepository();
        this.ofertaRepository = new OfertaRepository();
        this.auditService = AuditService.getInstanta();
        this.conexiune = DatabaseConnection.getInstanta().getConexiune();
    }

    public static synchronized LicitatieService getInstance() {
        if (instance == null) {
            instance = new LicitatieService();
        }
        return instance;
    }

    public void adaugaProdus(Produs p) {
        produsRepository.salveaza(p);
        auditService.scrieActiune("adaugare_produs");
    }

    public void creeazaLicitatie(Licitatie l) {
        licitatieRepository.salveaza(l);
        auditService.scrieActiune("creare_licitatie");
    }

    public void plaseazaOferta(String idLicitatie, Licitator licitator, double suma) throws SumaPreaMicaException {
        try {
            conexiune.setAutoCommit(false);
            String sqlLicitatie = "SELECT l.este_activa, p.pret_pornire, " +
                    "(SELECT MAX(o.suma) FROM oferte o WHERE o.licitatie_id = l.id) AS max_suma " +
                    "FROM licitatii l " +
                    "JOIN produse p ON l.produs_id = p.id " +
                    "WHERE l.id = ? FOR UPDATE";

            boolean esteActiva = false;
            double pretPornire = 0;
            Double maxSuma = null;
            boolean gasit = false;

            try (PreparedStatement stmt = conexiune.prepareStatement(sqlLicitatie)) {
                stmt.setString(1, idLicitatie);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        gasit = true;
                        esteActiva = rs.getBoolean("este_activa");
                        pretPornire = rs.getDouble("pret_pornire");
                        double tempMax = rs.getDouble("max_suma");
                        if (!rs.wasNull()) {
                            maxSuma = tempMax;
                        }
                    }
                }
            }

            if (!gasit) {
                conexiune.rollback();
                return;
            }

            if (!esteActiva) {
                conexiune.rollback();
                throw new RuntimeException("Licitatia este inchisa!");
            }

            double pretCurent = (maxSuma != null) ? maxSuma : pretPornire;
            if (suma <= pretCurent) {
                conexiune.rollback();
                throw new SumaPreaMicaException("Suma de " + suma + " este prea mica! Pretul actual este " + pretCurent);
            }

            Oferta oferta = new Oferta(licitator, suma);
            ofertaRepository.salveazaOferta(oferta, idLicitatie);

            conexiune.commit();
            auditService.scrieActiune("plasare_oferta");
        } catch (SQLException e) {
            try {
                conexiune.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conexiune.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    public List<Licitatie> filtrareDupaCategorie(String categorie) {
        auditService.scrieActiune("filtrare_licitatii_categorie");
        List<Licitatie> rez = new ArrayList<>();
        String sql = "SELECT l.id FROM licitatii l JOIN produse p ON l.produs_id = p.id WHERE p.categorie = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setString(1, categorie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String licId = rs.getString("id");
                    licitatieRepository.cautaDupaId(licId).ifPresent(rez::add);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rez;
    }

    public List<Produs> ordonareProduseDupaPret() {
        auditService.scrieActiune("ordonare_produse_pret");
        List<Produs> lista = produsRepository.gasesteToate();
        Collections.sort(lista);
        return lista;
    }

    public Licitatie cautaLicitatieDupaId(String id) {
        auditService.scrieActiune("cautare_licitatie_id");
        return licitatieRepository.cautaDupaId(id).orElse(null);
    }

    public void stergeLicitatie(String id) {
        auditService.scrieActiune("stergere_licitatie");
        String sql = "SELECT COUNT(*) AS nr_oferte FROM oferte WHERE licitatie_id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("nr_oferte") == 0) {
                    licitatieRepository.sterge(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Licitatie> listeazaToateLicitatiile() {
        auditService.scrieActiune("listare_licitatii");
        return licitatieRepository.gasesteToate();
    }

    public List<Oferta> obtineOferteLicitatieCuJoin(String idLicitatie) {
        auditService.scrieActiune("listare_oferte_licitatie_join");
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT o.* FROM oferte o JOIN utilizatori u ON o.licitator_id = u.id WHERE o.licitatie_id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql)) {
            stmt.setString(1, idLicitatie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    double suma = rs.getDouble("suma");
                    Timestamp moment = rs.getTimestamp("moment_timp");
                    String licitatorId = rs.getString("licitator_id");
                    Licitator licitator = (Licitator) new UtilizatorRepository().cautaDupaId(licitatorId)
                            .orElseThrow(() -> new RuntimeException("Licitatorul nu exista"));
                    Oferta oferta = new Oferta(licitator, suma);
                    try {
                        java.lang.reflect.Field f = Oferta.class.getDeclaredField("momentTimp");
                        f.setAccessible(true);
                        f.set(oferta, moment.toLocalDateTime());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    lista.add(oferta);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Map<String, List<Oferta>> obtineIstoricOfertePerLicitator() {
        auditService.scrieActiune("istoric_oferte_licitator");
        Map<String, List<Oferta>> istoric = new HashMap<>();
        String sql = "SELECT o.*, u.nume FROM oferte o " +
                "JOIN utilizatori u ON o.licitator_id = u.id " +
                "JOIN licitatii l ON o.licitatie_id = l.id " +
                "JOIN produse p ON l.produs_id = p.id";
        try (PreparedStatement stmt = conexiune.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nume = rs.getString("nume");
                double suma = rs.getDouble("suma");
                Timestamp moment = rs.getTimestamp("moment_timp");
                String licitatorId = rs.getString("licitator_id");
                Licitator licitator = (Licitator) new UtilizatorRepository().cautaDupaId(licitatorId)
                        .orElseThrow(() -> new RuntimeException("Licitatorul nu exista"));
                Oferta oferta = new Oferta(licitator, suma);
                try {
                    java.lang.reflect.Field f = Oferta.class.getDeclaredField("momentTimp");
                    f.setAccessible(true);
                    f.set(oferta, moment.toLocalDateTime());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                istoric.computeIfAbsent(nume, k -> new ArrayList<>()).add(oferta);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return istoric;
    }

    public void actualizeazaLicitatie(Licitatie l) {
        licitatieRepository.actualizeaza(l);
    }
}
