package com.familymenu.backend.port.in;

import com.familymenu.backend.domain.dish.Dish;

import java.util.List;

public interface DishUseCase {

    void createDish(Dish dish);

    Dish updateDish(Dish dish);

    void deleteDish(String id);

    List<Dish> getDishes();

    Dish getDish(String id);
}
