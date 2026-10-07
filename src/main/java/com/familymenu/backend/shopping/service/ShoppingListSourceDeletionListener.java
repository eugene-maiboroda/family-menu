package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.event.DishDeletionRequested;
import com.familymenu.backend.menu.api.event.MenuDeletionRequested;
import com.familymenu.backend.product.api.event.ProductDeletionRequested;
import com.familymenu.backend.shopping.persistence.ShoppingListRepository;
import com.familymenu.backend.shopping.service.exception.ShoppingSourceInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingListSourceDeletionListener {

    private final ShoppingListRepository shoppingListRepository;

    @EventListener
    public void validateMenuCanBeDeleted(MenuDeletionRequested event) {
        if (shoppingListRepository.containsMenu(event.id())) {
            throw new ShoppingSourceInUseException(event.id());
        }
    }

    @EventListener
    public void validateDishCanBeDeleted(DishDeletionRequested event) {
        if (shoppingListRepository.containsDish(event.id())) {
            throw new ShoppingSourceInUseException(event.id());
        }
    }

    @EventListener
    public void validateProductCanBeDeleted(ProductDeletionRequested event) {
        if (shoppingListRepository.containsManualProduct(event.id())) {
            throw new ShoppingSourceInUseException(event.id());
        }
    }
}
