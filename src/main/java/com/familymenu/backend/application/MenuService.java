package com.familymenu.backend.application;

import com.familymenu.backend.application.dto.CreateMenuCommand;
import com.familymenu.backend.domain.shopping.ShoppingListItem;
import com.familymenu.backend.domain.menu.Menu;
import com.familymenu.backend.port.in.MenuUseCase;
import com.familymenu.backend.port.out.MenuPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MenuService implements MenuUseCase {

    private final MenuPersistencePort menuPersistence;

    @Override
    public Menu createMenu(CreateMenuCommand menuCommand) {
        return null;
    }

    @Override
    public void updateMenu(Menu menu) {

    }

    @Override
    public void deleteMenu(String id) {

    }

    @Override
    public List<Menu> getMenus() {
        return List.of();
    }

    @Override
    public Menu getMenu(String id) {
        return null;
    }

    @Override
    public List<ShoppingListItem> getShoppingList(String menuId) {
        return List.of();
    }
}
