package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "material_inventory")
public class MaterialInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int inventory_id;

    @Column(name = "material_id", nullable = false)
    private int material_id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "entry_date")
    private LocalDate entry_date;

    @Column(name = "supplier_name")
    private String supplier_name;

    @Column(name = "price")
    private int price;

    @Lob
    @Column(name = "remarks")
    private String remarks;

}