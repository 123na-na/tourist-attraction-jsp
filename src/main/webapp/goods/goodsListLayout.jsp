<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品平铺式列表</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
	<link rel="stylesheet" type="text/css" href="css/cssGoodsListLayout.css">
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="GoodsListLayout" method="get">
	<div class="divGrid note right" style="margin-top:-3px;">
		&emsp;&emsp;按分类筛选：
		<select name="category_id">
			<option value="">&nbsp;</option>
			<c:forEach var="category" items="${categoryList}">
				<c:if test="${category.category_id != param.category_id}">
					<option value="${category.category_id}">${category.category_name}</option>
				</c:if>
				<c:if test="${category.category_id == param.category_id}">
					<option value="${category.category_id}" selected="selected">${category.category_name}</option>
				</c:if>
			</c:forEach>
		</select>&ensp;
		<input type="submit" name="buttonFilter" value="筛选">
		&emsp;
		&emsp;&emsp;搜索：
		<input type="text" name="search" value="${search}" style="width:80px;">
		<input type="submit" name="buttonSearch" value="搜索">
		<span class="note">（在名称、地点中搜索）</span>
	</div>	
	
	<h3>商品平铺式列表</h3>
	
  	<c:if test="${goodsList.size() > 0}">
		<div class="divGrid">		
			<div class="list">
				<ul>
							
	<c:forEach var="goods" items="${goodsList}" varStatus="status" >
		<li title="【导游】${goods.guide}，&emsp;【开始日期】${goods.start_date}，&emsp;【结束日期】${goods.end_date}">
			 <div class="imgDiv">
				 <a href="GoodsShow?goodsId=${goods.goodsId}" title="${goods.goodsName}">
				 	<img src="fileUpload/goodsImage/${goods.image}" alt="（无图片）" class="img" 
				 			onerror="this.src='fileUpload/goodsImage/noImage.png'"></a>
			 </div>
			 <div class="goodsName" >
			 	<a href="GoodsShow?goodsId=${goods.goodsId}" title="${goods.goodsName}">${goods.goodsName}</a>
			 </div>
			 
			 <div class="priceDiv">
				 <span class="priceCode">￥</span>
				 <span class="price">${goods.price}</span>&emsp;				 
				 <a href="GoodsCar?goodsId=${goods.goodsId}" onclick="return confirm('确认加入购物车？')"> 
				 	<span class="cart">加入购物车</span></a>
			 </div>
		</li>
	</c:forEach>		
				
				</ul>	
			</div>
		</div>
		
		<div class="divGrid note right">
			${page}
		</div>
  	</c:if>
  	
  	<!-- 如果没有商品记录 -->
    <c:if test="${empty goodsList or goodsList.size() == 0}">
        <div style="text-align:center; padding:50px; color:#666;">
            <c:if test="${not empty param.category_id or not empty param.search}">
                没有找到符合条件的商品。
                <br><br>
                <a href="GoodsListLayout">返回全部商品列表</a>
            </c:if>
            <c:if test="${empty param.category_id and empty param.search}">
                暂无商品记录，<a href="GoodsAdd">点击添加新商品</a>。
            </c:if>
        </div>
    </c:if>
	</form>
		
	<div id="msg" class="msg">${msg}</div>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>