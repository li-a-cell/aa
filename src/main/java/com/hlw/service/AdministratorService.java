package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.pojo.Equipment;
import com.hlw.pojo.Material;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 提供管理员相关服务
 */
@Service
public class AdministratorService {
    @Autowired
    private AdministratorMapper administratorMapper;

    /**
     * 获取当前招标的数量
     * @return 招标数量
     */
    public int getBiddingsNum() {
        return administratorMapper.getBiddingNum();
    }

    /**
     * 获取当前员工总数
     * @return 员工数量
     */
    public int getEmployeeNum() {
        return administratorMapper.getEmployeeNum();
    }

    /**
     * 获取指定年月的新员工数量
     * @param year 年份
     * @param month 月份
     * @return 新员工数量
     */
    public int getNewEmployeeNum(int year, int month) {
        return administratorMapper.getNewEmployeeNum(year, month);
    }


    /**
     * 更新项目信息
     * @param projectId 项目ID
     * @param projectName 项目名称
     * @param mangerName 项目经理名称
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param budget 预算
     * @param status 状态
     * @param description 描述
     * @param projectType 项目类型
     */
    public void updateProjects(int projectId, String projectName, String mangerName, LocalDate startDate, LocalDate endDate, Double budget, String status, String description, String projectType) {
        administratorMapper.updateProjects(projectId, projectName, mangerName, startDate, endDate, budget, status, description, projectType);
    }

    /**
     * 删除指定的项目
     * @param projectId 项目ID
     */
    public void deleteProject(int projectId) {
        administratorMapper.deleteProject(projectId);
    }

    /**
     * 根据项目ID和状态获取节点数量
     * @param projectId 项目ID
     * @param status 节点状态
     * @return 节点数量
     */
    public int getNodeCountByStatus(int projectId, ProjectNode.NodeStatus status) {
        return administratorMapper.getNodeCountByStatus(projectId, status);
    }

    /**
     * 获取指定年月的新招标数量
     * @param year 年份
     * @param month 月份
     * @return 新招标数量
     */
    public int getNewTenderNum(int year, int month) {
        return administratorMapper.getNewTenderNum(year, month);
    }

    /**
     * 更新员工信息
     * @param employeeId 员工ID
     * @param updatedEmployee 更新后的员工信息
     * @return 更新是否成功
     */
    public boolean updateEmployeeInfo(int employeeId, User updatedEmployee) {
        return administratorMapper.updateEmployeeInfo(employeeId, updatedEmployee);
    }

    /**
     * 删除指定ID的员工
     * @param employeeId 员工ID
     * @return 删除是否成功
     */
    public boolean deleteEmployeeById(int employeeId) {
        return administratorMapper.deleteEmployeeById(employeeId);
    }

    /**
     * 添加新员工
     * @param newEmployee 新员工信息
     * @return 添加是否成功
     */
    public boolean addEmployee(User newEmployee) {
        return administratorMapper.addEmployee(newEmployee);
    }

    /**
     * 获取所有员工信息
     * @return 员工列表
     */
    public List<User> getAllEmployees() {
        return administratorMapper.getAllEmployees();
    }

    /**
     * 添加材料入库记录
     * @param materialName 材料名称
     * @param quantity 数量
     * @param localDate 日期
     * @param supplierName 供应商名称
     * @param price 价格
     * @param remarks 备注
     * @return 添加结果
     */
    public Result addMaterialStorage(String materialName, int quantity, LocalDate localDate, String supplierName, int price, String remarks) {
        Integer materialId = administratorMapper.getMaterialIdByName(materialName);
        if (materialId == null) {
            administratorMapper.addNewMaterialIfNotExists(materialName, quantity);
        } else {
            administratorMapper.updateMaterialQuantity(materialName, quantity);
        }
        int inventoryId = administratorMapper.addMaterialStorage(materialName, quantity, localDate, supplierName, price, remarks);
        return inventoryId > 0 ? Result.success("材料入库成功") : Result.error("材料入库失败");
    }

    /**
     * 获取指定项目的父节点
     * @param projectId 项目ID
     * @return 父节点信息
     */
    public ProjectNode getParentNodeByProjectId(int projectId) {
        return administratorMapper.getParentNodeByProjectId(projectId);
    }

    /**
     * 获取所有材料信息
     * @return 材料列表
     */
    public List<Material> getAllMaterials() {
        return administratorMapper.getAllMaterials();
    }
    public boolean addEquipment(Equipment newEquipment) {
        return administratorMapper.addEquipment(newEquipment);
    }

    public List<Equipment> getAllEquipment() {
        return administratorMapper.getAllEquipment();
    }

    public Equipment getEquipment(int equipmentId) {
        return administratorMapper.getEquipment(equipmentId);
    }

    public void updateEquipment(Equipment equipment) {
        administratorMapper.updateEquipment(equipment);
    }

    public void deleteEquipment(int equipmentId) {
        administratorMapper.deleteEquipment(equipmentId);
    }
}
