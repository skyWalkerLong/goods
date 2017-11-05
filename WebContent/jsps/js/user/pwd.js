$(function() {	
	$(".input").blur(function(){
		var id=$(this).attr("id");//获取当前失去焦点的输入框的id
		var funName="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//以字符串的形式得到方法名
		eval(funName);//eval可以将字符串当做js代码执行，现在的funName就是一个方法
	});
	
	//表单提交时校验
	$("#updatePasswordForm").submit(function(){
		var boolean=true;

		if(!validateLoginpass()){
			boolean=false;
		}
		if(!validateNewpass()){
			boolean=false;
		}
		if(!validateReloginpass()){
			boolean=false;
		}
		if(!validateVerifyCode()){
			boolean=false;
		}
		return boolean;
	});
});



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

// 校验新密码
function validateNewpass() {
	var bool = true;
	$("#newpassError").css("display", "none");
	var value = $("#newpass").val();
	if(!value) {// 非空校验
		$("#newpassError").css("display", "");
		$("#newpassError").text("新密码不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		$("#newpassError").css("display", "");
		$("#newpassError").text("新密码长度必须在3 ~ 20之间！");
		bool = false;
	}
	return bool;
}

/*
 * 校验确认密码
 */
function validateReloginpass() {
	var bool = true;
	$("#reloginpassError").css("display", "none");
	var value = $("#reloginpass").val();
	if(!value) {// 非空校验
		$("#reloginpassError").css("display", "");
		$("#reloginpassError").text("确认密码不能为空！");
		bool = false;
	} else if(value != $("#newpass").val()) {//两次输入是否一致
		$("#reloginpassError").css("display", "");
		$("#reloginpassError").text("两次密码输入不一致！");
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
		$("#verifyCodeError").text("验证码长度不符！");
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
