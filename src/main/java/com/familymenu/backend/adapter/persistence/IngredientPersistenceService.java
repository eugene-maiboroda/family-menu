package com.familymenu.backend.adapter.persistence;

import com.familymenu.backend.adapter.persistence.entity.IngredientEntity;
import com.familymenu.backend.adapter.persistence.mapper.IngredientPersistenceMapper;
import com.familymenu.backend.adapter.persistence.repository.IngredientRepository;
import com.familymenu.backend.domain.dish.Ingredient;
import com.familymenu.backend.infrastructure.exception.IngredientNotFoundException;
import com.familymenu.backend.port.out.IngredientPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IngredientPersistenceService implements IngredientPersistencePort {

    private final IngredientRepository ingredientRepository;
    private final IngredientPersistenceMapper ingredientMapper;

    @Override
    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name)
                .map(ingredientMapper::toDomain);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        IngredientEntity savedEntity = ingredientRepository.save(ingredientMapper.toEntity(ingredient));
        return ingredientMapper.toDomain(savedEntity);
    }
}
