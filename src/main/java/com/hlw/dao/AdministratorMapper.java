package com.hlw.dao;

import com.hlw.pojo.Equipment;
import com.hlw.pojo.Material;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * AdministratorMapper接口定义了管理员相关操作的映射方法
 */
@Mapper
public interface AdministratorMapper {

    /**
     * 获取待招标项目数量
     *
     * @return 待招标项目的数量
     */
    @Select("SELECT COUNT(*) FROM project WHERE status='待招标'")
    int getBiddingNum();


    /**
     * 获取员工数量
     *
     * @return 员工总数量
     */
    @Select("SELECT COUNT(*) FROM employee ")
    int getEmployeeNum();


    /**
     * 获取某年某月新入职员工数量
     *
     * @param year  年份
     * @param month 月份
     * @return 指定年月新入职员工数量
     */
    @Select("SELECT COUNT(*) FROM employee WHERE YEAR(hire_date)=#{year} AND MONTH(hire_date)=#{month}")
    int getNewEmployeeNum(int year, int month);


    /**
     * 更新项目信息
     *
     * @param projectId    项目ID
     * @param projectName  项目名称
     * @param managerName  项目经理名称
     * @param startDate    计划开始日期
     * @param endDate      计划结束日期
     * @param budget       预算
     * @param status       项目状态
     * @param description  项目描述
     * @param projectType  项目类型
     */
    @Update("UPDATE project " +
            "SET project_name = #{projectName}, " +
            "manager_id = (SELECT employee_id FROM employee WHERE name = #{managerName}), " +
            "planned_start_date = #{startDate}, " +
            "planned_end_date = #{endDate}, " +
            "budget = #{budget}, " +
            "status = #{status}, " +
            "description = #{description}, " +
            "project_type = #{projectType} " +
            "WHERE project_id = #{projectId}")
    void updateProjects(@Param("projectId") int projectId,
                        @Param("projectName") String projectName,
                        @Param("managerName") String managerName,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("budget") Double budget,
                        @Param("status") String status,
                        @Param("description") String description,
                        @Param("projectType") String projectType);


    /**
     * 删除项目
     *
     * @param projectId 项目ID
     */
    @Delete("DELETE FROM project WHERE project_id = #{projectId} ON DELETE CASCADE")
    void deleteProject(int projectId);


    /**
     * 获取指定状态的节点数量
     *
     * @param projectId 项目ID
     * @param status    节点状态
     * @return 指定状态的节点数量
     */
    @Select("SELECT COUNT(*) FROM projectnode WHERE status = #{status} AND project_id=#{projectId}")
    int getNodeCountByStatus(@Param("projectId") int projectId, @Param("status") ProjectNode.NodeStatus status);


    /**
     * 获取某年某月新的招标数量
     *
     * @param year  年份
     * @param month 月份
     * @return 指定年月新的招标数量
     */
    @Select("SELECT COUNT(*) FROM tenderrecord WHERE YEAR(request_date)=#{year} AND MONTH(request_date)=#{month}")
    int getNewTenderNum(int year, int month);


    /**
     * 更新员工信息
     *
     * @param employeeId    员工ID
     * @param updatedEmployee 更新后的员工信息
     * @return 更新操作的执行结果
     */
    @Update("UPDATE employee SET account=#{updatedEmployee.account}, name=#{updatedEmployee.name}, job_type=#{updatedEmployee.jobType}, salary=#{updatedEmployee.salary}, hire_date=#{updatedEmployee.hireDate}, phone_number=#{updatedEmployee.phoneNumber}, password=#{updatedEmployee.password}, gender=#{updatedEmployee.gender}, birth_date=#{updatedEmployee.birthDate}, address=#{updatedEmployee.address} WHERE employee_id=#{employeeId}")
    boolean updateEmployeeInfo(@Param("employeeId") int employeeId, @Param("updatedEmployee") User updatedEmployee);


    /**
     * 删除员工
     *
     * @param employeeId 员工ID
     * @return 删除操作的执行结果
     */
    @Delete("DELETE FROM employee WHERE employee_id=#{employeeId}")
    boolean deleteEmployeeById(int employeeId);


