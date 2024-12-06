package com.hlw.controller;

import com.hlw.dto.ProjectNodeIdDto;
import com.hlw.pojo.*;
import com.hlw.service.EmpService;
import com.hlw.service.ManagerService;
import com.hlw.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmpService empService;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    /**
     * 创建项目节点的接口
     */
    @PostMapping("/create")
    public Result createProjectNode(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
        // 参数校验
        if (projectNode == null) {
            return Result.error("Project node data is missing");
        }

        // 检查必填字段
        if (projectNode.getProjectId() == 0 || projectNode.getNodeName() == null || projectNode.getStartDate() == null || projectNode.getEndDate() == null) {
            return Result.error("Missing required fields: projectId, nodeName, startDate, endDate");
        }

        // 从请求属性中获取 employee_id
        Object employeeIdObj = request.getAttribute("employeeId");

        // 检查 employee_id 是否为空
        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }

        int employeeId;
        try {
            // 转换为 int 类型
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 检查日期逻辑
        if (projectNode.getStartDate().isAfter(projectNode.getEndDate())) {
            return Result.error("Start date cannot be after end date");
        }

        // 调用服务层方法，创建项目节点
        try {
            return managerService.createProjectNode(projectNode.getProjectId(), projectNode.getParentNodeId(), projectNode.getNodeName(),
                    projectNode.getStartDate(), projectNode.getEndDate(), projectNode.getNodeInfo());
        } catch (Exception e) {
            return Result.error("Failed to create project node: " + e.getMessage());
        }
    }


    /**
      * 获取顶层节点
      */
    @PostMapping("/topLevelNodes")
    public Result getTopLevelNodes(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
        // 校验 employeeId 是否存在
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        // 校验 employeeId 格式
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 校验 ProjectNode 的 status 是否为空
        if (projectNode.getStatus() == null) {
            return Result.error("Node status is missing in the request");
        }

        // 获取顶层节点
        try {
            List<ProjectNode> topLevelNodes = managerService.getTopLevelNodesByStatus(employeeId, projectNode.getStatus());
            if (topLevelNodes != null && !topLevelNodes.isEmpty()) {
                return Result.success(topLevelNodes);
            } else {
                return Result.error("No top-level nodes found for the specified status");
            }
        } catch (Exception e) {
            return Result.error("An error occurred while retrieving top-level nodes");
        }
    }
    /**
     * 获取指定父节点的子节点
     */
    @GetMapping("/childNodes/{parentNodeId}")
    public Result getChildNodes(@PathVariable int parentNodeId) {
        if (parentNodeId <= 0) {
            return Result.error("Invalid parent node ID");
        }
        // 获取子节点
        try {
            List<ProjectNode> childNodes = managerService.getChildNodesByParentNodeId(parentNodeId);

            // 检查子节点列表是否为空
            if (childNodes == null || childNodes.isEmpty()) {
                return Result.error("No child nodes found for the given parent node.");
            }
            return Result.success(childNodes);
        } catch (Exception e) {
            // 记录详细的错误日志
            return Result.error("An error occurred while retrieving child nodes.");
        }
    }
    /**
     * 添加材料数量
     */
    @PostMapping("/configure")
    public Result configureMaterial(@RequestBody String materialNode) {
        if (materialNode == null || materialNode.isEmpty()) {
            return Result.error("材料节点信息为空");
        }

        JsonUtils jsonUtils = new JsonUtils();
        String nodeId = JsonUtils.getValueFromJson(materialNode, "nodeId");
        String materialName = JsonUtils.getValueFromJson(materialNode, "materialName");
        String requiredQuantity = JsonUtils.getValueFromJson(materialNode, "requiredQuantity");

        // 参数校验
        if (nodeId == null || nodeId.isEmpty() || materialName == null || materialName.isEmpty() || requiredQuantity == null || requiredQuantity.isEmpty()) {
            return Result.error("材料节点信息不完整");
        }

        try {
            // 转换参数并调用服务层
            return managerService.configureMaterialForNode(Integer.parseInt(nodeId), materialName, Integer.parseInt(requiredQuantity));
        } catch (NumberFormatException e) {
            return Result.error("无效的数字格式：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("材料配置失败：" + e.getMessage());
        }
    }
    /**
     * 添加设备
     */
    @PostMapping("/configureEquipment")
    public Result configureEquipment(@RequestBody Equipment equipment) {
        // 参数校验
        if (equipment == null || equipment.getNodeId() <= 0 || equipment.getEquipmentName() == null || equipment.getEquipmentName().isEmpty()) {
            return Result.error("设备信息不完整，节点ID和设备名称不能为空");
        }

        try {
            // 调用服务层进行设备配置
            return managerService.configureEquipmentForNode(equipment.getNodeId(), equipment.getEquipmentName());
        } catch (Exception e) {
            return Result.error("设备配置失败: " + e.getMessage());
        }
    }

    /**
     * 修改材料数量
     */
    @PostMapping("/updateMaterialQuantityByName")
    public Result updateMaterialQuantityByName(@RequestBody String materialNode) {
        JsonUtils jsonUtils = new JsonUtils();
        String nodeId = JsonUtils.getValueFromJson(materialNode, "nodeId");
        String materialName = JsonUtils.getValueFromJson(materialNode, "materialName");
        String newQuantity = JsonUtils.getValueFromJson(materialNode, "newQuantity");

        // 校验新数量是否为负数
        if (Integer.parseInt(newQuantity) < 0) {
            return Result.error("新数量不能为负数");
        }

        return managerService.updateMaterialQuantityForNodeByName(Integer.parseInt(nodeId), materialName, Integer.parseInt(newQuantity));
    }


    /**
     * 控制层方法：将项目节点在使用的设备状态改为指定状态
     */
    @PostMapping("/updateStatus")
    public Result updateEquipmentStatus(@RequestBody Equipment equipment) {
        // 参数验证
        if (equipment.getNodeId() == 0 || equipment.getEquipmentName() == null || equipment.getEquipmentName().isEmpty() || equipment.getEquipmentType() == null || equipment.getEquipmentType().isEmpty()) {
            return Result.error("设备信息无效，无法更新状态");
        }

        return managerService.updateStatus(equipment.getNodeId(), equipment.getEquipmentName(), equipment.getEquipmentType());
    }

    /**
     * 控制层方法：将指定项目节点下所有在使用的设备状态改为 '未使用'
     */
    @PostMapping("/releaseAllFromNode")
    public Result releaseAllEquipmentFromNode(@RequestBody Equipment equipment) {
        // 参数验证，确保节点 ID 合法
        if (equipment.getNodeId() <= 0) {
            return Result.error("无效的节点 ID");
        }

        return managerService.releaseAllEquipmentFromNode(equipment.getNodeId());
    }

  /**
   * 控制层方法：更新项目节点状态
   */
  @PostMapping("/updateNodeStatus")
  public Result updateProjectNodeStatus(@RequestBody ProjectNode projectNode) {
      if (projectNode.getNodeId() <= 0 || projectNode.getStatus() == null) {
          return Result.error("无效的节点ID或状态");
      }
      return managerService.updateProjectNodeStatus(projectNode.getNodeId(), String.valueOf(projectNode.getStatus()));
  }

 /**
  * 获取项目经理管理的某一状态项目节点的数量
  */
 @PostMapping("/nodes/count")
 public Result getNodeCountByStatus(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
     if (projectNode == null) {
         return Result.error("Project node data is missing in the request");
     }

     Object employeeIdObj = request.getAttribute("employeeId");
     if (employeeIdObj == null) {
         return Result.error("employee_id is missing in the request");
     }

     int employeeId;
     try {
         employeeId = Integer.parseInt(employeeIdObj.toString());
     } catch (NumberFormatException e) {
         return Result.error("Invalid Employee ID format");
     }

     if (projectNode.getStatus() == null) {
         return Result.error("Node status is missing in the request");
     }

     int count = managerService.getNodeCountByStatus(employeeId, projectNode.getStatus());
     return Result.success(count);
 }


    /**
     * 获取非已完成状态项目节点的开始日期和结束日期
     */
    @GetMapping("/nodes/startEndDate")
    public Result getProjectNodeStartEndDate(HttpServletRequest request) {
    // 获取 employee_id
    Object employeeIdObj = request.getAttribute("employeeId");

    if (employeeIdObj == null) {
        return Result.error("employee_id is missing in the request");
    }

    int employeeId;
    try {
        employeeId = Integer.parseInt(employeeIdObj.toString());
    } catch (NumberFormatException e) {
        return Result.error("Invalid Employee ID format");
    }

    // 调用服务层方法获取日期
    List<ProjectNodeIdDto> startEndDates = managerService.getProjectNodeStartEndDate(employeeId);

    if (startEndDates != null && !startEndDates.isEmpty()) {
        return Result.success(startEndDates);
    } else {
        return Result.error("没有符合条件的项目节点");
    }
}

    /**
     * 获取当前项目经理未完成项目的所有材料名字及使用数量
     */
    @GetMapping("/incompleteProjects/materials")
    public Result getMaterialsForIncompleteProjects(HttpServletRequest request) {
        // 获取 `employee_id`（即项目经理 ID）
        Object employeeIdObj = request.getAttribute("employeeId");

        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }

        int managerId;
        try {
            managerId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        return managerService.getMaterialsForIncompleteProjects(managerId);
    }


    /**
     * 获取某一项目节点的材料名称和数量
     */
    @GetMapping("/node/{nodeId}/materials")
    public Result getMaterialsByNodeId(@PathVariable int nodeId) {
        try {
            return managerService.getMaterialsByNodeId(nodeId);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("获取节点材料失败，节点 ID: {}", nodeId, e);
            return Result.error("获取节点材料信息失败: " + e.getMessage());
        }
    }

    // 获取某一节点的设备详情

    /**
     * 获取某一节点的设备详情
     */
    @GetMapping("/node/{nodeId}/equipment/details")
    public Result getEquipmentDetailsByNodeId(@PathVariable int nodeId) {
        // 参数校验
        if (nodeId <= 0) {
            return Result.error("无效的节点ID");
        }

        try {
            return managerService.getEquipmentDetailsByNodeId(nodeId);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("获取设备详情失败，节点 ID: {}", nodeId, e);
            return Result.error("获取设备详情失败: " + e.getMessage());
        }
    }

    /**
   * 创建检查任务的接口
   */

@PostMapping("/createInspectionTask")
public Result createInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request) {
    // 从请求属性中获取 employee_id
    Object employeeIdObj = request.getAttribute("employeeId");

    // 校验 employee_id 是否存在
    if (employeeIdObj == null) {
        return Result.error("Employee ID is missing in the request");
    }

    int employeeId;
    try {
        employeeId = Integer.parseInt(employeeIdObj.toString());
    } catch (NumberFormatException e) {
        return Result.error("Invalid Employee ID format");
    }

    // 提取并校验 inspectionTask 字段
    JsonUtils jsonUtils = new JsonUtils();
    String nodeId = JsonUtils.getValueFromJson(inspectionTask, "nodeId");
    String inspectorName = JsonUtils.getValueFromJson(inspectionTask, "inspectorName");
    String status = JsonUtils.getValueFromJson(inspectionTask, "status");
    String inspectionType = JsonUtils.getValueFromJson(inspectionTask, "inspectionType");
    String startDate = JsonUtils.getValueFromJson(inspectionTask, "startDate");
    String dueDate = JsonUtils.getValueFromJson(inspectionTask, "dueDate");




    // 校验参数是否缺失
    if (nodeId == null || inspectorName == null || status == null || inspectionType == null || startDate == null || dueDate == null) {
        return Result.error("Missing required parameters");
    }

    // 通过 inspectorName 获取 inspectorId
    int inspectorId = empService.GetNameByID(inspectorName);

    // 校验是否能找到 inspectorId
    if (inspectorId == 0) {
        return Result.error("Inspector not found");
    }

    // 定义日期格式
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 解析日期并增加异常处理
    LocalDate localStartDate;
    LocalDate localDueDate;
    try {
        localStartDate = LocalDate.parse(startDate, formatter);
        localDueDate = LocalDate.parse(dueDate, formatter);

        // 日期合理性校验
        if (localStartDate.isAfter(localDueDate)) {
            return Result.error("Start date cannot be after due date");
        }
    } catch (DateTimeParseException e) {
        return Result.error("Invalid date format. Expected format: yyyy-MM-dd");
    }

    // 调用服务层，创建检查任务
    try {
        return managerService.createInspectionTask(Integer.parseInt(nodeId), inspectorId, status, inspectionType, localStartDate, localDueDate);
    } catch (Exception e) {
        // 记录异常并返回错误
        logger.error("Failed to create inspection task", e);
        return Result.error("Error creating inspection task: " + e.getMessage());
    }
}


