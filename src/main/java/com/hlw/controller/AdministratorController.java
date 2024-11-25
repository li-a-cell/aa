package com.hlw.controller;


import com.hlw.pojo.ProjectNode;
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
}