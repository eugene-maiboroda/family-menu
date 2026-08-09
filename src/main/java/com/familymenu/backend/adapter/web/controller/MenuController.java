package com.familymenu.backend.adapter.web.controller;

import com.familymenu.backend.adapter.web.dto.ShoppingListItemResponse;
import com.familymenu.backend.adapter.web.mapper.MenuMapper;
import com.familymenu.backend.adapter.web.mapper.ShoppingListMapper;
import com.familymenu.backend.port.in.MenuUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

private final MenuUseCase menuUseCase;
private final ShoppingListMapper shoppingMapper;

    @GetMapping("/{menuId}/shopping-list")
    public List<ShoppingListItemResponse> getShoppingList(@PathVariable String menuId) {
        return shoppingMapper.toResponse(menuUseCase.getShoppingList(menuId));
    }
}
