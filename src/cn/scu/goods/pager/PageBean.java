package cn.scu.goods.pager;

import java.util.List;

/**
*@author longchao
 * @param <T>
*@E-mail:353158647@qq.com
*@time:2016年6月23日 下午8:05:18
*/

//分页Bean，他会在各层传递
public class PageBean<T> {
	private int pc;//当前页码
	private int tr;//总记录数
	private int ps;//每页记录数
	private String url;//请求路径和参数
	private List<T> beanList;
	
	public int getTp(){
		int tp=tr/ps;
		return tr%ps==0?tp:tp+1;
	}
	/**
	 * @return the pc
	 */
	public int getPc() {
		return pc;
	}
	/**
	 * @param pc the pc to set
	 */
	public void setPc(int pc) {
		this.pc = pc;
	}
	/**
	 * @return the tr
	 */
	public int getTr() {
		return tr;
	}
	/**
	 * @param tr the tr to set
	 */
	public void setTr(int tr) {
		this.tr = tr;
	}
	/**
	 * @return the ps
	 */
	public int getPs() {
		return ps;
	}
	/**
	 * @param ps the ps to set
	 */
	public void setPs(int ps) {
		this.ps = ps;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the beanList
	 */
	public List<T> getBeanList() {
		return beanList;
	}
	/**
	 * @param beanList the beanList to set
	 */
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
	
	
}
