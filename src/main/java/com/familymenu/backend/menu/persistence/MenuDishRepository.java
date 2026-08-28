package com.familymenu.backend.menu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDishRepository extends JpaRepository<MenuDishEntity, String> {

    List<MenuDishEntity> findAllByMenuId(String menuId);

    void deleteAllByMenuId(String menuId);
}
