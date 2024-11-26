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
    private Integer tend_task_id;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "tender_task_status")
    private Status tender_task_status;

    public enum Status {
        待发布,
        待招标,
        已完成
    }

}