package com.hlw.controller;


import com.hlw.pojo.Material;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.AdministratorService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;
    // 获取待招标项目数量
    @GetMapping ("/biddingNum")
    public Result getBiddingsNum(HttpServletRequest request){
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

    // 获取员工数量
    @GetMapping("/employee")
    public Result getEmployeeNum(HttpServletRequest request){
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

    // 获取新增员工数量
    @PostMapping("/newEmployee")
    public Result getNewEmployeeNum(@RequestBody String employee, HttpServletRequest request){
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
        String year = jsonUtils.getValueFromJson(employee, "year");
        String month = jsonUtils.getValueFromJson(employee, "month");
        int num=administratorService.getNewEmployeeNum(Integer.parseInt(year),Integer.parseInt(month));
        return Result.success(num);
    }

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
        String projectId = jsonUtils.getValueFromJson(projects, "projectId");
        String projectName = jsonUtils.getValueFromJson(projects, "projectName");
        String mangerName = jsonUtils.getValueFromJson(projects, "managerName");
        String startDate = jsonUtils.getValueFromJson(projects, "plannedStartDate");
        String endDate = jsonUtils.getValueFromJson(projects, "plannedEndDate");
        String budget = jsonUtils.getValueFromJson(projects, "budget");
        String status = jsonUtils.getValueFromJson(projects, "status");
        String description = jsonUtils.getValueFromJson(projects, "description");
        String projectType = jsonUtils.getValueFromJson(projects, "projectType");
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// 解析字符串为 LocalDate

        LocalDate localstartDate = LocalDate.parse(startDate, formatter);

        LocalDate localendDate = LocalDate.parse(endDate, formatter);
        administratorService.updateProjects(Integer.parseInt(projectId), projectName, mangerName, localstartDate, localendDate, Double.parseDouble(budget), status, description, projectType);
        return Result.success();
    }

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
        String projectId = jsonUtils.getValueFromJson(project, "projectId");
        administratorService.deleteProject(Integer.parseInt(projectId));
        return Result.success();
    }

    //获取新员工数量
    @PostMapping("/newTender")
    public Result getNewTenderNum(@RequestBody String employee, HttpServletRequest request){
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
        String year = jsonUtils.getValueFromJson(employee, "year");
        String month = jsonUtils.getValueFromJson(employee, "month");
        int num=administratorService.getNewTenderNum(Integer.parseInt(year),Integer.parseInt(month));
        return Result.success(num);
    }
    //更新员工信息
    @PutMapping("/updateEmployee/{employeeId}")
    public Result updateEmployee(@PathVariable("employeeId") int employeeId, @RequestBody User updatedEmployee) {
        boolean success = administratorService.updateEmployeeInfo(employeeId, updatedEmployee);
        if (success) {
            return Result.success("Employee updated successfully");
        } else {
            return Result.error("Failed to update employee");
        }
    }

    // 删除员工信息
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public Result deleteEmployee(@PathVariable("employeeId") int employeeId) {
        boolean success = administratorService.deleteEmployeeById(employeeId);
        if (success) {
            return Result.success("Employee deleted successfully");
        } else {
            return Result.error("Failed to delete employee");
        }
    }

    // 添加新员工
    @PostMapping("/createEmployee")
    public Result addEmployee(@RequestBody User newEmployee) {
        boolean success = administratorService.addEmployee(newEmployee);
        if (success) {
            return Result.success("Employee added successfully");
        } else {
            return Result.error("Failed to add employee");
        }
    }
    // 获取所有员工信息
    @GetMapping("/employees")
    public Result getAllEmployees() {
        List<User> employees = administratorService.getAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            return Result.success(employees);
        } else {
            return Result.error("No employees found");
        }
    }

    // 添加材料入库
    @PostMapping("/materialStorage")
    public Result addMaterialStorage(@RequestBody String material ,HttpServletRequest request) {
        JsonUtils jsonUtils = new JsonUtils();
        String materialName = jsonUtils.getValueFromJson(material, "materialName");
        String quantity = jsonUtils.getValueFromJson(material, "quantity");
        String entryDate = jsonUtils.getValueFromJson(material, "entryDate");
        String supplierName = jsonUtils.getValueFromJson(material, "supplierName");
        String price = jsonUtils.getValueFromJson(material, "price");
        String remarks = jsonUtils.getValueFromJson(material, "remarks");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(entryDate, formatter);
        return administratorService.addMaterialStorage(materialName, Integer.parseInt(quantity), localDate, supplierName, Integer.parseInt(price), remarks);

    }


    // 获取父节点
    @GetMapping("/topLevelNodes/{projectId}")
    public Result getParentNodeByProjectId(@PathVariable int projectId) {
        ProjectNode parentNode = administratorService.getParentNodeByProjectId(projectId);

        if (parentNode == null) {
            return Result.error("No parent node found for the given project ID.");
        }

        return Result.success(parentNode);
    }

    // 获取所有材料
    @GetMapping("/getAllMaterial")
    public List<Material> getAllMaterials() {
        return administratorService.getAllMaterials();
    }
}
