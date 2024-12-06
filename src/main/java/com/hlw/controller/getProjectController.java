package com.hlw.controller;

import com.hlw.dto.ProjectDto;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 项目控制器类，用于处理与项目相关的HTTP请求
 */
@RestController
@RequestMapping("/project")
public class getProjectController {

    @Autowired
    private ProjectService projectservice;

    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);
    /**
     * 获取正在进行的项目
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回正在进行的项目列表
     */
    @GetMapping("/onGoing")
    public Result getOngoingProjects(HttpServletRequest request) {
        // 从请求中获取员工ID
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

        // 确保用户是项目经理
        if (projectservice.isProjectManager(employeeId)) {
            try {
                // 获取项目状态为“onGoing”的项目
                List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "onGoing");
                return Result.success(projects);
            } catch (Exception e) {
                logger.error("Error fetching ongoing projects for employeeId: {}", employeeId, e);
                return Result.error("系统异常，无法获取进行中的项目");
            }
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
        // 从请求中获取员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 确保用户是项目经理
        if (projectservice.isProjectManager(employeeId)) {
            try {
                // 获取项目状态为“completed”的项目
                List<ProjectDto> projects = projectservice.getProjectsByStatus(employeeId, "completed");
                return Result.success(projects);
            } catch (Exception e) {
                logger.error("Error fetching completed projects for employeeId: {}", employeeId, e);
                return Result.error("系统异常，无法获取已完成的项目");
            }
        } else {
            return Result.error("你不是项目经理");
        }
    }

    // 提取员工ID的方法
    private int extractEmployeeId(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return -1;  // 表示员工ID缺失
        }

        try {
            return Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return -1;  // 返回-1表示无效的员工ID格式
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
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 检查 projectNode 是否有效
        if (projectNode == null || projectNode.getProjectId() == null || projectNode.getStatus() == null) {
            return Result.error("Invalid request data: projectId or status is missing");
        }

        try {
            // 获取项目节点列表
            List<ProjectNode> projectNodes = projectservice.getProjectNodes(projectNode.getProjectId(), projectNode.getStatus());
            return Result.success(projectNodes);
        } catch (Exception e) {
            // 记录错误日志
            logger.error("Error fetching project nodes for projectId: {}, status: {}",
                    projectNode.getProjectId(), projectNode.getStatus(), e);
            return Result.error("系统异常，无法获取项目节点");
        }
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
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 检查 projectDto 是否有效
        if (projectDto == null || projectDto.getStatus() == null) {
            return Result.error("Invalid request data: status is missing");
        }

        try {
            // 获取项目数量
            int num = projectservice.getProjectsNum(projectDto.getStatus());
            return Result.success(num);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching project count for status: {}", projectDto.getStatus(), e);
            return Result.error("系统异常，无法获取项目数量");
        }
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
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 校验 projectDto 是否有效
        if (projectDto == null || projectDto.getProjectType() == null) {
            return Result.error("Invalid request data: projectType is missing");
        }

        try {
            // 获取按项目类型统计的项目数量
            int num = projectservice.getProjectsNumByType(String.valueOf(projectDto.getProjectType()));
            return Result.success(num);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching project count by type: {}", projectDto.getProjectType(), e);
            return Result.error("系统异常，无法获取项目数量");
        }
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
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 校验 projectDto 是否有效
        if (projectDto == null || projectDto.getProjectType() == null) {
            return Result.error("Invalid request data: projectType is missing");
        }

        try {
            // 获取按项目类型统计的项目成本
            double cost = projectservice.getProjectsCostNumByType(String.valueOf(projectDto.getProjectType()));
            return Result.success(cost);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching project cost by type: {}", projectDto.getProjectType(), e);
            return Result.error("系统异常，无法获取项目成本");
        }
    }


    /**
     * 获取所有项目
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回所有项目列表
     */
    @GetMapping("/all")
    public Result getAllProjects(HttpServletRequest request) {
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        try {
            // 获取所有项目
            List<ProjectDto> projects = projectservice.getAllProjects();
            return Result.success(projects);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching all projects", e);
            return Result.error("系统异常，无法获取项目列表");
        }
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
        // 提取并验证员工ID
        int employeeId = extractEmployeeId(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        // 检查传入的ProjectDto是否有效
        if (projectDto == null || projectDto.getStatus() == null) {
            return Result.error("Invalid project status provided");
        }

        try {
            // 根据项目状态获取项目列表
            List<ProjectDto> projects = projectservice.getProjectsByStatus(projectDto.getStatus());
            return Result.success(projects);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching projects by status", e);
            return Result.error("系统异常，无法获取项目列表");
        }
    }
}
