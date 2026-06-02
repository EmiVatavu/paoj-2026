package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.model.Licitator;
import com.pao.proiect.licitatii.model.Oferta;
import com.pao.proiect.licitatii.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfertaRepository implements Repository<Oferta, Integer> {
    private final Connection conexiune;
    private final UtilizatorRepository utilizatorRepository;

    public OfertaRepository() {
        this.conexiune = DatabaseConnection.getInstanta().getConexiune();
        this.utilizatorRepository = new UtilizatorRepository();
    }

    @Override
    public void salveaza(Oferta entitate) {
        throw new UnsupportedOperationException("Folositi salveazaOferta cu idLicitatie");
    }

    public void salveazaOferta(Oferta oferta, String idLicitatie) {
        String interogare = "INSERT INTO oferte (licitatie_id, licitator_id, suma, moment_timp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, idLicitatie);
            stmt.setString(2, oferta.getLicitator().getId().getValoare());
            stmt.setDouble(3, oferta.getSuma());
            stmt.setTimestamp(4, Timestamp.valueOf(oferta.getMomentTimp()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Oferta> cautaDupaId(Integer id) {
        String interogare = "SELECT * FROM oferte WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construiesteOferta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Oferta> gasesteToate() {
        List<Oferta> lista = new ArrayList<>();
        String interogare = "SELECT * FROM oferte";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(construiesteOferta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<Oferta> gasesteDupaLicitatie(String idLicitatie) {
        List<Oferta> lista = new ArrayList<>();
        String interogare = "SELECT * FROM oferte WHERE licitatie_id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, idLicitatie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(construiesteOferta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public void actualizeaza(Oferta entitate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sterge(Integer id) {
        String interogare = "DELETE FROM oferte WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Oferta construiesteOferta(ResultSet rs) throws SQLException {
        String licitatorId = rs.getString("licitator_id");
        double suma = rs.getDouble("suma");
        Timestamp moment = rs.getTimestamp("moment_timp");
        LocalDateTime momentTimp = moment.toLocalDateTime();

        Licitator licitator = (Licitator) utilizatorRepository.cautaDupaId(licitatorId)
                .orElseThrow(() -> new RuntimeException("Licitatorul nu exista"));

        Oferta oferta = new Oferta(licitator, suma);
        setMomentTimp(oferta, momentTimp);
        return oferta;
    }

    private void setMomentTimp(Oferta oferta, LocalDateTime momentTimp) {
        try {
            java.lang.reflect.Field f = Oferta.class.getDeclaredField("momentTimp");
            f.setAccessible(true);
            f.set(oferta, momentTimp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
