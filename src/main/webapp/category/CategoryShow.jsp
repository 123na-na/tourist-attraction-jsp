<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
    <title>分类详情</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>分类详情</h3>

    <c:if test="${ category != null }">
    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>分类ID</td>
            <td class="left" style="font-weight:bold;">${ category.category_id }</td>
        </tr>
        <tr>
            <td>分类名称</td>
            <td class="left" style="font-weight:bold; font-size:18px; color:#333;">${ category.category_name }</td>
        </tr>
        <tr>
		    <td colspan="2">
		        <c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
		            <a href="CategoryEdit?category_id=${ category.category_id }">修改</a>&emsp;
		            <!-- 使用category_id参数 -->
		            <a href="CategoryDeleteDo?category_id=${ category.category_id }"
		                    onclick="return confirm('确定要删除该分类吗？删除后该分类下的商品将变为未分类状态。');">删除</a>
		        </c:if>
		    </td>
		</tr>
    </table>
    </c:if>

    <!-- 显示该分类下的商品列表 -->
    <c:if test="${ not empty goodsList }">
        <h3 style="margin-top:30px;">该分类下的商品（共 ${goodsList.size()} 件）</h3>
        <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
            <tr class="tr_header">
                <td>序号</td>
                <td>商品名称</td>
                <td>开始日期</td>
                <td>结束日期</td>
                <td>价格</td>
                <td>导游</td>
                <td>状态</td>
                <td>操作</td>
            </tr>
            <c:forEach var="goods" items="${goodsList}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td class="left">
                        <a href="GoodsShow?goodsId=${ goods.goodsId }" 
                           style="text-decoration:none; color:#333; font-weight:bold;">
                            ${ goods.goodsName }
                        </a>
                    </td>
                    <td>${ goods.start_date }</td>
                    <td>${ goods.end_date }</td>
                    <td style="color:red; font-weight:bold;">¥${ goods.price }</td>
                    <td>${ goods.guide }</td>
                    <td>
                        <c:choose>
                            <c:when test="${ goods.status == '进行中' }">
                                <span style="color:green;">${ goods.status }</span>
                            </c:when>
                            <c:when test="${ goods.status == '已结束' }">
                                <span style="color:red;">${ goods.status }</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color:blue;">${ goods.status }</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="GoodsShow?goodsId=${ goods.goodsId }">查看</a>
                        <c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
                            &ensp;<a href="GoodsEdit?goodsId=${ goods.goodsId }">编辑</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${ empty goodsList and category != null }">
        <div style="text-align:center; margin:20px 0; color:#666;">
            <p>该分类下暂无商品</p>
            <c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
                <a href="GoodsAdd?categoryId=${ category.category_id }">添加商品</a>
            </c:if>
        </div>
    </c:if>

    <div id="msg" class="msg">${ msg }</div>
    <br>
    
    <a href="CategoryList">返回分类列表</a>
    
    <%@ include file="../footer.jsp" %>
  </body>
</html>