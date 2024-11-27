package com.hlw.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class ProjectDetailsView {
    private int projectId; // project_id
    private String  managerName; // manager_name
    private String projectName; // project_name
    private Date plannedStartDate; // planned_start_date
    private Date plannedEndDate; // planned_end_date
    private String siteName; // site_name
    private String contractorName; // contractor_name
    private BigDecimal budget; // budget
    private String status; // status
    private String description; // description
    private String projectType;// pr
}
