package com.familymenu.backend.menu.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class MenuEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @CreationTimestamp
    private Instant createdDate;
}
