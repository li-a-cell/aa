package com.hlw.dao;

import com.hlw.pojo.ProjectNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    //获取新入职员工数量
    @Select("SELECT COUNT(*) FROM tenderrecord WHERE YEAR(request_date)=#{year} AND MONTH(request_date)=#{month}")
    int getNewTenderNum(int year, int month);
}