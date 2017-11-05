package cn.scu.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.scu.goods.user.dao.UserDao;
import cn.scu.goods.user.doamin.User;
import cn.scu.goods.user.service.exception.UserException;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年6月7日 上午10:37:45
*UserService封装了业务功能，在UserService中每个方法对应一个业务功能，
*例如：注册方法、登录方法等等。一个业务方法可能需要多次调用DAO中的方法！
*所以，Service依赖Dao，我们需要在UserService中给出一个UserDao类型的成员。
*/
public class UserService {
	private UserDao userDao = new UserDao();
	
	//修改密码
	public void updatePassword(String uid,String newPass,String oldPass) throws UserException{
		try{
			//1.校验老密码
			boolean bool=userDao.findByUidAndPassLoginpass(uid, oldPass);
			if(!bool){
				throw new UserException("老密码错误!");
			}
			
			//2.修改密码
			userDao.updatePassword(uid, newPass);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	//登录功能
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//激活功能
	public void activation(String code) throws UserException{
		/*
		 * 1.通过激活码查询用户
		 * 2.如果User为null，说明是无效的激活码，抛出异常，给出异常信息
		 * 3.如果用户状态是否为true，如果为true，说明已经激活过了，抛出异常，给出异常信息
		 * 3.修改用户状态为true
		 * */
		try{
			User user=userDao.findByCode(code);
			if(user==null){
				throw new UserException("无效的激活码！");
			}
			if(user.isStatus()){
				throw new UserException("你已经激活过了！");
			}
			userDao.updateStatus(user.getUid(), true);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	//用户名注册校验
	public boolean ajaxValidateLoginname(String loginname){
		try{
			return userDao.ajaxValidateLoginname(loginname);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	//email校验
	public boolean ajaxValidateEmail(String email){
		try{
			return userDao.ajaxValidateEmail(email);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void regist(User user){
		//1.数据的补齐
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//2.向数据库插入数据
		try{
			userDao.add(user);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		//3.发邮件
		
		//把配置文件内容加载到prop中
		Properties prop=new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//登录服务器，得到session
		String host=prop.getProperty("host");
		String name=prop.getProperty("username");
		String pass=prop.getProperty("password");
		Session session=MailUtils.createSession(host, name, pass);
		
		//创建Mail对象
		String from=prop.getProperty("from");
		String to=user.getEmail();
		String subject=prop.getProperty("subject");
		//将content中的占位符替换为激活码
		String content=MessageFormat.format(prop.getProperty("content"),user.getActivationCode());
		Mail mail=new Mail(from,to,subject,content);
		
		//发送邮件
		try {

				MailUtils.send(session, mail);

			
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
		
	}
}
