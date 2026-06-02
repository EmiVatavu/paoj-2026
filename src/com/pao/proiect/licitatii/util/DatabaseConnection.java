package com.pao.proiect.licitatii.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instanta;
    private Connection conexiune;

    private DatabaseConnection() {
        try {
            Properties proprietati = new Properties();
            File fisier = gasesteFisier("db.properties");
            try (FileInputStream fis = new FileInputStream(fisier)) {
                proprietati.load(fis);
            }

            String url = proprietati.getProperty("db.url");
            String utilizator = proprietati.getProperty("db.user");
            String parola = proprietati.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexiune = DriverManager.getConnection(url, utilizator, parola);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File gasesteFisier(String numeFisier) {
        File dir = new File(".").getAbsoluteFile();
        while (dir != null) {
            File f = new File(dir, numeFisier);
            if (f.exists()) {
                return f;
            }
            File fRes = new File(dir, "resources/" + numeFisier);
            if (fRes.exists()) {
                return fRes;
            }
            dir = dir.getParentFile();
        }
        return new File(numeFisier);
    }

    public static synchronized DatabaseConnection getInstanta() {
        try {
            if (instanta == null || instanta.getConexiune().isClosed()) {
                instanta = new DatabaseConnection();
            }
        } catch (SQLException e) {
            instanta = new DatabaseConnection();
        }
        return instanta;
    }

    public Connection getConexiune() {
        return conexiune;
    }
}
