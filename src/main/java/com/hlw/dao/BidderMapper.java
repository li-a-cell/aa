package com.hlw.dao;

import com.hlw.pojo.Bidder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface BidderMapper {

    // 根据账户和密码查询投标人信息
    @Select("SELECT * FROM bidder WHERE account = #{account} AND password = #{password}")
    Bidder findByAccountAndPassword(String account, String password);

    // 查询所有投标人信息
    @Select("SELECT * FROM bidder")
    List<Bidder> findAllBidders();

    // 根据id查询投标人信息
    @Select("SELECT * FROM bidder WHERE bidder_id = #{bidderId}")
    Bidder findById(Integer bidderId);
}