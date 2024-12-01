package com.hlw.pojo;

import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspectionrecord")
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id") // 映射到数据库中的 record_id 列
    private int recordId; // 检查记录ID

    @Column(name = "record_name", nullable = false, length = 255)
    private String recordName; // 检查记录名称

    @Lob
    @Column(name = "record_photo")
    private byte[] recordPhoto; // 检查记录照片，映射到 BLOB 类型


    @Column(name = "inspection_task_id", nullable = false)
   private int inspectionTaskId; // 关联的检查任务（外键）


    @Column(name = "inspector_id", nullable = false)
    private int  inspector; // 关联的检查员（外键）

    @Column(name = "record_description", columnDefinition = "TEXT")
    private String recordDescription; // 检查记录描述
}
