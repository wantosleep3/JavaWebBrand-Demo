package com.itheima.web.Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 替换HttpServlet的  根据请求的最后一段路径进行方法分发
 */
public class BaseServlet extends HttpServlet {

	// 根据请求的最后一段路径进行方法分发
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 	1、获取请求路径
		String uri = req.getRequestURI();  ///brand-case/brand/selectAll
		// 	2、获取最后一段路径，方法名
		int index = uri.lastIndexOf("/"); // 最后一个斜杠位置
		String methodName = uri.substring(index + 1);
		/**
		 * String uri = req.getRequestURI();            /brand-case/brand/selectAll
		 *String methodName = uri.substring(index);         /selectAll
		 */

		// 	3、执行方法
		// 	3.1 获取BrandServlet 或者UserServlet   的字节码对象 class
		// this:谁调用我[this所在的方法]  我[this]代表谁          this == baseservlet的子类
		// System.out.println(this);   //这里面的this就是brandservlet 或者userservlet

		Class<? extends BaseServlet> cls = this.getClass();
		// 	3.2 获取方法method对象
		try {
			Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			// 	3.3 执行方法
			method.invoke(this, req, resp);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}


	}
}
