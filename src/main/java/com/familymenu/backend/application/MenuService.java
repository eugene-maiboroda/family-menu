package com.familymenu.backend.application;

import com.familymenu.backend.application.dto.ShoppingListItem;
import com.familymenu.backend.domain.Menu;
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
    public Menu createMenu(Menu menu) {
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
    public List<ShoppingListItem> getShoppingList(Long menuId) {
        return List.of();
    }
}
