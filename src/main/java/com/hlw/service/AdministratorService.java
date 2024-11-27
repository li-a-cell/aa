package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.pojo.Equipment;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public void updateProjects(int projectId, String projectName, String mangerName, LocalDate startDate, LocalDate endDate, Double budget, String status, String description, String projectType) {
        administratorMapper.updateProjects( projectId, projectName, mangerName, startDate, endDate, budget, status, description, projectType);
    }

    public void deleteProject(int project_id) {
        administratorMapper.deleteProject(project_id);
    }
    public boolean addEquipment(Equipment newEquipment) {
        return administratorMapper.addEquipment(newEquipment);
    }
    // 添加材料入库记录
    public Result addMaterialStorage(String materialName, int quantity, LocalDate localDate, String supplierName, int price, String remarks) {
        Integer materialId = administratorMapper.getMaterialIdByName(materialName);
        if (materialId == null) {
            administratorMapper.addNewMaterialIfNotExists(materialName, quantity);
        }
        else {
            administratorMapper.updateMaterialQuantity(materialName, quantity);
        }
        int inventoryId = administratorMapper.addMaterialStorage(materialName, quantity, localDate, supplierName, price, remarks);
        return inventoryId > 0 ? Result.success("材料入库成功") : Result.error("材料入库失败");
    }
   // 获取父节点
    public List<ProjectNode> getParentNodeByProjectId(int projectId) {
        return administratorMapper.getParentNodeByProjectId(projectId);
    }

}