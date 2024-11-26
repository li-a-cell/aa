package com.hlw.service;

import com.hlw.dao.BiddingManagementMapper;
import com.hlw.pojo.TenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Service
public class BiddingManagementService {
    @Autowired
    private BiddingManagementMapper biddingManagementMapper;

    public List<TenderTask> getTenderTask() {
         return  biddingManagementMapper.getTenderTask();

    }
    public void addTenderRecord(int projectId, int tendererId, LocalDate requestDate, int bidderId) {
        biddingManagementMapper.addTenderRecord(projectId, tendererId, requestDate, bidderId);
    }
}
