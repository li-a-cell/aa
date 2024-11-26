package com.hlw.dao;

import com.hlw.pojo.TenderTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
