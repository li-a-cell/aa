package com.hlw.dao;

import com.hlw.dto.ProjectDetailsView;
import com.hlw.dto.ProjectDto;
import com.hlw.pojo.ProjectNode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目映射器接口，用于定义与项目相关的数据库操作
 */
@Mapper
public interface ProjectMapper {

    /**
     * 根据项目经理ID获取所有项目
     *
     * @param managerId 项目经理ID
     * @return 项目列表
     */
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{managerId}")
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "managerId", column = "manager_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "plannedStartDate", column = "planned_start_date"),
            @Result(property = "plannedEndDate", column = "planned_end_date"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "contractorId", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "siteName", column = "construction_site_name"),
            @Result(property = "bidderName", column = "bidder_name_result")
    })
    List<ProjectDto> findProjectsByManagerId(int managerId);

    /**
     * 根据项目经理ID获取正在进行的项目
     *
     * @param managerId 项目经理ID
     * @return 进行中的项目列表
     */
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{managerId} AND p.status IN ('待发布', '待招标', '施工中')")
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "managerId", column = "manager_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "plannedStartDate", column = "planned_start_date"),
            @Result(property = "plannedEndDate", column = "planned_end_date"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "contractorId", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "siteName", column = "construction_site_name"),
            @Result(property = "bidderName", column = "bidder_name_result")
    })
    List<ProjectDto> findOngoingProjectsByManagerId(int managerId);

    /**
     * 根据项目经理ID获取已完成的项目
     *
     * @param managerId 项目经理ID
     * @return 已完成的项目列表
     */
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "WHERE p.manager_id = #{managerId} AND p.status = '已完成'")
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "managerId", column = "manager_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "plannedStartDate", column = "planned_start_date"),
            @Result(property = "plannedEndDate", column = "planned_end_date"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "contractorId", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "siteName", column = "construction_site_name"),
            @Result(property = "bidderName", column = "bidder_name_result")
    })
    List<ProjectDto> findCompletedProjectsByManagerId(int managerId);

    /**
     * 获取指定状态的项目节点
     *
     * @param projectId 项目ID
     * @param status 节点状态
     * @return 项目节点列表
     */
    @Select("SELECT node_id, node_name, start_date, end_date, status, node_info " +
            "FROM projectnode " +
            "WHERE project_id = #{projectId} AND status = #{status}")
    List<ProjectNode> getProjectNodes(@Param("projectId") int projectId, ProjectNode.NodeStatus status);

    /**
     * 获取指定状态的项目数量
     *
     * @param status 项目状态
     * @return 项目数量
     */
    @Select("SELECT COUNT(*) FROM project WHERE status=#{status}")
    int getProjectsNum(String status);

    /**
     * 按类型查询项目数量
     *
     * @param projectType 项目类型
     * @return 项目数量
     */
    @Select("SELECT COUNT(*) FROM project WHERE project_type=#{projectType}")
    int getProjectsNumByType(String projectType);

    /**
     * 按类型查询项目金额
     *
     * @param projectType 项目类型
     * @return 项目金额总数
     */
    @Select("SELECT SUM(budget) FROM project WHERE project_type=#{projectType}")
    double getProjectsCostNumByType(String projectType);

    /**
     * 根据项目ID获取项目详情视图
     *
     * @param projectId 项目ID
     * @return 项目详情视图对象
     */
    @Select("SELECT * FROM project_details_view WHERE projectId = #{projectId}")
    ProjectDetailsView findProjectById(Integer projectId);

    /**
     * 获取所有项目，包括项目经理名称
     *
     * @return 项目列表
     */
    @Select("SELECT p.project_id, p.manager_id, e.name,p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "LEFT JOIN employee e ON p.manager_id =e.employee_id "
    )
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "managerId", column = "manager_id"),
            @Result(property = "managerName", column = "name"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "plannedStartDate", column = "planned_start_date"),
            @Result(property = "plannedEndDate", column = "planned_end_date"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "contractorId", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "siteName", column = "construction_site_name"),
            @Result(property = "bidderName", column = "bidder_name_result")
    })
    List<ProjectDto> findAllProjects();

    /**
     * 根据项目状态获取项目列表，包括项目经理名称
     *
     * @param status 项目状态
     * @return 项目列表
     */
    @Select("SELECT p.project_id, p.manager_id, p.project_name, p.planned_start_date, p.planned_end_date, " +
            "p.site_id, p.contractor_id, p.budget, p.status, p.description, p.project_type, " +
            "cs.site_name AS construction_site_name, b.name AS bidder_name_result, " +
            "e.name AS manager_name " +
            "FROM project p " +
            "LEFT JOIN constructionsite cs ON p.site_id = cs.site_id " +
            "LEFT JOIN bidder b ON p.contractor_id = b.bidder_id " +
            "LEFT JOIN employee e ON p.manager_id = e.employee_id " +
            "WHERE p.status = #{status}")  // 根据状态筛选项目
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "managerId", column = "manager_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "plannedStartDate", column = "planned_start_date"),
            @Result(property = "plannedEndDate", column = "planned_end_date"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "contractorId", column = "contractor_id"),
            @Result(property = "budget", column = "budget"),
            @Result(property = "status", column = "status"),
            @Result(property = "description", column = "description"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "siteName", column = "construction_site_name"),
            @Result(property = "bidderName", column = "bidder_name_result"),
            @Result(property = "managerName", column = "manager_name")
    })
    List<ProjectDto> findProjectsByStatus(@Param("status") String status); // 方法参数传入状态
}
