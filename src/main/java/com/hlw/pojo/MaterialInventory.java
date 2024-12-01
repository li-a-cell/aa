package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "material_inventory")
public class MaterialInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    @Column(name = "material_id", nullable = false)
    private int materialId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "price")
    private int price;

    @Lob
    @Column(name = "remarks")
    private String remarks;

}