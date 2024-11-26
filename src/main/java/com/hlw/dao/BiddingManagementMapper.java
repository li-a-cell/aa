package com.hlw.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BiddingManagementMapper {

    @Select("SELECT * FROM tenderr_task")
    Void getTenderTask();

}
