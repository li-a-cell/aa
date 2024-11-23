package com.hlw.service;

import com.hlw.dao.ManagerMapper;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    // 方法：创建项目节点
    public Result createProjectNode(int managerId, int parentNodeId, String nodeName, Date startDate, Date endDate, String nodeInfo) {
        try {
            // 调用 Mapper 层方法，插入数据
            managerMapper.createProjectNodesByManagerId(managerId, parentNodeId, nodeName, startDate, endDate, nodeInfo);
            return Result.success("Project node created successfully.");
        } catch (Exception e) {
            return Result.error("Failed to create project node: " + e.getMessage());
        }
    }

     // 获取指定状态的顶层节点（父节点为空）
    public List<ProjectNode> getTopLevelNodesByStatus(int managerId, ProjectNode.NodeStatus status) {
        return managerMapper.getTopLevelNodesByStatus(managerId, status);
    }

    // 根据父节点 ID 获取其子节点
    public List<ProjectNode> getChildNodesByParentNodeId(int parentNodeId) {
        return managerMapper.getChildNodesByParentNodeId(parentNodeId);
    }





    // 方法：配置节点所需材料
    public Result configureMaterialForNode(int nodeId, String materialName, int requiredQuantity) {
        try {
            // 查询当前材料的库存
            int currentStock = managerMapper.getCurrentStockQuantityByName(materialName);

            // 校验库存是否足够
            if (currentStock < requiredQuantity) {
                return Result.error("库存不足，无法配置该材料");
            }

            // 这里假设通过 materialName 能够找到对应的 materialId，这里需要进一步处理
             // 通过材料名称查询材料ID
            int materialId = managerMapper.getMaterialIdByName(materialName);


            // 插入节点材料配置
            managerMapper.addMaterialToNode(nodeId, materialId, requiredQuantity);

             managerMapper.updateMaterial(materialName,currentStock-requiredQuantity);
            return Result.success("材料配置成功");
        } catch (Exception e) {
            return Result.error("材料配置失败: " + e.getMessage());
        }
    }







    // 为节点配置设备的方法
    public Result configureEquipmentForNode(int nodeId, String equipmentName) {
        try {
            // 根据设备名称获取设备 ID
            int equipmentId = managerMapper.getEquipmentIdByName(equipmentName);

            // 检查设备状态是否为未使用
            String equipmentStatus = managerMapper.getEquipmentStatusById(equipmentId);
            if (!"未使用".equals(equipmentStatus)) {
                return Result.error("只有状态为未使用的设备才能配置到节点。");
            }

            // 更新设备状态为使用中
            managerMapper.updateEquipmentStatus(equipmentId, "使用中");

            return Result.success("设备已成功配置到节点。");
        } catch (Exception e) {
            return Result.error("设备配置失败: " + e.getMessage());
        }
    }

}