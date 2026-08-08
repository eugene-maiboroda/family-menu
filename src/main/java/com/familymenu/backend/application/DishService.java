package com.familymenu.backend.application;

import com.familymenu.backend.adapter.persistence.repository.DishRepository;
import com.familymenu.backend.domain.Dish;
import com.familymenu.backend.port.in.DishUseCase;
import com.familymenu.backend.port.out.DishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DishService implements DishUseCase {

    private final DishPersistencePort dishPersistence;

    @Override
    public Dish createDish(Dish dish) {
        return null;
    }

    @Override
    public void updateDish(Dish dish) {

    }

    @Override
    public void deleteDish(String id) {

    }

    @Override
    public List<Dish> getDishes() {
        return List.of();
    }

    @Override
    public Dish getDish(String id) {
        return null;
    }
}
