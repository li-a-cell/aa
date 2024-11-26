package com.hlw.service;

import com.hlw.pojo.TenderTask;

import java.time.LocalDate;

public class BiddingManagementDepartment {
    public TenderTask createTenderTask(LocalDate deadline, String tenderTaskStatus) {
        TenderTask tenderTask = new TenderTask();
        tenderTask.setDeadline(deadline);
        tenderTask.setTenderTaskStatus(tenderTaskStatus);
        return tenderTaskRepository.save(tenderTask);  // 保存到数据库
    }
}
