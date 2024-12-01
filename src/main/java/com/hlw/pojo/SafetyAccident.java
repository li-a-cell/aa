package com.hlw.pojo;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "safetyaccident")
public class SafetyAccident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accident_id") // 映射到数据库中的 accident_id 列
    private int accidentId; // 事故ID

    @Column(name = "accident_name", nullable = false, length = 255)
    private String accidentName; // 事故名称

    @Column(name = "accident_type", length = 100)
    private String accidentType; // 事故类型

    @Column(name = "accident_description", columnDefinition = "TEXT")
    private String accidentDescription; // 事故描述


    @Column(name = "responsible_person_id",  nullable = false)
    private int responsiblePersonId; // 事故责任人（外键，关联 Bidder）

    @Column(name = "accident_date")
    private LocalDateTime accidentDate; // 事故发生日期（DATETIME）
}
