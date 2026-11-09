<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
    <title>商品列表</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <style type="text/css">
        .goodsName {
            display:        inline-block;       /*行内块级元素*/
            width:            350px; 
            vertical-align:    middle;                /*上下居中对齐*/
            white-space:     nowrap;                /*不换行*/
            overflow:         hidden;                /*隐藏超出部分*/
            text-overflow:     ellipsis;            /*不显示部分以三个点表示*/
        }
    </style>
  </head>
  <body>
    <%@ include file="../header.jsp" %>

    <form action="GoodsList" method="get">        <!-- 表单提交给对应的Servlet类GoodsList -->
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
    
    <h3>商品列表</h3>
    
    <c:if test="${goodsList.size() > 0}">        <%-- 这里使用了jstl标签 --%>
        <div class="divGrid">
        <table style="width:950px; margin:0 auto;" class="table_border table_border_bg table_hover">
            <tr class="tr_header">
                <td style="width:46px;">序号</td>
                <td>名称</td>
                <td>地点</td> <!-- 改为地点列 -->
                <td>价格</td>
                <td style="width:88px;">详情
	                <c:if test="${ myUser.role != 'user'}">
	                	/修改
	                </c:if>
                </td>
                <c:if test="${ myUser.role == 'user'}">
               	 	<td style="width:66px;">加入购物车</td>
                </c:if>
                <c:if test="${ myUser.role != 'user'}">
                	<td style="width:46px;">选择</td>
               </c:if>
            </tr>
            
            <c:forEach var="goods" items="${goodsList}" varStatus="status">
                <%-- 修改title，使用实际存在的属性 --%>
                <tr title="【导游】${goods.guide}，&emsp;【开始日期】${goods.start_date}，&emsp;【结束日期】${goods.end_date}">
                    <td class="note">${status.index + indexStart + 1}</td>
                    <td style="font-weight:bold; text-align:left;">        <!-- 如果图片加载失败则不显示 -->
                        <a href="GoodsShow?goodsId=${goods.goodsId}" style="text-decoration:none;">
                            <img src="fileUpload/goodsImage/${goods.image}" alt="无图" 
                                    onerror="this.style.display='none'" 
                                    style="width:100px; vertical-align:middle; margin-right:5px;">
                            <span title="${goods.goodsName}" class="goodsName">${goods.goodsName}</span></a></td>
                    <%-- 改为显示地点 --%>
                    <td>${goods.location}</td>
                    <td>${goods.price}</td>
                    <td>
                        <a href="GoodsShow?goodsId=${goods.goodsId}" title="详情">
                                <img src="image/icon_show.gif" border="0"></a>&emsp;
                                <c:if test="${myUser.role == 'admin'||myUser.role == 'group'}">
                        <a href="GoodsEdit?goodsId=${goods.goodsId}" title="修改">
                                <img src="image/icon_edit.gif" border="0"></a>
                                </c:if>
                    </td>
                    <c:if test="${ myUser.role == 'user'}">
                     <td>
                        <a href="GoodsCar?goodsId=${goods.goodsId}" title="加入购物车">
                                <img src="image/icon_add.png" border="0" style="width: 20px"></a>&emsp;
                    </td>
                    </c:if>
                     <c:if test="${ myUser.role != 'user'}">
                    	<td>
                        	<input type="checkbox" name="goodsId" value="${goods.goodsId}" onchange="check()">
                    	</td>
                    </c:if>
                </tr>
            </c:forEach>
              
            <tr>
            
                <td colspan="8" class="note" style="text-align:right; height:50px;">
                <c:if test="${myUser.role == 'admin'||myUser.role == 'group'}">
                    （<a href="GoodsAdd">新添商品</a>）
                    &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                    <input type="submit" name="buttonDelete" value="删除" 
                            onclick="return confirm('确认删除所选记录？');">
                    &emsp;
                    <label>全选:<input type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
                </c:if>
               
                </td>
                
            </tr>
        </table>
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
                <a href="GoodsList">返回全部商品列表</a>
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
<script type="text/javascript">
function checkAll() {
    var checkboxList = document.getElementsByName("goodsId");            //获取复选框列表
    var checkboxAll  = document.getElementsByName("checkboxAll")[0];    //全选复选框
    
    for (var i = 0; i < checkboxList.length; i++) {                        //对于列表中的每一个复选框
        checkboxList[i].checked = checkboxAll.checked;                    //此复选框的勾选情况与全选复选框一致
    }
}

function check() {
    var checkboxList = document.getElementsByName("goodsId");            //获取复选框列表
    var checkboxAll  = document.getElementsByName("checkboxAll")[0];    //全选复选框
    var isChecked = true;
    
    for (var i = 0; i < checkboxList.length; i++) {                        //对于列表中的每一个复选框
        if (checkboxList[i].checked == false) {                            //如果没被勾选
            isChecked = false;
            break;
        }
    }
    
    if (isChecked) {
        checkboxAll.checked = true;                                        //全选复选框被勾选
    } else {
        checkboxAll.checked = false;                                    //全选复选框被取消勾选
    }
}
</script>
</html>