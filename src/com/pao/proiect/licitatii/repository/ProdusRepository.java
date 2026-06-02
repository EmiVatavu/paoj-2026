package com.pao.proiect.licitatii.repository;

import com.pao.proiect.licitatii.model.Identificator;
import com.pao.proiect.licitatii.model.Produs;
import com.pao.proiect.licitatii.model.ProdusArta;
import com.pao.proiect.licitatii.model.ProdusElectronic;
import com.pao.proiect.licitatii.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdusRepository implements Repository<Produs, String> {
    private final Connection conexiune;

    public ProdusRepository() {
        this.conexiune = DatabaseConnection.getInstanta().getConexiune();
    }

    @Override
    public void salveaza(Produs produs) {
        String interogare = "INSERT INTO produse (id, nume, descriere, pret_pornire, categorie, artist, an_creare, garantie_luni) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, produs.getId().getValoare());
            stmt.setString(2, produs.getNume());
            stmt.setString(3, getDescriereProdus(produs));
            stmt.setDouble(4, produs.getPretPornire());
            stmt.setString(5, produs.getCategorie());

            if (produs instanceof ProdusArta) {
                ProdusArta arta = (ProdusArta) produs;
                stmt.setString(6, arta.getArtist());
                stmt.setInt(7, arta.getAnCreare());
                stmt.setNull(8, java.sql.Types.INTEGER);
            } else if (produs instanceof ProdusElectronic) {
                ProdusElectronic electronic = (ProdusElectronic) produs;
                stmt.setNull(6, java.sql.Types.VARCHAR);
                stmt.setNull(7, java.sql.Types.INTEGER);
                stmt.setInt(8, electronic.getGarantieLuni());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Produs> cautaDupaId(String id) {
        String interogare = "SELECT * FROM produse WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construiesteProdus(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Produs> gasesteToate() {
        List<Produs> lista = new ArrayList<>();
        String interogare = "SELECT * FROM produse";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(construiesteProdus(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public void actualizeaza(Produs produs) {
        String interogare = "UPDATE produse SET nume = ?, descriere = ?, pret_pornire = ?, categorie = ?, artist = ?, an_creare = ?, garantie_luni = ? WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, produs.getNume());
            stmt.setString(2, getDescriereProdus(produs));
            stmt.setDouble(3, produs.getPretPornire());
            stmt.setString(4, produs.getCategorie());
            stmt.setString(8, produs.getId().getValoare());

            if (produs instanceof ProdusArta) {
                ProdusArta arta = (ProdusArta) produs;
                stmt.setString(5, arta.getArtist());
                stmt.setInt(6, arta.getAnCreare());
                stmt.setNull(7, java.sql.Types.INTEGER);
            } else if (produs instanceof ProdusElectronic) {
                ProdusElectronic electronic = (ProdusElectronic) produs;
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setNull(6, java.sql.Types.INTEGER);
                stmt.setInt(7, electronic.getGarantieLuni());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sterge(String id) {
        String interogare = "DELETE FROM produse WHERE id = ?";
        try (PreparedStatement stmt = conexiune.prepareStatement(interogare)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Produs construiesteProdus(ResultSet rs) throws SQLException {
        String idValoare = rs.getString("id");
        String nume = rs.getString("nume");
        String descriere = rs.getString("descriere");
        double pretPornire = rs.getDouble("pret_pornire");
        String categorie = rs.getString("categorie");
        Identificator id = new Identificator(idValoare);

        if ("Arta".equalsIgnoreCase(categorie)) {
            String artist = rs.getString("artist");
            int anCreare = rs.getInt("an_creare");
            return new ProdusArta(id, nume, descriere, pretPornire, artist, anCreare);
        } else {
            int garantieLuni = rs.getInt("garantie_luni");
            return new ProdusElectronic(id, nume, descriere, pretPornire, garantieLuni);
        }
    }

    private String getDescriereProdus(Produs produs) {
        try {
            java.lang.reflect.Field f = Produs.class.getDeclaredField("descriere");
            f.setAccessible(true);
            return (String) f.get(produs);
        } catch (Exception e) {
            return "";
        }
    }
}
