package com.hlw.dao;

import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.ProjectBiddingRecord;
import com.hlw.pojo.ProjectBiddingRecordDto;
import com.hlw.pojo.TenderTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BiddingManagementMapper {
    //
    @Insert("INSERT INTO tenderrecord (project_id, tenderer_id, request_date, bidder_id) " +
            "VALUES (#{projectId}, #{tendererId}, #{requestDate}, #{bidderId})")
    void addTenderRecord(@Param("projectId") int projectId,
                         @Param("tendererId") int tendererId,

                         @Param("requestDate") LocalDate requestDate,
                         @Param("bidderId") int bidderId);

    @Select("SELECT * FROM tender_task")
    TenderTask getTenderTask();


    @Select("SELECT * FROM project_bidding_record")
    List<ProjectBiddingRecord> getAllProjectBiddingRecords();

    @Select("SELECT project_name FROM project WHERE project_id = #{projectId}")
    String getProjectNameById(@Param("projectId") int projectId);

    @Select("SELECT name FROM bidder WHERE bidder_id = #{bidderId}")
    String getBidderNameById(@Param("bidderId") int bidderId);

    // 查询所有投标记录及相关项目名称和投标人名称
    @Select("SELECT p.project_name,p.project_id, b.name AS bidder_name,b.bidder_id, r.bidding_price, r.bidding_time " +
            "FROM project_bidding_record r " +
            "JOIN project p ON p.project_id = r.project_id " +
            "JOIN bidder b ON b.bidder_id = r.bidder_id")
    List<ProjectBiddingRecordDto> getAllProjectBiddingRecordsDto();

    @Select("SELECT tt.tender_task_id, tt.project_id, p.project_name, tt.deadline, tt.tender_task_status " +
            "FROM tender_task tt " +
            "JOIN project p ON tt.project_id = p.project_id " )
    List<TenderTaskDto> getTenderTaskWithProjectName();
    @Update("UPDATE project SET status = '待招标' WHERE project_id = #{projectId}")
    void updateProjectStatusToPendingTender(@Param("projectId") int projectId);

    // 更新招标详情表的状态为待招标
    @Update("UPDATE tender_task SET tender_task_status = '待招标' WHERE project_id = #{projectId}")
    void updateTenderTaskStatusToPendingTender(@Param("projectId") int projectId);

    // 根据投标任务ID查询投标记录
    @Select("SELECT pbr.record_id, pbr.project_id, pr.project_name, pbr.bidder_id, b.name AS bidder_name, " +
            "pbr.bidding_price, pbr.bidding_time " +
            "FROM project_bidding_record pbr " +
            "LEFT JOIN project pr ON pbr.project_id = pr.project_id " +
            "LEFT JOIN bidder b ON pbr.bidder_id = b.bidder_id " +
            "WHERE pbr.project_id = #{projectId}")
    @Results({
            @Result(property = "record_id", column = "record_id"),
            @Result(property = "project_id", column = "project_id"),
            @Result(property = "project_name", column = "project_name"),
            @Result(property = "bidder_id", column = "bidder_id"),
            @Result(property = "bidder_name", column = "bidder_name"),
            @Result(property = "bidding_price", column = "bidding_price"),
            @Result(property = "bidding_time", column = "bidding_time")
    })
    List<ProjectBiddingRecordDto> findBiddingRecordsByProjectId(@Param("projectId") int projectId);

}
