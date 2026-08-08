package com.familymenu.backend.port.in;

import com.familymenu.backend.application.dto.ShoppingListItem;
import com.familymenu.backend.domain.Menu;

import java.util.List;

public interface MenuUseCase {

    Menu createMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(String id);

    List<Menu> getMenus();

    Menu getMenu(String id);

    List<ShoppingListItem> getShoppingList(Long menuId);
}
