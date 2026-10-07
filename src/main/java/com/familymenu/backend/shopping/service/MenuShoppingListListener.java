package com.familymenu.backend.shopping.service;

import com.familymenu.backend.menu.api.event.MenuCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MenuShoppingListListener {

    private final SavedShoppingListService shoppingListService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void addCreatedMenuToInbox(MenuCreated event) {
        shoppingListService.addMenuToShoppingList(SavedShoppingListService.INBOX_ID, event.menuId());
    }
}
