<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户功能</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
  </head>
  
  <body>
	<%@ include file="header.jsp" %>
   	<h3>用户功能</h3>
   	 
	<c:if test="${myUser.role == null }">			<!-- 如果未登录 -->
		<c:redirect url="login.jsp"></c:redirect>			<!-- 网页重定向 -->
	</c:if>	
  		
	<table style="width:600px; margin:0 auto;" class="table_border table_border_bg table_hover">
		<tr height="50" class="tr_header">
			<th colspan="2">
				用户功能列表
			</th>
		</tr>
		<tr height="50">
			<td width="30%">
				登录相关
			</td>
			<td class="left">&emsp;
				<a href="UserShow?userid=${ myUser.userid }">个人信息</a>&emsp;&emsp;
				<a href="Logout">退出登录</a>&emsp;&emsp;
			</td>
		</tr>
		
		<c:if test="${myUser.role == 'guest' }">
			<tr height="50">
				<td>
					订单管理
				</td>
				<td class="left">&emsp;
					<a href="GoodsListLayout">商品列表</a>&emsp;&emsp;
					购物车&emsp;&emsp;
					订单列表&emsp;&emsp;
				</td>
			</tr>
		</c:if>
		
		<c:if test="${myUser.role == 'admin'}">
			<tr height="50">
				<td>
					商品管理
				</td>
				<td class="left">&emsp;
					<a href="GoodsList">商品列表</a>&emsp;&emsp;
					<a href="GoodsAdd">商品新添</a>&emsp;&emsp;
					<a href="GoodsListLayout">商品列表（平铺式）</a>&emsp;&emsp;
				</td>
			</tr>
			<tr height="50">
				<td>
					分类管理
				</td>
				<td class="left">&emsp;
					<a href="CategoryList">分类列表</a>&emsp;&emsp;
					<a href="CategoryAdd">分类新添</a>&emsp;&emsp;
				</td>
			</tr>
			<tr height="50">
				<td>
					用户管理
				</td>
				<td class="left">&emsp;
					<a href="UserList">用户列表</a>&emsp;&emsp;
					<a href="UserAdd">用户新添</a>&emsp;&emsp;
				</td>
			</tr>	
		</c:if>
		
		<c:if test="${myUser.role == 'user'}">
			<tr height="50">
				<td>
					商品管理
				</td>
				<td class="left">&emsp;
					<a href="GoodsList">商品列表</a>&emsp;&emsp;
					 <a href="CartItemList">购物车</a>&emsp;&emsp;
					<a href="GoodsListLayout">商品列表（平铺式）</a>&emsp;&emsp;
				</td>
			</tr>
			<tr height="50">
				<td>
					分类管理
				</td>
				<td class="left">&emsp;
					<a href="CategoryList">分类列表</a>&emsp;&emsp;
				</td>
			</tr>
		</c:if>
		
		<c:if test="${myUser.role == 'group'}">
			<tr height="50">
				<td>
					商品管理
				</td>
				<td class="left">&emsp;
					<a href="GoodsList">商品列表</a>&emsp;&emsp;
					<a href="GoodsAdd">商品新添</a>&emsp;&emsp;
					<a href="GoodsListLayout">商品列表（平铺式）</a>&emsp;&emsp;
				</td>
			</tr>
			<tr height="50">
				<td>
					分类管理
				</td>
				<td class="left">&emsp;
					<a href="CategoryList">分类列表</a>&emsp;&emsp;
					<a href="CategoryAdd">分类新添</a>&emsp;&emsp;
				</td>
			</tr>
		</c:if>
	</table>
	
	<%@ include file="footer.jsp" %>
  </body>
</html>
