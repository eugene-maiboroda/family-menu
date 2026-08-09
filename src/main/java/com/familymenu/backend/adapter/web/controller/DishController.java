package com.familymenu.backend.adapter.web.controller;

import com.familymenu.backend.adapter.web.dto.CreateDishRequest;
import com.familymenu.backend.port.in.DishUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishUseCase dishUseCase;

    @RequestMapping("/create")
    public void createDish(@RequestBody @Valid CreateDishRequest request) {

    }
}
