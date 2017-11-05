package cn.scu.goods.cart.domain;

import java.math.BigDecimal;

import cn.scu.goods.book.domin.Book;
import cn.scu.goods.user.doamin.User;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年7月5日 下午3:15:10
*/
public class CartItem {
	private String cartItemId;//主键
	private int quantity;//数量
	private Book book;//条目对应的图书
	private User user;//所属用户
	/**
	 * @return the cartItemId
	 */
	
	//添加小计计算
	public double getSubtotal(){
		//使用BIgDecimal不会有误差，要求必须使用String类型的构造器
		BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b2=new BigDecimal(quantity+"");
		BigDecimal b3=b1.multiply(b2);
		return b3.doubleValue();
		
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	/**
	 * @param cartItemId the cartItemId to set
	 */
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
