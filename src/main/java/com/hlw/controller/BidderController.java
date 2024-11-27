package com.hlw.controller;

import com.hlw.dto.TenderProjectView;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.Project;
import com.hlw.pojo.ProjectBiddingRecord;
import com.hlw.service.BidderService;
import com.hlw.service.ProjectBiddingRecordService;
import com.hlw.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bidders")
public class BidderController {

    @Autowired
    private BidderService bidderService;
    @Autowired
    private ProjectBiddingRecordService projectBiddingRecordService;
    @Autowired
    private ProjectService projectService;
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer projectId) {
        Project project = projectService.findProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // 投标人登录接口
    @PostMapping("/login")
    public ResponseEntity<Bidder> login(@RequestBody Bidder bidder) {
        Bidder loggedInBidder = bidderService.login(bidder.getAccount(), bidder.getPassword());
        if (loggedInBidder!= null) {
            return new ResponseEntity<>(loggedInBidder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // 获取所有投标人信息接口
    @GetMapping("/all")
    public ResponseEntity<List<Bidder>> getAllBidders() {
        List<Bidder> bidders = bidderService.getAllBidders();
        return new ResponseEntity<>(bidders, HttpStatus.OK);
    }

    // 根据id获取投标人信息接口
    @GetMapping("/{bidderId}")
    public ResponseEntity<Bidder> getBidderById(@PathVariable Integer bidderId) {
        Bidder bidder = bidderService.getBidderById(bidderId);
        if (bidder!= null) {
            return new ResponseEntity<>(bidder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 获取视图中所有项目信息接口
    @GetMapping("/tender-project-view/all")
    public ResponseEntity<List<TenderProjectView>> getAllTenderProjectView() {
        List<TenderProjectView> views = bidderService.getAllTenderProjectView();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    // 根据项目id获取视图中具体项目信息接口
    @GetMapping("/tender-project-view/{projectId}")
    public ResponseEntity<TenderProjectView> getTenderProjectViewByProjectId(@PathVariable Integer projectId) {
        TenderProjectView view = bidderService.getTenderProjectViewByProjectId(projectId);
        if (view!= null) {
            return new ResponseEntity<>(view, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 投标人投标接口
    @PostMapping("/bid")
    public ResponseEntity<Void> bid(@RequestBody ProjectBiddingRecord record) {
        bidderService.bid(record);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}