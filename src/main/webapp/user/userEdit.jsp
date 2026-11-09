<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
    <title>用户修改</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
    <%@ include file="../header.jsp" %>    
    <h3>用户修改</h3>

    <div id="msg" class="msg">${ msg }</div>
    
    <c:if test="${ user != null }">
    <table style="width:900px; margin:0 auto;" class="table_border table_padding10">
        <tr class="tr_header">
            <td>项目</td>
            <td>内容&emsp;&emsp;&emsp;&emsp;</td>
        </tr>
        <tr>
            <td>用户名</td>
            <td class="left">
                <input type="text" name="username" value="${ user.username }" 
                        maxlength="45" style="width:400px;">&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        <tr>
            <td>用户ID</td>
            <td class="left">
                <input type="text" name="userid" value="${ user.userid }" readonly style="background-color:#f0f0f0;">&ensp;
                <span class="note">（用户ID不可修改）</span>
            </td>
        </tr>
        <tr>
            <td>角色</td>
            <td class="left">
                <select name="role">
                    <option value="">&lt;请选择&gt;</option>
                    <option value="admin" <c:if test="${ user.role == 'admin' }">selected="selected"</c:if>>管理员</option>
                    <option value="user" <c:if test="${ user.role == 'user' }">selected="selected"</c:if>>普通用户</option>
                    <option value="guest" <c:if test="${ user.role == 'guest' }">selected="selected"</c:if>>游客</option>
                </select>&ensp;
                <span class="msg">*</span>
            </td>
        </tr>
        
        <tr>
            <td>邮箱</td>
            <td class="left">
                <input type="text" name="email" value="${ user.email }" maxlength="100">&ensp;
            </td>
        </tr>
        <tr>
            <td>手机号</td>
            <td class="left">
                <input type="text" name="phone" value="${ user.phone }" maxlength="20">&ensp;
            </td>
        </tr>
        <tr>
            <td>创建时间</td>
            <td class="left">
                <input type="text" name="create_time" value="${ user.create_time }" maxlength="19">&ensp;
                <span class="msg">*</span>&ensp;&ensp;
                <span class="note">例如：2022-05-26 08:00:00</span>
            </td>
        </tr>
        <tr height="40">
            <td colspan="2" style="padding-left:100px; font-size:small;">            
                <input type="submit" value="提交" onclick="ajax()">
                &emsp;&emsp;
                <a href="UserShow?userid=${ user.userid }">返回用户详情页</a>
            </td>
        </tr>
    </table>
      </c:if>
    
    <br>
    <c:if test="${ myUser.role == 'admin'}">
		 <a href="UserList">返回用户列表</a>
	</c:if>
   
    
    <%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">    

function ajax() {    
    var userid  = $("[name='userid']").val();                //根据控件的name属性值获取输入的值
    var username    = $("[name='username']").val();
    var role        = $("[name='role']").val();
    var email       = $("[name='email']").val();
    var phone       = $("[name='phone']").val();
    var create_time = $("[name='create_time']").val();    
    
    var url = "UserEditDo?userid=${ user.userid }";            //****目标地址
    var arg = {    "userid" : userid, "username" : username, 
                   "role"  : role,  "email"     : email, 
                   "phone"   : phone,   "create_time"  : create_time};                            //JSON格式数据： {key:value, k2:v2}
                
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