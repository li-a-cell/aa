package com.hlw.dto;
import com.hlw.pojo.TenderTask;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TenderTaskDto {

    private Integer tendTaskId;

    private int projectId;

    private String projectName;

    private LocalDate deadline;

    private TenderTask.TenderTaskStatus tender_task_status;

}
