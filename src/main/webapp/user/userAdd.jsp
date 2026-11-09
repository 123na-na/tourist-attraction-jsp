<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
	<title>用户新添</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>用户新添</h3>

	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>用户名</td>
			<td class="left">
				<input type="text" name="username" maxlength="45" style="width:400px;">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
    		<td>密码</td>
    			<td class="left">
        		<input type="password" name="password" maxlength="45">&ensp;
        		<span class="msg">*</span>
    		</td>
		</tr>
		<tr>
    		<td>用户ID</td>
    			<td class="left">
       		    <span class="note">（系统自动生成）</span>
        		<!-- 移除用户ID输入框 -->
    		</td>
		</tr>
		<tr>
			<td>角色</td>
			<td class="left">
				<input type="text" name="role" maxlength="45">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
	    <td>邮箱</td>
	    	<td class="left">
	        	<input type="text" name="email" maxlength="100">&ensp;
	    	</td>
		</tr>
		<tr>
	    	<td>手机号</td>
	    		<td class="left">
	        		<input type="text" name="phone" maxlength="20">&ensp;
	    		</td>
			</tr>
		<tr>
			<td>创建时间</td>
			<td class="left">
				<input type="text" name="create_time" value="2023-01-01 08:00:00" maxlength="19">&ensp;
				<span class="msg">*</span>&ensp;&ensp;
				<span class="note">例如：2022-05-26 08:00:00</span>
			</td>
		</tr>
		<tr height="40">
			<td colspan="2" style="padding-left:30px; font-size:small;">			
				<input type="submit" value="提交" onclick="ajax()">	
			</td>
		</tr>
	</table>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<a href="UserList">返回用户列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
    
    var username = $("[name='username']").val();                    //根据控件的name属性值获取输入的值
    var password = $("[name='password']").val();                    //获取密码
    var role = $("[name='role']").val();
    var create_time = $("[name='create_time']").val();
    var email = $("[name='email']").val();
    var phone = $("[name='phone']").val();                
    
    var url = "UserAddDo";                                          //****目标地址
    var arg = { "username" : username, "password": password,
                "role"  : role,  "email"     : email, 
                "phone"   : phone,   "create_time"  : create_time};        //JSON格式数据： {key:value, k2:v2}

    $.post(url, arg, function(data) {
        setMsg(data);
    });               
}
  
function setMsg(msg) {
	
	var prefix = "@Redirect:";							//前缀
	
	if (msg.indexOf(prefix) == 0) {						//如果msg的前面部分是“@Redirect:”
		var url = msg.substring(prefix.length); 		//截取前缀后面的路径名
		window.location.href = url;						//网页转向
		return;
	}
	
	$("#msg").html(msg);								//显示消息
}
</script>

<!-- //####【新添代码】：引入UEditor组件 -->
<script type="text/javascript" src="UEditor/ueditor.config.js"></script>
<script type="text/javascript" src="UEditor/ueditor.all.min.js"></script>

<script type="text/javascript">
	var ue = UE.getEditor("detail",{
		autoHeightEnabled:false,
		initialFrameHeight:300,
	});
</script>









</html>
