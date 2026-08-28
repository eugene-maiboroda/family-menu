package com.familymenu.backend.shopping.web;

import com.familymenu.backend.shopping.service.ShoppingListService;
import com.familymenu.backend.shopping.web.dto.ShoppingListItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListWebMapper shoppingMapper;

    @GetMapping("/{menuId}/shopping-list")
    public List<ShoppingListItemResponse> getShoppingList(@PathVariable String menuId) {
        return shoppingMapper.toResponse(shoppingListService.getShoppingList(menuId));
    }
}
