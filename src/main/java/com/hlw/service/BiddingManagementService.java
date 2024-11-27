
package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.dao.BiddingManagementMapper;
import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.*;
import com.hlw.pojo.ProjectBiddingRecordDto;
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

    // 获取所有投标人信息
    public List<Bidder> getAllBidders() {
        return biddingManagementMapper.getAllBidders();
    }

    // 添加投标人
    public void addBidder(Bidder bidder) {
        biddingManagementMapper.addBidder(bidder);
    }

    // 更新投标人信息
    public void updateBidder(Bidder bidder) {
        biddingManagementMapper.updateBidder(bidder);
    }

    // 删除投标人
    public void deleteBidder(int bidderId) {
        biddingManagementMapper.deleteBidder(bidderId);
    }

}
