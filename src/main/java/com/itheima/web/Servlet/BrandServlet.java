package com.itheima.web.Servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.Brand;
import com.itheima.pojo.PageBean;
import com.itheima.service.BrandService;
import com.itheima.service.impl.BrandServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


// 通配方式    通过目录映射的方式配置来配url映射路径
@WebServlet("/brand/*")
public class BrandServlet extends BaseServlet {
	private BrandService brandService = new BrandServiceImpl();

	public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、调用service查询
		List<Brand> brands = brandService.selectAll();
		// 	2、把数据转为json
		String jsonString = JSON.toJSONString(brands);
		// 	3、写数据
		response.setContentType("text/json;charset=UTF-8");// 可能存在中文
		response.getWriter().write(jsonString);

		// System.out.println("brand.selsectall...");
	}

	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 	1、接收品牌数据
		BufferedReader br = request.getReader();// 读取请求的消息体数据
		String params = br.readLine();// json字符串

		// 转为brand对象
		Brand brand = JSON.parseObject(params, Brand.class);
		// 2、调用service添加
		brandService.add(brand);

		// 	3、响应成功标识
		response.getWriter().write("success");

		// System.out.println("brand.add...");

	}

	/**
	 * 批量删除
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteByIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 	1、接收数据  [1,2,3]
		BufferedReader br = request.getReader();// 读取请求的消息体数据
		String params = br.readLine();// json字符串

		// 转为int[]
		int[] ids = JSON.parseObject(params, int[].class);
		// 2、调用service添加
		brandService.deleteByIds(ids);

		// 	3、响应成功标识
		response.getWriter().write("success");

		// System.out.println("brand.add...");

	}

	/**
	 * 分页查询
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void selectByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、接收 当前页码 和 每页展示条数  url?currentPage=1&pageSize=5  普通的do get请求方式  把参数放到url后面
		String _currentPage = request.getParameter("currentPage");
		String _pageSize = request.getParameter("pageSize");

		// 转换为Int
		int currentPage = Integer.parseInt(_currentPage);
		int pageSize = Integer.parseInt(_pageSize);

		// 2、调用对应的service查询
		PageBean<Brand> pageBean = brandService.selectByPage(currentPage, pageSize);

		// 	2、把数据转为json
		String jsonString = JSON.toJSONString(pageBean);
		// 	3、写数据
		response.setContentType("text/json;charset=UTF-8");// 可能存在中文
		response.getWriter().write(jsonString);

	}


	/**
	 * 分页条件查询
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void selectByPageAndCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、接收 当前页码 和 每页展示条数  url?currentPage=1&pageSize=5  普通的do get请求方式  把参数放到url后面
		String _currentPage = request.getParameter("currentPage");
		String _pageSize = request.getParameter("pageSize");

		// 转换为Int
		int currentPage = Integer.parseInt(_currentPage);
		int pageSize = Integer.parseInt(_pageSize);

		// 获取对应的查询条件对象
		BufferedReader br = request.getReader();// 读取请求的消息体数据
		String params = br.readLine();// json字符串

		// 转为brand对象
		Brand brand = JSON.parseObject(params, Brand.class);

		// 2、调用对应的service查询
		PageBean<Brand> pageBean = brandService.selectByPageAndCondition(currentPage, pageSize, brand);

		// 	2、把数据转为json
		String jsonString = JSON.toJSONString(pageBean);
		// 	3、写数据
		response.setContentType("text/json;charset=UTF-8");// 可能存在中文
		response.getWriter().write(jsonString);

	}

	/**
	 * 删除单个
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	/*
	写servlet：

		前端的id号以url的形式传到后端
		前端的id号我们也可以放到前端ajax的data的位置上，以json的形式传到后端

		//方法1、url的形式传到后端     注意方法名  删除单个的方法这里搞了一晚上 实在是对于mvn模式还不熟练 很折磨啊
	public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _id = request.getParameter("id");
		System.out.println(_id);
		int id = Integer.parseInt(_id);

		brandService.deleteById(id);
		response.getWriter().write("success");
	}



	//方法2、前端的id号我们也可以放到前端ajax的data的位置上，以json的形式传到后端
	public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 	1、接收数据
		BufferedReader br = request.getReader();// 读取请求的消息体数据
		String params = br.readLine();// json字符串

		// 转为int
		int id = JSON.parseObject(params, Integer.class);
		// 2、调用service删除
		brandService.deleteById(id);

		// 	3、响应成功标识
		response.getWriter().write("success");

		// System.out.println("brand.add...");

	}

	 */

	// 删除单个
	// url的形式传到后端
	public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _id = request.getParameter("id");
		int id = Integer.parseInt(_id);

		brandService.deleteById(id);
		response.getWriter().write("success");
	}


	// 记得改好名字，因为此处没在代码中体现被引用
	public void updateByBrand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 	1、接收品牌数据
		BufferedReader br = request.getReader();// 读取请求的消息体数据
		String params = br.readLine();// json字符串

		// 转为brand对象
		Brand brand = JSON.parseObject(params, Brand.class);
		// 2、调用service修改

		brandService.updateByBrand(brand);

		// 	3、响应成功标识
		response.getWriter().write("success");


	}
}