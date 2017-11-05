package cn.scu.goods.user.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.scu.goods.user.doamin.User;
import cn.scu.goods.user.service.UserService;
import cn.scu.goods.user.service.exception.UserException;
//UserServlet用来接收客户端请求，处理与WEB相关的问题。例如获取客户端的请求参数，
//然后转发或重定向等。在UserServlet中完成业务功能需要使用UserService，
//所以我们需要在UserServlet中给出一个UserService的成员。
public class UserServlet extends HttpServlet {
	private UserService userService = new UserService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet()......");
		if(request.getParameter("mark").equals("activation")){
			
			activation(request, response);
		}
		if(request.getParameter("mark").equals("quit")){
			
			quit(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("mark").equals("loginname")){
			ajaxValidateLoginname(request, response);
		}
		if(request.getParameter("mark").equals("email")){
			ajaxValidateEmail(request, response);
		}
		if(request.getParameter("mark").equals("verifyCode")){
			ajaxValidateVerifyCode(request, response);
		}
		if(request.getParameter("mark").equals("regist")){
			
			regist(request, response);
		}
		if(request.getParameter("mark").equals("login")){
			
			login(request, response);
		}
		if(request.getParameter("mark").equals("updatePassword")){
			
			updatePassword(request, response);
		}

		System.out.println("doPost()......");
		doGet(request, response);
	}
	
	//修改密码功能
	public String updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.封装表单数据到user中
		 * 2.从session中获取uid
		 * 		如果没登录，跳转到登陆页面登陆
		 * 3.使用uid和表单提交的新旧密码来调用service方法
		 * 		如果出现异常，保存异常信息到request，转发到pwd.jsp
		 * 4.保存成功信息到request
		 * 5.转发到msg.jsp
		 * */
		User formUser =CommonUtils.toBean(request.getParameterMap(), User.class);
		//校验之，如果校验失败，保存错误信息，返回到pwd.jsp显示
				Map<String,String> errors=validateUpdatePassword(formUser,request.getSession());
				System.out.println(errors.size());
				if(errors.size()>0){
					request.setAttribute("user", formUser);
					request.setAttribute("errors", errors);
					request.getRequestDispatcher("/jsps/user/pwd.jsp").forward(request, response); 
					return null;
				}
				
		User user=(User) request.getSession().getAttribute("sessionUser");
		if(user==null){
			request.setAttribute("msg", "您还没有登陆!");//使用转发是为了保存request中的信息
			request.getRequestDispatcher("/jsps/user/login.jsp").forward(request, response);
			return null;
		}
		try{
			userService.updatePassword(user.getUid(),formUser.getNewpass(), formUser.getLoginpass());
			request.setAttribute("msg", "修改密码成功！");
			request.setAttribute("code", "success");
			request.getRequestDispatcher("/jsps/msg.jsp").forward(request, response);
		}catch(UserException e){
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", formUser);//为了回显
			request.getRequestDispatcher("/jsps/user/pwd.jsp").forward(request, response);
			return null;
		}
		
