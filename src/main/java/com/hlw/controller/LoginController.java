package com.hlw.controller;

import com.hlw.dao.EmpMapper;
import com.hlw.pojo.Bidder;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
import com.hlw.service.BidderService;
import com.hlw.service.LoginService;
import com.hlw.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth") // 推荐增加统一的请求前缀，简化管理
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private BidderService bidderService;

    /**
     * 用户认证登录
     *
     * @param User 用户信息，包括账户和密码
     * @return 认证结果，成功则返回JWT令牌，失败则返回错误信息
     */
    @PostMapping("/login")
    public Result authenticate(@RequestBody User User) {
        if (loginService.authenticate(User.getAccount(), User.getPassword())) {
            System.out.println("ok");
            User user = empMapper.findByAccount(User.getAccount());
            Map<String, Object> claims=new HashMap<>();
            claims.put("employeeId",user.getEmployeeId());
            // 注意：这里将用户密码作为jobType放入claims可能存在安全风险
            claims.put("jobType",user.getPassword());
            String jwt=JwtUtils.generateJwt(claims);
            return Result.success(jwt);

        } else {
            return Result.error("账号或密码错误");
        }
    }

    /**
     * 投标人登录
     *
     * @param bidder 投标人信息，包括账户和密码
     * @return 登录结果，成功则返回JWT令牌，失败则返回错误信息
     */
    @PostMapping("/bidderLogin")
    public Result login(@RequestBody Bidder bidder) {
        Bidder loggedInBidder = bidderService.login(bidder.getAccount(), bidder.getPassword());
        if (loggedInBidder!= null) {
            Map<String, Object> claims=new HashMap<>();
            claims.put("employeeId",bidder.getBidderId());
            claims.put("jobType","投标人");
            String jwt=JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        } else {
            return Result.error("账号或密码错误");
        }
    }

    /**
     * 检查个人详细信息
     *
     * @param request HTTP请求，用于获取员工ID
     * @return 员工详细信息，如果信息不存在则返回错误
     */
    @GetMapping("/person")
    public Result HavePerson(HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employee_id");

        // 检查 employee_id 是否为空
        if (employeeIdObj == null) {
            return Result.error("Employee ID is missing in the request");
        }

        int employeeId;
        try {
            // 转换为 int 类型
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        if (loginService.GetPerson(employeeId) != null) {
            return Result.success(loginService.GetPerson(employeeId));
        } else {
            return Result.error("NOT_LOGIN");
        }
    }

    /**
     * 用户注册
     *
     * @param user 用户信息，包括账户和密码
     * @return 注册结果，成功则返回用户信息，失败则返回错误信息
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (loginService.register(user)) {
            return Result.success(user);
        } else {
            return Result.error("用户已存在");
        }
    }
}
