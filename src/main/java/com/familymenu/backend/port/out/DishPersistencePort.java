package com.familymenu.backend.port.out;

import com.familymenu.backend.domain.dish.Dish;

import java.util.List;
import java.util.Optional;

public interface DishPersistencePort {

    void save(Dish dish);

    Optional<Dish> findById(String id);

    void deleteById(String id);

    Dish update(String id, Dish dish);

    List<Dish> findAll();
}
