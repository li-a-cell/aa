
package com.hlw.dto;

import com.hlw.pojo.TenderTask;
import jakarta.persistence.*;

import java.time.LocalDate;

public class TenderTaskDto {

    private Integer tendTaskId;

    private int projectId;

    private String projectName;

    private LocalDate deadline;

    private TenderTask.TenderTaskStatus tender_task_status;

}
