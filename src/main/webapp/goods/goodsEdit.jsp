<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
    <title>商品修改</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>商品修改</h3>

    <div id="msg" class="msg">${ msg }</div>
    
    <c:if test="${ goods != null }">
    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>商品名称</td>
            <td class="left">
                <input type="text" name="goodsName" value="${ goods.goodsName }" 
                    maxlength="100" style="width:400px;">&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        <tr>
            <td>商品ID</td>
            <td class="left">
                <input type="text" name="goodsId" value="${ goods.goodsId }" readonly style="background-color:#f0f0f0;">&ensp;
                <span class="note">（商品ID不可修改）</span>
            </td>
        </tr>
        <tr>
            <td>开始时间</td>
            <td class="left">
                <input type="datetime-local" name="start_date" 
                       value="${ goods.start_date.replace(' ', 'T') }" 
                       id="startDateInput">&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        <tr>
            <td>结束时间</td>
            <td class="left">
                <input type="datetime-local" name="end_date" 
                       value="${ goods.end_date.replace(' ', 'T') }" 
                       id="endDateInput">
            </td>
        </tr>
        <tr>
            <td>价格</td>
            <td class="left">
                <input type="number" name="price" value="${ goods.price }" 
                    id="priceInput" step="0.01" min="0" style="width:150px;">&ensp;
                <span class="msg">*</span>&ensp;元
            </td>
        </tr>
        <tr>
            <td>导游</td>
            <td class="left">
                <input type="text" name="guide" value="${ goods.guide }" 
                    maxlength="50" style="width:300px;">
            </td>
        </tr>
        <tr>
    <td>状态</td>
    <td class="left">
        <select name="status" id="statusSelect">
            <option value="">&lt;请选择&gt;</option>
            <option value="计划中" <c:if test="${ goods.status == '计划中' }">selected="selected"</c:if>>计划中</option>
            <option value="进行中" <c:if test="${ goods.status == '进行中' }">selected="selected"</c:if>>进行中</option>
            <option value="已结束" <c:if test="${ goods.status == '已结束' }">selected="selected"</c:if>>已结束</option>
        </select>&ensp;
        <span class="msg">*</span>
    </td>
</tr>
        <tr>
            <td>分类</td>
            <td class="left">
                <select name="category_id" id="categorySelect">
                    <option value="">&lt;请选择&gt;</option>
                    <c:forEach var="category" items="${ categoryList }"> 
                        <option value="${ category.category_id }" 
                            ${ category.category_id == goods.category_id ? 'selected="selected"' : '' }>
                            ${ category.category_name }
                        </option>
                    </c:forEach>
                </select>&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        <tr>
            <td>地点</td>
            <td class="left">
                <input type="text" name="location" value="${ goods.location }" 
                    maxlength="100" style="width:400px;">
            </td>
        </tr>
        <tr>
            <td>商品图片</td>
			<td class="left">
				<div style="height:100px; margin:0; padding:0; display:flex; align-items:center;">	<!-- 内容上下居中对齐 -->
					${ goods.image }
					<c:if test="${ myUser.role == 'admin' || myUser.role == 'group' }">
						<div style='margin-top:60px; font-size:small;'>
							&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
							<c:if test="${ goods.image == '（无图片）' }">
								（<a href="GoodsShowImageRenew?goodsId=${ goods.goodsId }">添加图片</a>）
							</c:if>
							<c:if test="${ goods.image != '（无图片）' }">
								（<a href="GoodsShowImageRenew?goodsId=${ goods.goodsId }">管理图片</a>）
							</c:if>
						</div>
					</c:if>
				</div>
			</td>
        </tr>
        <tr height="40">
            <td colspan="2" style="padding-left:100px; font-size:small;">            
                <input type="submit" value="提交" onclick="ajax()">
                &emsp;&emsp;
                <a href="GoodsShow?goodsId=${ goods.goodsId }">返回商品详情页</a>
            </td>
        </tr>
    </table>
    </c:if>
    
    <c:if test="${ goods == null }">
        <div style="text-align:center; padding:50px;">
            <p style="color:red;">${msg}</p>
            <a href="GoodsList">返回商品列表</a>
        </div>
    </c:if>
    
    <br>
    <a href="GoodsList">返回商品列表</a>
    
    <%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">    

function ajax() {    
    var goodsId     = $("[name='goodsId']").val();
    var goodsName   = $("[name='goodsName']").val();
    var start_date  = $("[name='start_date']").val().replace('T', ' ');
    var end_date    = $("[name='end_date']").val().replace('T', ' ');
    var price       = $("[name='price']").val();
    var guide       = $("[name='guide']").val();
    var status      = $("[name='status']").val();
    var category_id = $("[name='category_id']").val();
    var location    = $("[name='location']").val();
    var image       = $("[name='image']").val();
    
    var url = "GoodsEditDo?goodsId=${ goods.goodsId }";            // 目标地址
    var arg = {    
        "goodsId"   : goodsId, 
        "goodsName" : goodsName, 
        "start_date": start_date,
        "end_date"  : end_date,
        "price"     : price,
        "guide"     : guide,
        "status"    : status,
        "category_id": category_id,
        "location"  : location,
        "image"     : image
    };                            // JSON格式数据： {key:value, k2:v2}
                
    $.post(url, arg, function(data) {
        setMsg(data);
    });
}
  
function setMsg(msg) {
    
    var prefix = "@Redirect:";                            // 前缀
    
    if (msg.indexOf(prefix) == 0) {                        // 如果msg的前面部分是"@Redirect:"
        var url = msg.substring(prefix.length);         // 截取前缀后面的路径名
        window.location.href = url;                        // 网页转向
        return;
    }
    
    $("#msg").html(msg);                                // 显示消息
}
</script>
</html>