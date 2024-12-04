package com.hlw.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hlw.dao.BidderMapper;
import com.hlw.dao.ProjectBiddingRecordMapper;
import com.hlw.dao.TenderProjectViewMapper;
import com.hlw.dto.TenderProjectView;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.ProjectBiddingRecord;

/**
 * 提供投标人相关的服务
 */
@Service
public class BidderService {

    @Autowired
    private BidderMapper bidderMapper;

    @Autowired
    private ProjectBiddingRecordMapper projectBiddingRecordMapper;

    @Autowired
    private TenderProjectViewMapper tenderProjectViewMapper;

    /**
     * 根据账户和密码验证投标人登录
     *
     * @param account 投标人账户
     * @param password 投标人密码
     * @return 匹配的投标人对象，如果账户或密码不正确则返回null
     */
    public Bidder login(String account, String password) {
        return bidderMapper.findByAccountAndPassword(account, password);
    }

    /**
     * 获取所有投标人信息
     *
     * @return 投标人列表
     */
    public List<Bidder> getAllBidders() {
        return bidderMapper.findAllBidders();
    }

    /**
     * 根据id获取投标人信息
     *
     * @param bidderId 投标人ID
     * @return 投标人对象，如果找不到则返回null
     */
    public Bidder getBidderById(Integer bidderId) {
        return bidderMapper.findById(bidderId);
    }

    /**
     * 获取视图中的所有项目信息
     *
     * @return 项目视图列表
     */
    public List<TenderProjectView> getAllTenderProjectView() {
        return tenderProjectViewMapper.findAllTenderProjectView();
    }

    /**
     * 根据项目id获取视图中的具体项目信息
     *
     * @param projectId 项目ID
     * @return 项目视图对象，如果找不到则返回null
     */
    public TenderProjectView getTenderProjectViewByProjectId(Integer projectId) {
        return tenderProjectViewMapper.findByProjectId(projectId);
    }

    /**
     * 投标人进行投标操作，插入投标记录
     *
     * @param record 投标记录对象
     */
    public void bid(ProjectBiddingRecord record) {
        projectBiddingRecordMapper.insertProjectBiddingRecord(record);
    }
}