/**
 * 更新检查任务
 */
@PostMapping("/updateInspectionTask")
public Result updateInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request) {
    // 从请求属性中获取 employee_id
    Object employeeIdObj = request.getAttribute("employeeId");

    // 检查 employee_id 是否为空
    if (employeeIdObj == null) {
        return Result.error("Employee ID is missing in the request");
    }

    int employeeId;
    try {
        employeeId = Integer.parseInt(employeeIdObj.toString());
    } catch (NumberFormatException e) {
        return Result.error("Invalid Employee ID format");
    }

    // 提取并校验 inspectionTask 字段
    JsonUtils jsonUtils = new JsonUtils();
    String taskId = JsonUtils.getValueFromJson(inspectionTask, "taskId");
    String inspectorName = JsonUtils.getValueFromJson(inspectionTask, "inspectorName");
    String status = JsonUtils.getValueFromJson(inspectionTask, "status");
    String inspectionType = JsonUtils.getValueFromJson(inspectionTask, "inspectionType");
    String startDate = JsonUtils.getValueFromJson(inspectionTask, "startDate");
    String dueDate = JsonUtils.getValueFromJson(inspectionTask, "dueDate");

    // 校验必填字段是否缺失
    if (taskId == null || inspectorName == null || status == null || inspectionType == null || startDate == null || dueDate == null) {
        return Result.error("Missing required parameters");
    }

    // 获取 inspectorId
    int inspectorId = empService.GetNameByID(inspectorName);
    if (inspectorId == 0) {
        return Result.error("Inspector not found");
    }

    // 定义日期格式
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 解析日期并进行校验
    LocalDate localStartDate;
    LocalDate localDueDate;
    try {
        localStartDate = LocalDate.parse(startDate, formatter);
        localDueDate = LocalDate.parse(dueDate, formatter);

        // 校验日期合理性：开始日期不能晚于截止日期
        if (localStartDate.isAfter(localDueDate)) {
            return Result.error("Start date cannot be after due date");
        }
    } catch (DateTimeParseException e) {
        return Result.error("Invalid date format. Expected format: yyyy-MM-dd");
    }

    // 调用服务层，更新检查任务
    try {
        return managerService.updateInspectionTask(Integer.parseInt(taskId), inspectorId, status, inspectionType, localStartDate, localDueDate);
    } catch (Exception e) {
        // 记录异常并返回错误
        logger.error("Failed to update inspection task", e);
        return Result.error("Error updating inspection task: " + e.getMessage());
    }
}


