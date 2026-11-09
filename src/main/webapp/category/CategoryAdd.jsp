<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
    <title>旅游分类新添</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>旅游分类新添</h3>

    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>分类名称</td>
            <td class="left">
                <input type="text" name="categoryName" maxlength="50" style="width:300px;">&ensp;
                <span class="msg">* 必填，分类名称必须唯一</span>
            </td>
        </tr>
        <tr height="40">
            <td colspan="2" style="padding-left:30px; font-size:small;">            
                <input type="submit" value="提交" onclick="ajaxSubmit()">&emsp;
                <input type="button" value="重置" onclick="resetForm()">
            </td>
        </tr>
    </table>

    <div id="msg" class="msg">${ msg }</div>
    <br>
    
    <!-- 显示现有分类列表 -->
    <c:if test="${not empty categoryList}">
        <h4>现有分类列表</h4>
        <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
            <tr class="tr_header">
                <td>分类ID</td>
                <td>分类名称</td>
            </tr>
            <c:forEach var="category" items="${categoryList}">
                <tr>
                    <td>${category.category_id}</td>
                    <td>${category.category_name}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    
    <br>
    <a href="CategoryList">返回分类列表</a>
    
    <%@ include file="../footer.jsp" %>
  </body>

<script type="text/javascript">    

function ajaxSubmit() {
    
    var categoryName = $("[name='categoryName']").val();
    
    // 验证必填字段
    if (categoryName.trim() === "") {
        $("#msg").html("分类名称不能为空！");
        $("[name='categoryName']").focus();
        return;
    }
    
    // 检查分类名称长度
    if (categoryName.length > 50) {
        $("#msg").html("分类名称不能超过50个字符！");
        $("[name='categoryName']").focus();
        return;
    }
    
    var url = "CategoryAddDo";                        //****目标地址
    var arg = {
        "categoryName": categoryName
    };
    
    // 显示加载状态
    $("#msg").html("正在提交，请稍候...").css("color", "blue");
    
    $.post(url, arg, function(data) {
        setMsg(data);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        // 处理AJAX请求失败
        $("#msg").html("提交失败，请检查网络连接！状态：" + textStatus).css("color", "red");
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

function resetForm() {
    $("input[type='text']").val("");
    $("#msg").html("");
}
</script>
</html>