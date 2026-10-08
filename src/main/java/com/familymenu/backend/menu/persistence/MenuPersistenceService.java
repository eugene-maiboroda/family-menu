package com.familymenu.backend.menu.persistence;

import com.familymenu.backend.menu.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MenuPersistenceService {

    private final MenuRepository menuRepository;
    private final MenuDishRepository menuDishRepository;

    public Menu saveMenu(Menu menu) {
        MenuEntity entity = new MenuEntity();
        entity.setId(menu.id());
        entity.setName(menu.name());
        entity.setCreatedDate(menu.createdDate());
        MenuEntity saved = menuRepository.save(entity);

        menuDishRepository.deleteAllByMenuId(menu.id());
        List<MenuDishEntity> relations = menu.dishIds().stream()
                .map(dishId -> toRelation(menu.id(), dishId))
                .toList();
        menuDishRepository.saveAll(relations);

        return new Menu(saved.getId(), saved.getName(), saved.getCreatedDate(), List.copyOf(menu.dishIds()));
    }

    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
    }

    public Optional<Menu> findMenuById(String id) {
        return menuRepository.findById(id).map(this::toDomain);
    }

    public List<Menu> findAllMenus(String query) {
        String searchQuery = "";
        if (query != null) {
            searchQuery = query.strip();
        }
        return menuRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchQuery).stream()
                .map(this::toDomain)
                .toList();
    }

    private Menu toDomain(MenuEntity entity) {
        List<String> dishIds = menuDishRepository.findAllByMenuId(entity.getId()).stream()
                .map(MenuDishEntity::getDishId)
                .toList();
        return new Menu(entity.getId(), entity.getName(), entity.getCreatedDate(), dishIds);
    }

    private MenuDishEntity toRelation(String menuId, String dishId) {
        MenuDishEntity relation = new MenuDishEntity();
        relation.setId(UUID.randomUUID().toString());
        relation.setMenuId(menuId);
        relation.setDishId(dishId);
        return relation;
    }
}
