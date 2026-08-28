package com.familymenu.backend.ingredient.service;

import com.familymenu.backend.ingredient.api.IngredientApi;
import com.familymenu.backend.ingredient.api.dto.IngredientView;
import com.familymenu.backend.ingredient.domain.Ingredient;
import com.familymenu.backend.ingredient.persistence.IngredientMapper;
import com.familymenu.backend.ingredient.persistence.IngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientService implements IngredientApi {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    @Transactional
    public IngredientView resolveByName(String name) {
        return ingredientRepository.findByName(name)
                .map(ingredientMapper::toView)
                .orElseGet(() -> create(name));
    }

    @Override
    public List<IngredientView> getByIds(Collection<String> ids) {
        return ingredientRepository.findAllById(ids).stream()
                .map(ingredientMapper::toView)
                .toList();
    }

    private IngredientView create(String name) {
        Ingredient ingredient = new Ingredient(UUID.randomUUID().toString(), name);
        return ingredientMapper.toView(ingredientRepository.save(ingredientMapper.toEntity(ingredient)));
    }
}
