package com.hlw.service;

import com.hlw.dao.EmpMapper;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供登录和注册服务的类
 */
@Service
public class LoginService {

    /**
     * 注入EmpMapper以访问数据库
     */
    @Autowired
    private EmpMapper empMapper;

    /**
     * 验证用户账号和密码
     *
     * @param account 用户账号
     * @param password 用户密码
     * @return 如果账号和密码匹配则返回true，否则返回false
     */
    public boolean authenticate(String account, String password) {
        // 查询数据库，验证账号和密码是否匹配
        User user = empMapper.findByAccount(account);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * 注册新用户
     *
     * @param user 待注册的用户信息
     * @return 如果注册成功则返回true，否则返回false
     */
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

    /**
     * 根据员工ID获取用户信息
     *
     * @param employeeID 员工ID
     * @return 返回对应的用户信息，如果找不到则返回null
     */
    public User GetPerson(int employeeID){
        return empMapper.findByEmployeeID(employeeID);
    }
}
