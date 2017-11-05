package cn.scu.goods.book.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.book.domin.Book;
import cn.scu.goods.book.service.BookService;
import cn.scu.goods.pager.PageBean;

/**
 * Servlet implementation class BookServlet
 */
public class BookServlet extends HttpServlet {
	private BookService bookService=new BookService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			if(request.getParameter("mark").equals("findByCategory")){
				findByCategory(request, response);
			}
			if(request.getParameter("mark").equals("findByAuthor")){
				findByAuthor(request, response);
			}
			if(request.getParameter("mark").equals("findByPress")){
				findByPress(request, response);
			}
			if(request.getParameter("mark").equals("findByCombination")){
				findByCombination(request, response);
			}
			if(request.getParameter("mark").equals("findByBname")){
				findByBname(request, response);
			}
			if(request.getParameter("mark").equals("load")){
				findByBid(request, response);
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	
	//获取当前页的页码
	private int getPc(HttpServletRequest request){
		int pc=1;
		String param=request.getParameter("pc");
		System.out.println(param);
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
		System.out.println(pc);//测试pc的值
		//2.得到url
		String url=getUrl(request);
		//3.获得查询条件，本方法就是cid，即分类的id
		String cid=request.getParameter("cid");
		//4.使用pc和cid调用service的findByCategory方法得到PageBean
		PageBean<Book> pb=bookService.findByCategory(cid, pc);
		//5.给PageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		request.getRequestDispatcher("/jsps/book/list.jsp").forward(request, response); 
		return null;
	}
	
	//按作者查询
	public String findByAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//1.得到pc：如果页面传递，使用页面的，否则pc=1
		int pc=getPc(request);
		System.out.println(pc);//测试pc的值
		//2.得到url
		String url=getUrl(request);
		//3.获得查询条件，本方法就是cid，即分类的id
		String author=request.getParameter("author");
		//4.使用pc和cid调用service的findByCategory方法得到PageBean
		PageBean<Book> pb=bookService.findByAuthor(author, pc);
		//5.给PageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		request.getRequestDispatcher("/jsps/book/list.jsp").forward(request, response); 
		return null;
	}
	
	//按出版社查询
public String findByPress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//1.得到pc：如果页面传递，使用页面的，否则pc=1
		int pc=getPc(request);
		System.out.println(pc);//测试pc的值
		//2.得到url
		String url=getUrl(request);
		//3.获得查询条件，本方法就是cid，即分类的id
		String press=request.getParameter("press");
		//4.使用pc和cid调用service的findByCategory方法得到PageBean
		PageBean<Book> pb=bookService.findByPress(press, pc);
		//5.给PageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		request.getRequestDispatcher("/jsps/book/list.jsp").forward(request, response); 
		return null;
	}

//模糊查询
public String findByBname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	//1.得到pc：如果页面传递，使用页面的，否则pc=1
	int pc=getPc(request);
	System.out.println(pc);//测试pc的值
	//2.得到url
	String url=getUrl(request);
	//3.获得查询条件，本方法就是cid，即分类的id
	String bname=request.getParameter("bname");
	//4.使用pc和cid调用service的findByCategory方法得到PageBean
	PageBean<Book> pb=bookService.findByBname(bname, pc);
	//5.给PageBean设置url，保存pageBean，转发到/jsps/book/list.jsp
	pb.setUrl(url);
	request.setAttribute("pb", pb);
	request.getRequestDispatcher("/jsps/book/list.jsp").forward(request, response); 
	return null;
}

//多条件组合查询
public String findByCombination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	

	int pc=getPc(request);
	System.out.println(pc);//测试pc的值
	//2.得到url
	String url=getUrl(request);

	String cid=request.getParameter("cid");
	Book criteria=CommonUtils.toBean(request.getParameterMap(), Book.class);
	PageBean<Book> pb=bookService.findByCombination(criteria, pc);

	pb.setUrl(url);
	request.setAttribute("pb", pb);
	request.getRequestDispatcher("/jsps/book/list.jsp").forward(request, response); 
	return null;
}

//按bid查询
public String findByBid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String bid=request.getParameter("bid");
	Book book=bookService.load(bid);
	request.setAttribute("book", book);
	request.getRequestDispatcher("/jsps/book/desc.jsp").forward(request, response); 
	return null;
}

}
