package cn.scu.goods.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.book.web.servlet.BookServlet;
import cn.scu.goods.category.doamin.Category;
import cn.scu.goods.category.service.CategoryService;


public class CategoryServlet extends HttpServlet {
	private CategoryService categoryService=new CategoryService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("LeftFind")){
			
			findAll(request, response);
		}
		if(request.getParameter("mark").equals("findByCategory")){
			if(request.getParameter("cname").equals("图书")){
				String cid=request.getParameter("cid");
				request.getRequestDispatcher("/BookServlet?mark=findByCategory&cid=cid").forward(request, response);
			}
			if(request.getParameter("cname").equals("鞋子")){
				String cid=request.getParameter("cid");
				request.getRequestDispatcher("/ShoseServlet?mark=findByCategory&cid=cid").forward(request, response);
			}
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

	
	protected String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//查询所有分类
		List<Category> parents=categoryService.findAll();
		request.setAttribute("parents", parents);
		request.getRequestDispatcher("/jsps/left.jsp").forward(request, response);
		return null;
	}

}
