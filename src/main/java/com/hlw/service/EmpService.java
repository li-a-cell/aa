package com.hlw.service;

import com.hlw.dao.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EmpService类提供了员工相关的服务功能
 */
@Service
public class EmpService {

    /**
     * EmpMapper的自动装配，用于执行员工相关的数据库操作
     */
    @Autowired
    private EmpMapper empMapper;

    /**
     * 根据员工姓名获取员工ID
     *
     * @param name 员工姓名，用于查询员工ID
     * @return 返回员工ID，如果未找到则返回0
     */
    public int GetNameByID(String name){
        return empMapper.findIdByName(name);
    }
}
