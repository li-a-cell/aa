package com.hlw.controller;


import com.hlw.pojo.Result;
import com.hlw.service.BiddingManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        biddingManagementService.getTenderTask();
        return Result.success();
    }

}
