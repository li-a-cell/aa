package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void updateProjects(int employeeId, String projectId, String projectName, String mangerName, String startDate, String endDate, String budget, String status, String description, String projectType) {
        administratorMapper.updateProjects(employeeId, projectId, projectName, mangerName, startDate, endDate, budget, status, description, projectType);
    }
}
