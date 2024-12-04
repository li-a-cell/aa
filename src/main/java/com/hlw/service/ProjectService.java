package com.hlw.service;

import com.hlw.dao.ProjectMapper;
import com.hlw.dto.ProjectDetailsView;
import com.hlw.dto.ProjectDto;
import com.hlw.pojo.Project;
import com.hlw.pojo.ProjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目服务类，提供项目相关的业务逻辑处理
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 判断员工是否是项目经理，并返回他管理的项目
     * @param employeeId 员工ID
     * @return 如果员工是项目经理且管理至少一个项目，则返回true；否则返回false
     */
    public boolean isProjectManager(int employeeId) {
        List<ProjectDto> projects = projectMapper.findProjectsByManagerId(employeeId);
        return !projects.isEmpty();
    }

    /**
     * 获取某个项目经理管理的所有项目
     * @param employeeId 项目经理的员工ID
     * @return 项目经理管理的项目列表
     */
    public List<ProjectDto> getProjectsByManagerId(int employeeId) {
        return projectMapper.findProjectsByManagerId(employeeId);
    }

    /**
     * 获取某个项目经理管理的所有项目，根据状态过滤
     * @param employeeId 项目经理的员工ID
     * @param status 项目状态（"ongoing"进行中，"completed"已完成）
     * @return 根据状态过滤的项目经理管理的项目列表
     * @throws IllegalArgumentException 如果状态参数无效，则抛出此异常
     */
    public List<ProjectDto> getProjectsByStatus(int employeeId, String status) {
        if ("ongoing".equalsIgnoreCase(status)) {
            // 正在进行的项目
            return projectMapper.findOngoingProjectsByManagerId(employeeId);
        } else if ("completed".equalsIgnoreCase(status)) {
            // 已完成的项目
            return projectMapper.findCompletedProjectsByManagerId(employeeId);
        } else {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    /**
     * 获取某个项目节点
     * @param projectId 项目ID
     * @param status 节点状态
     * @return 项目节点列表
     */
    public List<ProjectNode> getProjectNodes(int projectId, ProjectNode.NodeStatus status) {
        return projectMapper.getProjectNodes(projectId,status);
    }

    /**
     * 获取符合指定状态的项目数量
     * @param status 项目状态
     * @return 项目数量
     */
    public int getProjectsNum(String status) {
        return projectMapper.getProjectsNum(status);
    }

    /**
     * 获取所有项目列表
     * @return 项目列表
     */
    public List<ProjectDto> getAllProjects() {
        return projectMapper.findAllProjects();
    }

    /**
     * 按类型查询项目数量
     * @param projectType 项目类型
     * @return 项目数量
     */
    public int getProjectsNumByType(String projectType) {
        return projectMapper.getProjectsNumByType(projectType);
    }

    /**
     * 按类型查询项目金额
     * @param projectType 项目类型
     * @return 项目金额总数
     */
    public double getProjectsCostNumByType(String projectType) {
        return projectMapper.getProjectsCostNumByType(projectType);
    }

    /**
     * 根据项目ID查找项目详细信息
     * @param projectId 项目ID
     * @return 项目详细信息视图
     */
    public ProjectDetailsView findProjectById(Integer projectId) {
        return projectMapper.findProjectById(projectId);
    }

    /**
     * 根据状态获取项目列表
     * @param status 项目状态
     * @return 项目列表
     */
    public List<ProjectDto> getProjectsByStatus(String status) {
        return projectMapper.findProjectsByStatus(status);
    }
}
