package com.hlw.dao;

import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface AdministratorMapper {
    // 获取待招标项目数量
    @Select("SELECT COUNT(*) FROM project WHERE status='待招标'")
    int getBiddingsNum();


    // 获取员工数量
    @Select("SELECT COUNT(*) FROM employee ")
    int getEmployeeNum();



    @Select("SELECT COUNT(*) FROM employee WHERE YEAR(hire_date)=#{year} AND MONTH(hire_date)=#{month}")
    int getNewEmployeeNum(int year, int month);




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


    @Delete("DELETE FROM project WHERE project_id = #{projectId}")
    void deleteProject(int projectId);
}
