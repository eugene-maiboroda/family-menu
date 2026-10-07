package com.familymenu.backend.product.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    Optional<ProductEntity> findByNameIgnoreCase(String name);

    List<ProductEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String query);
}
