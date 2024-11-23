package com.hlw.controller;

import com.hlw.pojo.Equipment;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.MaterialNode;
import com.hlw.pojo.Result;
import com.hlw.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
@CrossOrigin(originPatterns = "*",allowCredentials = "true")
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

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
        return Result.success(topLevelNodes);
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

    @PostMapping("/configure")
    public Result configureMaterial(@RequestBody MaterialNode meterialNode) {
        return managerService.configureMaterialForNode(meterialNode.getNode_id(), meterialNode.getMaterial_name(),
                meterialNode.getRequired_quantity());
    }
       @PostMapping("/configureEquipment")
    public Result configureEquipment(@RequestBody Equipment equipment) {
        return managerService.configureEquipmentForNode(equipment.getNode_id(), equipment.getEquipment_name());
    }
}