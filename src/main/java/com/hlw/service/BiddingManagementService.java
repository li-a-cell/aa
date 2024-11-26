package com.hlw.service;

import com.hlw.dao.BiddingManagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class BiddingManagementService {
    @Autowired
    private BiddingManagementMapper biddingManagementMapper;

    public void getTenderTask() {
        biddingManagementMapper.getTenderTask();
    }
}
