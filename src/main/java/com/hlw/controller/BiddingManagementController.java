package com.hlw.controller;

import com.hlw.dto.ProjectBiddingRecordDto;
import com.hlw.dto.TenderTaskDto;
import com.hlw.pojo.*;
import com.hlw.service.BiddingManagementService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    // 提取员工ID解析逻辑
    private int parseEmployeeId(Object employeeIdObj) {
        try {
            return Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Employee ID format");
        }
    }

    // 将字符串转换为整数，若转换失败则抛出异常
    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " should be a valid number");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

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

        // 参数校验：确保投标记录的必填字段不为空
        if (tenderRecord == null || tenderRecord.getProjectId() == null || tenderRecord.getTendererId() == null
                || tenderRecord.getRequestDate() == null || tenderRecord.getBidderId() == null) {
            return Result.error("Missing required tender record information");
        }

        try {
            // 调用Service层方法添加投标记录
            biddingManagementService.addTenderRecord(tenderRecord.getProjectId(), tenderRecord.getTendererId(), tenderRecord.getRequestDate(), tenderRecord.getBidderId());

            // 返回操作结果
            return Result.success("Tender record added successfully");
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error adding tender record: {}", e.getMessage());
            return Result.error("Failed to add tender record, system error occurred");
        }
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

        try {
            // 获取投标任务列表
            List<TenderTaskDto> tenderTasks = biddingManagementService.getTenderTask();

            // 如果没有任务，返回合适的提示
            if (tenderTasks == null || tenderTasks.isEmpty()) {
                return Result.error("No tender tasks found");
            }

            // 返回投标任务列表
            return Result.success(tenderTasks);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching tender tasks: {}", e.getMessage(), e);
            return Result.error("Failed to fetch tender tasks, system error occurred");
        }
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

        try {
            // 获取所有投标记录
            List<ProjectBiddingRecordDto> biddingRecords = biddingManagementService.getAllBiddingRecords();

            // 如果没有投标记录，返回合适的提示
            if (biddingRecords == null || biddingRecords.isEmpty()) {
                return Result.error("No bidding records found");
            }

            // 返回投标记录列表
            return Result.success(biddingRecords);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error fetching bidding records: {}", e.getMessage(), e);
            return Result.error("Failed to fetch bidding records, system error occurred");
        }
    }


    /**
     * 发布招标操作
     *
     * @param tenderRecord 包含招标信息的JSON字符串
     * @param request      HTTP请求对象，用于获取员工ID
     * @return 返回操作结果，包括成功或错误信息
     */
    @PostMapping("/publishTender")
    public Result publishTender(@RequestBody TenderRecord tenderRecord, HttpServletRequest request) {
        // 从请求中获取员工ID并进行有效性检查
        Object employeeIdObj = request.getAttribute("employeeId");
        System.out.println("tenderRecord: " + tenderRecord);
        if (employeeIdObj == null) {
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }

        try {
            // 发布招标
            biddingManagementService.publishTender(tenderRecord.getProjectId());

            // 记录成功日志
            logger.info("Employee {} successfully published tender for project {}", employeeId, tenderRecord.getProjectId());

            return Result.success("Tender published successfully");
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error occurred while publishing tender: {}", e.getMessage(), e);
            return Result.error("Failed to publish tender, please try again later");
        }
    }


    /**
     * 获取项目相关的投标记录
     *
     * @param project 项目对象，包含项目ID
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回项目相关的投标记录列表
     */
    @PostMapping("/getProjectBiddingRecords")
    public Result getProjectBiddingRecords(@RequestBody Project project, HttpServletRequest request) {
        // 获取并验证员工ID
        int employeeId = getEmployeeIdFromRequest(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        try {
            // 获取项目ID并校验
            int projectId = project.getProjectId();
            if (projectId <= 0) {
                return Result.error("Invalid Project ID");
            }

            // 获取项目相关的投标记录
            List<ProjectBiddingRecordDto> biddingRecords = biddingManagementService.getBiddingRecordsByProjectId(projectId);
            if (biddingRecords == null || biddingRecords.isEmpty()) {
                return Result.error("No bidding records found for the given project ID");
            }

            // 返回投标记录
            return Result.success(biddingRecords);

        } catch (Exception e) {
            // 捕获并记录异常
            logger.error("Error occurred while fetching bidding records for project {}: {}", project.getProjectId(), e.getMessage(), e);
            return Result.error("Failed to retrieve bidding records, please try again later");
        }
    }

    // 封装获取员工ID的逻辑
    private int getEmployeeIdFromRequest(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return -1; // 返回-1表示员工ID缺失
        }
        try {
            return Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return -1; // 返回-1表示员工ID格式无效
        }
    }


    /**
     * 获取所有投标人信息
     *
     * @param request HTTP请求对象，用于获取员工ID
     * @return 返回所有投标人信息列表
     */
    @GetMapping("/getAllBidders")
    public Result getAllBidders(HttpServletRequest request) {
        // 获取并验证员工ID
        int employeeId = getEmployeeIdFromRequest(request);
        if (employeeId == -1) {
            return Result.error("employee_id is missing or invalid in the request");
        }

        try {
            // 获取所有投标人信息
            List<Bidder> bidders = biddingManagementService.getAllBidders();

            // 如果没有找到投标人信息，返回相应提示
            if (bidders == null || bidders.isEmpty()) {
                return Result.error("No bidders found");
            }

            // 返回投标人信息
            return Result.success(bidders);

        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.error("Error occurred while fetching bidders: {}", e.getMessage(), e);
            return Result.error("Failed to retrieve bidders, please try again later");
        }
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
