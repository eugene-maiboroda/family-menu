package com.familymenu.backend.ingredient.api;

import com.familymenu.backend.ingredient.api.dto.IngredientView;

import java.util.Collection;
import java.util.List;

public interface IngredientApi {

    IngredientView resolveByName(String name);

    List<IngredientView> getByIds(Collection<String> ids);
}
