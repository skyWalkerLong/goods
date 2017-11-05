package cn.scu.goods.shose.domain;

import cn.scu.goods.category.doamin.Category;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年8月15日 下午9:21:59
*/
public class Shose {
	private String sid;
	private String sname;
	private double price;
	private double curPrice;
	private double discount;
	private int size;
	private Category category;
	private String image_b;
	private String image_s;
	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}
	/**
	 * @return the sname
	 */
	public String getSname() {
		return sname;
	}
	/**
	 * @param sname the sname to set
	 */
	public void setSname(String sname) {
		this.sname = sname;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the curPrice
	 */
	public double getCurPrice() {
		return curPrice;
	}
	/**
	 * @param curPrice the curPrice to set
	 */
	public void setCurPrice(double curPrice) {
		this.curPrice = curPrice;
	}
	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * @return the image_b
	 */
	public String getImage_b() {
		return image_b;
	}
	/**
	 * @param image_b the image_b to set
	 */
	public void setImage_b(String image_b) {
		this.image_b = image_b;
	}
	/**
	 * @return the image_s
	 */
	public String getImage_s() {
		return image_s;
	}
	/**
	 * @param image_s the image_s to set
	 */
	public void setImage_s(String image_s) {
		this.image_s = image_s;
	}
	
}
