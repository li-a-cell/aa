package com.hlw.service;

import com.hlw.dao.BiddingManagementMapper;
import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.ProjectBiddingRecordDto;
import com.hlw.pojo.TenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Service
public class BiddingManagementService {
    @Autowired
    private BiddingManagementMapper biddingManagementMapper;

    public List<TenderTaskDto> getTenderTask() {
        return biddingManagementMapper.getTenderTaskWithProjectName();
    }
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }

    // 发布招标操作
    public void publishTender(int projectId) {
        // 更新项目表的状态为待招标
        biddingManagementMapper.updateProjectStatusToPendingTender(projectId);
        // 插入一条新的招标记录
        biddingManagementMapper.updateTenderTaskStatusToPendingTender(projectId);
    }
    // 获取所有投标记录
    public List<ProjectBiddingRecordDto> getAllBiddingRecords() {

        return biddingManagementMapper.getAllProjectBiddingRecordsDto();
    }
}
