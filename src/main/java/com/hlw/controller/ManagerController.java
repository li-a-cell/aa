package com.hlw.controller;

import com.hlw.dto.ProjectNodeIdDto;
import com.hlw.pojo.*;
import com.hlw.service.EmpService;
import com.hlw.service.ManagerService;
import com.hlw.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmpService empService;

    // 创建项目节点的接口
    @PostMapping("/create")
    public Result createProjectNode(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
        // 从请求属性中获取 employee_id
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

        // 调用服务层，创建项目节点
        return managerService.createProjectNode(employeeId, projectNode.getParent_node_id(), projectNode.getNode_name(),
                projectNode.getStart_date(), projectNode.getEnd_date(), projectNode.getNode_info());
    }


     // 获取顶层节点
     @GetMapping("/topLevelNodes")
    public Result getTopLevelNodes(@RequestBody ProjectNode projectNode,HttpServletRequest request) {
         Object employeeIdObj = request.getAttribute("employee_id");

         if (employeeIdObj == null) {
             return Result.error("employee_id is missing in the request");
         }

         int employeeId;
         try {
             employeeId = Integer.parseInt(employeeIdObj.toString());
         } catch (NumberFormatException e) {
             return Result.error("Invalid Employee ID format");
         }


         // 获取顶层节点
         List<ProjectNode> topLevelNodes = managerService.getTopLevelNodesByStatus(employeeId, projectNode.getStatus());
         if (topLevelNodes != null) {
             return Result.success(topLevelNodes);
         }
         else {
             return Result.error("没有节点");
         }
     }

    // 获取指定父节点的子节点
    @GetMapping("/childNodes/{parentNodeId}")
    public Result getChildNodes(@PathVariable int parentNodeId) {
        List<ProjectNode> childNodes = managerService.getChildNodesByParentNodeId(parentNodeId);
        if (childNodes == null || childNodes.isEmpty()) {
            return Result.error("No child nodes found for the given parent node.");
        }
        return Result.success(childNodes);
    }
    //添加材料数量
    @PostMapping("/configure")
    public Result configureMaterial(@RequestBody String meterialNode) {
        JsonUtils jsonUtils = new JsonUtils();
        String nodeId = jsonUtils.getValueFromJson(meterialNode, "node_id");
        String materialName = jsonUtils.getValueFromJson(meterialNode, "material_name");
        String requiredQuantity = jsonUtils.getValueFromJson(meterialNode, "required_quantity");
        return managerService.configureMaterialForNode(Integer.parseInt(nodeId),materialName,Integer.parseInt(requiredQuantity));
    }
    //添加设备
       @PostMapping("/configureEquipment")
    public Result configureEquipment(@RequestBody Equipment equipment) {
        return managerService.configureEquipmentForNode(equipment.getNode_id(), equipment.getEquipment_name());
    }
    //修改材料数量
   @PostMapping("/updateMaterialQuantityByName")
    public Result updateMaterialQuantityByName(@RequestBody String meterialNode) {
        JsonUtils jsonUtils = new JsonUtils();
        String nodeId = jsonUtils.getValueFromJson(meterialNode, "node_id");
        String materialName = jsonUtils.getValueFromJson(meterialNode, "material_name");
        String newQuantity = jsonUtils.getValueFromJson(meterialNode, "new_quantity");
        return managerService.updateMaterialQuantityForNodeByName(Integer.parseInt(nodeId),materialName,Integer.parseInt(newQuantity));
    }

    // 控制层方法：将项目节点在使用的设备状态改为指定状态
     @PostMapping("/updateStatus")
    public Result updateEquipmentStatus(@RequestBody Equipment equipment) {
        return managerService.updateEquipmentStatus(equipment.getNode_id(), equipment.getEquipment_name(), equipment.getEquipment_type());
    }

    // 控制层方法：将指定项目节点下所有在使用的设备状态改为 '未使用'
    @PostMapping("/releaseAllFromNode")
    public Result releaseAllEquipmentFromNode(@RequestBody Equipment equipment) {
        return managerService.releaseAllEquipmentFromNode(equipment.getNode_id());
    }

  // 控制层方法：更新项目节点状态
    @PostMapping("/updateNodeStatus")
    public Result updateProjectNodeStatus(@RequestBody ProjectNode projectNode) {
        return managerService.updateProjectNodeStatus(projectNode.getNode_id(), String.valueOf(projectNode.getStatus()));
    }

 // 获取项目经理管理的某一状态项目节点的数量
    @GetMapping("/nodes/count")
    public Result getNodeCountByStatus( @RequestBody ProjectNode projectNode,HttpServletRequest request) {
          Object employeeIdObj = request.getAttribute("employee_id");

         if (employeeIdObj == null) {
             return Result.error("employee_id is missing in the request");
         }

         int employeeId;
         try {
             employeeId = Integer.parseInt(employeeIdObj.toString());
         } catch (NumberFormatException e) {
             return Result.error("Invalid Employee ID format");
         }
        int count = managerService.getNodeCountByStatus(employeeId,projectNode.getStatus() );
        return Result.success(count);
    }


    // 获取非已完成状态项目节点的开始日期和结束日期
    @GetMapping("/nodes/startEndDate")
