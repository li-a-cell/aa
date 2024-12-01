package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenderrecord") // 映射到数据库的 'tenderRecord' 表
public class TenderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tender_record_id") // 映射到数据库中的 'tender_record_id' 列
    private int tenderRecordId; // 招标记录ID

    @Column(name = "project_id", nullable = false)
    private int projectId; // 关联的项目，数据库中的字段为 project_id

    @Column(name = "tenderer_id", nullable = false)
    private int tendererId; // 关联的招标方（项目），数据库中的字段为 tenderer_id

    @Column(name = "request_date")
    private LocalDate requestDate; // 请求日期，数据库中的字段为 request_date

    @Column(name = "bidder_id", nullable = false)
    private int bidderId; // 关联的投标人（承包商），数据库中的字段为 bidder_id
}
