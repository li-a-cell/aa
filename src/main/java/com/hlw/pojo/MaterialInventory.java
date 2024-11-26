package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
    private Integer inventory_id;

    @Column(name = "material_id", nullable = false)
    private int material_id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "entry_date")
    private LocalDateTime entry_date;

    @Column(name = "supplier_name")
    private String supplier_name;

    @Column(name = "price")
    private Integer price;

    @Lob
    @Column(name = "remarks")
    private String remarks;

}