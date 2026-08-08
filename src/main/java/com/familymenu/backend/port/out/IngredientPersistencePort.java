package com.familymenu.backend.port.out;

import com.familymenu.backend.domain.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientPersistencePort {

    Optional<Ingredient> findByName(String name);

    Ingredient save(Ingredient ingredient);

}
