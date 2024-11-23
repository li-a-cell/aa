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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "userDelServlet", value = "/userDel-servlet")
public class UserDelServlet extends HttpServlet {
    private String message;

    public void init() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long id = Long.valueOf(request.getParameter("id"));
        UserServiceInterface userservice = new UserServiceImpl();
        if(userservice.delUser(id)) {
            request.getRequestDispatcher("/admin/admin.html").forward(request,
                    response);
        }
        else{
            request.getRequestDispatcher("/error.html").forward(request,
                    response);
        }
    }

    public void destroy() {
    }
}