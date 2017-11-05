<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%--图书显示页面 --%>
<html>
  <head>
    <title>鞋子列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/book/list.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />
    <script type="text/javascript" src="<c:url value='/jsps/pager/pager.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/jsps/js/book/list.js'/>"></script>
  </head>
  
  <body>

<ul>
<c:forEach items="${pb.beanList }" var="shose">
  <li>
  <div class="inner">
    <a class="pic" href="<c:url value='/ShoseServlet?mark=load&sid=${shose.sid }'/>"><img src="<c:url value='/${shose.image_s }'/>" border="0"/></a>
    <p class="price">
		<span class="price_n">&yen;${shose.curPrice }</span>
		<span class="price_r">&yen;${shose.price }</span>
		(<span class="price_s">${shose.discount }折</span>)
	</p>
	<p><a id="bookname" title="${shose.sname }" href="<c:url value='/BookServlet?mark=load&sid=${shose.sid }'/>">${shose.sname }</a></p>
	

	


	<p class="publishing">
		<span>尺码：</span>${shose.size }
	</p>
  </div>
  </li>
  </c:forEach>








  



</ul>

<div style="float:left; width: 100%; text-align: center;">
	<hr/>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>

  </body>
 
</html>

