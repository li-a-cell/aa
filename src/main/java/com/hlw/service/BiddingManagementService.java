package com.hlw.service;

import com.hlw.dao.BiddingManagementMapper;
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

    public List<TenderTask> getTenderTask() {
         return  biddingManagementMapper.getTenderTask();

    }
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }

    // 获取待发布项目的数量
    public int getPendingProjectsNum() {
        return biddingManagementMapper.getPendingProjectsNum();
    }

    // 获取待招标的项目数量
    public int getPendingTenderProjects() {
        return biddingManagementMapper.getPendingTenderProjectsNum();
    }

    // 获取施工中的项目数量
    public int getOngoingConstructionProjects() {
        return biddingManagementMapper.getOngoingConstructionProjectsNum();
    }

    // 发布招标操作
    public void publishTender(int projectId) {
        // 更新项目表的状态为待招标
        biddingManagementMapper.updateProjectStatusToPendingTender(projectId);
        // 插入一条新的招标记录
        biddingManagementMapper.updateTenderTaskStatusToPendingTender(projectId);
    }
}
