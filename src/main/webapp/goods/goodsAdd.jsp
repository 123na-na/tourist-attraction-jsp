<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
    <title>商品新添</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>商品新添</h3>

    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>商品名称</td>
            <td class="left">
                <input type="text" name="goodsName" maxlength="45" style="width:400px;">&ensp;
                <span class="msg">*必填</span>
            </td>
        </tr>
        <tr>
            <td>分类</td>
            <td class="left">
                <select name="category_id">
                    <option value="">&lt;请选择&gt;</option>
                    <c:forEach var="category" items="${ categoryList }">
                        <option value="${ category.category_id }">${ category.category_name }</option>
                    </c:forEach>
                </select>&ensp;
                <span class="msg">*必填</span>
            </td>
        </tr>
        <tr>
            <td>价格</td>
            <td class="left">
                <input type="text" name="price" maxlength="10">&ensp;
                <span class="msg">*必填</span>
            </td>
        </tr>
        <tr>
            <td>起售时间</td>
            <td class="left">
                <input type="text" name="start_date" placeholder="yyyy-MM-dd" maxlength="10">&ensp;
                <span class="msg">*必填</span>&ensp;&ensp;
                <span class="note">格式：2023-01-01</span>
            </td>
        </tr>
        <tr>
            <td>结束时间</td>
            <td class="left">
                <input type="text" name="end_date" placeholder="yyyy-MM-dd" maxlength="10">&ensp;
                <span class="note">格式：2023-12-31（可选）</span>
            </td>
        </tr>
        <tr>
            <td>地点</td>
            <td class="left">
                <input type="text" name="location" maxlength="100" style="width:400px;">
            </td>
        </tr>
        <tr>
            <td>导游/指导</td>
            <td class="left">
                <input type="text" name="guide" maxlength="100" style="width:400px;">
            </td>
        </tr>
        <tr>
            <td>状态</td>
            <td class="left">
                <select name="status">
                    <option value="计划中" <c:if test="${ goods.status == '计划中' }">selected="selected"</c:if>>计划中</option>
            		<option value="进行中" <c:if test="${ goods.status == '进行中' }">selected="selected"</c:if>>进行中</option>
            		<option value="已结束" <c:if test="${ goods.status == '已结束' }">selected="selected"</c:if>>已结束</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>商品图片</td>
            <td class="left" style="padding:0 3px;">
                <iframe src="GoodsImageUpload?goodsId=${ goods.goodsId }&image=${ goods.image }" 
                        style="height:155px; width:760px; border:0;"></iframe>
                <input type="hidden" name="goodsUUID" value="${ goods.goodsId }">
                <input type="hidden" name="image" value="${ goods.image }">
            </td>
        </tr>
        <tr height="40">
            <td colspan="2" style="padding-left:30px;">            
                <input type="submit" value="提交" onclick="ajax()">
                &nbsp;&nbsp;
                <input type="button" value="返回" onclick="location.href='GoodsList'">
            </td>
        </tr>
    </table>

    <div id="msg" class="msg">${ msg }</div>
    
    <%@ include file="../footer.jsp" %>
  </body>
  
<script type="text/javascript">    
function ajax() {
    
    var goodsName = $("[name='goodsName']").val();
    var category_id = $("[name='category_id']").val();
    var price = $("[name='price']").val();
    var start_date = $("[name='start_date']").val();
    var end_date = $("[name='end_date']").val();
    var location = $("[name='location']").val();
    var guide = $("[name='guide']").val();
    var status = $("[name='status']").val();
    var goodsUUID = $("[name='goodsUUID']").val();
    var image = $("[name='image']").val();
    
    // 验证必填字段
    if (!goodsName) {
        setMsg("商品名称不能为空");
        return;
    }
    if (!category_id) {
        setMsg("请选择商品分类");
        return;
    }
    if (!price) {
        setMsg("价格不能为空");
        return;
    }
    if (!start_date) {
        setMsg("起售时间不能为空");
        return;
    }
    
    // 验证日期格式
    var dateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dateRegex.test(start_date)) {
        setMsg("起售时间格式不正确，应为：yyyy-MM-dd");
        return;
    }
    if (end_date && !dateRegex.test(end_date)) {
        setMsg("结束时间格式不正确，应为：yyyy-MM-dd");
        return;
    }
    
    var url = "GoodsAddDo";
    var arg = {
        "goodsName": goodsName,
        "category_id": category_id,
        "price": price,
        "start_date": start_date,
        "end_date": end_date,
        "location": location,
        "guide": guide,
        "status": status,
        "goodsUUID": goodsUUID,
        "image": image
    };
    
    $.post(url, arg, function(data) {
        setMsg(data);
    });           
}
  
function setMsg(msg) {
    
    var prefix = "@Redirect:";
    
    if (msg.indexOf(prefix) == 0) {
        var url = msg.substring(prefix.length);
        window.location.href = url;
        return;
    }
    
    $("#msg").html(msg);
}
</script>
</html>