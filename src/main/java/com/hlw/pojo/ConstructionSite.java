package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constructionsite")  // 确保与数据库中的表名一致
public class ConstructionSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")  // 映射数据库表中的 site_id 列
    private int siteId; // 施工场地ID

    @Column(name = "site_name", nullable = false, length = 100)
    private String siteName; // 施工场地名称

    @Column(name = "site_address", length = 255)
    private String siteAddress; // 施工场地地址

    @Column(name = "area_size")
    private double areaSize; // 施工场地面积
}
