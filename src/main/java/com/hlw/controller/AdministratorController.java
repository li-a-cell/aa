package com.hlw.controller;


import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import com.hlw.service.AdministratorService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;
    // 获取待招标项目数量
    @GetMapping("/biddingnum")
    public Result getBiddingsNum(HttpServletRequest request){
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
        return Result.success(administratorService.getBiddingsNum());
    }

    // 获取员工数量
    @GetMapping("/employee")
    public Result getEmployeeNum(HttpServletRequest request){
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
        return Result.success(administratorService.getEmployeeNum());
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
    //获取新入职员工数量
    @PostMapping("/newemployee")
    public Result getNewEmployeeNum(@RequestBody String employee, HttpServletRequest request){
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
        JsonUtils jsonUtils = new JsonUtils();
        String year = jsonUtils.getValueFromJson(employee, "year");
        String month = jsonUtils.getValueFromJson(employee, "month");
        int num=administratorService.getNewEmployeeNum(Integer.parseInt(year),Integer.parseInt(month));
        return Result.success(num);
    }
    //获取新招标项目数量
    @PostMapping("/newtender")
    public Result getNewTenderNum(@RequestBody String employee, HttpServletRequest request){
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
        JsonUtils jsonUtils = new JsonUtils();
        String year = jsonUtils.getValueFromJson(employee, "year");
        String month = jsonUtils.getValueFromJson(employee, "month");
        int num=administratorService.getNewTenderNum(Integer.parseInt(year),Integer.parseInt(month));
        return Result.success(num);
    }
    //更新员工信息
    @PutMapping("/updateemployee/{employeeId}")
    public Result updateEmployee(@PathVariable("employeeId") int employeeId, @RequestBody User updatedEmployee) {
        boolean success = administratorService.updateEmployeeInfo(employeeId, updatedEmployee);
        if (success) {
            return Result.success("Employee updated successfully");
        } else {
            return Result.error("Failed to update employee");
        }
    }

    // 删除员工信息
    @DeleteMapping("/deleteemployee/{employeeId}")
    public Result deleteEmployee(@PathVariable("employeeId") int employeeId) {
        boolean success = administratorService.deleteEmployeeById(employeeId);
        if (success) {
            return Result.success("Employee deleted successfully");
        } else {
            return Result.error("Failed to delete employee");
        }
    }

    // 添加新员工
    @PostMapping("/createemployee")
    public Result addEmployee(@RequestBody User newEmployee) {
        boolean success = administratorService.addEmployee(newEmployee);
        if (success) {
            return Result.success("Employee added successfully");
        } else {
            return Result.error("Failed to add employee");
        }
    }
    @GetMapping("/employees")
    public Result getAllEmployees() {
        List<User> employees = administratorService.getAllEmployees();
        if (employees != null && !employees.isEmpty()) {
            return Result.success(employees);
        } else {
            return Result.error("No employees found");
        }
    }

}