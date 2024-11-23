package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")  // 映射到数据库中的 material_id
    private int material_id; // 材料ID

    @Column(name = "material_name", nullable = false, length = 100)
    private String material_name; // 材料名称

    @Lob
    @Column(name = "material_photo")
    private byte[] material_photo; // 材料照片，存储为 BLOB 类型

    @Column(name = "material_type", length = 100)
    private String material_type; // 材料类型

    @Column(name = "supplier_name", length = 100)
    private String supplier_name; // 供应商名称

    @Column(name = "current_stock_quantity")
    private int currentStock_quantity; // 当前库存量

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_status", nullable = false)
    private QualityStatus quality_status; // 质量状态

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks; // 备注

    // 枚举：质量状态
    public enum QualityStatus {
        没问题, 有问题
    }
}
