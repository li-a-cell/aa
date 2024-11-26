package com.hlw.service;

import com.hlw.dao.ManagerMapper;
import com.hlw.dto.EquipmentDetails;
import com.hlw.dto.MaterialUsage;
import com.hlw.dto.ProjectNodeIdDto;
import com.hlw.dto.nodematerial;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Regulations;
import com.hlw.pojo.InspectionTask;
import com.hlw.pojo.Result;
import com.hlw.pojo.TenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    // 方法：创建项目节点
    public Result createProjectNode(int managerId, int parentNodeId, String nodeName, LocalDate startDate, LocalDate endDate, String nodeInfo) {
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
             managerMapper.updateEquipmentnodeID(equipmentId, nodeId);
            return Result.success("设备已成功配置到节点。");
        } catch (Exception e) {
            return Result.error("设备配置失败: " + e.getMessage());
        }
    }




    // 方法：根据材料名称修改节点材料数量，并更新相应材料的库存量
    public Result updateMaterialQuantityForNodeByName(int nodeId, String materialName, int newQuantity) {
        try {
            // 获取当前节点该材料的配置数量
            int currentQuantity = managerMapper.getCurrentMaterialQuantityForNodeByName(nodeId, materialName);
            if (currentQuantity < 0) {
                return Result.error("当前节点不存在该材料配置，无法修改数量");
            }

            // 获取当前材料的库存
            int currentStock = managerMapper.getCurrentStockQuantityByName(materialName);
            // 计算库存变化量（当前数量 - 新数量），正数表示增加库存，负数表示减少库存
            int stockChange = currentQuantity-newQuantity ;

            // 更新库存（这里要确保库存更新后不能为负数，可添加更严谨的逻辑判断）
            if (currentStock + stockChange < 0) {
                return Result.error("库存不足，无法修改为该数量");
            }
            managerMapper.updateMaterial(materialName, currentStock + stockChange);

            // 更新节点材料配置的数量
            managerMapper.updateMaterialQuantityForNodeByName(nodeId, materialName, newQuantity);

            return Result.success("节点材料数量修改成功，库存已更新");
        } catch (Exception e) {
            return Result.error("节点材料数量修改失败: " + e.getMessage());
        }
    }



     // 方法：将项目节点的某台设备状态改为指定状态（未使用，出现问题，已使用）
    public Result updateEquipmentStatus(int nodeId, String equipmentName, String status) {
        try {
            // 根据设备名称获取设备 ID
            int equipmentId = managerMapper.getEquipmentIdByName(equipmentName);

            // 检查设备当前状态
            String equipmentStatus = managerMapper.getEquipmentStatusById(equipmentId);
            if (!"使用中".equals(equipmentStatus) && !"未使用".equals(equipmentStatus) && !"出现问题".equals(equipmentStatus)) {
                return Result.error("设备状态无效，无法更新为指定状态。");
            }

            // 更新设备状态为指定状态
            managerMapper.updateEquipmentStatus(equipmentId, status);

            return Result.success("设备状态已更新为 " + status + "。");
        } catch (Exception e) {
            return Result.error("设备状态更新失败: " + e.getMessage());
        }
    }

    // 方法：将指定项目节点下所有在使用的设备状态改为 '未使用'
    public Result releaseAllEquipmentFromNode(int nodeId) {
        try {
            // 获取指定节点下所有在使用中的设备 ID
            List<Integer> equipmentIds = managerMapper.getInUseEquipmentIdsByNodeId(nodeId);

            for (int equipmentId : equipmentIds) {
                // 更新设备状态为未使用
                managerMapper.updateEquipmentStatus(equipmentId, "未使用");
                   managerMapper.clearEquipmentNodeId(equipmentId);
            }

            return Result.success("节点下所有设备状态已更新为未使用。");
        } catch (Exception e) {
            return Result.error("节点下设备状态更新失败: " + e.getMessage());
        }
    }




    // 方法：更新项目节点状态



    public Result updateProjectNodeStatus(int nodeId, String status) {
        try {
            // 更新项目节点状态
            managerMapper.updateProjectNodeStatus(nodeId, status);

            // 如果状态为已完成，则将该节点下所有设备状态改为未使用
                if ("已完成".equals(status)) {
                releaseAllEquipmentFromNode(nodeId);
                // 检查项目下所有节点状态
                int projectId = managerMapper.getProjectIdByNodeId(nodeId);
                List<String> nodeStatuses = managerMapper.getAllNodeStatusesByProjectId(projectId);
                boolean allCompleted = nodeStatuses.stream().allMatch(s -> "已完成".equals(s));
                if (allCompleted) {
                    managerMapper.updateProjectStatus(projectId, "已完成");
                    return Result.success("项目节点状态已更新为 " + status + "。"+"项目已完成！");
                }
            }

            return Result.success("项目节点状态已更新为 " + status + "。");
        } catch (Exception e) {
            return Result.error("项目节点状态更新失败: " + e.getMessage());
        }
    }

    // 获取项目经理管理的某一状态项目节点的数量
    public int getNodeCountByStatus(int managerId, ProjectNode.NodeStatus status) {
        return managerMapper.getNodeCountByStatus(managerId, status);
    }

 // 获取非已完成状态项目节点的开始和结束日期
    public List<ProjectNodeIdDto> getProjectNodeStartEndDate(int managerId) {

            return managerMapper.getProjectNodeStartEndDate(managerId);

    }



    // 获取当前项目经理未完成的项目对应的所有材料名字及使用数量
    public Result getMaterialsForIncompleteProjects(int managerId) {
        try {
            // 查询未完成项目对应的材料及其数量
            List<MaterialUsage> materials = managerMapper.getMaterialsForIncompleteProjects(managerId);

            if (materials == null || materials.isEmpty()) {
                return Result.error("未完成的项目没有使用任何材料");
            }

            return Result.success(materials);
        } catch (Exception e) {
            return Result.error("获取未完成项目的材料信息失败: " + e.getMessage());
        }
    }





    // 获取某一项目节点的材料名称和数量
    public Result getMaterialsByNodeId(int nodeId) {
        try {
            List<nodematerial> materials = managerMapper.getMaterialsByNodeId(nodeId);

            if (materials == null || materials.isEmpty()) {
                return Result.error("该节点没有使用任何材料");
            }

            return Result.success(materials);
        } catch (Exception e) {
            return Result.error("获取节点材料信息失败: " + e.getMessage());
        }
    }



    // 获取某一节点的设备详情
    public Result getEquipmentDetailsByNodeId(int nodeId) {
        try {
            List<EquipmentDetails> equipmentDetails = managerMapper.getEquipmentDetailsByNodeId(nodeId);

            if (equipmentDetails == null || equipmentDetails.isEmpty()) {
                return Result.error("该节点没有配置任何设备");
            }

            return Result.success(equipmentDetails);
        } catch (Exception e) {
            return Result.error("获取节点设备信息失败: " + e.getMessage());
        }
    }
  // 创建检查任务
    public Result createInspectionTask(int nodeId, int inspectorId, String status, String inspectionType, LocalDate startDate, LocalDate dueDate) {
    try {
        // 校验检查任务必填字段
        if (status == null || inspectionType == null || startDate == null || dueDate == null) {
            return Result.error("缺少必要的检查任务字段，创建失败");
        }

        // 默认检查任务状态为 "未开始"
        if (status.isEmpty()) {
            status = "未开始";
        }

        // 调用 DAO 层方法插入检查任务
        managerMapper.createInspectionTask(nodeId, inspectorId, status, inspectionType, startDate, dueDate);

        return Result.success("检查任务创建成功！");
    } catch (Exception e) {
        return Result.error("检查任务创建失败: " + e.getMessage());
    }

}