public Result getProjectNodeStartEndDate(HttpServletRequest request) {
    // 获取 employee_id
    Object employeeIdObj = request.getAttribute("employee_id");

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

    // 获取当前项目经理未完成项目的所有材料名字及使用数量
    @GetMapping("/incompleteProjects/materials")
    public Result getMaterialsForIncompleteProjects(HttpServletRequest request) {
        // 获取 `employee_id`（即项目经理 ID）
        Object employeeIdObj = request.getAttribute("employee_id");

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


    // 获取某一项目节点的材料名称和数量
    @GetMapping("/node/{nodeId}/materials")
    public Result getMaterialsByNodeId(@PathVariable int nodeId) {
        return managerService.getMaterialsByNodeId(nodeId);
    }

    // 获取某一节点的设备详情
;

    // 获取某一节点的设备详情
    @GetMapping("/node/{nodeId}/equipment/details")
    public Result getEquipmentDetailsByNodeId(@PathVariable int nodeId) {
        return managerService.getEquipmentDetailsByNodeId(nodeId);
    }
  // 创建检查任务的接口
@PostMapping("/createInspectionTask")
public Result createInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request) {
    // 从请求属性中获取 employee_id
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
    JsonUtils jsonUtils = new JsonUtils();
    String nodeId = jsonUtils.getValueFromJson(inspectionTask, "node_id");
    String inspectorName = jsonUtils.getValueFromJson(inspectionTask, "inspector_name");
    String status = jsonUtils.getValueFromJson(inspectionTask, "status");
    String inspectionType = jsonUtils.getValueFromJson(inspectionTask, "inspection_type");
    String startDate = jsonUtils.getValueFromJson(inspectionTask, "start_date");
    String dueDate = jsonUtils.getValueFromJson(inspectionTask, "due_date");
    int inspectorId = empService.GetNameByID(inspectorName);



// 定义日期格式
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// 解析字符串为 LocalDate

    LocalDate localdueDate = LocalDate.parse(dueDate, formatter);

    LocalDate localstartDate = LocalDate.parse(startDate, formatter);


    // 调用服务层，创建检查任务
    return managerService.createInspectionTask(Integer.parseInt(nodeId), inspectorId, status, inspectionType, localstartDate, localdueDate);
}

//更新检查任务
@PostMapping("/updateInspectionTask")
public Result updateInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request) {
    // 从请求属性中获取 employee_id
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
    JsonUtils jsonUtils = new JsonUtils();
    String taskId = jsonUtils.getValueFromJson(inspectionTask, "task_id");
    String inspectorName = jsonUtils.getValueFromJson(inspectionTask, "inspector_name");
    String status = jsonUtils.getValueFromJson(inspectionTask, "status");
    String inspectionType = jsonUtils.getValueFromJson(inspectionTask, "inspection_type");
    String startDate = jsonUtils.getValueFromJson(inspectionTask, "start_date");
    String dueDate = jsonUtils.getValueFromJson(inspectionTask, "due_date");
    int inspectorId = empService.GetNameByID(inspectorName);



// 定义日期格式
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// 解析字符串为 LocalDate

    LocalDate localdueDate = LocalDate.parse(dueDate, formatter);

    LocalDate localstartDate = LocalDate.parse(startDate, formatter);


    // 调用服务层，创建检查任务
    return managerService.updateInspectionTask(Integer.parseInt(taskId), inspectorId, status, inspectionType, localstartDate, localdueDate);
}

//获取检查任务信息
@GetMapping("/getInspectionTask")
    public Result selectInspectionTask(@RequestBody String inspectionTask, HttpServletRequest request){
        // 从请求属性中获取 employee_id
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
    JsonUtils jsonUtils = new JsonUtils();
    String taskId = jsonUtils.getValueFromJson(inspectionTask, "task_id");
    return managerService.getInspectionTask(Integer.parseInt(taskId));
    }

    //获取规章制度
    @GetMapping("/getRegulations")
    public Result getRegulations(@RequestBody String regulations,HttpServletRequest request){
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
    JsonUtils jsonUtils = new JsonUtils();
    String name = jsonUtils.getValueFromJson(regulations, "regulation_name");
    return managerService.getRegulationsByName(name);
    }


    //创建项目
    @PostMapping("/createProject")
    public Result createProject(@RequestBody String projectDetails, HttpServletRequest request) {
        // 从请求属性中获取 employee_id
        Object employeeIdObj = request.getAttribute("employee_id");

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
        String projectName = jsonUtils.getValueFromJson(projectDetails, "project_name");
        String startDate = jsonUtils.getValueFromJson(projectDetails, "planned_start_date");
        String endDate = jsonUtils.getValueFromJson(projectDetails, "planned_end_date");
        String  budget=jsonUtils.getValueFromJson(projectDetails, "budget");
        String projectDescription = jsonUtils.getValueFromJson(projectDetails, "project_description");
        String constructionSiteName = jsonUtils.getValueFromJson(projectDetails, "site_name");
        String  status=jsonUtils.getValueFromJson(projectDetails, "status");
        String  project_type=jsonUtils.getValueFromJson(projectDetails, "project_type");
        // 调用 Service 层方法创建项目
        return managerService.createProject(employeeId, projectName, startDate, endDate,
                projectDescription, constructionSiteName,budget,status,project_type);
    }

    @PostMapping("/getAllRegylations")
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
}