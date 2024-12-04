package com.hlw.service;

import com.hlw.dao.ProjectBiddingRecordMapper;
import com.hlw.pojo.ProjectBiddingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目投标记录服务类，负责处理项目投标记录的相关业务逻辑
 */
@Service
public class ProjectBiddingRecordService {

    /**
     * 自动注入的项目投标记录数据访问对象，用于与数据库交互
     */
    @Autowired
    private ProjectBiddingRecordMapper projectBiddingRecordMapper;

    /**
     * 插入项目投标记录信息
     *
     * @param record 项目投标记录对象，包含需要插入的记录信息
     * @return 插入操作的影响行数，成功插入返回1，否则返回0
     */
    public int addProjectBiddingRecord(ProjectBiddingRecord record) {
        return projectBiddingRecordMapper.insertProjectBiddingRecord(record);
    }
}
