package com.hlw.dto;
import lombok.Data;
import java.sql.Date;

@Data
public class TenderProjectView {

    private Integer projectId;
    private String projectName;
    private String managerName;
    private String managerPhone;
    private Date startDate;
    private Date endDate;
    private Date tenderDeadline;
    private String projectType;
}