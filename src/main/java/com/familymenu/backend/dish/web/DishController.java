package com.familymenu.backend.dish.web;

import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.service.DishService;
import com.familymenu.backend.dish.web.dto.CreateDishRequest;
import com.familymenu.backend.dish.web.dto.DishResponse;
import com.familymenu.backend.dish.web.dto.UpdateDishRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishWebAssembler dishAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishResponse createDish(@RequestBody @Valid CreateDishRequest request) {
        return dishAssembler.toDishResponse(dishService.createDish(dishAssembler.toDish(request)));

    }

    @PutMapping("/{id}")
    public DishResponse updateDish(@PathVariable String id, @RequestBody @Valid UpdateDishRequest request) {
        Dish dish = dishAssembler.toDish(request, id);
        Dish updatedDish = dishService.updateDish(dish);
        return dishAssembler.toDishResponse(updatedDish);
    }

    @GetMapping("/{id}")
    public DishResponse getDish(@PathVariable String id) {
        return dishAssembler.toDishResponse(dishService.getDish(id));
    }

    @GetMapping
    public List<DishResponse> listDishes(@RequestParam(required = false) String query) {
        return dishService.listDishes(query).stream()
                .map(dishAssembler::toDishResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable String id) {
        dishService.deleteDish(id);
    }
}
