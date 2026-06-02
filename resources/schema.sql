CREATE TABLE IF NOT EXISTS utilizatori (
    id VARCHAR(50) PRIMARY KEY,
    tip VARCHAR(20) NOT NULL,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    numar_telefon VARCHAR(20) DEFAULT NULL,
    adresa_livrare VARCHAR(200) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS produse (
    id VARCHAR(50) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    descriere VARCHAR(500) DEFAULT NULL,
    pret_pornire DOUBLE NOT NULL,
    categorie VARCHAR(20) NOT NULL,
    artist VARCHAR(100) DEFAULT NULL,
    an_creare INT DEFAULT NULL,
    garantie_luni INT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS licitatii (
    id VARCHAR(50) PRIMARY KEY,
    produs_id VARCHAR(50) NOT NULL,
    data_incheiere DATETIME NOT NULL,
    este_activa BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (produs_id) REFERENCES produse (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS oferte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    licitatie_id VARCHAR(50) NOT NULL,
    licitator_id VARCHAR(50) NOT NULL,
    suma DOUBLE NOT NULL,
    moment_timp DATETIME NOT NULL,
    FOREIGN KEY (licitatie_id) REFERENCES licitatii (id) ON DELETE CASCADE,
    FOREIGN KEY (licitator_id) REFERENCES utilizatori (id) ON DELETE CASCADE
);
