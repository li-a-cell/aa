
package com.hlw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private int projectId;  // 保持与数据库字段一致

    private int managerId;

    private String managerName;

    private String projectName;

    private LocalDate plannedStartDate;

    private LocalDate plannedEndDate;

    private int siteId;

    private int contractorId;

    private double budget;

    private String status;

    private String description;

    private ProjectType projectType;

    /**
     * 施工地名称，与查询的 construction_site_name 字段匹配
     */
    private String siteName;

    /**
     * 投标人名称，与查询的 bidder_name_result 字段匹配
     */
    private String bidderName;

    public enum ProjectType {
        房屋建筑,
        市政工程
    }
}
