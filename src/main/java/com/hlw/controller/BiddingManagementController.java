package com.hlw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/biddingmanagentdepartment")
public class BiddingManagementDepartmentController {
    @PostMapping("/createTask")
    public Result createTenderTask(@RequestParam String deadline, @RequestParam String tenderTaskStatus) {
        // 将字符串日期转换为 LocalDate
        LocalDate parsedDeadline = LocalDate.parse(deadline);

        // 调用服务层方法创建招标任务
        TenderTask tenderTask = tenderTaskService.createTenderTask(parsedDeadline, tenderTaskStatus);

        // 返回操作结果
        return Result.success(tenderTask);
    }

}
