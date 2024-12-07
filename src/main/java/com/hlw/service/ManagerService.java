
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
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;
    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);
    /**
     * 方法：创建项目节点
     *
     * @param projectId    项目ID
     * @param parentNodeId 父节点ID，如果为null则表示是顶级节点
     * @param nodeName     节点名称
     * @param startDate    节点开始日期
     * @param endDate      节点结束日期
     * @param nodeInfo     节点信息
     * @return 返回创建结果，包括成功或错误信息
     */
    @Transactional
    public Result createProjectNode(int projectId, Integer parentNodeId, String nodeName, LocalDate startDate, LocalDate endDate, String nodeInfo) {
        try {
            if (parentNodeId != null) {
                // 如果是子节点，调用相应的插入方法
                managerMapper.createProjectNodesByManagerId(projectId, parentNodeId, nodeName, startDate, endDate, nodeInfo);
            } else {
                // 如果是顶级节点，调用另一个插入方法
                managerMapper.createParentProjectNodesByManagerId(projectId, nodeName, startDate, endDate, nodeInfo);
            }

            // 返回成功信息
            return Result.success("Project node created successfully.");
        } catch (DataAccessException e) {
            // 捕获数据库访问异常
            return Result.error("Database error: " + e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常
            return Result.error("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * 获取指定状态的顶层节点（父节点为空）
     *
     * @param managerId 项目经理ID
     * @param status    节点状态
     * @return 返回符合条件的项目节点列表
     */
    public List<ProjectNode> getTopLevelNodesByStatus(int managerId, ProjectNode.NodeStatus status) {
        try {
            return managerMapper.getTopLevelNodesByStatus(managerId, status);
        } catch (DataAccessException e) {
            // 捕获数据库访问异常，记录日志并返回空列表
            return Collections.emptyList();  // 返回空列表而不是抛出异常
        }
    }
    /**
     * 根据父节点 ID 获取其子节点
     *
     * @param parentNodeId 父节点ID
     * @return 返回子节点列表
     */
    public List<ProjectNode> getChildNodesByParentNodeId(int parentNodeId) {
        try {
            return managerMapper.getChildNodesByParentNodeId(parentNodeId);
        } catch (DataAccessException e) {
            // 记录数据库访问错误
            return Collections.emptyList();  // 返回空列表而不是抛出异常
        } catch (Exception e) {
            // 记录其他类型的错误
            return Collections.emptyList();
        }
    }


    /**
     * 方法：配置节点所需材料
     *
     * @param nodeId           节点ID
     * @param materialName     材料名称
     * @param requiredQuantity 所需数量
     * @return 返回配置结果，包括成功或错误信息
     */
    public Result configureMaterialForNode(int nodeId, String materialName, int requiredQuantity) {
        try {
            // 查询当前材料的库存
            int currentStock = managerMapper.getCurrentStockQuantityByName(materialName);

            // 校验库存是否足够
            if (currentStock < requiredQuantity) {
                logger.warn("库存不足: {} 当前库存: {}, 所需数量: {}", materialName, currentStock, requiredQuantity);
                return Result.error("库存不足，无法配置该材料");
            }

            // 通过材料名称查询材料ID
            int materialId = managerMapper.getMaterialIdByName(materialName);
            if (materialId <= 0) {
                logger.error("未找到材料: {}", materialName);
                return Result.error("未找到该材料");
            }

            // 插入节点材料配置
            managerMapper.addMaterialToNode(nodeId, materialId, requiredQuantity);

            // 更新材料库存
            managerMapper.updateMaterial(materialName, currentStock - requiredQuantity);
            return Result.success("材料配置成功");
        } catch (Exception e) {
            logger.error("材料配置失败，节点ID: {}, 材料: {}, 所需数量: {}", nodeId, materialName, requiredQuantity, e);
            return Result.error("材料配置失败: " + e.getMessage());
        }
    }
    /**
     * 为节点配置设备的方法
     *
     * @param nodeId        节点ID
     * @param equipmentName 设备名称
     * @return 返回配置结果，包括成功或错误信息
     */
    public Result configureEquipmentForNode(int nodeId, String equipmentName) {
        try {
            // 根据设备名称获取设备 ID
            int equipmentId = managerMapper.getEquipmentIdByName(equipmentName);
            if (equipmentId <= 0) {
                logger.error("未找到设备: {}", equipmentName);
                return Result.error("未找到该设备");
            }

            // 检查设备状态是否为未使用
            String equipmentStatus = managerMapper.getEquipmentStatusById(equipmentId);
            if (!"未使用".equals(equipmentStatus)) {
                logger.warn("设备 {} 当前状态为 {}，无法配置", equipmentName, equipmentStatus);
                return Result.error("只有状态为未使用的设备才能配置到节点。");
            }

            // 更新设备状态为使用中
            managerMapper.updateEquipmentStatus(equipmentId, "使用中");

            // 更新设备的节点ID
            managerMapper.updateEquipmentNodeID(equipmentId, nodeId);

            // 记录操作日志
            logger.info("设备 {} 已成功配置到节点 {}，设备ID: {}", equipmentName, nodeId, equipmentId);

            return Result.success("设备已成功配置到节点。");
        } catch (Exception e) {
            logger.error("设备配置失败，节点ID: {}, 设备: {}", nodeId, equipmentName, e);
            return Result.error("设备配置失败: " + e.getMessage());
        }
    }
    /**
     * 方法：根据材料名称修改节点材料数量，并更新相应材料的库存量
     *
     * @param nodeId       节点ID
     * @param materialName 材料名称
     * @param newQuantity  新的数量
     * @return 返回更新结果，包括成功或错误信息
     */
    @Transactional
    public Result updateMaterialQuantityForNodeByName(int nodeId, String materialName, int newQuantity) {
        try {
            // 获取当前节点该材料的配置数量
            int currentQuantity = managerMapper.getCurrentMaterialQuantityForNodeByName(nodeId, materialName);
            if (currentQuantity < 0) {
                return Result.error("当前节点不存在该材料配置，无法修改数量");
            }

            // 获取当前材料的库存
            int currentStock = managerMapper.getCurrentStockQuantityByName(materialName);

            // 计算库存变化量
            int stockChange = newQuantity - currentQuantity;

            // 确保库存不能为负数
            if (currentStock + stockChange < 0) {
                return Result.error("库存不足，无法修改为该数量");
            }

            // 更新库存
            managerMapper.updateMaterial(materialName, currentStock + stockChange);

            // 更新节点材料配置的数量
            managerMapper.updateMaterialQuantityForNodeByName(nodeId, materialName, newQuantity);

            return Result.success("节点材料数量修改成功，库存已更新");
        } catch (Exception e) {
            return Result.error("节点材料数量修改失败: " + e.getMessage());
        }
    }

    /**
     * 方法：将项目节点的某台设备状态改为指定状态（未使用，出现问题，已使用）
     *
     * @param nodeId        节点ID
     * @param equipmentName 设备名称
     * @param status        新的状态
     * @return 返回更新结果，包括成功或错误信息
     */
    public Result updateStatus(int nodeId, String equipmentName, String status) {
        try {
            // 验证设备状态是否合法
            if (!isValidStatus(status)) {
                return Result.error("无效的设备状态: " + status);
            }

            // 根据设备名称获取设备 ID
            int equipmentId = managerMapper.getEquipmentIdByName(equipmentName);
            if (equipmentId == 0) {
                return Result.error("设备不存在");
            }

            // 更新设备状态
            managerMapper.updateEquipmentStatus(equipmentId, status);
            return Result.success("设备状态已更新为 " + status);
        } catch (Exception e) {
            return Result.error("设备状态更新失败: " + e.getMessage());
        }
    }
    /**
     * 校验方法：判断设备状态是否合法
     *
     * @param status 设备状态
     * @return 返回是否合法
     */
    private boolean isValidStatus(String status) {
        return "使用中".equals(status) || "未使用".equals(status) || "出现问题".equals(status);
    }

    /**
     * 方法：将指定项目节点下所有在使用的设备状态改为 '未使用'
     *
     * @param nodeId 节点ID
     * @return 返回更新结果，包括成功或错误信息
     */
    public Result releaseAllEquipmentFromNode(int nodeId) {
        try {
            // 获取指定节点下所有在使用中的设备 ID
            List<Integer> equipmentIds = managerMapper.getInUseEquipmentIdsByNodeId(nodeId);

            for (int equipmentId : equipmentIds) {
                // 更新设备状态为未使用
                managerMapper.updateEquipmentStatus(equipmentId, "未使用");
                // 清除设备的节点ID
                managerMapper.clearEquipmentNodeId(equipmentId);
            }

            return Result.success("节点下所有设备状态已更新为未使用。");
        } catch (Exception e) {
            return Result.error("节点下设备状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 方法：更新项目节点状态
     *
     * @param nodeId 节点ID
     * @param status 新的状态
     * @return 返回更新结果，包括成功或错误信息
     */
    @Transactional  // 使用事务，确保所有操作要么成功，要么回滚
    public Result updateProjectNodeStatus(int nodeId, String status) {
        try {
            // 更新项目节点状态
            managerMapper.updateProjectNodeStatus(nodeId, status);

            // 如果状态为已完成，则将该节点下所有设备状态改为未使用
            if ("已完成".equals(status)) {
                // 异步处理释放设备逻辑（根据具体需求选择是否使用异步）
                releaseAllEquipmentFromNode(nodeId);

                // 更新项目状态：检查所有节点是否都已完成
                int projectId = managerMapper.getProjectIdByNodeId(nodeId);
                List<String> nodeStatuses = managerMapper.getAllNodeStatusesByProjectId(projectId);
                boolean allCompleted = nodeStatuses.stream().allMatch(s -> "已完成".equals(s));

                if (allCompleted) {
                    // 更新项目状态为已完成
                    managerMapper.updateProjectStatus(projectId, "已完成");
                    return Result.success("项目节点状态已更新为 " + status + "。项目已完成！");
                }
            }

            return Result.success("项目节点状态已更新为 " + status + "。");
        } catch (Exception e) {

            logger.error("项目节点状态更新失败", e);  // 记录详细日志
            return Result.error("项目节点状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取项目经理管理的某一状态项目节点的数量
     *
     * @param managerId 项目经理ID
     * @param status    节点状态
     * @return 返回节点数量
     */
    public int getNodeCountByStatus(int managerId, ProjectNode.NodeStatus status) {
        return managerMapper.getNodeCountByStatus(managerId, status);
    }

    /**
     * 获取非已完成状态项目节点的开始和结束日期
     *
     * @param managerId 项目经理ID
     * @return 返回项目节点的开始和结束日期列表
     */
    public List<ProjectNodeIdDto> getProjectNodeStartEndDate(int managerId) {
        return managerMapper.getProjectNodeStartEndDate(managerId);
    }

    /**
     * 获取当前项目经理未完成的项目对应的所有材料名字及使用数量
     *
     * @param managerId 项目经理ID
     * @return 返回材料使用情况列表，包括成功或错误信息
     */
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

    /**
     * 获取某一项目节点的材料名称和数量。
     *
     * @param nodeId 项目节点ID
     * @return 包含材料列表的结果对象，如果节点没有使用任何材料或发生异常，返回错误信息
     */
    public Result getMaterialsByNodeId(int nodeId) {
        try {
            // 获取材料列表
            List<nodematerial> materials = managerMapper.getMaterialsByNodeId(nodeId);

            // 如果材料为空，返回适当的错误信息
            if (materials == null || materials.isEmpty()) {
                return Result.error("该节点没有使用任何材料");
            }

            return Result.success(materials);
        } catch (Exception e) {
            // 记录其他类型的错误
            logger.error("获取节点材料失败，节点 ID: {}", nodeId, e);
            return Result.error("获取节点材料信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取某一节点的设备详情。
     *
     * @param nodeId 项目节点ID
     * @return 包含设备详情列表的结果对象，如果节点没有配置任何设备或发生异常，返回错误信息
     */
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

    /**
     * 创建检查任务。
     *
     * @param nodeId 项目节点ID
     * @param inspectorId 检查员ID
     * @param status 检查任务状态
     * @param inspectionType 检查任务类型
     * @param startDate 开始日期
     * @param dueDate 截止日期
     * @return 创建成功的消息或错误信息
     */
    public Result createInspectionTask(int nodeId, int inspectorId, String status, String inspectionType, LocalDate startDate, LocalDate dueDate) {
        try {
            // 校验检查任务必填字段
            if (status == null || inspectionType == null || startDate == null || dueDate == null) {
                return Result.error("缺少必要的检查任务字段，创建失败");
            }

            // 默认检查任务状态为 "未开始"（处理空白字符串）
            if (StringUtils.isBlank(status)) {
                status = "未开始";
            }

            // 调用 DAO 层方法插入检查任务
            managerMapper.createInspectionTask(nodeId, inspectorId, status, inspectionType, startDate, dueDate);

            return Result.success("检查任务创建成功！");
        } catch (Exception e) {
            // 记录其他类型的错误
            logger.error("检查任务创建失败", e);
            return Result.error("检查任务创建失败: " + e.getMessage());
        }
    }
    /**
     * 更新检查任务。
     *
     * @param taskId 检查任务ID
     * @param inspectorId 检查员ID
     * @param status 检查任务状态
     * @param inspectionType 检查任务类型
     * @param startDate 开始日期
     * @param dueDate 截止日期
     * @return 更新成功的消息或错误信息
     */
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

    /**
     * 获取检查任务。
     *
     * @param recordName 检查任务记录名称
     * @return 包含检查任务详情的结果对象，如果检查任务不存在或发生异常，返回错误信息
     */
    public Result getInspectionTask(String recordName) {
        try {
            // 调用 DAO 层方法查询检查任务
            InspectionTask inspectionTask = managerMapper.getInspectionTaskByName(recordName);
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

    /**
     * 获取规章制度。
     *
     * @param name 规章制度名称
     * @return 包含规章制度详情的结果对象，如果规章制度不存在或发生异常，返回错误信息
     */
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

    /**
     * 创建项目。
     *
     * @param managerId 项目经理ID
     * @param projectName 项目名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param projectDescription 项目描述
     * @param constructionSiteName 施工地名称
     * @param budget 项目预算
     * @param status 项目状态
     * @param projectType 项目类型
     * @return 创建成功的消息或错误信息
     */
    public Result createProject(int managerId, String projectName, String startDate,
                                String endDate, String projectDescription, String constructionSiteName, String budget, String status, String projectType) {

        // 查询施工地ID
        Integer constructionSiteId = managerMapper.getConstructionSiteIdByName(constructionSiteName);
        if (constructionSiteId == null) {
            return Result.error("施工地名称无效");
        }

        // 如果您需要将 Integer 转换为 int，请先检查是否为 null
        int validSiteId = (constructionSiteId != null) ? constructionSiteId : -1;  // 处理 null

        // 转换日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        double Budget = Double.parseDouble(budget);

        // 创建项目
        int projectId = managerMapper.createProject(managerId, projectName, start, end, projectDescription, validSiteId, Budget, status, projectType);

        return projectId > 0 ? Result.success("项目创建成功") : Result.error("项目创建失败");
    }

    /**
     * 获取所有规章制度名称。
     *
     * @return 规章制度名称列表
     */
    public List<String> getAllRegulations() {
        return managerMapper.getAllRegulations();
    }

    /**
     * 创建投标任务。
     *
     * @param projectId 项目ID
     * @param tenderTaskStatus 投标任务状态
     * @param deadline 截止日期
     * @return 创建成功的布尔值
     */
    public boolean createTenderTask(int projectId, String tenderTaskStatus, LocalDate deadline) {
        // 调用 Mapper 层方法将数据插入数据库
        return managerMapper.addTenderTask(projectId, tenderTaskStatus, deadline);
    }

    /**
     * 通过项目节点ID查找检查任务。
     *
     * @param nodeId 项目节点ID
     * @return 包含检查任务列表的结果对象，如果节点没有检查任务或发生异常，返回错误信息
     */
    public Result getInspectionTasksByNodeId(int nodeId) {
        try {
            List<InspectionTask> inspectionTasks = managerMapper.getInspectionTasksByNodeId(nodeId);

            if (inspectionTasks == null || inspectionTasks.isEmpty()) {
                return Result.error("该节点没有检查任务");
            }

            return Result.success(inspectionTasks);
        } catch (Exception e) {
            return Result.error("查找检查任务失败: " + e.getMessage());
        }
    }

}
