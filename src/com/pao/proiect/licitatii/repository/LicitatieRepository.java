package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.model.Identificator;
import com.pao.proiect.licitatii.model.Licitatie;
import com.pao.proiect.licitatii.model.Oferta;
import com.pao.proiect.licitatii.model.Produs;
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

public class LicitatieRepository implements Repository<Licitatie, String> {
    private final Connection conexiune;
    private final ProdusRepository produsRepository;
    private final OfertaRepository ofertaRepository;

    public LicitatieRepository() {
        this.conexiune = DatabaseConnection.getInstanta().getConexiune();
        this.produsRepository = new ProdusRepository();
        this.ofertaRepository = new OfertaRepository();
    }

    @Override
    public void salveaza(Licitatie licitatie) {
        if (!produsRepository.cautaDupaId(licitatie.getProdus().getId().getValoare()).isPresent()) {
            produsRepository.salveaza(licitatie.getProdus());
        }
        String interogare = "INSERT INTO licitatii (id, produs_id, data_incheiere, este_activa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, licitatie.getId().getValoare());
            stmt.setString(2, licitatie.getProdus().getId().getValoare());
            stmt.setTimestamp(3, Timestamp.valueOf(licitatie.getDataIncheiere()));
            stmt.setBoolean(4, licitatie.isEsteActiva());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Licitatie> cautaDupaId(String id) {
        String interogare = "SELECT * FROM licitatii WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construiesteLicitatie(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Licitatie> gasesteToate() {
        List<Licitatie> lista = new ArrayList<>();
        String interogare = "SELECT * FROM licitatii";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(construiesteLicitatie(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public void actualizeaza(Licitatie licitatie) {
        String interogare = "UPDATE licitatii SET produs_id = ?, data_incheiere = ?, este_activa = ? WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, licitatie.getProdus().getId().getValoare());
            stmt.setTimestamp(2, Timestamp.valueOf(licitatie.getDataIncheiere()));
            stmt.setBoolean(3, licitatie.isEsteActiva());
            stmt.setString(4, licitatie.getId().getValoare());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sterge(String id) {
        String interogare = "DELETE FROM licitatii WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Licitatie construiesteLicitatie(ResultSet rs) throws SQLException {
        String idValoare = rs.getString("id");
        String produsId = rs.getString("produs_id");
        Timestamp dataIncheiereTS = rs.getTimestamp("data_incheiere");
        boolean esteActiva = rs.getBoolean("este_activa");

        Produs produs = produsRepository.cautaDupaId(produsId)
                .orElseThrow(() -> new RuntimeException("Produsul nu exista"));

        Licitatie licitatie = new Licitatie(new Identificator(idValoare), produs, dataIncheiereTS.toLocalDateTime());
        licitatie.setEsteActiva(esteActiva);

        List<Oferta> oferte = ofertaRepository.gasesteDupaLicitatie(idValoare);
        for (Oferta oferta : oferte) {
            licitatie.adaugaOferta(oferta);
        }
        return licitatie;
    }
}
