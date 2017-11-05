package cn.scu.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.scu.goods.category.dao.CategoryDao;
import cn.scu.goods.category.doamin.Category;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年6月21日 下午7:42:33
*/
public class CategoryService {
	private CategoryDao categoryDao=new CategoryDao();
	
	//添加分类
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//查询所有分类
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//获取所有父分类，不带子分类
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//修改分类
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//加载分类
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询指定父分类下子分类的个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCountByParent(String pid) {
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 删除分类
	 * @param cid
	 */
	public void delete(String cid) {
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询指定父分类下的所有子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findChildren(String pid) {
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
