package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.domain.Menu;
import com.familymenu.backend.port.out.MenuPersistencePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuPersistenceService implements MenuPersistencePort {
    @Override
    public void save(String id) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Optional<Menu> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        return List.of();
    }
}
