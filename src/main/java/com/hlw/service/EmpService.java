package com.hlw.service;

import com.hlw.dao.EmpMapper;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {

    @Autowired
    private EmpMapper empMapper;

    public int GetNameByID(String name){
       return empMapper.findidByName(name);
    }
}
