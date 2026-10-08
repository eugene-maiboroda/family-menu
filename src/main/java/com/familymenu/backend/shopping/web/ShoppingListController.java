package com.familymenu.backend.shopping.web;

import com.familymenu.backend.shopping.service.SavedShoppingListService;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.web.dto.ShoppingListItemPurchaseRequest;
import com.familymenu.backend.shopping.service.dto.AdditionalProduct;
import com.familymenu.backend.shopping.service.dto.SavedShoppingListView;
import com.familymenu.backend.shopping.web.dto.ShoppingListNameRequest;
import com.familymenu.backend.shopping.web.dto.ShoppingListProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

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

    @PatchMapping("/{id}")
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

    @PutMapping("/{id}/items/{productId}/{dimension}/purchase-status")
    public SavedShoppingListView updateShoppingListItemPurchaseStatus(
            @PathVariable String id,
            @PathVariable String productId,
            @PathVariable Unit.Dimension dimension,
            @Valid @RequestBody ShoppingListItemPurchaseRequest request
    ) {
        return shoppingListService.updateShoppingListItemPurchaseStatus(
                id, productId, dimension, request.purchased()
        );
    }

    @DeleteMapping("/{id}/purchase-status")
    public SavedShoppingListView resetShoppingListPurchases(@PathVariable String id) {
        return shoppingListService.resetShoppingListPurchases(id);
    }

    private AdditionalProduct toAdditionalProduct(ShoppingListProductRequest request) {
        return new AdditionalProduct(request.productId(), request.amount(), request.unit());
    }
}
