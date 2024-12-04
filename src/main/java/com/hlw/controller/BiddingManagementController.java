package com.hlw.controller;

import com.hlw.pojo.*;
import com.hlw.service.BiddingManagementService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投标管理控制器
 */
@RestController
@RequestMapping("/biddingManagement")
public class BiddingManagementController {
    /**
     * 自动注入投标管理服务
     */
    @Autowired
    private BiddingManagementService biddingManagementService;

    /**
     * 添加投标记录
     *
     * @param tenderRecord 投标记录对象，包含投标所需的信息
     * @param request      HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @PostMapping("/tenderRecord")
    public Result addTenderRecord(@RequestBody TenderRecord tenderRecord, HttpServletRequest request) {
        // 从请求中获取员工ID
        Object employeeIdObj = request.getAttribute("employeeId");

        // 检查员工ID是否存在
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        // 转换员工ID为整数类型
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 调用Service层方法添加投标记录
        biddingManagementService.addTenderRecord(tenderRecord.getProjectId(), tenderRecord.getTendererId(), tenderRecord.getRequestDate(), tenderRecord.getBidderId());

        // 返回操作结果
        return Result.success("Tender record added successfully");
    }

    /**
     * 获取投标任务
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回投标任务列表
     */
    @GetMapping("/getTenderTask")
    public Result getTenderTask(HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        // 返回投标任务列表
        return Result.success(biddingManagementService.getTenderTask());
    }

    /**
     * 获取所有投标记录
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回所有投标记录列表
     */
    @GetMapping("/getBiddingRecord")
    public Result getAllBiddingRecords(HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        // 返回所有投标记录
        return Result.success(biddingManagementService.getAllBiddingRecords());
    }

    /**
     * 发布招标操作
     *
     * @param tenderRecord 包含招标信息的JSON字符串
     * @param request      HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @PostMapping("/publishTender")
    public Result publishTender(@RequestBody String tenderRecord, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 解析JSON字符串获取项目ID并调用Service层方法发布招标
        JsonUtils jsonUtils = new JsonUtils();
        String projectId = JsonUtils.getValueFromJson(tenderRecord, "projectId");
        biddingManagementService.publishTender(Integer.parseInt(projectId));
        return Result.success("Tender published successfully");
    }

    /**
     * 获取项目相关的投标记录
     *
     * @param project 项目对象，包含项目ID
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回项目相关的投标记录列表
     */
    @PostMapping("/getProjectBiddingRecords")
    Result getProjectBiddingRecords(@RequestBody Project project, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        // 返回项目相关的投标记录
        return Result.success(biddingManagementService.getBiddingRecordsByProjectId(project.getProjectId()));
    }

    /**
     * 获取所有投标人信息
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回所有投标人信息列表
     */
    @GetMapping("/getAllBidders")
    public Result getAllBidders(HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 获取所有投标人信息并返回
        List<Bidder> bidders = biddingManagementService.getAllBidders();
        return Result.success(bidders);
    }

    /**
     * 添加投标人
     *
     * @param bidder 投标人对象，包含投标人信息
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @PostMapping("/addBidder")
    public Result addBidder(@RequestBody Bidder bidder, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 调用Service层方法添加投标人
        biddingManagementService.addBidder(bidder);
        return Result.success("Bidder added successfully");
    }

    /**
     * 更新投标人信息
     *
     * @param bidder 投标人对象，包含更新后的投标人信息
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @PutMapping("/updateBidder")
    public Result updateBidder(@RequestBody Bidder bidder, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 调用Service层方法更新投标人信息
        biddingManagementService.updateBidder(bidder);
        return Result.success("Bidder updated successfully");
    }

    /**
     * 删除投标人
     *
     * @param bidderId 投标人ID
     * @param request  HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @DeleteMapping("/deleteBidder/{bidderId}")
    public Result deleteBidder(@PathVariable("bidderId") int bidderId, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        // 调用Service层方法删除投标人
        biddingManagementService.deleteBidder(bidderId);
        return Result.success("Bidder deleted successfully");
    }
}
