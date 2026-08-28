package com.familymenu.backend.menu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, String> {
}
