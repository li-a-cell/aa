package com.hlw.dao;

import com.hlw.pojo.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.Mapping;

@Mapper
public interface EmpMapper {
    //获取账号对应的个人信息
    @Select("select * from employee where account = #{account}")
    User findByAccount(String account);

    //插入账号信息
    @Insert("insert into employee (account, password, phone_number, profile_picture, name,gender, birth_date,address, job_type,hire_date,salary)" +
            "values(#{account}, #{password}, #{phoneNumber}, #{profilePicture}, #{name},#{gender}, #{birthDate},#{address}, #{jobType},#{hireDate},#{salary})")
    void insert(User user);

    //根据id获取个人信息
    @Select("select * from employee where employee_id = #{employeeID}")
    User findByEmployeeID(int employeeID);
    //根据名字获取id
     @Select ("select employee_id from employee where name = #{name}")
    int findIdByName(String name);


}

