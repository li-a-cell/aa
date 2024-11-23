package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private int equipment_id;  // 设备 ID


    @Column(name = "equipment_name", nullable = false)
    private String equipment_name;  // 设备名称

    @Lob
    @Column(name = "equipment_photo")
    private byte[] equipment_photo;  // 设备照片

    @Column(name = "equipment_type")
    private String equipment_type;  // 设备类型

    @Column(name = "equipment_model")
    private String equipment_model;  // 设备型号

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EquipmentStatus status;  // 设备状态


    @Column(name = "node_id", nullable = false)
    private int  node_id;  // 关联的项目节点

    @Column(name = "remarks")
    private String remarks;  // 备注

    // 枚举：设备状态
    public enum EquipmentStatus {
        未使用, 使用中, 出现问题
    }

    // Getter 和 Setter
//    public int getEquipmentId() {
//        return equipmentId;
//    }
//
//    public void setEquipmentId(int equipmentId) {
//        this.equipmentId = equipmentId;
//    }
//
//    public String getEquipmentName() {
//        return equipmentName;
//    }
//
//    public void setEquipmentName(String equipmentName) {
//        this.equipmentName = equipmentName;
//    }
//
//    public byte[] getEquipmentPhoto() {
//        return equipmentPhoto;
//    }
//
//    public void setEquipmentPhoto(byte[] equipmentPhoto) {
//        this.equipmentPhoto = equipmentPhoto;
//    }
//
//    public String getEquipmentType() {
//        return equipmentType;
//    }
//
//    public void setEquipmentType(String equipmentType) {
//        this.equipmentType = equipmentType;
//    }
//
//    public String getEquipmentModel() {
//        return equipmentModel;
//    }
//
//    public void setEquipmentModel(String equipmentModel) {
//        this.equipmentModel = equipmentModel;
//    }
//
//    public EquipmentStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(EquipmentStatus status) {
//        this.status = status;
//    }
//
//    public Projectnode getNode() {
//        return node;
//    }
//
//    public void setNode(Projectnode node) {
//        this.node = node;
//    }
//
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }
}