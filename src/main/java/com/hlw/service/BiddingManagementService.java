package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.dao.BiddingManagementMapper;
import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.*;
import com.hlw.dto.ProjectBiddingRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 招标管理服务类
 */
@Service
public class BiddingManagementService {

    @Autowired
    private BiddingManagementMapper biddingManagementMapper;

    /**
     * 添加招标记录
     *
     * @param projectId     项目ID
     * @param tendererId    招标人ID
     * @param requestDate   申请日期
     * @param bidderId      投标人ID
     */
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }

    /**
     * 获取招标任务列表
     *
     * @return 招标任务列表
     */
    public List<TenderTaskDto> getTenderTask() {
        return biddingManagementMapper.getTenderTaskWithProjectName();
    }

    /**
     * 获取所有投标记录
     *
     * @return 投标记录列表
     */
    public List<ProjectBiddingRecordDto> getAllBiddingRecords() {
        return biddingManagementMapper.getAllProjectBiddingRecordsDto();
    }

    /**
     * 发布招标
     *
     * @param projectId 项目ID
     */
    public void publishTender(int projectId) {
        // 更新项目表的状态为待招标
        biddingManagementMapper.updateProjectStatusToPendingTender(projectId);
        // 插入一条新的招标记录
        biddingManagementMapper.updateTenderTaskStatusToPendingTender(projectId);
    }

    /**
     * 根据项目ID获取投标记录
     *
     * @param projectId 项目ID
     * @return 投标记录列表
     */
    public List<ProjectBiddingRecordDto> getBiddingRecordsByProjectId(int projectId) {
        return biddingManagementMapper.findBiddingRecordsByProjectId(projectId);
    }

    /**
     * 获取所有投标人信息
     *
     * @return 投标人列表
     */
    public List<Bidder> getAllBidders() {
        return biddingManagementMapper.getAllBidders();
    }

    /**
     * 添加投标人
     *
     * @param bidder 投标人对象
     */
    public void addBidder(Bidder bidder) {
        biddingManagementMapper.addBidder(bidder);
    }

    /**
     * 更新投标人信息
     *
     * @param bidder 投标人对象
     */
    public void updateBidder(Bidder bidder) {
        biddingManagementMapper.updateBidder(bidder);
    }

    /**
     * 删除投标人
     *
     * @param bidderId 投标人ID
     */
    public void deleteBidder(int bidderId) {
        biddingManagementMapper.deleteBidder(bidderId);
    }

}
