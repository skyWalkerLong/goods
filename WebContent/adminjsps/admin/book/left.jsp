<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'left.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/left.css'/>">
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
<script language="javascript">
//1.对象名必须与第一个参数相同
//2.显示在菜单上的大标题
//
var bar = new Q6MenuBar("bar", "弄潮儿网上商城");
$(function() {
	bar.colorStyle = 2;//指定配色样式，一共0,1,2,3,4
	bar.config.imgDir = "<c:url value='/menu/img/'/>";//小工具所需图片路径
	bar.config.radioButton=true;//是否排斥,多个一级分类是否排斥
	/*
	1.参数1：一级分类名称，第一次出现一级分类时才会创建
	2.参数2：二级分类名称
	3.参数3：点击二级分类后连接到的URL
	4.参数4：；链接的内容在哪个框架页显示
	*/
	<c:forEach items="${parents}" var="parent">
		<c:forEach items="${parent.children}" var="child">
	bar.add("${parent.cname}", "${child.cname}", "/goods/admin/AdminBookServlet?mark=findByCategory&cid=${child.cid}", "body");
		</c:forEach>
	</c:forEach>
	$("#menu").html(bar.toString());
});
</script>
  </head>
  
  <body onload="load()">
<div id="menu"></div>
  </body>
</html>
