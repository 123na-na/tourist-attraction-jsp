<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户登录</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  
  <body onkeyup="submitInput(event)">	<!-- //####添加代码 -->		<!-- 为了实现按回车键也能提交所输入的信息 -->
	<%
		//生成“加密”后的密码
		//String username = "zhangsan";								//登录用户名
		//String password = "1";
		//String salt	  = "login_fdsfj45349fd";					//加点料（盐）。此料应与登录验证LoginCheck.java中加的料相同
		//String md5 = util.Md5.getMd5(username + password + salt);	//生成MD5加密结果。请复制粘贴到数据表tb_user，用于测试登录
		//out.print(md5);											//将得到加密后的密码：7F3E096131569A77E3D63EEE4B79DD30
	%>
	<%@ include file="header.jsp" %>	
   	<h3>用户登录</h3>
   	
   	<div style="width:250px; margin:0 auto; text-align:left;">
	   	用户名：<input type="text" name="username" style="width:150px;">
	   	<br>
	   	密&emsp;码：<input type="password" name="password" style="width:150px;">
		<br>		
		验证码：<input type="text" name="code" maxlength="4" style="width:50px;">&ensp;
		<a href="#"><img src="" id="imgCode" onclick="changeCode()" 
				style="vertical-align:middle; border:0;"></a>
		<a href="#" style="font-size:small;" onclick="changeCode()">刷新</a>
		<br>
		&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;			
		<input type="submit" value="提交" onclick="ajax()">	
	</div>
	
	<div id="msg" class="msg">${ msg }</div>
	<br>
	<div class="note">
		普通用户角色user登录：zhangsan，1&emsp;&emsp;管理员角色admin登录：admin，1
	</div>

	<%@ include file="footer.jsp" %>
  </body>
<script type="text/javascript">	

window.onload = changeCode();									//网页转载、后退时，将刷新验证码图片

function changeCode() {											//刷新验证码图片
	
	var t = (new Date()).getTime();								//将当前时间转换为毫秒
	$("#imgCode").attr("src", "VerifyCodeNumChar?t=" + t);		//【重要】设置属性src的值。添加时间参数为了能刷新图片
	$("[name='code']").val("");									//清空验证码输入框
	$("[name='code']").focus();									//获得焦点，插入光标定位在验证码输入框中
}

function ajax() {												//应用Ajax技术实现登录
	
	var username 	= $("[name='username']").val();				//根据name获取输入的值
	var password 	= $("[name='password']").val();
	var code 		= $("[name='code']").val();
	
	var url = "LoginDo";										//【重要】目标地址
	var arg = {	"username" : username, "password" : password, 
		   		"code" 	   : code };							//JSON格式数据： {key:value, k2:v2}
//把内容提交到url页面
	$.post(url,arg,function(data){
		setMsg(data);
	});						//应用jQuery中的Ajax技术
	
		   	
}
  
function setMsg(msg) {
	
	var prefix = "@Redirect:";									//前缀
	
	if (msg.indexOf(prefix) == 0) {								//如果msg的前面部分是“@Redirect:”
		var url = msg.substring(prefix.length); 				//截取前缀后面的路径名，得到main.jsp
		window.location.href = url;								//网页转向
		return;
	}

	prefix = "@RefreshCode:";									//前缀
	
	if (msg.indexOf(prefix) == 0) {								//如果msg的前面部分是“@RefreshCode:”
		changeCode();											//刷新验证码
		msg = msg.substring(prefix.length); 					//得到前缀后面的消息内容
	}
	     
	$("#msg").html(msg);										//显示消息
}

function submitInput(event) { 			//执行键盘按键命令

	if(event.key == 13){            //回车键是13
		ajax();
	}						//提交输入
	
}
</script>
</html>
