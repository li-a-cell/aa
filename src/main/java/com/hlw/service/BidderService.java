package com.hlw.service;


import com.hlw.dao.BidderMapper;
import com.hlw.dao.ProjectBiddingRecordMapper;
import com.hlw.dao.TenderProjectViewMapper;
import com.hlw.dto.TenderProjectView;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.ProjectBiddingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidderService {

    @Autowired
    private BidderMapper bidderMapper;

    @Autowired
    private ProjectBiddingRecordMapper projectBiddingRecordMapper;

    @Autowired
    private TenderProjectViewMapper tenderProjectViewMapper;

    // 根据账户和密码验证投标人登录
    public Bidder login(String account, String password) {
        return bidderMapper.findByAccountAndPassword(account, password);
    }

    // 获取所有投标人信息
    public List<Bidder> getAllBidders() {
        return bidderMapper.findAllBidders();
    }

    // 根据id获取投标人信息
    public Bidder getBidderById(Integer bidderId) {
        return bidderMapper.findById(bidderId);
    }

    // 获取视图中的所有项目信息
    public List<TenderProjectView> getAllTenderProjectView() {
        return tenderProjectViewMapper.findAllTenderProjectView();
    }

    // 根据项目id获取视图中的具体项目信息
    public TenderProjectView getTenderProjectViewByProjectId(Integer projectId) {
        return tenderProjectViewMapper.findByProjectId(projectId);
    }

    // 投标人进行投标操作，插入投标记录
    public void bid(ProjectBiddingRecord record) {
        projectBiddingRecordMapper.insertProjectBiddingRecord(record);
    }
}