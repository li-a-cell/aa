package com.hlw.dao;
import com.hlw.pojo.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import java.util.List;
@Mapper
public interface ProjectMapper {
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{manager_id}")
    @Results({
            @Result(property = "project_id", column = "project_id"),
            @Result(property = "manager_id", column = "manager_id"),
            @Result(property = "project_name", column = "project_name"),
            @Result(property = "planned_start_date", column = "planned_start_date"),
            @Result(property = "planned_end_date", column = "planned_end_date"),
            @Result(property = "site_id", column = "site_id"),
            @Result(property = "contractor_id", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "project_type", column = "project_type"),
            @Result(property = "site_name", column = "construction_site_name"),
            @Result(property = "bidder_name", column = "bidder_name_result")
    })
    List<Project> findProjectsByManagerId(int manager_id);
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{manager_id} AND p.status IN ('待发布', '待招标', '施工中')")
    @Results({
            @Result(property = "project_id", column = "project_id"),
            @Result(property = "manager_id", column = "manager_id"),
            @Result(property = "project_name", column = "project_name"),
            @Result(property = "planned_start_date", column = "planned_start_date"),
            @Result(property = "planned_end_date", column = "planned_end_date"),
            @Result(property = "site_id", column = "site_id"),
            @Result(property = "contractor_id", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "project_type", column = "project_type"),
            @Result(property = "site_name", column = "construction_site_name"),
            @Result(property = "bidder_name", column = "bidder_name_result")
    })
    List<Project> findOngoingProjectsByManagerId(int manager_id);
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{manager_id} AND p.status = '已完成'")
    @Results({
            @Result(property = "project_id", column = "project_id"),
            @Result(property = "manager_id", column = "manager_id"),
            @Result(property = "project_name", column = "project_name"),
            @Result(property = "planned_start_date", column = "planned_start_date"),
            @Result(property = "planned_end_date", column = "planned_end_date"),
            @Result(property = "site_id", column = "site_id"),
            @Result(property = "contractor_id", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "project_type", column = "project_type"),
            @Result(property = "site_name", column = "construction_site_name"),
            @Result(property = "bidder_name", column = "bidder_name_result")
   })
    List<Project> findCompletedProjectsByManagerId(int manager_id);
}