package com.hlw.dao;


import com.hlw.pojo.ProjectBiddingRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProjectBiddingRecordMapper {

    @Insert("INSERT INTO project_bidding_record (project_id, bidder_id, bidding_price) VALUES (#{projectId}, #{bidderId}, #{biddingPrice})")
    int insertProjectBiddingRecord(ProjectBiddingRecord record);
    // 根据投标人id查询其所有投标记录
    @Select("SELECT * FROM project_bidding_record WHERE bidder_id = #{bidderId}")
    List<ProjectBiddingRecord> findByBidderId(Integer bidderId);
}