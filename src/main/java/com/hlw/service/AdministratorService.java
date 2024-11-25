package com.hlw.service;

import com.hlw.dao.AdministratorMapper;
import com.hlw.pojo.ProjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorMapper administratorMapper;

    // 获取招标数量
    public int getBiddingsNum() {
        return administratorMapper.getBiddingsNum();
    }

    // 获取员工数量
    public int getEmployeeNum() {
        return administratorMapper.getEmployeeNum();
    }
    public  int getNodeCountByStatus(int projectId, ProjectNode.NodeStatus status) {
        return administratorMapper.getNodeCountByStatus(projectId, status);
    }

}