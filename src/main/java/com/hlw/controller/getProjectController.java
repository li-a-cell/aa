package com.hlw.controller;

import com.hlw.dto.ProjectDto;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 项目控制器类，用于处理与项目相关的HTTP请求
 */
@RestController
@RequestMapping("/project")
public class getProjectController {

    @Autowired
    private ProjectService projectservice;

    /**
     * 获取正在进行的项目
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回正在进行的项目列表
     */
    @GetMapping("/onGoing")
    public Result getOngoingProjects(HttpServletRequest request) {
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

        if (projectservice.isProjectManager(employeeId)) {
            List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "onGoing");
            return Result.success(projects);
        } else {
            return Result.error("你不是项目经理");
        }
    }

    /**
     * 获取已完成的项目
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回已完成的项目列表
     */
    @GetMapping("/completed")
    public Result getCompletedProjects(HttpServletRequest request) {
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

        if (projectservice.isProjectManager(employeeId)) {
            List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "completed");
            return Result.success(projects);
        } else {
            return Result.error("你不是项目经理");
        }
    }

    /**
     * 获取项目的对应状态节点的详细信息
     *
     * @param projectNode 包含项目ID和状态的项目节点对象
     * @param request     HTTP请求对象，用于获取员工ID
     * @return 返回符合条件的项目节点列表
     */
    @PostMapping("/nodes")
    public Result getProjectNodes(@RequestBody ProjectNode projectNode, HttpServletRequest request) {
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

        List<ProjectNode> projectNodes = projectservice.getProjectNodes(projectNode.getProjectId(), projectNode.getStatus());
        return Result.success(projectNodes);
    }

    /**
     * 获取某一状态项目的数量
     *
     * @param projectDto 包含项目状态的项目DTO对象
     * @param request    HTTP请求对象，用于获取员工ID
     * @return 返回符合条件的项目数量
     */
    @PostMapping("/projectNum")
    public Result getProjectsNum(@RequestBody ProjectDto projectDto, HttpServletRequest request) {
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
        int num = projectservice.getProjectsNum(projectDto.getStatus());
        return Result.success(num);
    }

    /**
     * 根据项目类型返回项目数量
     *
     * @param projectDto 包含项目类型的项目DTO对象
     * @param request    HTTP请求对象，用于获取员工ID
     * @return 返回符合条件的项目数量
     */
    @PostMapping("/numByType")
    public Result getProjectsNumByType(@RequestBody ProjectDto projectDto, HttpServletRequest request) {
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
        int num = projectservice.getProjectsNumByType(String.valueOf(projectDto.getProjectType()));
        return Result.success(num);
    }

    /**
     * 按类型查询项目金额
     *
     * @param projectDto 包含项目类型的项目DTO对象
     * @param request    HTTP请求对象，用于获取员工ID
     * @return 返回符合条件的项目总金额
     */
    @PostMapping("/costByType")
    public Result getProjectsCostNumByType(@RequestBody ProjectDto projectDto, HttpServletRequest request) {
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
        double cost = projectservice.getProjectsCostNumByType(String.valueOf(projectDto.getProjectType()));
        return Result.success(cost);
    }

    /**
     * 获取所有项目
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回所有项目列表
     */
    @GetMapping("/all")
    public Result getAllProjects(HttpServletRequest request) {
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
        List<ProjectDto> projects = projectservice.getAllProjects();
        return Result.success(projects);
    }

    /**
     * 根据状态获取项目
     *
     * @param projectDto 包含项目状态的项目DTO对象
     * @param request    HTTP请求对象，用于获取员工ID
     * @return 返回符合条件的项目列表
     */
    @PostMapping("/status")
    public Result getProjectsByStatus(@RequestBody ProjectDto projectDto, HttpServletRequest request) {
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

        return Result.success(projectservice.getProjectsByStatus(projectDto.getStatus()));
    }
}
