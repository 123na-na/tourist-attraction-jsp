<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="css/cssHeaderFooter.css">
	
<div class="header">
	<div style="width:900px; margin:0 auto;">
		<div class="headerTitle">
			<img src="${pageContext.request.contextPath}/image/logoGoods0.png" style="width:225px; height:113px;">
			<img src="${pageContext.request.contextPath}/image/logoGoods3.png" style="width:225px; height:60px;">    	
			<!-- 上网搜索并访问“在线生成艺术字”网站，生成艺术字图片时建议设置背景透明，
				将图片另存到本地后，再复制到Word，然后在Word中裁剪、缩放图片，
				最后另存为网页（*.htm;*.html），所需艺术字图片保存在该网页的文件夹“网页名.files”中 -->		
		</div>
		
		<c:if test="${myUser.role == null }">		<!-- 未登录 -->
			<div class="headerUsername">
				<br><br><br>&emsp;&emsp;
				<a href='GoodsListLayout'>商品列表</a>&emsp;&emsp;
				<a href="login.jsp">用户登录</a>&emsp;&emsp;
				<a href="Register">用户注册</a>
			</div>
		</c:if>
		
		<c:if test="${myUser.role == 'guest' }">			<!-- 无管理权限的注册用户，角色为guest -->
			<div class="headerUsername">
				<br><br><br>
				用户：<span style="color:#800;">${ myUser.username }</span>&emsp;&emsp;
				<a href='GoodsListLayout'>商品列表</a>&emsp;
				购物车&emsp;
				订单&emsp;
				<a href="main.jsp">用户功能</a>&emsp;
				<a href="Logout">退出</a>
			</div>
		</c:if>
		
		<c:if test="${myUser.role == 'admin'||myUser.role == 'user'||myUser.role == 'group'}">				<!-- 有管理权限，角色role为“admin”或“user” -->
			<div class="headerUsername">
				<br><br><br>
				用户：<span style="color:#800;">${ myUser.username }</span>&emsp;&emsp;
				管理：&ensp;<a href='GoodsList'>商品</a>&emsp;
				<a href='CategoryList'>分类</a>&emsp;
				<a href="main.jsp">用户功能</a>&emsp;
				<a href="Logout">退出</a>
			</div>
		</c:if>
	</div>
</div>
    