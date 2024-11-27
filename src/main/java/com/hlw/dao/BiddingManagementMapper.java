package com.hlw.dao;

import com.hlw.pojo.TenderTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BiddingManagementMapper {

    @Select("SELECT * FROM tender_task")
    List<TenderTask> getTenderTask();

    @Insert("INSERT INTO tenderrecord (project_id, tenderer_id, request_date, bidder_id) " +
            "VALUES (#{projectId}, #{tendererId}, #{requestDate}, #{bidderId})")
    void addTenderRecord(@Param("projectId") int projectId,
                         @Param("tendererId") int tendererId,

                         @Param("requestDate") LocalDate requestDate,
                         @Param("bidderId") int bidderId);

    // 获取待发布的项目数量
    @Select("SELECT COUNT(*) FROM project WHERE status = '待发布'")
    int getPendingProjectsNum();

    // 获取待招标的项目数量
    @Select("SELECT count(*) FROM project WHERE status = '待招标'")
    int getPendingTenderProjectsNum();

    // 获取施工中的项目数量
    @Select("SELECT count(*) FROM project WHERE status = '施工中'")
    int getOngoingConstructionProjectsNum();

    // 更新项目表的状态为待招标
    @Update("UPDATE project SET status = '待招标' WHERE project_id = #{projectId}")
    void updateProjectStatusToPendingTender(@Param("projectId") int projectId);

    // 更新招标详情表的状态为待招标
    @Update("UPDATE tender_task SET tender_task_status = '待招标' WHERE project_id = #{projectId}")
    void updateTenderTaskStatusToPendingTender(@Param("projectId") int projectId);

}
