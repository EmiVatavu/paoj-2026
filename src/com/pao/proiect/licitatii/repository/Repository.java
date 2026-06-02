package com.pao.proiect.licitatii.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void salveaza(T entitate);
    Optional<T> cautaDupaId(ID id);
    List<T> gasesteToate();
    void actualizeaza(T entitate);
    void sterge(ID id);
}
