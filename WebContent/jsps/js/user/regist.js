$(function(){
	$(".labelError").each(function(){//注册时错误信息的显示
		showError($(this));
	});
	
	//光标移动到注册按钮时切换注册按钮，光标移到注册按钮上，就调用hover事件
	$("#submit").hover(
			function(){
				$("#submit").attr("src","/goods/images/regist2.jpg");
			},
			function(){
				$("#submit").attr("src","/goods/images/regist1.jpg");
			}
	);
	
	//输入框得到焦点时隐藏错误
	$(".input").focus(function(){
		var lableId=$(this).attr("id")+"Error";//通过输入框找到对应lableError的id
		$("#"+lableId).text("");//把lable信息清空
		showError($("#"+lableId));//隐藏没有信息的lable
	});
	
	//输入框失去焦点时显示错误
	$(".input").blur(function(){
		var id=$(this).attr("id");//获取当前失去焦点的输入框的id
		var funName="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//以字符串的形式得到方法名
		eval(funName);//eval可以将字符串当做js代码执行，现在的funName就是一个方法
	});
	
	//表单提交时校验
	$("#registForm").submit(function(){
		var boolean=true;
		if(!validateLoginname()){
			boolean=false;
		}
		if(!validateLoginpass()){
			boolean=false;
		}
		if(!validateReloginpass()){
			boolean=false;
		}
		if(!validateEmail()){
			boolean=false;
		}
		if(!validateVerifyCode()){
			boolean=false;
		}
		return boolean;
	});
});

//登录名校验方法
function validateLoginname(){
	var id="loginname";
	var value=$("#"+id).val();//jquery中获取输入框内容的方式
	//1.非空校验
	if(!value){
		$("#"+id+"Error").text("用户名不能为空!");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.长度校验
	if(value.length<3||value.length>20){
		$("#"+id+"Error").text("用户名长度必须在3-20之间!");
		showError($("#"+id+"Error"));
		return false;
	}
	//3.是否注册校验
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"doPost",loginname:value,mark:"loginname"},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("用户名已被注册!");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
}
//密码校验
function validateLoginpass(){
	var id="loginpass";
	var value=$("#"+id).val();//jquery中获取输入框内容的方式
	//1.非空校验
	if(!value){
		$("#"+id+"Error").text("密码不能为空!");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
}

function validateReloginpass(){
	var id="reloginpass";
	var value=$("#"+id).val();//jquery中获取输入框内容的方式
	//1.非空校验
	if(!value){
		$("#"+id+"Error").text("确认密码不能为空!");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.两次密码的一致性校验
	if(value!=$("#loginpass").val()){
		$("#"+id+"Error").text("两次输入的密码不一致!");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
}

function validateEmail(){
	var id="email";
	var value=$("#"+id).val();//jquery中获取输入框内容的方式
	//1.非空校验
	if(!value){
		$("#"+id+"Error").text("注册邮箱不能为空!");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.格式校验
	if(!/^\w+@\w+\.\w+$/.test(value)){
		$("#"+id+"Error").text("email格式错误");
		showError($("#"+id+"Error"));
		return false;
	}
	//3.是否注册校验
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"doPost",email:value,mark:"email"},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("邮箱已被注册!");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	return true;
}

function validateVerifyCode(){
	var id="verifyCode";
	var value=$("#"+id).val();//jquery中获取输入框内容的方式
	//1.非空校验
	if(!value){
		$("#"+id+"Error").text("验证码不能为空!");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.长度校验
	if(value.length!=4){
		$("#"+id+"Error").text("验证码必须为4位!");
		showError($("#"+id+"Error"));
		return false;
	}
	//3.验证码是否正确
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"doPost",verifyCode:value,mark:"verifyCode"},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("验证码错误!");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	return true;
}

function showError(ele){
	var text=ele.text();
	if(!text){
		ele.css("display","none");
	}else{
		ele.css("display","");
	}
}

function _hyz(){
	$("#vCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());//换一张时已经输入的信息不能消失，感觉有点像ajax
}

