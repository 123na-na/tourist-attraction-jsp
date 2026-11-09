<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
    <title>分类修改</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>分类修改</h3>

    <c:if test="${ category != null }">
    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>分类ID</td>
            <td class="left">
                ${ category.category_id }
                <input type="hidden" name="categoryId" value="${ category.category_id }">
            </td>
        </tr>
        <tr>
            <td>分类名称</td>
            <td class="left">
                <input type="text" name="categoryName" value="${ category.category_name }" 
                        maxlength="50" style="width:300px;">&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        <tr height="40">
            <td colspan="2" style="padding-left:100px; font-size:small;">            
                <input type="submit" value="提交" onclick="ajax()">
                &emsp;&emsp;
                <a href="CategoryShow?category_id=${ category.category_id }">返回分类详情页</a>
            </td>
        </tr>
    </table>
    </c:if>
    
    <div id="msg" class="msg">${ msg }</div>
    <br>
    <a href="CategoryList">返回分类列表</a>
    
    <%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">    

function ajax() {    
    
    var categoryName = $("[name='categoryName']").val();
    
    // 表单验证
    if (categoryName.trim() === "") {
        $("#msg").html("分类名称不能为空！");
        $("[name='categoryName']").focus();
        return;
    }
    
    if (categoryName.length > 50) {
        $("#msg").html("分类名称不能超过50个字符！");
        $("[name='categoryName']").focus();
        return;
    }
    
    var url = "CategoryEditDo?category_id=${ category.category_id }";            //目标地址
    var arg = {
        "categoryName": categoryName
    };
                
    $.post(url, arg, function(data) {
        setMsg(data);
    });
}
  
function setMsg(msg) {
    
    var prefix = "@Redirect:";                            //前缀
    
    if (msg.indexOf(prefix) == 0) {                        //如果msg的前面部分是"@Redirect:"
        var url = msg.substring(prefix.length);         //截取前缀后面的路径名
        window.location.href = url;                        //网页转向
        return;
    }
    
    $("#msg").html(msg);                                //显示消息
}
</script>
</html>