    /**
     * 添加新员工
     *
     * @param newEmployee 新员工信息
     * @return 添加操作的执行结果
     */
    @Insert("INSERT INTO employee (account, name, job_type, salary, hire_date, phone_number, password, gender, birth_date, address) " +
            "VALUES (#{newEmployee.account}, #{newEmployee.name}, #{newEmployee.jobType}, #{newEmployee.salary}, #{newEmployee.hireDate}, #{newEmployee.password}, #{newEmployee.profilePicture}, #{newEmployee.gender}, #{newEmployee.birthDate}, #{newEmployee.address})")
    boolean addEmployee(@Param("newEmployee") User newEmployee);


    /**
     * 获取所有员工信息
     *
     * @return 所有员工信息列表
     */
    @Select("SELECT * FROM employee")
    List<User> getAllEmployees();


    /**
     * 通过材料名称查询材料 ID 是否存在
     *
     * @param materialName 材料名称
     * @return 材料ID，如果不存在则返回null
     */
    @Select("SELECT material_id FROM material WHERE material_name = #{materialName} LIMIT 1")
    Integer getMaterialIdByName(@Param("materialName") String materialName);


    /**
     * 添加材料入库记录，并在材料表中插入相应材料（如果不存在）
     *
     * @param materialName 材料名称
     * @param quantity     入库数量
     */
    @Insert("INSERT INTO material (material_name, current_stock_quantity) " +
            "SELECT #{materialName}, #{quantity} " +
            "WHERE NOT EXISTS (SELECT 1 FROM material WHERE material_name = #{materialName})")
    void addNewMaterialIfNotExists(@Param("materialName") String materialName,
                                   @Param("quantity") int quantity);


    /**
     * 更新现有材料的数量
     *
     * @param materialName 材料名称
     * @param quantity     变更的数量
     */
    @Update("UPDATE material " +
            "SET current_stock_quantity = current_stock_quantity + #{quantity} " +
            "WHERE material_name = #{materialName}")
    void updateMaterialQuantity(@Param("materialName") String materialName,
                                @Param("quantity") int quantity);


    /**
     * 添加材料入库记录
     *
     * @param materialName 材料名称
     * @param quantity     入库数量
     * @param localDate    入库日期
     * @param supplierName 供应商名称
     * @param price        价格
     * @param remarks      备注
     * @return 添加操作的执行结果
     */
    @Insert("INSERT INTO material_inventory (material_id, quantity, entry_date, supplier_name, price, remarks) " +
            "VALUES ((SELECT material_id FROM material WHERE material_name = #{materialName}), " +
            "#{quantity}, #{localDate}, #{supplierName}, #{price}, #{remarks})")
    int addMaterialStorage(@Param("materialName") String materialName,
                           @Param("quantity") int quantity,
                           @Param("localDate") LocalDate localDate,
                           @Param("supplierName") String supplierName,
                           @Param("price") int price,
                           @Param("remarks") String remarks);


    /**
     * 获取父节点
     *
     * @param projectId 项目ID
     * @return 项目对应的父节点
     */
    @Select("SELECT * FROM projectnode WHERE project_id = #{projectId} AND parent_node_id IS NULL")
    ProjectNode getParentNodeByProjectId(@Param("projectId") int projectId);


    /**
     * 查询所有材料信息
     *
     * @return 所有材料信息列表
     */
    @Select("SELECT * FROM material")
    List<Material> getAllMaterials();

    // 插入新设备
    @Insert("INSERT INTO equipment (equipment_name, equipment_photo, equipment_type, equipment_model, status,  remarks) " +
            "VALUES (#{newEquipment.equipmentName}, #{newEquipment.equipmentPhoto}, #{newEquipment.equipmentType}, #{newEquipment.equipmentModel}, " +
            "#{newEquipment.status}, #{newEquipment.remarks})")
    boolean addEquipment(@Param("newEquipment") Equipment newEquipment);

    @Select("SELECT * FROM equipment")
    List<Equipment> getAllEquipment();

    @Select("SELECT * FROM equipment WHERE equipment_name = #{equipmentName}")
    Equipment getEquipment(int equipmentId);

    @Update("UPDATE equipment SET equipment_name = #{equipment.equipmentName}, equipment_photo = #{equipment.equipmentPhoto}, " +
            "equipment_type = #{equipment.equipmentType}, equipment_model = #{equipment.equipmentModel}, status = #{equipment.status}, " +
            "remarks = #{equipment.remarks} WHERE equipment_id = #{equipment.equipmentId}")
    void updateEquipment(Equipment equipment);

    @Delete("DELETE FROM equipment WHERE equipment_id = #{equipmentId}")
    void deleteEquipment(int equipmentId);
}
