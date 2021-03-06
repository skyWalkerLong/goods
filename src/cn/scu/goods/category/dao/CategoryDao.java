package cn.scu.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.scu.goods.category.doamin.Category;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年6月21日 下午7:42:04
*/
//分类持久层
public class CategoryDao {
	private QueryRunner qr=new TxQueryRunner();
	//返回所有分类
	public List<Category> findAll() throws SQLException{
		//1.查询出所有一级分类
		String sql="select *from t_category where pid is null order by orderBy";
		List<Map<String,Object>> mapList=qr.query(sql,new MapListHandler());
		List<Category> parents=toCategoryList(mapList);
		//2.循环遍历所有一级分类，为每个一级分类加载它的二级分类
		for(Category parent:parents){
			//查询当前父分类的所有子分类
			List<Category> children=findByParent(parent.getCid());
			//设置父分类
			parent.setChildren(children);
		}
		return parents;
	}
	
	//把一个Map中的数据映射到category中
	private Category toCategory(Map<String,Object> map){
		/*
		 * map{cid:xx,cname:xx,pid:xx,desc:xx,orderBy:xx}
		 * Category{cid:xx,cname:xx,parent:xx,desc:xx}
		 * */
		Category category=CommonUtils.toBean(map, Category.class);
		String pid=(String)map.get("pid");//若为一级分类，pid=null
		if(pid!=null){//若父分类id不为空
			/*
			 * 使用一个父分类对象来装载pid
			 * 再把父分类设置给category
			 * */
			Category parent=new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	//可以把多个Map映射成多个Category
	private List<Category> toCategoryList(List<Map<String,Object>> mapList){
		List<Category> categoryList=new ArrayList<Category>();
		for(Map<String,Object> map:mapList){
			Category c=toCategory(map);
			categoryList.add(c);
		}
		return categoryList;
	}
	
	//通过父分类查询子分类
	public List<Category> findByParent(String pid) throws SQLException{
		String sql="select *from t_category where pid=?";
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
	
	//添加分类
	public void add(Category category) throws SQLException{
		String sql="insert t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		//一级分类和二级分类都可以添加
		String pid=null;//一级分类
		if(category.getParent()!=null){
			pid=category.getParent().getCid();
		}
		Object[] params={category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql,params);
	}
	
	//获取所有父分类不带其子分类
	public List<Category> findParents() throws SQLException{
		//1.查询出所有一级分类
		String sql="select *from t_category where pid is null order by orderBy";
		List<Map<String,Object>> mapList=qr.query(sql,new MapListHandler());
		return toCategoryList(mapList);
	
	}
	
	/**
	 * 加载分类
	 * 即可加载一级分类，也可加载二级分类
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException {
		String sql = "select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(), cid));
	}
	
	/**
	 * 修改分类
	 * 即可修改一级分类，也可修改二级分类
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException {
		String sql = "update t_category set cname=?, pid=?, `desc`=? where cid=?";
		String pid = null;
		if(category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(), pid, category.getDesc(), category.getCid()};
		qr.update(sql, params);
	}
	
	/**
	 * 查询指定父分类下子分类的个数
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public int findChildrenCountByParent(String pid) throws SQLException {
		String sql = "select count(*) from t_category where pid=?";
		Number cnt = (Number)qr.query(sql, new ScalarHandler(), pid);
		return cnt == null ? 0 : cnt.intValue();
	}
	
	/**
	 * 删除分类
	 * @param cid
	 * @throws SQLException 
	 */
	public void delete(String cid) throws SQLException {
		String sql = "delete from t_category where cid=?";
		qr.update(sql, cid);
	}
}
