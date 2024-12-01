package com.hlw.controller;

import com.hlw.dto.ProjectDto;
import com.hlw.pojo.*;
import com.hlw.service.AdministratorService;
import com.hlw.service.BiddingManagementService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biddingmanagent")
public class BiddingManagementController {
    @Autowired
    private BiddingManagementService biddingManagementService;
    // 添加投标记录
    @PostMapping("/tenderRecord")
    public Result addTenderRecord(@RequestBody TenderRecord tenderRecord,HttpServletRequest request) {
        // 调用 Service 层的方法
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        biddingManagementService.addTenderRecord(tenderRecord.getProjectId(), tenderRecord.getTendererId(), tenderRecord.getRequestDate(), tenderRecord.getBidderId());

        // 返回操作结果
        return Result.success("Tender record added successfully");
    }
    // 获取投标任务
    @GetMapping("/gettendertask")
    public Result getTenderTask(HttpServletRequest request){
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        return Result.success(biddingManagementService.getTenderTask());
    }
    // 获取所有投标记录
    @GetMapping("/getbiddingrecord")
    public Result  getAllBiddingRecords(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        return Result.success(biddingManagementService.getAllBiddingRecords());
    }
    // 发布招标操作
    @PostMapping("/publishTender")
    public Result publishTender(@RequestBody String tenderRecord, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        JsonUtils jsonUtils = new JsonUtils();
        String projectId = jsonUtils.getValueFromJson(tenderRecord, "project_id");
        biddingManagementService.publishTender(Integer.parseInt(projectId));
        return Result.success("Tender published successfully");
    }
    @PostMapping("/getProjectBiddingRecords")
    Result getProjectBiddingRecords(@RequestBody Project project, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        return Result.success(biddingManagementService.getBiddingRecordsByProjectId(project.getProjectId()));
    }

    // 获取所有投标人信息
    @GetMapping("/getAllBidders")
    public Result getAllBidders(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        List<Bidder> bidders = biddingManagementService.getAllBidders();
        return Result.success(bidders);
    }

    // 添加投标人
    @PostMapping("/addBidder")
    public Result addBidder(@RequestBody Bidder bidder, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        biddingManagementService.addBidder(bidder);
        return Result.success("Bidder added successfully");
    }

    // 更新投标人信息
    @PutMapping("/updateBidder")
    public Result updateBidder(@RequestBody Bidder bidder, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        biddingManagementService.updateBidder(bidder);
        return Result.success("Bidder updated successfully");
    }

    // 删除投标人
    @DeleteMapping("/deleteBidder/{bidderId}")
    public Result deleteBidder(@PathVariable("bidderId") int bidderId, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        biddingManagementService.deleteBidder(bidderId);
        return Result.success("Bidder deleted successfully");
    }
}