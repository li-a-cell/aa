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
    private int materialId; // 材料ID

    @Column(name = "material_name", nullable = false, length = 100)
    private String materialName; // 材料名称

    @Lob
    @Column(name = "material_photo")
    private byte[] materialPhoto; // 材料照片，存储为 BLOB 类型

    @Column(name = "material_type", length = 100)
    private String materialType; // 材料类型

    @Column(name = "current_stock_quantity")
    private int currentStockQuantity; // 当前库存量

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_status", nullable = false)
    private QualityStatus qualityStatus; // 质量状态

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks; // 备注

    // 枚举：质量状态
    public enum QualityStatus {
        没问题, 有问题
    }
}
