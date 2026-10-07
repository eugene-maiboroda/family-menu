package com.familymenu.backend.shopping.persistence;

import com.familymenu.backend.shopping.domain.ManualProductPurchase;
import com.familymenu.backend.shopping.domain.PurchasedItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shopping_lists")
public class ShoppingListEntity {

    @Id
    private String id;

    private String name;

    private boolean inbox;

    @Version
    private long version;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "menu_ids", columnDefinition = "jsonb", nullable = false)
    private List<String> menuIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dish_ids", columnDefinition = "jsonb", nullable = false)
    private List<String> dishIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "manual_products", columnDefinition = "jsonb", nullable = false)
    private List<ManualProductPurchase> manualProducts = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "purchased_items", columnDefinition = "jsonb", nullable = false)
    private List<PurchasedItem> purchasedItems = new ArrayList<>();
}
