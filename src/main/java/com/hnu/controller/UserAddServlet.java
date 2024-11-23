package com.hnu.controller;

import com.hnu.entity.User;
import com.hnu.service.UserServiceImpl;
import com.hnu.service.UserServiceInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "addUserServlet", value = "/addUser-servlet")
public class UserAddServlet extends HttpServlet {
    private String message;

    public void init() {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String realName = request.getParameter("realName");
        int gender = Integer.parseInt(request.getParameter("gender"));
        int userType = Integer.parseInt(request.getParameter("userType"));
        User user = new User();
        user.setId(4);
        user.setUsername(username);
        user.setGender(gender);
        user.setRealName(realName);
        user.setUserType(userType);
        user.setPassword("123");
        UserServiceInterface userService = new UserServiceImpl();
        if (userService.registerUser(user)) {
            out.print("{\"message\":\"增加用户成功\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.print("{\"message\":\"增加用户失败\"}");
        }
    }

    public void destroy() {
    }
}