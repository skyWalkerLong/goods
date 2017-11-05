package cn.scu.goods.cart;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.book.domin.Book;
import cn.scu.goods.cart.domain.CartItem;
import cn.scu.goods.cart.service.CartItemService;
import cn.scu.goods.user.doamin.User;

/**
 * Servlet implementation class CartItemServlet
 */
public class CartItemServlet extends HttpServlet {
	private CartItemService cartItemService=new CartItemService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("myCart")){
			myCart(request,response);
		}
		if(request.getParameter("mark").equals("add")){
			add(request,response);
		}
		if(request.getParameter("mark").equals("batchDelete")){
			batchDelete(request,response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("updateQuantity")){
			updateQuantity(request,response);
		}
		if(request.getParameter("mark").equals("loadCartItems")){
			loadCartItems(request,response);
		}
		doGet(request, response);
	}
	
	/**
	 * 加载多个CartItem
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取cartItemIds参数
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		double total = Double.parseDouble(req.getParameter("total"));
		/*
		 * 2. 通过service得到List<CartItem>
		 */
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 3. 保存，然后转发到/cart/showitem.jsp
		 */
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		req.getRequestDispatcher("/jsps/cart/showitem.jsp").forward(req, resp);
	}
	
	//购物车同一本书的数量的"+""-"实现
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemId = req.getParameter("cartItemId");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		
		// 给客户端返回一个json对象
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");

		resp.getWriter().print(sb);
		return null;
	}
	
	/**
	 * 批量删除功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public void batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取cartItemIds参数
		 * 2. 调用service方法完成工作
		 * 3. 返回到list.jsp
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		myCart(req, resp);
	}
	
	//添加购物车条目
	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.封装表单数据到CartItem(bid,quantity)
		Map map=request.getParameterMap();
		CartItem cartItem=CommonUtils.toBean(map,CartItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		User user=(User)request.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		//调用service完成添加
		cartItemService.add(cartItem);
		
		//查询出当前用户的所有条目，转发到list.jsp显示
		
		myCart(request,response);
	}
	
	
	public void myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到uid
		User user=(User)request.getSession().getAttribute("sessionUser");
		String uid=user.getUid();
		//2.通过service得到当前用户的所有购物车条目
		List<CartItem> cartItemList=cartItemService.myCart(uid);
		//3.保存起来，转发到/cart/list.jsp
		request.setAttribute("cartItemList", cartItemList);
		request.getRequestDispatcher("/jsps/cart/list.jsp").forward(request, response);
	}

}
