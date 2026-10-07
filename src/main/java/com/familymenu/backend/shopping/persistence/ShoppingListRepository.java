package com.familymenu.backend.shopping.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, String> {

    List<ShoppingListEntity> findAllByOrderByInboxDescNameAsc();

    @Query(value = "SELECT EXISTS (SELECT 1 FROM shopping_lists WHERE menu_ids @> jsonb_build_array(CAST(:id AS text)))", nativeQuery = true)
    boolean containsMenu(@Param("id") String menuId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM shopping_lists WHERE dish_ids @> jsonb_build_array(CAST(:id AS text)))", nativeQuery = true)
    boolean containsDish(@Param("id") String dishId);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1 FROM shopping_lists
                WHERE manual_products @> jsonb_build_array(jsonb_build_object('productId', CAST(:id AS text)))
            )
            """, nativeQuery = true)
    boolean containsManualProduct(@Param("id") String productId);
}
