package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constructionSite")  // 确保与数据库中的表名一致
public class Constructionsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")  // 映射数据库表中的 site_id 列
    private int site_id; // 施工场地ID

    @Column(name = "site_name", nullable = false, length = 100)
    private String site_name; // 施工场地名称

    @Column(name = "site_address", length = 255)
    private String site_address; // 施工场地地址

    @Column(name = "area_size")
    private double area_size; // 施工场地面积
}
