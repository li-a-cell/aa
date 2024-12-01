package com.hlw.pojo;

import com.hlw.pojo.Equipment;
import com.hlw.pojo.MaterialNodeId;
import com.hlw.pojo.Project;
import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "materialnode")
@IdClass(MaterialNodeId.class) // 使用复合主键类
public class MaterialNode {
    @Id
    @Column(name = "node_id")
    private int nodeId; // 节点ID

    @Id
    @Column(name = "material_id")
    private int materialId; // 材料ID

    @Column(name = "required_quantity")
    private int requiredQuantity; // 所需数量

}