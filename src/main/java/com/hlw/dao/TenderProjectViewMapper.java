package com.hlw.dao;

import com.hlw.dto.TenderProjectView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TenderProjectViewMapper {

    // 查询视图中的所有信息
    @Select("SELECT * FROM tender_project_view")
    List<TenderProjectView> findAllTenderProjectView();

    // 根据项目id查询视图中的具体项目信息
    @Select("SELECT * FROM tender_project_view WHERE project_id = #{projectId}")
    TenderProjectView findByProjectId(Integer projectId);
}