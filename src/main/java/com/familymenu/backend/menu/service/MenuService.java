package com.familymenu.backend.menu.service;

import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.menu.api.event.MenuDeletionRequested;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.menu.api.event.MenuCreated;
import org.springframework.context.ApplicationEventPublisher;
import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.menu.domain.Menu;
import com.familymenu.backend.menu.persistence.MenuPersistenceService;
import com.familymenu.backend.menu.service.dto.CreateMenuCommand;
import com.familymenu.backend.menu.service.exception.MenuNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService implements MenuApi {

    private final MenuPersistenceService menuPersistence;
    private final DishApi dishApi;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Menu createMenu(CreateMenuCommand menuCommand) {
        validateDishesExist(menuCommand.dishIds());
        Menu menu = new Menu(
                UUID.randomUUID().toString(),
                Instant.now(),
                List.copyOf(menuCommand.dishIds())
        );
        Menu savedMenu = menuPersistence.saveMenu(menu);
        eventPublisher.publishEvent(new MenuCreated(savedMenu.id()));
        return savedMenu;
    }

    @Transactional
    public void updateMenu(Menu menu) {
        validateDishesExist(menu.dishIds());
        menuPersistence.saveMenu(menu);
    }

    @Transactional
    public void deleteMenu(String id) {
        eventPublisher.publishEvent(new MenuDeletionRequested(id));
        menuPersistence.deleteMenu(id);
    }

    public List<Menu> getMenus() {
        return menuPersistence.findAllMenus();
    }

    public Menu getMenu(String id) {
        return menuPersistence.findMenuById(id)
                .orElseThrow(() -> new MenuNotFoundException(id + " not found"));
    }

    @Override
    public Optional<MenuView> findById(String id) {
        return menuPersistence.findMenuById(id).map(this::toView);
    }

    private void validateDishesExist(List<String> dishIds) {
        HashSet<String> foundIds = dishApi.getByIds(dishIds).stream()
                .map(DishView::id)
                .collect(java.util.stream.Collectors.toCollection(HashSet::new));
        List<String> missingIds = dishIds.stream()
                .distinct()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new IllegalArgumentException("Dishes not found: " + missingIds);
        }
    }

    private MenuView toView(Menu menu) {
        return new MenuView(menu.id(), menu.createdDate(), List.copyOf(menu.dishIds()));
    }
}
