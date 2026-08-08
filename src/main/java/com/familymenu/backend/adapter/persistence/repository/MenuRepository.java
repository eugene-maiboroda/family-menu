package com.familymenu.backend.adapter.persistence.repository;

import com.familymenu.backend.adapter.persistence.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String> {
}
