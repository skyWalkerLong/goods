package cn.scu.goods.shose.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.scu.goods.book.domin.Book;
import cn.scu.goods.pager.Expression;
import cn.scu.goods.pager.PageBean;
import cn.scu.goods.pager.PageConstants;
import cn.scu.goods.shose.domain.Shose;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年8月15日 下午9:22:25
*/
public class ShoseDao {
	private QueryRunner qr=new TxQueryRunner();
	//按分类查询
	public PageBean<Shose> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("cid","=",cid));
		return findByCriteria(exprList,pc);
	}
	
	//通用查询方法
	private PageBean<Shose> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		/*
		 * 1.得到ps
		 * 2.得到tr
		 * 3.得到beanList
		 * 4，创建PageBean，返回
		 * */
		//1.得到ps
		int ps=PageConstants.GOODS_PAGE_SIZE;//每页记录数
		
		//2.通过exprList来生where子句
		StringBuilder whereSql=new StringBuilder(" where 1=1");
		List<Object> params =new ArrayList<Object>();//sql中有问号，它是对应问号的值
		for(Expression expr:exprList){
			/*
			 * 添加一个条件：
			 * 1.以and开头
			 * 2.条件的名称
			 * 3.条件的运算符，可以使=、>、<......或者is null ，is null就没有值了
			 * 4.如果条件运算符不是is null，再追加问号，然后再向params中添加一与问号对应的值
			 * */
			whereSql.append(" and ").append(expr.getName()).append(" ").append(expr.getOperator())
				.append(" ");
			if(!expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
			
		}
		//System.out.println(whereSql);//测试whereSql
		
		//3.总记录数
		String sql="select count(*) from t_shose"+whereSql;
		Number number=(Number)qr.query(sql,new ScalarHandler(),params.toArray());
		int tr=number.intValue();//得到了总记录数
		
		//4.得到beanList，即当前页记录
		sql="select *from t_shose"+whereSql+" order by orderBy limit ?,?";//根据orderBy列升序
		params.add((pc-1)*ps);//当前页第一行记录的下标
		params.add(ps);//一共查询几行，即每一页的记录数，这个查询语句的意思是：查出当前页的所有记录
		List<Shose> beanList=qr.query(sql, new BeanListHandler<Shose>(Shose.class),params.toArray());
		
		//5.创建PageBean，设置参数
		PageBean<Shose> pb=new PageBean<Shose>();
		//其中PageBean没有url，这个任务由Servlet完成
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}
}
