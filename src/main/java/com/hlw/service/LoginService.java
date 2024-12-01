package com.hlw.service;

import com.hlw.dao.EmpMapper;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private EmpMapper empMapper;

    public boolean authenticate(String account, String password) {
        // 查询数据库，验证账号和密码是否匹配
        User user = empMapper.findByAccount(account);
        return user != null && user.getPassword().equals(password);
    }

    public boolean register(User user) {
        // 检查用户名是否已存在
        if (empMapper.findByAccount(user.getAccount()) != null) {
            return false; // 用户名已存在，注册失败
        } else {
            // 保存用户到数据库
            empMapper.insert(user);
            return true;
        }
    }
    public User GetPerson(int employeeID){
       return empMapper.findByEmployeeID( employeeID);
    }
}