/**
 * 获取检查任务信息
 */
@PostMapping("/getInspectionTask")
public Result selectInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request) {
    // 从请求属性中获取 employee_id
    Object employeeIdObj = request.getAttribute("employeeId");

    // 检查 employee_id 是否为空
    if (employeeIdObj == null) {
        return Result.error("Employee ID is missing in the request");
    }

    int employeeId;
    try {
        // 转换为 int 类型
        employeeId = Integer.parseInt(employeeIdObj.toString());
    } catch (NumberFormatException e) {
        return Result.error("Invalid Employee ID format");
    }

    // 提取 recordName
    JsonUtils jsonUtils = new JsonUtils();
    String  recordName = JsonUtils.getValueFromJson(inspectionTask, " recordName");

    // 校验 recordName 是否为空
    if (recordName == null || recordName.trim().isEmpty()) {
        return Result.error("Record Name is required");
    }

    // 调用服务层获取检查任务
    try {
        return managerService.getInspectionTask(recordName);
    } catch (Exception e) {
        // 记录异常并返回错误
        logger.error("Failed to fetch inspection task for recordName: {}", recordName, e);
        return Result.error("Error fetching inspection task: " + e.getMessage());
    }
}




    /**
     * 获取规章制度
     */
    @PostMapping("/getRegulations")
    public Result getRegulations(@RequestBody String regulations, HttpServletRequest request) {
        // 从请求属性中获取 employee_id
        Object employeeIdObj = request.getAttribute("employeeId");

        // 检查 employee_id 是否为空
        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }

        int employeeId;
        try {
            // 转换为 int 类型
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 提取 regulationName
        JsonUtils jsonUtils = new JsonUtils();
        String regulationName = JsonUtils.getValueFromJson(regulations, "regulationName");

        // 校验 regulationName 是否为空
        if (regulationName == null || regulationName.trim().isEmpty()) {
            return Result.error("Regulation name is required");
        }

        try {
            // 调用服务层获取法规信息
            return managerService.getRegulationsByName(regulationName);
        } catch (Exception e) {
            // 记录异常并返回错误
            logger.error("Failed to fetch regulations for regulationName: {}", regulationName, e);
            return Result.error("Error fetching regulations: " + e.getMessage());
        }
    }

    /**
     * 创建项目
     */
    @PostMapping("/createProject")
    public Result createProject(@RequestBody String projectDetails, HttpServletRequest request) {
        // 从请求属性中获取 employee_id
        Object employeeIdObj = request.getAttribute("employeeId");

        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 使用 JsonUtils 获取项目的具体信息
        JsonUtils jsonUtils = new JsonUtils();
        String projectName = JsonUtils.getValueFromJson(projectDetails, "projectName");
        String startDate = JsonUtils.getValueFromJson(projectDetails, "plannedStartDate");
        String endDate = JsonUtils.getValueFromJson(projectDetails, "plannedEndDate");
        String  budget= JsonUtils.getValueFromJson(projectDetails, "budget");
        String projectDescription = JsonUtils.getValueFromJson(projectDetails, "projectDescription");
        String constructionSiteName = JsonUtils.getValueFromJson(projectDetails, "siteName");
        String  status= JsonUtils.getValueFromJson(projectDetails, "status");
        String  projectType= JsonUtils.getValueFromJson(projectDetails, "projectType");
        // 校验必填字段
        if (projectName == null || startDate == null || endDate == null || budget == null) {
            return Result.error("Missing required fields: projectName, startDate, endDate, or budget");
        }

        // 校验日期格式
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            return Result.error("Invalid date format. Expected format: yyyy-MM-dd");
        }

        try {
            // 调用 Service 层方法创建项目
            return managerService.createProject(employeeId, projectName, startDate, endDate,
                    projectDescription, constructionSiteName, budget, status, projectType);
        } catch (Exception e) {
            logger.error("Error creating project: {}", projectDetails, e);
            return Result.error("Failed to create project: " + e.getMessage());
        }
    }
    /**
     * 校验日期格式
     */
    private boolean isValidDateFormat(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", dateStr, e);
            return false;
        }
        }

    /**
     * 获取所有规章制度
     */
    @GetMapping("/getAllRegulations")
    public Result getAllRegulations(HttpServletRequest request){
        Object employeeIdObj = request.getAttribute("employee_id");
        // 检查 employee_id 是否为空
        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }
        int employeeId;
        try {
            // 转换为 int 类型
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        List<String> regulations = managerService.getAllRegulations();
        return Result.success(regulations);
    }
    /**
     * 创建招标任务
     */
    @PostMapping("/createTask")
    public Result createTenderTask(@RequestBody String task) {
        // 解析输入的 JSON 字符串
        JsonUtils jsonUtils = new JsonUtils();
        String projectId = JsonUtils.getValueFromJson(task, "projectId");
        String deadline = JsonUtils.getValueFromJson(task, "deadline");
        String tenderTaskStatus = JsonUtils.getValueFromJson(task, "tenderTaskStatus");

        // 解析项目ID和招标任务状态等字段
        int projectId1 = Integer.parseInt(projectId);  // project_name 实际上应该是 project_id
        LocalDate localDeadline = LocalDate.parse(deadline);

        // 调用 service 层的业务逻辑方法
        boolean isCreated = managerService.createTenderTask(projectId1, tenderTaskStatus, localDeadline);

        // 返回操作结果
        if (isCreated) {
            return Result.success("Tender task created successfully");
        } else {
            return Result.error("Failed to create tender task");
        }
    }



    /**
     * 通过项目节点ID查找检查任务
     */
    @GetMapping("/inspectionTasksByNodeId")
    public Result getInspectionTasksByNodeId(@RequestBody String inspectionTask) {
        JsonUtils jsonUtils = new JsonUtils();
        String nodeId = JsonUtils.getValueFromJson(inspectionTask, "nodeId");
        try {
            return managerService.getInspectionTasksByNodeId(Integer.parseInt(nodeId));
        }
        catch (Exception e) {
            return Result.error("Error fetching inspection tasks: " + e.getMessage());
        }
    }



}