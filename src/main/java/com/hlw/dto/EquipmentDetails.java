package com.hlw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDetails {
    private String equipmentName;   // 设备名称
    private byte[] equipmentPhoto;  // 设备照片（BLOB）
    private String status;          // 设备状态（未使用、使用中、出现问题）
    private String equipmentType;   // 设备类型
    private String equipmentModel;  // 设备型号
    private String remarks;         // 备注

    /**
     * 如果需要将设备照片返回为 Base64 格式（前端方便显示）
     */
    public String getEquipmentPhotoBase64() {
        return equipmentPhoto != null ? Base64.getEncoder().encodeToString(equipmentPhoto) : null;
    }
}
