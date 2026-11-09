</html><%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品详情</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>商品详情</h3>

	<c:if test="${ goods != null }">
	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>商品名称</td>
			<td class="left" style="font-weight:bold;">${ goods.goodsName }</td>
		</tr>
		<tr>
			<td>商品编码</td>
			<td class="left">${ goods.goodsId }</td>
		</tr>
		<tr>
			<td>开始时间</td>
			<td class="left">${ goods.start_date }</td>
		</tr>
		<tr>
			<td>结束时间</td>
			<td class="left">${ goods.end_date }</td>
		</tr>
		<tr>
			<td>价格</td>
			<td class="left">${ goods.price }</td>
		</tr>
		<tr>
			<td>导游</td>
			<td class="left">${ goods.guide }</td>
		</tr>
		<tr>
			<td>状态</td>
			<td class="left">${ goods.status }</td>
		</tr>
		
		<tr>
		    <td>商品图片</td>
		    <td class="left">
		        <div style="display: flex; align-items: center; gap: 20px;">
		            <!-- 显示商品图片 -->
		            <div style="width: 150px; height: 100px; border: 1px solid #ddd; border-radius: 4px; overflow: hidden;">
		                <img src="fileUpload/goodsImage/${goods.image}" 
		                     alt="${goods.goodsName}" 
		                     style="width: 100%; height: 100%; object-fit: cover;"
		                     onerror="this.src='fileUpload/goodsImage/noImage.png'; this.alt='（无图片）'">
		            
		            	
		            </div>
		            
		            <!-- 图片信息和管理链接 -->
		            <div>
		                <div style="margin-bottom: 5px;">
		                    <span style="color: #666;">图片:</span> 
		                    <span style="font-weight: bold;">
		                        <c:choose>
		                            <c:when test="${empty goods.image or goods.image == '（无图片）'}">
		                                （无图片）
		                            </c:when>
		                            <c:otherwise>
		                                ${goods.image}
		                            </c:otherwise>
		                        </c:choose>  
		                    </span>
		                </div>
		          
		                
		                <c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
		                    <div style="font-size: 14px;">
		                        <c:if test="${empty goods.image or goods.image == '（无图片）'}">
		                            <a href="GoodsShowImageRenew?goodsId=${ goods.goodsId }">上传图片</a>
		                        </c:if>
		                        <c:if test="${not empty goods.image and goods.image != '（无图片）'}">
		                            <a href="GoodsShowImageRenew?goodsId=${ goods.goodsId }">更换图片</a>
		                        </c:if>
		                    </div>
		                </c:if>
		                
		                <c:if test="${not empty goods.image and goods.image != '（无图片）'}">
		                    <div style="font-size: 14px; margin-top: 5px;">
		                        <a href="fileUpload/goodsImage/${goods.image}" target="_blank">查看原图</a>
		                    </div>
		                </c:if>
		            </div>
		        </div>
		    </td>
		</tr>
		
			<td>简介</td>
			<td class="left" style="width:650px;">${ goods.location }</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
					<a href="GoodsEdit?goodsId=${ goods.goodsId }">修改</a>&emsp;
					<a href="GoodsDeleteDo?goodsId=${ goods.goodsId }"
							onclick="return confirm('确定要删除吗？');">删除</a>
				</c:if>
				
				<c:if test="${myUser.role == 'user' }">
					<a href="#" onclick="alert('已添加到购物车。')">立刻购买</a>&ensp;
				</c:if> 
				<c:if test="${ myUser.role == 'user'}">
					<span>无修改权限</span>
				</c:if>
				
			</td>
		</tr>
	</table>
  	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' }">
		<a href="GoodsList">返回商品列表</a>
	</c:if>
	<c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
		<a href="GoodsListLayout">返回商品列表</a>
	</c:if>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>