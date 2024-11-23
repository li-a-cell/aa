package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regulations")
public class Regulations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regulation_id") // 映射到数据库中的 regulation_id 列
    private int regulation_id; // 规章ID

    @Column(name = "regulation_name", nullable = false, length = 255)
    private String regulation_name; // 规章名称

    @Column(name = "regulation_content", nullable = false, columnDefinition = "TEXT")
    private String regulation_content; // 规章内容
}
