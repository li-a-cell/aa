package com.hlw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialUsage {
    private String material_name;   // 材料名称
    private int  total_quantity;  // 所需数量

}
