package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.port.out.IngredientPersistencePort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class IngredientPersistenceService implements IngredientPersistencePort {

    @Override
    public Optional<Ingredient> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return null;
    }
}
