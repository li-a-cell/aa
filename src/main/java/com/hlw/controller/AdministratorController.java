package com.hlw.controller;


import com.hlw.pojo.Result;
import com.hlw.service.AdministratorService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;
    // 获取待招标项目数量
    @GetMapping ("/biddingnum")
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

    // 获取新增员工数量
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

    @PostMapping("/updateprojects")
    public Result updateProjects(@RequestBody String projects, HttpServletRequest request) {
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
        String project_id = jsonUtils.getValueFromJson(projects, "project_id");
        String project_name = jsonUtils.getValueFromJson(projects, "project_name");
        String manger_name = jsonUtils.getValueFromJson(projects, "manager_name");
        String start_date = jsonUtils.getValueFromJson(projects, "planned_start_date");
        String end_date = jsonUtils.getValueFromJson(projects, "planned_end_date");
        String budget = jsonUtils.getValueFromJson(projects, "budget");
        String status = jsonUtils.getValueFromJson(projects, "status");
        String description = jsonUtils.getValueFromJson(projects, "description");
        String project_type = jsonUtils.getValueFromJson(projects, "project_type");
        administratorService.updateProjects(employeeId, project_id, project_name, manger_name, start_date, end_date, budget, status, description, project_type);
        return Result.success();
    }

}
