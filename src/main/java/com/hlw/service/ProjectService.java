package com.hlw.service;

import com.hlw.dao.ProjectMapper;
import com.hlw.dto.ProjectDetailsView;
import com.hlw.dto.ProjectDto;
import com.hlw.pojo.Project;
import com.hlw.pojo.ProjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    // 判断员工是否是项目经理，并返回他管理的项目
    public boolean isProjectManager(int employeeId) {
        List<ProjectDto> projects = projectMapper.findProjectsByManagerId(employeeId);
        return !projects.isEmpty();
    }

    // 获取某个项目经理管理的所有项目
    public List<ProjectDto> getProjectsByManagerId(int employeeId) {
        return projectMapper.findProjectsByManagerId(employeeId);
    }

    // 获取某个项目经理管理的所有项目，根据状态过滤
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

    // 获取某个项目节点
    public List<ProjectNode> getProjectNodes(int projectId, ProjectNode.NodeStatus status) {
        return projectMapper.getProjectNodes(projectId,status);
    }

    public int getProjectsNum(String status) {
        return projectMapper.getProjectsNum(status);
    }

    public List<ProjectDto> getAllProjects() {
        return projectMapper.findAllProjects();
    }

    // 按类型查询项目数量
    public int getProjectsNumByType(String projectType) {
        return projectMapper.getProjectsNumByType(projectType);
    }
    // 按类型查询项目金额
    public double getProjectsCostNumByType(String projectType) {
        return projectMapper.getProjectsCostNumByType(projectType);
    }

    public ProjectDetailsView findProjectById(Integer projectId) {
        return projectMapper.findProjectById(projectId);
    }


    public List<ProjectDto> getProjectsByStatus( String status) {
        return projectMapper.findProjectsByStatus(status);
    }
}
