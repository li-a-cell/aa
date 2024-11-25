package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public  int getNodeCountByStatus(int projectId, ProjectNode.NodeStatus status) {
        return administratorMapper.getNodeCountByStatus(projectId, status);
    }
    public int getNewEmployeeNum(int year, int month) {
        return administratorMapper.getNewEmployeeNum(year, month);
    }
    public int getNewTenderNum(int year, int month) {
        return administratorMapper.getNewTenderNum(year, month);
    }
    // 通过 employee_id 修改员工信息
    public boolean updateEmployeeInfo(int employeeId, User updatedEmployee) {
        // 调用 DAO 层方法根据 employeeId 查找员工并进行更新
        return administratorMapper.updateEmployeeInfo(employeeId, updatedEmployee);
    }

    // 通过 employee_id 删除员工信息
    public boolean deleteEmployeeById(int employeeId) {
        return administratorMapper.deleteEmployeeById(employeeId);
    }

    // 添加新员工
    public boolean addEmployee(User newEmployee) {
        return administratorMapper.addEmployee(newEmployee);
    }
    // 获取所有员工信息
    public List<User> getAllEmployees() {
        return administratorMapper.getAllEmployees();
    }

}