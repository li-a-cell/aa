package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.dao.BiddingManagementMapper;
import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.ProjectBiddingRecord;
import com.hlw.pojo.ProjectBiddingRecordDto;
import com.hlw.pojo.Result;
import com.hlw.pojo.TenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BiddingManagementService {
    @Autowired
    private BiddingManagementMapper biddingManagementMapper;
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }
    public List<TenderTaskDto> getTenderTask() {
        return biddingManagementMapper.getTenderTaskWithProjectName();
    }
    // 获取所有投标记录
    public List<ProjectBiddingRecordDto> getAllBiddingRecords() {

        return biddingManagementMapper.getAllProjectBiddingRecordsDto();
    }

    public void publishTender(int projectId) {
        // 更新项目表的状态为待招标
        biddingManagementMapper.updateProjectStatusToPendingTender(projectId);
        // 插入一条新的招标记录
        biddingManagementMapper.updateTenderTaskStatusToPendingTender(projectId);
    }
    public  List<ProjectBiddingRecordDto> getBiddingRecordsByProjectId(int projectId) {
        return biddingManagementMapper.findBiddingRecordsByProjectId(projectId);
    }

}