public Result updateInspectionTask(int taskId, int inspectorId, String status, String inspectionType, LocalDate startDate, LocalDate dueDate) {
        try {
            // 校验检查任务必填字段
            if (taskId <= 0 || status == null || inspectionType == null || startDate == null || dueDate == null) {
                return Result.error("缺少必要的检查任务字段，更新失败");
            }

            // 校验任务是否存在
            InspectionTask existingTask = managerMapper.getInspectionTaskById(taskId);
            if (existingTask == null) {
                return Result.error("检查任务不存在，更新失败");
            }

            // 调用 DAO 层方法更新检查任务
            managerMapper.updateInspectionTask(taskId, inspectorId, status, inspectionType, startDate, dueDate);

            return Result.success("检查任务更新成功！");
        } catch (Exception e) {
            return Result.error("检查任务更新失败: " + e.getMessage());
        }
    }

    public Result getInspectionTask(String record_name) {
        try {
            // 调用 DAO 层方法查询检查任务
            InspectionTask inspectionTask = managerMapper.getInspectionTaskByName(record_name);
            // 如果检查任务不存在，返回错误信息
            if (inspectionTask == null) {
                return Result.error("检查任务不存在");
            }
            // 返回查询结果
            return Result.success(inspectionTask);
        } catch (Exception e) {
            return Result.error("检查任务查询失败: " + e.getMessage());
       }
    }

    public Result getRegulationsByName(String name) {
        try {
            // 调用 DAO 层方法查询检查任务
            Regulations regulations = managerMapper.getRegulationsByName(name);
            // 如果检查任务不存在，返回错误信息
            if (regulations == null) {
                return Result.error("规章制度不存在。");
            }
            // 返回查询结果
            return Result.success(regulations);
        } catch (Exception e) {
            return Result.error("规章制度查询失败: " + e.getMessage());
       }
    }

    public Result createProject(int managerId, String projectName, String startDate,
                                String endDate, String projectDescription, String constructionSiteName,String budget,String status,String project_type)
    {




        // 查询施工地ID
        Integer constructionSiteId = managerMapper.getConstructionSiteIdByName(constructionSiteName);
        if (constructionSiteId == null) {
            return Result.error("施工地名称无效");
        }


        // 这里您可以直接使用 supplierId, constructionSiteId, contractorId 作为 Integer 类型

        // 如果您需要将 Integer 转换为 int，请先检查是否为 null
        int validSiteId = (constructionSiteId != null) ? constructionSiteId : -1;  // 处理 null


        // 转换日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        double Budget = Double.parseDouble(budget);



        // 创建项目
        int projectId = managerMapper.createProject(managerId, projectName, start, end, projectDescription, validSiteId,Budget,status,project_type );

        return projectId > 0 ? Result.success("项目创建成功") : Result.error("项目创建失败");
    }


    public List<String> getAllRegulations() {
        return managerMapper.getAllRegulations();
    }
    // 创建投标任务
    public boolean createTenderTask(int projectId, String tenderTaskStatus, LocalDate deadline) {


        // 调用 Mapper 层方法将数据插入数据库
        return managerMapper.addTenderTask(projectId, tenderTaskStatus, deadline);
    }
}