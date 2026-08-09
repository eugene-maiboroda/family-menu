package com.familymenu.backend.port.out;

import com.familymenu.backend.domain.menu.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuPersistencePort {

    void save(String id);

    void delete(String id);

    Optional<Menu> findById(String id);

    List<Menu> findAll();

}
