package com.familymenu.backend.port.in;

import com.familymenu.backend.application.dto.CreateMenuCommand;
import com.familymenu.backend.domain.shopping.ShoppingListItem;
import com.familymenu.backend.domain.menu.Menu;

import java.util.List;

public interface MenuUseCase {

    Menu createMenu(CreateMenuCommand menuCommand);

    void updateMenu(Menu menu);

    void deleteMenu(String id);

    List<Menu> getMenus();

    Menu getMenu(String id);

    List<ShoppingListItem> getShoppingList(String menuId);
}
