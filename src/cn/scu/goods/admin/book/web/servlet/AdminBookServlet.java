package cn.scu.goods.admin.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.book.domin.Book;
import cn.scu.goods.book.service.BookService;
import cn.scu.goods.category.doamin.Category;
import cn.scu.goods.category.service.CategoryService;
import cn.scu.goods.pager.PageBean;

/**
 * Servlet implementation class AdminBookServlet
 */
public class AdminBookServlet extends HttpServlet {
	private BookService bookService=new BookService();
	private CategoryService categoryService=new CategoryService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	if(request.getParameter("mark").equals("findCategoryAll")){
			
		findCategoryAll(request, response);
		}
	
	if(request.getParameter("mark").equals("findByCategory")){
		findByCategory(request, response);
	}
	if(request.getParameter("mark").equals("findByAuthor")){
		findByAuthor(request, response);
	}
	if(request.getParameter("mark").equals("findByPress")){
		findByPress(request, response);
	}

	if(request.getParameter("mark").equals("findByBname")){
		findByBname(request, response);
	}
	if(request.getParameter("mark").equals("load")){
		load(request, response);
	}
	if(request.getParameter("mark").equals("addPre")){
		addPre(request, response);
	}
	if(request.getParameter("mark").equals("ajaxFindChildren")){
		ajaxFindChildren(request, response);
	}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("edit")){
			edit(request, response);
		}
		
		if(request.getParameter("mark").equals("delete")){
			delete(request, response);
		}
		
		if(request.getParameter("mark").equals("findByCombination")){
			findByCombination(request, response);
		}
		
		doGet(request, response);
	}
	
	
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		
		/*
		 * 删除图片
		 */
		Book book = bookService.load(bid);
		String savepath = this.getServletContext().getRealPath("/");//获取真实的路径
		new File(savepath, book.getImage_w()).delete();//删除文件
		new File(savepath, book.getImage_b()).delete();//删除文件
		
		bookService.delete(bid);//删除数据库的记录
		
		req.setAttribute("msg", "删除图书成功！");
		req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req, resp);
		return null;
	}
	
	//修改图书
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 把表单数据封装到Book对象中
		 * 2. 封装cid到Category中
		 * 3. 把Category赋给Book
		 * 4. 调用service完成工作
		 * 5. 保存成功信息，转发到msg.jsp
		 */
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		bookService.edit(book);
		req.setAttribute("msg", "修改图书成功！");
		req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req, resp);
		return null;
	}
	
	/**
	 * 加载图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取bid，得到Book对象，保存之
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		
		/*
		 * 2. 获取所有一级分类，保存之
		 */
		req.setAttribute("parents", categoryService.findParents());
		/*
		 * 3. 获取当前图书所属的一级分类下所有2级分类
		 */
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findChildren(pid));
		
		/*
		 * 4. 转发到desc.jsp显示
		 */
		req.getRequestDispatcher("/adminjsps/admin/book/desc.jsp").forward(req, resp);
		return null;
	}
	
	/**
	 * 添加图书：第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取所有一级分类，保存之
		 * 2. 转发到add.jsp，该页面会在下拉列表中显示所有一级分类
		 */
		List<Category> parents = categoryService.findParents();
		req.setAttribute("parents", parents);
		req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req, resp);
		return null;
	}
	
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取pid
		 * 2. 通过pid查询出所有2级分类
		 * 3. 把List<Category>转换成json，输出给客户端
		 */
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
		String json = toJson(children);
		resp.getWriter().print(json);
		return null;
	}
	
	// {"cid":"fdsafdsa", "cname":"fdsafdas"}
	private String toJson(Category category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	// [{"cid":"fdsafdsa", "cname":"fdsafdas"}, {"cid":"fdsafdsa", "cname":"fdsafdas"}]
	private String toJson(List<Category> categoryList) {
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i < categoryList.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch(RuntimeException e) {}
		}
		return pc;
	}
	
	/**
	 * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
	 * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，如果不存在那就不用截取。
		 */
		int index = url.lastIndexOf("&pc=");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	protected String findCategoryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//查询所有分类
		List<Category> parents=categoryService.findAll();
		request.setAttribute("parents", parents);
		request.getRequestDispatcher("/adminjsps/admin/book/left.jsp").forward(request, response);
		return null;
	}
	
	
	
	/**
	 * 按分类查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		String cid = req.getParameter("cid");
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp); 
		return null;
	}
	
	/**
	 * 按作者查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		String author = req.getParameter("author");
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp);
		return null;
	}
	
	/**
	 * 按出版社查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		String press = req.getParameter("press");
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByPress(press, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp);
		return null;
	}
	
	/**
	 * 按图名查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		String bname = req.getParameter("bname");
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp);
		return null;
	}
	
	/**
	 * 多条件组合查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp);
		return null;
	}

}
