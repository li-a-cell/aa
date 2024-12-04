package com.hlw.dao;

import com.hlw.dto.TenderProjectView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TenderProjectView的DAO接口，用于执行与 tender_project_view 表相关的数据库操作
 */
@Repository
@Mapper
public interface TenderProjectViewMapper {

    /**
     * 查询视图中的所有信息
     *
     * @return 包含所有视图信息的列表
     */
    @Select("SELECT * FROM tender_project_view")
    List<TenderProjectView> findAllTenderProjectView();

    /**
     * 根据项目id查询视图中的具体项目信息
     *
     * @param projectId 项目的唯一标识符
     * @return 对应项目id的视图信息
     */
    @Select("SELECT * FROM tender_project_view WHERE project_id = #{projectId}")
    TenderProjectView findByProjectId(Integer projectId);
}
