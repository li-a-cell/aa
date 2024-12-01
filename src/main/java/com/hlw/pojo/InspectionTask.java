package com.hlw.pojo;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspectiontask")
public class InspectionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_task_id") // 映射到数据库中的 inspection_task_id 列
    private int inspectionTaskId; // 检查任务ID


    @Column(name = "node_id")
    private int nodeId; // 关联的项目节点（外键）


    @Column(name = "inspector_id", nullable = false)
    private int inspectorId; // 关联的检查员（外键，User 是 Employee 实体）

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status; // 检查任务的状态

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_type", nullable = false)
    private InspectionType inspectionType; // 检查类型

    @Column(name = "start_date")
    private LocalDate startDate; // 检查任务开始日期

    @Column(name = "due_date")
    private LocalDate dueDate; // 检查任务到期日期

    // 状态枚举：任务状态
    public enum Status {
        未开始, // 任务未开始
        检查中, // 任务正在进行中
        发现问题, // 发现问题
        已完成  // 任务完成
    }

    // 检查类型枚举：任务类型
    public enum InspectionType {
        关键工序, // 关键工序检查
        实测实量, // 实测实量检查
        设备检查, // 设备检查
        材料检查  // 材料检查
    }
}
