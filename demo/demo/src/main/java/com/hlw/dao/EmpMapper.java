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
    @Select ("select * from employee where account = #{account}")
    User findByAccount(String account);

    @Insert("insert into employee (account, password, phone_number, profile_picture, name,gender, birth_date,address, job_type,hire_date,salary)" +
            "values(#{account}, #{password}, #{phone_number}, #{profile_picture}, #{name},#{gender}, #{birth_date},#{address}, #{job_type},#{hire_date},#{salary})")
    void insert(User user);

}

