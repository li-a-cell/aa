package com.hlw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private int projectId;  // 保持与数据库字段一致

    private int managerId;

    private String projectName;

    private LocalDate plannedStartDate;

    private LocalDate plannedEndDate;

    private int siteId;

    private int contractorId;

    private double budget;

    private String status;

    private String description;

    private ProjectType projectType;


    public enum ProjectType {
        房屋建筑,
        市政工程
    }
}
