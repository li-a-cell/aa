package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.dao.BiddingManagementMapper;
import com.hlw.pojo.Result;
import com.hlw.pojo.TenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class BiddingManagementService {
    @Autowired
    private BiddingManagementMapper biddingManagementMapper;
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }
    public void getTenderTask() {
        biddingManagementMapper.getTenderTask();
    }
}
