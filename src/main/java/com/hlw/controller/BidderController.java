package com.hlw.controller;

import com.hlw.dto.ProjectDetailsView;
import com.hlw.dto.TenderProjectView;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.ProjectBiddingRecord;
import com.hlw.service.BidderService;
import com.hlw.service.ProjectBiddingRecordService;
import com.hlw.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制投标人相关操作的控制器
 */
@RestController
@RequestMapping("/bidders")
public class BidderController {

    @Autowired
    private BidderService bidderService;
    @Autowired
    private ProjectBiddingRecordService projectBiddingRecordService;
    @Autowired
    private ProjectService projectService;

    /**
     * 根据项目ID获取项目详细信息
     *
     * @param projectId 项目ID
     * @return 项目详细信息视图对象
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectDetailsView> getProjectById(@PathVariable Integer projectId) {
        ProjectDetailsView project = projectService.findProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    /**
     * 获取所有投标人信息接口
     *
     * @return 所有投标人信息列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<Bidder>> getAllBidders() {
        List<Bidder> bidders = bidderService.getAllBidders();
        return new ResponseEntity<>(bidders, HttpStatus.OK);
    }

    /**
     * 根据id获取投标人信息接口
     *
     * @param bidderId 投标人ID
     * @return 投标人信息，如果找不到返回404
     */
    @GetMapping("/{bidderId}")
    public ResponseEntity<Bidder> getBidderById(@PathVariable Integer bidderId) {
        Bidder bidder = bidderService.getBidderById(bidderId);
        if (bidder != null) {
            return new ResponseEntity<>(bidder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 获取视图中所有项目信息接口
     *
     * @return 所有项目视图信息列表
     */
    @GetMapping("/tender-project-view/all")
    public ResponseEntity<List<TenderProjectView>> getAllTenderProjectView() {
        List<TenderProjectView> views = bidderService.getAllTenderProjectView();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    /**
     * 根据项目id获取视图中具体项目信息接口
     *
     * @param projectId 项目ID
     * @return 项目视图信息，如果找不到返回404
     */
    @GetMapping("/tender-project-view/{projectId}")
    public ResponseEntity<TenderProjectView> getTenderProjectViewByProjectId(@PathVariable Integer projectId) {
        TenderProjectView view = bidderService.getTenderProjectViewByProjectId(projectId);
        if (view != null) {
            return new ResponseEntity<>(view, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 投标人投标接口
     *
     * @param record 投标记录，包含项目ID和投标人ID等信息
     * @return 投标成功返回200
     */
    @PostMapping("/bid")
    public ResponseEntity<Void> bid(@RequestBody ProjectBiddingRecord record) {
        bidderService.bid(record);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
