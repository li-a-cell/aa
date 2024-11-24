package com.hlw.controller;

import com.hlw.dto.ProjectDto;
import com.hlw.pojo.Project;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


@RestController
@RequestMapping("/project")
public class GetprojectController {

    @Autowired
    private ProjectService projectservice;

    // 获取正在进行的项目
    @GetMapping("/ongoing")
    public Result getOngoingProjects(HttpServletRequest request) {
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

        if (projectservice.isProjectManager(employeeId)) {
            List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "ongoing");
            return Result.success(projects);
        } else {
            return Result.error("你不是项目经理");
        }
    }

    // 获取已完成的项目
    @GetMapping("/completed")
    public Result getCompletedProjects(HttpServletRequest request) {
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

        if (projectservice.isProjectManager(employeeId)) {
            List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "completed");
            return Result.success(projects);
        } else {
            return Result.error("你不是项目经理");
        }
    }
     // 获取项目的对应状态节点的详细信息
     @GetMapping("/nodes")
    public Result getProjectNodes(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
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

        List<ProjectNode> projectNodes=projectservice.getProjectNodes(projectNode.getProject_id(),projectNode.getStatus());
        return Result.success(projectNodes);
    }
    //获取某一状态项目的数量
      @GetMapping("/all")
    public Result getProjectsNum(@RequestBody ProjectDto projectDto, HttpServletRequest request) {
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
        int num = projectservice.getProjectsNum(projectDto.getStatus());
        return Result.success(num);
    }
}
