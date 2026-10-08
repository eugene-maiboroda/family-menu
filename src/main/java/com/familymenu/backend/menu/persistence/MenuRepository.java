package com.familymenu.backend.menu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, String> {

    List<MenuEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String query);
}
