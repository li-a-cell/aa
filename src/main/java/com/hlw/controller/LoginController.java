package com.hlw.controller;

import com.hlw.dao.EmpMapper;
import com.hlw.pojo.Result;
import com.hlw.pojo.User;
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
    @PostMapping("/login")
    public Result authenticate(@RequestBody User User) {
        if (loginService.authenticate(User.getAccount(), User.getPassword())) {
            System.out.println("ok");
            User user = empMapper.findByAccount(User.getAccount());
            Map<String, Object> claims=new HashMap<>();
            claims.put("employee_id",user.getEmployee_id());
            claims.put("job_type",user.getJob_type());
            String jwt=JwtUtils.generateJwt(claims);
           return Result.success(jwt);

        } else {
            return Result.error("账号或密码错误");
        }
    }


      @GetMapping("/person")
    public Result HavePerson( HttpServletRequest request) {
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
          }
      else {
              return Result.error("NOT_LOGIN");
          }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (loginService.register(user)) {
            return Result.success(user);
        } else {
            return Result.error("用户已存在");
        }
    }
}
