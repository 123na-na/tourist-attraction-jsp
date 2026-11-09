<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户平铺式列表</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
	<link rel="stylesheet" type="text/css" href="css/cssGoodsListLayout.css">
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="" method="post">		<!-- 表单提交给对应的Servlet类GoodsListLayout -->
	<div class="divGrid note right" style="margin-top:-3px;">
		&emsp;&emsp;按分类筛选：
		<select name="typeName">
			<option value="">&nbsp;</option>
			<c:forEach var="type" items="${typeList }">
				<c:if test="${ type.typeName != search }">
					<option value="${ type.typeName }">${ type.typeName }</option>
				</c:if>
				<c:if test="${ type.typeName == search }">
					<option value="${ type.typeName }" selected="selected">${ type.typeName }</option>
				</c:if>
			</c:forEach>	
		</select>&ensp;
		<input type="submit" name="buttonFilter" value="筛选">
		&emsp;
		&emsp;&emsp;搜索：
		<input type="text" name="search" value="${ search }" style="width:80px;">
		<input type="submit" name="buttonSearch" value="搜索">
		<span class="note">（在编码、名称、分类中搜索）</span>
	</div>	
	
	<h3>商品平铺式列表</h3>
	
  	<c:if test="${ userList.size() > 0 }">		<%-- 这里使用了jstl标签 --%>
		<div class="divGrid">		
			<div class="list">
				<ul>
							
	<c:forEach var="user" items="${ userList }" varStatus="status">
		<li>
			 <div class="imgDiv">
				 <a href="UserShow?userid=${ user.userid }" title="${user.username }">
				 	<img src="fileUpload/goodsImage/${ goods.image }" alt="（无图片）" class="img" 
				 			onerror="this.src='fileUpload/goodsImage/noImage.png'"></a>		<!-- 图片不存在时以默认的图片代替 -->
			 </div>
			 <div class="username">
			 	<a href="UserShow?userid=${ user.userid }" title="${user.username }">${user.username }</a>
			 </div>
			 
		</li>
	</c:forEach>		
				
				</ul>	
			</div>
		</div>
		
		<div class="divGrid note right">
			${ page }
		</div>
  	</c:if>
	</form>
		
	<div id="msg" class="msg">${ msg }</div>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
