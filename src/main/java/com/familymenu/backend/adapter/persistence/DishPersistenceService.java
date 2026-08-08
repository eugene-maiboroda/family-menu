package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.domain.Dish;
import com.familymenu.backend.port.out.DishPersistencePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DishPersistenceService implements DishPersistencePort {
    @Override
    public void save(Dish dish) {

    }

    @Override
    public Optional<Dish> findById(String id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Dish update(String id, Dish dish) {
        return null;
    }

    @Override
    public List<Dish> findAll() {
        return List.of();
    }
}
