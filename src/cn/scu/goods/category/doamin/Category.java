package cn.scu.goods.category.doamin;

import java.util.List;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年6月21日 下午7:41:42
*/

//分类模块的实体类
public class Category {
	private String cid;//主键
	private String cname;//分类名称
	private Category parent;//父分类
	private String desc;//分类描述
	private List<Category> children;//子分类
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @return the parent
	 */
	public Category getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Category parent) {
		this.parent = parent;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the children
	 */
	public List<Category> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Category> children) {
		this.children = children;
	}
	
	
}