		return null;
	}
	
	//登录功能
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.封装表单数据到User
		 * 2.校验表单数据
		 * 3.使用service查询，得到User
		 * 4.查看用户是否存在，如果不存在
		 * 		保存错误信息：用户名或密码错误
		 * 		保存用户数据：为了回显
		 * 		转发到login.jsp
		 * 5.如果存在，查看状态，如果状态为false
		 * 		保存错误信息：您没有激活
		 * 		保存表单数据：为了回显
		 * 		转发到login.jsp
		 * 6.登陆成功：
		 * 		保存当前查询出的user到session中
		 * 		保存当前用户的名称到cookie，注意中文需要编码处理
		 * 		
		 * */
		
		//1.封装表单数据到User
		User formUser =CommonUtils.toBean(request.getParameterMap(), User.class);
		System.out.println("login()...start");
		//2.校验
		Map<String,String> errors=validatelogin(formUser,request.getSession());
		if(errors.size()>0){
			request.setAttribute("user", formUser);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/jsps/user/login.jsp").forward(request, response);
			return null;
		}
		//3.调用userService的login()方法
		User user=userService.login(formUser);
		//4.开始判断
		if(user==null){
			request.setAttribute("msg", "用户名或密码错误！");
			request.setAttribute("user", formUser);
			request.getRequestDispatcher("/jsps/user/login.jsp").forward(request, response);
			return null;
		}else{
			if(!user.isStatus()){
				request.setAttribute("msg", "您没有激活！");
				request.setAttribute("user", formUser);
				request.getRequestDispatcher("/jsps/user/login.jsp").forward(request, response);
				return null;
			}else{
				//保存用户到session中
				request.getSession().setAttribute("sessionUser", user);
				//获取用户名保存到cookie中
				String loginname=user.getLoginname();
				loginname=URLEncoder.encode(loginname,"utf-8");
				Cookie cookie=new Cookie("loginname",loginname);
				cookie.setMaxAge(60*60*24*5);//保存五天
				response.addCookie(cookie);
				response.sendRedirect("/goods/index.jsp");   //重定向到主页
				return null;
				
			}
		}
	}
	
	private Map<String,String> validatelogin(User formUser,HttpSession session){
		Map<String,String> errors=new HashMap<String,String>();
		//1.登录名校验
				String loginname=formUser.getLoginname();
				if(loginname==null||loginname.trim().isEmpty()){
					errors.put("loginname","用户名不能为空！");
				}else if(loginname.length()<3||loginname.length()>20){
					errors.put("loginname", "用户名长度必须在3-20之间！");
				}
				
				//2.登录密码校验
				String loginpass=formUser.getLoginpass();
				if(loginpass==null||loginpass.trim().isEmpty()){
					errors.put("loginpass","密码不能为空！");
				}

				//3.验证码校验
				String verifyCode=formUser.getVerifyCode();
				String vcode=(String) session.getAttribute("vCode");
				if(verifyCode==null||verifyCode.trim().isEmpty()){
					errors.put("verifyCode","验证码不能为空！");
				}else if(!verifyCode.equalsIgnoreCase(vcode)){
					errors.put("verifyCode","验证码错误！");
				}
				return errors;
			}		
	
	//注册表单提交操作
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("111");
		//1.封装表单数据到User对象
		User formUser =CommonUtils.toBean(request.getParameterMap(), User.class);
		
		//2.校验之，如果校验失败，保存错误信息，返回到regist.jsp显示
		Map<String,String> errors=validateRegist(formUser,request.getSession());
		System.out.println(errors.size());
		if(errors.size()>0){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/jsps/user/regist.jsp").forward(request, response); 
			return null;
		}
		//3.使用service完成业务
		
		userService.regist(formUser);
		//4.保存成功信息，转发到msg.jsp显示
		
		request.setAttribute("code","success");
		request.setAttribute("msg", "注册成功，请马上到邮箱激活！");
		request.getRequestDispatcher("/jsps/msg.jsp").forward(request, response); 
		return null;
	}	
	
	//后台注册校验
	private Map<String,String> validateRegist(User formUser,HttpSession session){
		Map<String,String> errors=new HashMap<String,String>();
		//1.登录名校验
		String loginname=formUser.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errors.put("loginname","用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errors.put("loginname", "用户名长度必须在3-20之间！");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname","用户名已被注册！" );
		}
		System.out.print("validateRegist ok!");
		
		//2.登录密码校验
		String loginpass=formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errors.put("loginpass","密码不能为空！");
		}
		
		//3.确认密码校验
		String reLoginpass=formUser.getReloginpass();
		if(reLoginpass==null||reLoginpass.trim().isEmpty()){
			errors.put("reLoginpass","确认密码不能为空！");
		}else if(!(loginpass.equals(reLoginpass))){
			errors.put("reLoginpass","两次输入密码不一致！");
		}
		
		//4.email校验
		String email=formUser.getEmail();
		if(email==null||email.trim().isEmpty()){
			errors.put("email","邮箱不能为空！");
		}else if(!email.matches("^\\w+@\\w+.\\w+$")){
			errors.put("email", "邮箱格式错误!");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email","邮箱已被注册！" );
		}
		
		
		//5.验证码校验
		String verifyCode=formUser.getVerifyCode();
		String vcode=(String) session.getAttribute("vCode");
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode","验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode","验证码错误！");
		}
		return errors;
	}
	
	//后台修改密码校验
	private Map<String,String> validateUpdatePassword(User formUser,HttpSession session){
		Map<String,String> errors=new HashMap<String,String>();
		//1.旧密码校验
		String loginpass=formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errors.put("loginpass","旧密码不能为空！");
		}
		
		//2.新密码校验
		String newpass=formUser.getNewpass();
		if(newpass==null||newpass.trim().isEmpty()){
			errors.put("newpass","新密码不能为空！");
		}
		
		//3.确认新密码校验
		String reLoginpass=formUser.getReloginpass();
		if(reLoginpass==null||reLoginpass.trim().isEmpty()){
			errors.put("reLoginpass","确认密码不能为空！");
		}else if(!(newpass.equals(reLoginpass))){
			errors.put("reLoginpass","两次输入密码不一致！");
		}
		
		
		//4.验证码校验
		String verifyCode=formUser.getVerifyCode();
		String vcode=(String) session.getAttribute("vCode");
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode","验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode","验证码错误！");
		}
		return errors;
	}
	
	
	//激活功能
	public String activation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取激活码
		 * 2.用激活码调用service方法激活，service方法有可能抛出异常，把异常信息保存到request中，转发到msg.jsp中
		 * 3.保存成功信息到到request，转发到msg.jsp
		 * */
		String code=request.getParameter("activationCode");
		try {
			userService.activation(code);
			request.setAttribute("code", "success");
			request.setAttribute("msg", "恭喜，激活成功，请马上登录！");
		} catch (UserException e) {
			//说明激活有问题，抛出了异常
			request.setAttribute("code", "error");
			request.setAttribute("msg", e.getMessage());
		}
		request.getRequestDispatcher("/jsps/msg.jsp").forward(request, response);
		return null;
	}
	
	//退出功能
	public void quit(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.getSession().invalidate();
		System.out.println("已退出！");
		response.sendRedirect("/goods/jsps/user/login.jsp");
	}
	
	//用户名是否注册校验
	public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获取用户名
		String loginname=request.getParameter("loginname");
		//2.通过service得到校验结果
		boolean b=userService.ajaxValidateLoginname(loginname);
		System.out.println("loginname...");//验证语句，可删，下两个函数一样
		//3.发给客户端
		response.getWriter().print(b);
		return null;
	}	
	//email是否注册校验
	public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获取邮箱名
		String email=request.getParameter("email");
		//2.通过service得到校验结果
		boolean b=userService.ajaxValidateEmail(email);
		System.out.println("email...");
		//3.发给客户端
		response.getWriter().print(b);
		return null;
	}	
	//验证码是否正确校验
	public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获取输入框中的验证码
		String verifyCode=request.getParameter("verifyCode");
		System.out.println("verifyCode...");
		//2.获取图片中的验证码
		String vCode=(String)request.getSession().getAttribute("vCode");
		//3.进行忽略大小的比较，得到结果
		boolean b=verifyCode.equalsIgnoreCase(vCode);
		//4.发送给客户端
		response.getWriter().print(b);
		return null;
	}

}
