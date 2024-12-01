package com.hlw.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

// MaterialNodeId 是 MaterialNode 的复合主键类
@Setter
@Getter
public class MaterialNodeId implements Serializable {

    // Getters and Setters
    private int nodeId; // 节点ID
    private int materialId; // 材料ID

    // 必须要有无参构造函数
    public MaterialNodeId() {
    }

    // 全参构造函数
    public MaterialNodeId(int node_id, int material_id) {
        this.nodeId = node_id;
        this.materialId = material_id;
    }

    // 重写 equals 方法，确保对象比较的逻辑
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialNodeId that = (MaterialNodeId) o;
        return nodeId == that.nodeId && materialId == that.materialId;
    }

    // 重写 hashCode 方法，以便正确处理主键对象的哈希值
    @Override
    public int hashCode() {
        return Objects.hash(nodeId, materialId);
    }
}
