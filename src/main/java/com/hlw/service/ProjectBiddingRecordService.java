package com.hlw.service;

import com.hlw.dao.ProjectBiddingRecordMapper;
import com.hlw.pojo.ProjectBiddingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectBiddingRecordService {

    @Autowired
    private ProjectBiddingRecordMapper projectBiddingRecordMapper;

    // 插入项目投标记录信息
    public int addProjectBiddingRecord(ProjectBiddingRecord record) {
        return projectBiddingRecordMapper.insertProjectBiddingRecord(record);
    }
}