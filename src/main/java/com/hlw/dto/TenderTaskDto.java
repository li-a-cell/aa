package com.hlw.dto;

import com.hlw.pojo.TenderTask;
import jakarta.persistence.*;

import java.time.LocalDate;

public class TenderTaskDto {

    private Integer tend_task_id;

    private int project_id;

    private String project_name;

    private LocalDate deadline;

    private TenderTask.TenderTaskStatus tender_task_status;

}
