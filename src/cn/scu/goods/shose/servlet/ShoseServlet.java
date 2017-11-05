package cn.scu.goods.shose.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.scu.goods.book.domin.Book;
import cn.scu.goods.pager.PageBean;
import cn.scu.goods.shose.domain.Shose;
import cn.scu.goods.shose.service.ShoseService;

/**
 * Servlet implementation class ShoseServlet
 */
public class ShoseServlet extends HttpServlet {
	private ShoseService shoseService=new ShoseService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("findByCategory")){
			findByCategory(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	//获取当前页的页码
	private int getPc(HttpServletRequest request){
		int pc=1;
		String param=request.getParameter("pc");
		if(param!=null && !param.trim().isEmpty()){
			try {
				pc=Integer.parseInt(param);
			} catch (RuntimeException e) {}
		}
		return pc;
	}
	
	//获取url,页面中的分页导航中需要使用它作为超链接的目标
	//request.getRequestURI();  只能获取到/goods/BookServlet
	//request.getQueryString();可以得到路径中问号后面的字符串，即传递的各种参数
	private String getUrl(HttpServletRequest request){
		String url=request.getRequestURI()+"?"+request.getQueryString();
		//如果url存在pc参数，截取掉
		int index=url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
		return url;
	}
	
	//按分类查询
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.得到pc：如果页面传递，使用页面的，否则pc=1
		 * 2.得到url：。。。
		 * 3.获得查询条件，本方法就是cid，即分类的id
		 * 4.使用pc和cid调用service的findByCategory方法得到PageBean
		 * 5.给PageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		 * */
		
		//1.得到pc：如果页面传递，使用页面的，否则pc=1
		int pc=getPc(request);
		//2.得到url
		String url=getUrl(request);
		//3.获得查询条件，本方法就是cid，即分类的id
		String cid=request.getParameter("cid");
		//4.使用pc和cid调用service的findByCategory方法得到PageBean
		PageBean<Shose> pb=shoseService.findByCategory(cid, pc);
		//5.给PageBean设置url，保存pageBean，转发到/jsps/shose/list.jsp
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		request.getRequestDispatcher("/jsps/shose/list.jsp").forward(request, response); 
		return null;
	}

}
