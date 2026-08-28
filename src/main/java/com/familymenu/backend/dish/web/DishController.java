package com.familymenu.backend.dish.web;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.service.DishService;
import com.familymenu.backend.dish.web.dto.CreateDishRequest;
import com.familymenu.backend.dish.web.dto.DishResponse;
import com.familymenu.backend.dish.web.dto.UpdateDishRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishWebAssembler dishAssembler;

    @PostMapping("/create")
    public void createDish(@RequestBody @Valid CreateDishRequest request) {

    }

    @PutMapping("/{id}")
    public DishResponse updateDish(@PathVariable String id, @RequestBody @Valid UpdateDishRequest request) {
        Dish dish = dishAssembler.toDish(request, id);
        Dish updatedDish = dishService.updateDish(dish);
        return dishAssembler.toDishResponse(updatedDish);
    }
}
