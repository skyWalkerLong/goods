package cn.scu.goods.shose.service;

import java.sql.SQLException;

import cn.scu.goods.book.domin.Book;
import cn.scu.goods.pager.PageBean;
import cn.scu.goods.shose.dao.ShoseDao;
import cn.scu.goods.shose.domain.Shose;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年8月15日 下午9:22:55
*/
public class ShoseService {
	private ShoseDao shoseDao=new ShoseDao();
	
	//按分类查
	public PageBean<Shose> findByCategory(String cid,int pc){
		try {
			return shoseDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
