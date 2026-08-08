package com.familymenu.backend.adapter.web;

import com.familymenu.backend.application.dto.ShoppingListItem;
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

    @GetMapping("/{menuId}/shopping-list")
    public List<ShoppingListItem> getShoppingList(@PathVariable Long menuId) {
        return menuUseCase.getShoppingList(menuId);
    }
}
