package com.familymenu.backend.adapter.persistence.entity;

import com.familymenu.backend.domain.DishCategory;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
@Entity
@Table(name = "dishes")
public class DishEntity {

    @Id
    private String id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}