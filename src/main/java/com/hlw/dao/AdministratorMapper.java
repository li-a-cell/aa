package com.hlw.dao;

import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AdministratorMapper {
    // 获取待招标项目数量
    @Select("SELECT COUNT(*) FROM project WHERE status='待招标'")
    int getBiddingsNum();


    // 获取员工数量
    @Select("SELECT COUNT(*) FROM employee ")
    int getEmployeeNum();

    // 获取指定状态的节点数量
    @Select("SELECT COUNT(*) FROM projectnode WHERE status = #{status} AND project_id=#{project_id}")
    int getNodeCountByStatus(@Param("project_id") int project_id, @Param("status") ProjectNode.NodeStatus status);
    //获取新入职员工数量
    @Select("SELECT COUNT(*) FROM employee WHERE YEAR(hire_date)=#{year} AND MONTH(hire_date)=#{month}")
    int getNewEmployeeNum(int year, int month);
    //获取新招标数量
    @Select("SELECT COUNT(*) FROM tenderrecord WHERE YEAR(request_date)=#{year} AND MONTH(request_date)=#{month}")
    int getNewTenderNum(int year, int month);
    // 更新员工信息
    @Update("UPDATE employee SET account=#{updatedEmployee.account}, name=#{updatedEmployee.name}, job_type=#{updatedEmployee.job_type}, salary=#{updatedEmployee.salary}, hire_date=#{updatedEmployee.hire_date}, phone_number=#{updatedEmployee.phone_number}, profile_picture=#{updatedEmployee.profile_picture}, gender=#{updatedEmployee.gender}, birth_date=#{updatedEmployee.birth_date}, address=#{updatedEmployee.address} WHERE employee_id=#{employeeId}")
    boolean updateEmployeeInfo(@Param("employeeId") int employeeId, @Param("updatedEmployee") User updatedEmployee);

    // 删除员工
    @Delete("DELETE FROM employee WHERE employee_id=#{employeeId}")
    boolean deleteEmployeeById(int employeeId);

    // 添加新员工
    @Insert("INSERT INTO employee (account, name, job_type, salary, hire_date, phone_number, profile_picture, gender, birth_date, address) " +
            "VALUES (#{newEmployee.account}, #{newEmployee.name}, #{newEmployee.job_type}, #{newEmployee.salary}, #{newEmployee.hire_date}, #{newEmployee.phone_number}, #{newEmployee.profile_picture}, #{newEmployee.gender}, #{newEmployee.birth_date}, #{newEmployee.address})")
    boolean addEmployee(@Param("newEmployee") User newEmployee);
    // 获取所有员工信息
    @Select("SELECT * FROM employee")
    List<User> getAllEmployees();
    // 更新项目信息
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

// 删除项目
    @Delete("DELETE FROM project WHERE project_id = #{projectId}")
    void deleteProject(int projectId);
}