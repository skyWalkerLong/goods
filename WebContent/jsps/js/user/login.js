$(function() {
	/*
	 * 1. 让登录按钮在得到和失去焦点时切换图片
	 */
	$("#submit").hover(
		function() {
			$("#submit").attr("src", "/goods/images/login2.jpg");
		},
		function() {
			$("#submit").attr("src", "/goods/images/login1.jpg");
		}
	);
	
	$(".input").blur(function(){
		var id=$(this).attr("id");//获取当前失去焦点的输入框的id
		var funName="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//以字符串的形式得到方法名
		eval(funName);//eval可以将字符串当做js代码执行，现在的funName就是一个方法
	});
	
	//表单提交时校验
	$("#loginForm").submit(function(){
		var boolean=true;
		if(!validateLoginname()){
			boolean=false;
		}
		if(!validateLoginpass()){
			boolean=false;
		}
		if(!validateVerifyCode()){
			boolean=false;
		}
		return boolean;
	});
});

/*
 * 输入input名称，调用对应的validate方法。
 * 例如input名称为：loginname，那么调用validateLoginname()方法。
 */
function invokeValidateFunction(inputName) {
	inputName = inputName.substring(0, 1).toUpperCase() + inputName.substring(1);
	var functionName = "validate" + inputName;
	return eval(functionName + "()");	
}

/*
 * 校验登录名
 */
function validateLoginname() {
	var bool = true;
	$("#loginnameError").css("display", "none");
	var value = $("#loginname").val();
	if(!value) {// 非空校验
		$("#loginnameError").css("display", "");
		$("#loginnameError").text("用户名不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		$("#loginnameError").css("display", "");
		$("#loginnameError").text("用户名长度必须在3 ~ 20之间！");
		bool = false;
	}
	return bool;
}

/*
 * 校验密码
 */
function validateLoginpass() {
	var bool = true;
	$("#loginpassError").css("display", "none");
	var value = $("#loginpass").val();
	if(!value) {// 非空校验
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码长度必须在3 ~ 20之间！");
		bool = false;
	}
	return bool;
}

/*
 * 校验验证码
 */
function validateVerifyCode() {
	var bool = true;
	$("#verifyCodeError").css("display", "none");
	var value = $("#verifyCode").val();
	if(!value) {//非空校验
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("验证码不能为空！");
		bool = false;
	} else if(value.length != 4) {//长度不为4就是错误的
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("错误的验证码！");
		bool = false;
	} else {//验证码是否正确
		$.ajax({
			cache: false,
			async: false,
			type: "POST",
			dataType: "json",
			data:{method:"doPost",verifyCode:value,mark:"verifyCode"},
			url: "/goods/UserServlet",
			success: function(flag) {
				if(!flag) {
					$("#verifyCodeError").css("display", "");
					$("#verifyCodeError").text("错误的验证码！");
					bool = false;					
				}
			}
		});
	}
	return bool;
}
