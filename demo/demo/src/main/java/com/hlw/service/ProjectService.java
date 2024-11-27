package com.hlw.service;

import com.hlw.dao.ProjectMapper;
import com.hlw.pojo.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectmapper;

    // 判断员工是否是项目经理，并返回他管理的项目
    public boolean isProjectManager(int employeeId) {
        List<Project> projects = projectmapper.findProjectsByManagerId(employeeId);
        return !projects.isEmpty();
    }

    // 获取某个项目经理管理的所有项目
    public List<Project> getProjectsByManagerId(int employeeId) {
        return projectmapper.findProjectsByManagerId(employeeId);
    }

    // 获取某个项目经理管理的所有项目，根据状态过滤
    public List<Project> getProjectsByStatus(int employeeId, String status) {
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
}