package com.familymenu.backend.adapter.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "menus")
public class MenuEntity {

    @Id
    private String id;

    @CreationTimestamp
    private Instant createdDate;
}
