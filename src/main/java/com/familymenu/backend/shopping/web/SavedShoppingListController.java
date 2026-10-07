package com.familymenu.backend.shopping.web;

import com.familymenu.backend.shopping.service.SavedShoppingListService;
import com.familymenu.backend.shopping.service.dto.AdditionalProduct;
import com.familymenu.backend.shopping.service.dto.SavedShoppingListView;
import com.familymenu.backend.shopping.web.dto.ShoppingListNameRequest;
import com.familymenu.backend.shopping.web.dto.ShoppingListProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class SavedShoppingListController {

    private final SavedShoppingListService shoppingListService;

    @GetMapping
    public List<SavedShoppingListView> listShoppingLists() {
        return shoppingListService.listShoppingLists();
    }

    @GetMapping("/{id}")
    public SavedShoppingListView getShoppingList(@PathVariable String id) {
        return shoppingListService.getShoppingList(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavedShoppingListView createShoppingList(@Valid @RequestBody ShoppingListNameRequest request) {
        return shoppingListService.createShoppingList(request.name());
    }

    @PutMapping("/{id}")
    public SavedShoppingListView renameShoppingList(
            @PathVariable String id, @Valid @RequestBody ShoppingListNameRequest request
    ) {
        return shoppingListService.renameShoppingList(id, request.name());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShoppingList(@PathVariable String id) {
        shoppingListService.deleteShoppingList(id);
    }

    @DeleteMapping("/{id}/contents")
    public SavedShoppingListView clearShoppingList(@PathVariable String id) {
        return shoppingListService.clearShoppingList(id);
    }

    @PostMapping("/{id}/menus/{menuId}")
    public SavedShoppingListView addMenuToShoppingList(@PathVariable String id, @PathVariable String menuId) {
        return shoppingListService.addMenuToShoppingList(id, menuId);
    }

    @DeleteMapping("/{id}/menus/{menuId}")
    public SavedShoppingListView removeMenuFromShoppingList(@PathVariable String id, @PathVariable String menuId) {
        return shoppingListService.removeMenuFromShoppingList(id, menuId);
    }

    @PostMapping("/{id}/dishes/{dishId}")
    public SavedShoppingListView addDishToShoppingList(@PathVariable String id, @PathVariable String dishId) {
        return shoppingListService.addDishToShoppingList(id, dishId);
    }

    @DeleteMapping("/{id}/dishes/{dishId}")
    public SavedShoppingListView removeDishFromShoppingList(@PathVariable String id, @PathVariable String dishId) {
        return shoppingListService.removeDishFromShoppingList(id, dishId);
    }

    @PostMapping("/{id}/products")
    public SavedShoppingListView addProductToShoppingList(
            @PathVariable String id, @Valid @RequestBody ShoppingListProductRequest request
    ) {
        AdditionalProduct product = toAdditionalProduct(request);
        return shoppingListService.addProductToShoppingList(id, product);
    }

    @PutMapping("/{id}/products/{itemId}")
    public SavedShoppingListView updateShoppingListProduct(
            @PathVariable String id,
            @PathVariable String itemId,
            @Valid @RequestBody ShoppingListProductRequest request
    ) {
        AdditionalProduct product = toAdditionalProduct(request);
        return shoppingListService.updateShoppingListProduct(id, itemId, product);
    }

    @DeleteMapping("/{id}/products/{itemId}")
    public SavedShoppingListView removeProductFromShoppingList(@PathVariable String id, @PathVariable String itemId) {
        return shoppingListService.removeProductFromShoppingList(id, itemId);
    }

    private AdditionalProduct toAdditionalProduct(ShoppingListProductRequest request) {
        return new AdditionalProduct(request.productId(), request.amount(), request.unit());
    }
}
