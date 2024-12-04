package com.hlw.controller;

import com.hlw.pojo.Equipment;
import com.hlw.pojo.Material;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.AdministratorService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AdministratorController 类用于处理管理员相关的 HTTP 请求。
 */
@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    /**
     * 获取待招标项目数量
     *
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含待招标项目数量的 Result 对象。
     */
    @GetMapping("/biddingNum")
    public Result getBiddingsNum(HttpServletRequest request) {
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
        return Result.success(administratorService.getBiddingsNum());
    }

    /**
     * 获取员工数量
     *
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含员工数量的 Result 对象。
     */
    @GetMapping("/employee")
    public Result getEmployeeNum(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        return Result.success(administratorService.getEmployeeNum());
    }

    /**
     * 获取新增员工数量
     *
     * @param employee 包含年份和月份信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含新增员工数量的 Result 对象。
     */
    @PostMapping("/newEmployee")
    public Result getNewEmployeeNum(@RequestBody String employee, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        JsonUtils jsonUtils = new JsonUtils();
        String year = JsonUtils.getValueFromJson(employee, "year");
        String month = JsonUtils.getValueFromJson(employee, "month");
        int num = administratorService.getNewEmployeeNum(Integer.parseInt(year), Integer.parseInt(month));
        return Result.success(num);
    }

    /**
     * 更新项目信息
     *
     * @param projects 包含项目信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示更新操作结果的 Result 对象。
     */
    @PostMapping("/updateProjects")
    public Result updateProjects(@RequestBody String projects, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        JsonUtils jsonUtils = new JsonUtils();
        String projectId = JsonUtils.getValueFromJson(projects, "projectId");
        String projectName = JsonUtils.getValueFromJson(projects, "projectName");
        String mangerName = JsonUtils.getValueFromJson(projects, "managerName");
        String startDate = JsonUtils.getValueFromJson(projects, "plannedStartDate");
        String endDate = JsonUtils.getValueFromJson(projects, "plannedEndDate");
        String budget = JsonUtils.getValueFromJson(projects, "budget");
        String status = JsonUtils.getValueFromJson(projects, "status");
        String description = JsonUtils.getValueFromJson(projects, "description");
        String projectType = JsonUtils.getValueFromJson(projects, "projectType");
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析字符串为 LocalDate
        LocalDate localstartDate = LocalDate.parse(startDate, formatter);
        LocalDate localendDate = LocalDate.parse(endDate, formatter);
        administratorService.updateProjects(Integer.parseInt(projectId), projectName, mangerName, localstartDate, localendDate, Double.parseDouble(budget), status, description, projectType);
        return Result.success();
    }

    /**
     * 删除项目信息
     *
     * @param project 包含项目 ID 信息的 JSON 字符串。
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示删除操作结果的 Result 对象。
     */
    @PostMapping("/deleteProject")
    public Result deleteProject(@RequestBody String project, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        JsonUtils jsonUtils = new JsonUtils();
        String projectId = JsonUtils.getValueFromJson(project, "projectId");
        administratorService.deleteProject(Integer.parseInt(projectId));
        return Result.success();
    }

    /**
     * 获取新员工数量
     *
     * @param employee 包含年份和月份信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含新员工数量的 Result 对象。
     */
    @PostMapping("/newTender")
    public Result getNewTenderNum(@RequestBody String employee, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        JsonUtils jsonUtils = new JsonUtils();
        String year = JsonUtils.getValueFromJson(employee, "year");
        String month = JsonUtils.getValueFromJson(employee, "month");
        int num = administratorService.getNewTenderNum(Integer.parseInt(year), Integer.parseInt(month));
        return Result.success(num);
    }

    /**
     * 更新员工信息
     *
     * @param employeeId 需要更新的员工 ID。
     * @param updatedEmployee 更新后的员工信息。
     * @return 返回表示更新操作结果的 Result 对象。
     */
    @PutMapping("/updateEmployee/{employeeId}")
    public Result updateEmployee(@PathVariable("employeeId") int employeeId, @RequestBody User updatedEmployee) {
        boolean success = administratorService.updateEmployeeInfo(employeeId, updatedEmployee);
        if (success) {
            return Result.success("Employee updated successfully");
        } else {
            return Result.error("Failed to update employee");
        }
    }

    /**
     * 删除员工信息
     *
     * @param employeeId 需要删除的员工 ID。
     * @return 返回表示删除操作结果的 Result 对象。
     */
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public Result deleteEmployee(@PathVariable("employeeId") int employeeId) {
        boolean success = administratorService.deleteEmployeeById(employeeId);
        if (success) {
            return Result.success("Employee deleted successfully");
        } else {
            return Result.error("Failed to delete employee");
        }
    }

    /**
     * 添加新员工
     *
     * @param newEmployee 新员工的信息。
     * @return 返回表示添加操作结果的 Result 对象。
     */
    @PostMapping("/createEmployee")
    public Result addEmployee(@RequestBody User newEmployee) {
        boolean success = administratorService.addEmployee(newEmployee);
        if (success) {
            return Result.success("Employee added successfully");
        } else {
            return Result.error("Failed to add employee");
        }
    }

    /**
     * 获取所有员工信息
     *
     * @return 返回包含所有员工信息的 Result 对象。
     */
    @GetMapping("/employees")
    public Result getAllEmployees() {
        List<User> employees = administratorService.getAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            return Result.success(employees);
        } else {
            return Result.error("No employees found");
        }
    }

    /**
     * 添加材料入库
     *
     * @param material 包含材料信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示添加操作结果的 Result 对象。
     */
    @PostMapping("/materialStorage")
    public Result addMaterialStorage(@RequestBody String material, HttpServletRequest request) {
        JsonUtils jsonUtils = new JsonUtils();
        String materialName = JsonUtils.getValueFromJson(material, "materialName");
        String quantity = JsonUtils.getValueFromJson(material, "quantity");
        String entryDate = JsonUtils.getValueFromJson(material, "entryDate");
        String supplierName = JsonUtils.getValueFromJson(material, "supplierName");
        String price = JsonUtils.getValueFromJson(material, "price");
        String remarks = JsonUtils.getValueFromJson(material, "remarks");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(entryDate, formatter);
        return administratorService.addMaterialStorage(materialName, Integer.parseInt(quantity), localDate, supplierName, Integer.parseInt(price), remarks);
    }

    /**
     * 获取父节点
     *
     * @param projectId 项目 ID。
     * @return 返回包含父节点信息的 Result 对象。
     */
    @GetMapping("/topLevelNodes/{projectId}")
    public Result getParentNodeByProjectId(@PathVariable int projectId) {
        ProjectNode parentNode = administratorService.getParentNodeByProjectId(projectId);

        if (parentNode == null) {
            return Result.error("No parent node found for the given project ID.");
        }

        return Result.success(parentNode);
    }

    /**
     * 获取所有材料
     *
     * @return 返回包含所有材料信息的列表。
     */
    @GetMapping("/getAllMaterial")
    public List<Material> getAllMaterials() {
        return administratorService.getAllMaterials();
    }

    //获取项目及对应节点数量
    @PostMapping("/nodes/count")
    public Result getNodeCountByStatus(@RequestBody String project) {
        JsonUtils jsonUtils = new JsonUtils();
        String project_id= jsonUtils.getValueFromJson(project, "project_id");
        ProjectNode.NodeStatus status = ProjectNode.NodeStatus.valueOf(jsonUtils.getValueFromJson(project, "status"));

        int count = administratorService.getNodeCountByStatus(Integer.parseInt(project_id),status );
        return Result.success(count);
    }
    // 添加设备
    @PostMapping("/createequipment")
    public Result addEquipment(@RequestBody Equipment newEquipment) {
        boolean success = administratorService.addEquipment(newEquipment);
        if (success) {
            return Result.success("设备添加成功");
        } else {
            return Result.error("设备添加失败");
        }
    }
}
