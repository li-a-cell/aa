package com.hlw.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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




    void updateProjects(int employeeId, String projectId, String projectName, String mangerName, String startDate, String endDate, String budget, String status, String description, String projectType);
}
