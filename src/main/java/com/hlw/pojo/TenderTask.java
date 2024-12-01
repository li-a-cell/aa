package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tender_task")
public class TenderTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tender_task_id", nullable = false)
    private Integer tendTaskId;

    @Column(name = "project_id", nullable = false)
    private int projectId;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "tender_task_status")
    private TenderTaskStatus tenderTaskStatus;
    public enum TenderTaskStatus {
        待发布, 待招标, 已完成
    }

}