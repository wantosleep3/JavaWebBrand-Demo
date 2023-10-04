package com.itheima.web.Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 通配方式    通过目录映射的方式配置来配url映射路径
@WebServlet("/user/*")
public class userServlet extends BaseServlet {
	public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("user.selsectall...");
	}

	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("user.add...");
	}
}
