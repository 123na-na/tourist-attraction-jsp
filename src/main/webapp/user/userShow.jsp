<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户详情</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>用户详情</h3>

	<c:if test="${ user != null }">
	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>用户名称</td>
			<td class="left" style="font-weight:bold;">${ user.username }</td>
		</tr>
		<tr>
			<td>用户ID</td>
			<td class="left">${ user.userid }</td>
		</tr>
		<tr>
			<td>分类</td>
			<td class="left">${ user.role }</td>
		</tr>
		<tr>
			<td>邮箱</td>
			<td class="left">${ user.email }</td>
		</tr>
		<tr>
			<td>手机号码</td>
			<td class="left">${ user.phone }</td>
		</tr>
		<tr>
			<td>注册时间</td>
			<td class="left">${ user.create_time }</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' || myUser.role != 'group' }">
					<a href="UserEdit?userid=${ user.userid }">修改</a>&emsp;
					<a href="UserDelete?userid=${ user.userid }"
							onclick="return confirm('确定要删除吗？');">删除</a>
				</c:if>
			</td>
		</tr>
	</table>
  	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<c:if test="${ myUser.role == 'admin'}">
		<a href="UserList">返回用户列表</a>
	</c:if>
	<c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
		<a href="UserListLayout">返回用户列表</a>
	</c:if>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
