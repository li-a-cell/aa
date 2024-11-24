package com.hlw.service;

import com.hlw.dao.ProjectMapper;
import com.hlw.dto.ProjectDto;
import com.hlw.pojo.ProjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectmapper;

    // 判断员工是否是项目经理，并返回他管理的项目
    public boolean isProjectManager(int employeeId) {
        List<ProjectDto> projects = projectmapper.findProjectsByManagerId(employeeId);
        return !projects.isEmpty();
    }

    // 获取某个项目经理管理的所有项目
    public List<ProjectDto> getProjectsByManagerId(int employeeId) {
        return projectmapper.findProjectsByManagerId(employeeId);
    }

    // 获取某个项目经理管理的所有项目，根据状态过滤
    public List<ProjectDto> getProjectsByStatus(int employeeId, String status) {
        if ("ongoing".equalsIgnoreCase(status)) {
            // 正在进行的项目
            return projectmapper.findOngoingProjectsByManagerId(employeeId);
        } else if ("completed".equalsIgnoreCase(status)) {
            // 已完成的项目
            return projectmapper.findCompletedProjectsByManagerId(employeeId);
        } else {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    // 获取某个项目节点
    public List<ProjectNode> getProjectNodes(int projectId, ProjectNode.NodeStatus status) {
        return projectmapper.getProjectNodes(projectId,status);
    }

    public int getProjectsNum(String status) {
        return projectmapper.getProjectsNum(status);
    }

    public List<ProjectDto> getAllProjects() {
        return projectmapper.findAllProjects();
    }

    // 按类型查询项目数量
    public int getProjectsNumByType(String project_type) {
        return projectmapper.getProjectsNumByType(project_type);
    }
    // 按类型查询项目金额
    public double getProjectsCostNumByType(String project_type) {
        return projectmapper.getProjectsCostNumByType(project_type);
    }
}
