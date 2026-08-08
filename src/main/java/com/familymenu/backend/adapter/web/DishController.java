package com.familymenu.backend.adapter.web;

import com.familymenu.backend.port.in.DishUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishUseCase dishUseCase;

    @RequestMapping("/create")
    public void createDish() {

    }
}
