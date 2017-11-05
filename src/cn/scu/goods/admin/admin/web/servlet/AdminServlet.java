package cn.scu.goods.admin.admin.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.admin.admin.domain.Admin;
import cn.scu.goods.admin.admin.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private AdminService adminService=new AdminService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("login")){
			
			login(request, response);
		}
		doGet(request, response);
	}
	
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到Admin
		 */
		Admin form = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(form);

		if(admin == null) {
			System.out.println("!!!!!!!!!");
			req.setAttribute("msg", "用户名或密码错误！");
			req.getRequestDispatcher("/adminjsps/login.jsp").forward(req, resp);
			return null;
		}
		req.getSession().setAttribute("admin", admin);
		
		resp.sendRedirect("/goods/adminjsps/admin/index.jsp");
		return null;
	}

}
