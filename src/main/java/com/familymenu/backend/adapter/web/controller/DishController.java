package com.familymenu.backend.adapter.web.controller;

import com.familymenu.backend.adapter.web.dto.CreateDishRequest;
import com.familymenu.backend.adapter.web.dto.DishResponse;
import com.familymenu.backend.adapter.web.dto.UpdateDishRequest;
import com.familymenu.backend.adapter.web.mapper.DishMapper;
import com.familymenu.backend.domain.dish.Dish;
import com.familymenu.backend.port.in.DishUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishUseCase dishUseCase;
    private final DishMapper dishMapper;

    @PostMapping("/create")
    public void createDish(@RequestBody @Valid CreateDishRequest request) {

    }

    @PutMapping("/{id}")
    public DishResponse updateDish(@PathVariable String id, @RequestBody @Valid UpdateDishRequest request) {
        Dish dish = dishMapper.toDish(request, id);
        Dish updatedDish = dishUseCase.updateDish( dish);
        return dishMapper.toDishResponse(updatedDish);
    }
}
