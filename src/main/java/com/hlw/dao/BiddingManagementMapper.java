
package com.hlw.dao;

import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.ProjectBiddingRecord;
import com.hlw.dto.ProjectBiddingRecordDto;
import com.hlw.pojo.TenderTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 招标管理映射器接口，用于定义与招标相关数据库操作的方法
 */
@Mapper
public interface BiddingManagementMapper {

    /**
     * 添加招标记录到数据库
     *
     * @param projectId 项目ID
     * @param tendererId 招标人ID
     * @param requestDate 请求日期
     * @param bidderId 投标人ID
     */
    @Insert("INSERT INTO tenderrecord (project_id, tenderer_id, request_date, bidder_id) " +
            "VALUES (#{projectId}, #{tendererId}, #{requestDate}, #{bidderId})")
    void addTenderRecord(@Param("projectId") int projectId,
                         @Param("tendererId") int tendererId,
                         @Param("requestDate") LocalDate requestDate,
                         @Param("bidderId") int bidderId);

    /**
     * 获取招标任务信息
     *
     * @return 招标任务对象
     */
    @Select("SELECT * FROM tender_task")
    TenderTask getTenderTask();

    /**
     * 获取所有项目投标记录
     *
     * @return 项目投标记录列表
     */
    @Select("SELECT * FROM project_bidding_record")
    List<ProjectBiddingRecord> getAllProjectBiddingRecords();

    /**
     * 根据项目ID获取项目名称
     *
     * @param projectId 项目ID
     * @return 项目名称
     */
    @Select("SELECT project_name FROM project WHERE project_id = #{projectId}")
    String getProjectNameById(@Param("projectId") int projectId);

    /**
     * 根据投标人ID获取投标人名称
     *
     * @param bidderId 投标人ID
     * @return 投标人名称
     */
    @Select("SELECT name FROM bidder WHERE bidder_id = #{bidderId}")
    String getBidderNameById(@Param("bidderId") int bidderId);

    /**
     * 查询所有投标记录及相关项目名称和投标人名称
     *
     * @return 包含项目和投标人信息的投标记录列表
     */
    @Select("SELECT p.project_name,p.project_id, b.name AS bidder_name,b.bidder_id, r.bidding_price, r.bidding_time " +
            "FROM project_bidding_record r " +
            "JOIN project p ON p.project_id = r.project_id " +
            "JOIN bidder b ON b.bidder_id = r.bidder_id")
    List<ProjectBiddingRecordDto> getAllProjectBiddingRecordsDto();

    /**
     * 获取包含项目名称的招标任务列表
     *
     * @return 招标任务列表，包括项目名称
     */
    @Select("SELECT tt.tender_task_id, tt.project_id, p.project_name, tt.deadline, tt.tender_task_status " +
            "FROM tender_task tt " +
            "JOIN project p ON tt.project_id = p.project_id " )
    List<TenderTaskDto> getTenderTaskWithProjectName();

    /**
     * 更新项目状态为待招标
     *
     * @param projectId 项目ID
     */
    @Update("UPDATE project SET status = '待招标' WHERE project_id = #{projectId}")
    void updateProjectStatusToPendingTender(@Param("projectId") int projectId);

    /**
     * 更新招标详情表的状态为待招标
     *
     * @param projectId 项目ID
     */
    @Update("UPDATE tender_task SET tender_task_status = '待招标' WHERE project_id = #{projectId}")
    void updateTenderTaskStatusToPendingTender(@Param("projectId") int projectId);

    /**
     * 根据项目ID查询投标记录
     *
     * @param projectId 项目ID
     * @return 该项目的投标记录列表
     */
    @Select("SELECT pbr.record_id, pbr.project_id, pr.project_name, pbr.bidder_id, b.name AS bidder_name, " +
            "pbr.bidding_price, pbr.bidding_time " +
            "FROM project_bidding_record pbr " +
            "LEFT JOIN project pr ON pbr.project_id = pr.project_id " +
            "LEFT JOIN bidder b ON pbr.bidder_id = b.bidder_id " +
            "WHERE pbr.project_id = #{projectId}")
    @Results({
            @Result(property = "recordId", column = "record_id"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "bidderId", column = "bidder_id"),
            @Result(property = "bidderName", column = "bidder_name"),
            @Result(property = "biddingPrice", column = "bidding_price"),
            @Result(property = "biddingTime", column = "bidding_time")
    })
    List<ProjectBiddingRecordDto> findBiddingRecordsByProjectId(@Param("projectId") int projectId);

    /**
     * 查询所有投标人
     *
     * @return 投标人列表
     */
    @Select("SELECT * FROM bidder")
    List<Bidder> getAllBidders();

    /**
     * 添加投标人
     *
     * @param bidder 投标人对象，包含投标人详细信息
     */
    @Insert("INSERT INTO bidder (account, password, name, phone_number, company_name, status) " +
            "VALUES (#{account}, #{password}, #{name}, #{phoneNumber}, #{companyName}, #{status})")
    void addBidder(Bidder bidder);

    /**
     * 更新投标人信息
     *
     * @param bidder 投标人对象，包含更新后的投标人信息
     */
    @Update("UPDATE bidder SET account = #{account}, password = #{password}, name = #{name}, " +
            "phone_number = #{phoneNumber}, company_name = #{companyName}, status = #{status} " +
            "WHERE bidder_id = #{bidderId}")
    void updateBidder(Bidder bidder);

    /**
     * 删除投标人
     *
     * @param bidderId 投标人ID
     */
    @Delete("DELETE FROM bidder WHERE bidder_id = #{bidderId}")
    void deleteBidder(@Param("bidderId") int bidderId);
}

