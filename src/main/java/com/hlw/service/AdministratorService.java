package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorMapper administratorMapper;

    // 获取招标数量
    public int getBiddingsNum() {
        return administratorMapper.getBiddingsNum();
    }

    // 获取员工数量
    public int getEmployeeNum() {
        return administratorMapper.getEmployeeNum();
    }

    public int getNewEmployeeNum(int year, int month) {
        return administratorMapper.getNewEmployeeNum(year, month);
    }

    public void updateProjects(int projectId, String projectName, String mangerName, LocalDate startDate, LocalDate endDate, Double budget, String status, String description, String projectType) {
        administratorMapper.updateProjects( projectId, projectName, mangerName, startDate, endDate, budget, status, description, projectType);
    }

    public void deleteProject(int project_id) {
        administratorMapper.deleteProject(project_id);
    }
}
