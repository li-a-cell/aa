package com.hlw.dao;

import java.util.List;
import com.hlw.pojo.ProjectBiddingRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * ProjectBiddingRecord的Mapper接口，用于执行与项目投标记录相关的数据库操作
 */
@Repository
@Mapper
public interface ProjectBiddingRecordMapper {

    /**
     * 插入一条项目投标记录
     *
     * @param record 要插入的项目投标记录
     * @return 受影响的行数
     */
    @Insert("INSERT INTO project_bidding_record (project_id, bidder_id, bidding_price) VALUES (#{projectId}, #{bidderId}, #{biddingPrice})")
    int insertProjectBiddingRecord(ProjectBiddingRecord record);

    /**
     * 根据投标人id查询其所有投标记录
     *
     * @param bidderId 投标人的id
     * @return 投标人的所有投标记录列表
     */
    @Select("SELECT * FROM project_bidding_record WHERE bidder_id = #{bidderId}")
    List<ProjectBiddingRecord> findByBidderId(Integer bidderId);
}
