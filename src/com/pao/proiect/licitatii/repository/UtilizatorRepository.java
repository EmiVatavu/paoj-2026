package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.model.Identificator;
import com.pao.proiect.licitatii.model.Licitator;
import com.pao.proiect.licitatii.model.Utilizator;
import com.pao.proiect.licitatii.model.Vanzator;
import com.pao.proiect.licitatii.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorRepository implements Repository<Utilizator, String> {
    private final Connection conexiune;

    public UtilizatorRepository() {
        this.conexiune = DatabaseConnection.getInstanta().getConexiune();
    }

    @Override
    public void salveaza(Utilizator utilizator) {
        String interogare = "INSERT INTO utilizatori (id, tip, nume, email, numar_telefon, adresa_livrare) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, utilizator.getId().getValoare());
            stmt.setString(2, utilizator.getTipUtilizator());
            if (utilizator instanceof Licitator) {
                Licitator licitator = (Licitator) utilizator;
                stmt.setString(3, licitator.getNume());
                stmt.setString(4, licitator.getEmail());
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setString(6, licitator.getAdresaLivrare());
            } else if (utilizator instanceof Vanzator) {
                Vanzator vanzator = (Vanzator) utilizator;
                stmt.setString(3, vanzator.getNume());
                stmt.setString(4, vanzator.getEmail());
                stmt.setString(5, vanzator.getNumarTelefon());
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> cautaDupaId(String id) {
        String interogare = "SELECT * FROM utilizatori WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construiesteUtilizator(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<Utilizator> cautaDupaEmail(String email) {
        String interogare = "SELECT * FROM utilizatori WHERE email = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construiesteUtilizator(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Utilizator> gasesteToate() {
        List<Utilizator> lista = new ArrayList<>();
        String interogare = "SELECT * FROM utilizatori";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(construiesteUtilizator(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public void actualizeaza(Utilizator utilizator) {
        String interogare = "UPDATE utilizatori SET tip = ?, nume = ?, email = ?, numar_telefon = ?, adresa_livrare = ? WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, utilizator.getTipUtilizator());
            stmt.setString(6, utilizator.getId().getValoare());
            if (utilizator instanceof Licitator) {
                Licitator licitator = (Licitator) utilizator;
                stmt.setString(2, licitator.getNume());
                stmt.setString(3, licitator.getEmail());
                stmt.setNull(4, java.sql.Types.VARCHAR);
                stmt.setString(5, licitator.getAdresaLivrare());
            } else if (utilizator instanceof Vanzator) {
                Vanzator vanzator = (Vanzator) utilizator;
                stmt.setString(2, vanzator.getNume());
                stmt.setString(3, vanzator.getEmail());
                stmt.setString(4, vanzator.getNumarTelefon());
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sterge(String id) {
        String interogare = "DELETE FROM utilizatori WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Utilizator construiesteUtilizator(ResultSet rs) throws SQLException {
        String idValoare = rs.getString("id");
        String tip = rs.getString("tip");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        Identificator id = new Identificator(idValoare);

        if ("Licitator".equalsIgnoreCase(tip)) {
            String adresaLivrare = rs.getString("adresa_livrare");
            return new Licitator(id, nume, email, adresaLivrare);
        } else {
            String numarTelefon = rs.getString("numar_telefon");
            return new Vanzator(id, nume, email, numarTelefon);
        }
    }
}
