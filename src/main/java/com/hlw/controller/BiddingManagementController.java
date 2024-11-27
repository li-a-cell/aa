package com.hlw.controller;


import com.hlw.pojo.Result;
import com.hlw.pojo.TenderRecord;
import com.hlw.pojo.TenderTask;
import com.hlw.service.BiddingManagementService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/biddingmanagement")
public class BiddingManagementController {

    @Autowired
    private BiddingManagementService biddingManagementService;

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
        List<TenderTask>  tenderTasks =biddingManagementService.getTenderTask();
        return Result.success(tenderTasks);
    }
    @PostMapping("/tenderRecord")
    public Result addTenderRecord(@RequestBody TenderRecord tenderRecord, HttpServletRequest request) {
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
        biddingManagementService.addTenderRecord(tenderRecord.getProject_id(), tenderRecord.getTenderer_id(), tenderRecord.getRequest_date(), tenderRecord.getBidder_id());

        // 返回操作结果
        return Result.success("Tender record added successfully");
    }

    // 获取待发布项目的数量
    @GetMapping("/pendingnum")
    public Result getPendingProjectsNum(HttpServletRequest request) {
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

        int num=biddingManagementService.getPendingProjectsNum();
        return Result.success(num);
    }

    // 获取待招标的项目数量
    @GetMapping("/pendingtender")
    public Result getPendingTenderProjectsNum(HttpServletRequest request) {
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
        int num= biddingManagementService.getPendingTenderProjects();
        return Result.success(num);
    }

    // 获取施工中的项目数量
    @GetMapping("/ongoingconstruction")
    public Result getOngoingConstructionProjectsNum(HttpServletRequest request) {
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
        int num = biddingManagementService.getOngoingConstructionProjects();
        return Result.success(num);
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
}
