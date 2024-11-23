package com.hlw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class nodematerial {
    private String materialName;   // 材料名称
    private int requiredQuantity;  // 所需数量
    private String materialType;   // 材料类型
    private String supplierName;   // 供应商名称
    private byte[] materialPhoto;  // 材料照片
    private String qualityStatus;  // 质量状态
    private String remarks;// 备注
     public String getEquipmentPhotoBase64() {
        return materialPhoto != null ? Base64.getEncoder().encodeToString(materialPhoto) : null;
    }
}
