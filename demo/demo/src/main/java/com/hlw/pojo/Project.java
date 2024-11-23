package com.hlw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private int project_id;  // 保持与数据库字段一致

    private int manager_id;

    private String project_name;

    private LocalDate planned_start_date;

    private LocalDate planned_end_date;

    private int site_id;

    private int contractor_id;

    private double budget;

    private String status;

    private String description;

    private ProjectType project_type;

    // 施工地名称，与查询的 construction_site_name 字段匹配
    private String site_name;

    // 投标人名称，与查询的 bidder_name_result 字段匹配
    private String bidder_name;

    public enum ProjectType {
        房屋建筑,
        市政工程
    }
}