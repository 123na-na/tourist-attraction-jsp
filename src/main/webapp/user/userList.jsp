<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户列表</title>
	<link rel="stylesheet" type="text/css" href="css/css.css">
	<style type="text/css">
		.goodsName {
			display:		inline-block; 		/*行内块级元素*/
			width:			350px; 
			vertical-align:	middle;				/*上下居中对齐*/
			white-space: 	nowrap;				/*不换行*/
			overflow: 		hidden;				/*隐藏超出部分*/
			text-overflow: 	ellipsis;			/*不显示部分以三个点表示*/
		}
	</style>
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="UserList" method="get" style="display: inline;">
		<div class="divGrid note right" style="margin-top:-3px;">
		    &emsp;&emsp;按角色筛选：
		    <select name="roleFilter">
		        <option value="">全部</option>
		        <option value="admin" ${param.roleFilter == 'admin' ? 'selected' : ''}>admin</option>
		        <option value="user" ${param.roleFilter == 'user' ? 'selected' : ''}>user</option>
		        <option value="group" ${param.roleFilter == 'group' ? 'selected' : ''}>group</option>
		    </select>&ensp;
		    <input type="submit" name="buttonRoleFilter" value="筛选">
		    <!-- 隐藏字段，用于清除搜索参数 -->
		    <input type="hidden" name="search" value="">
	</form>
	&emsp;
		<form action="UserList" method="get" style="display: inline;">
		    &emsp;&emsp;搜索：
		    <input type="text" name="search" value="${ search }" style="width:80px;">
		    <input type="submit" name="buttonSearch" value="搜索">
		    <span class="note">（在用户ID、用户名、角色中搜索）</span>
		    <!-- 隐藏字段，用于清除角色筛选参数 -->
		    <input type="hidden" name="roleFilter" value="">
		</form>
	</div> 
	<c:if test="${ myUser.role == 'admin'}">
		<a href="UserList">返回用户列表</a>
	</c:if>
  	<c:if test="${ userList.size() > 0 }">		<%-- 这里使用了jstl标签 --%>
		<div class="divGrid">
		<table style="width:900px; margin:0 auto;" class="table_border table_border_bg table_hover">
			<tr class="tr_header">
				<td style="width:46px;">序号</td>
				<td>名称</td>
				<td>用户ID</td>
				<td>手机号码</td>
				<td style="width:95px;">详情/修改</td>
				<td style="width:46px;">选择</td>
			</tr>
			
			<c:forEach var="user" items="${ userList }" varStatus="status">
				<tr title="【详情】，&emsp;【注册时间】${ user.create_time }，&emsp;【分类】${ user.role }">
					<td class="note">${status.index + indexStart + 1 }</td>
					<td style="font-weight:bold; text-align:left;">		<!-- 如果图片加载失败则不显示 -->
						<a href="UserShow?userid=${ user.userid}" style="text-decoration:none;">
							<span title="${ user.username }" class="username">${ user.username }</span></a></td>
					<td>${ user.userid }</td>
					<td>${ user.phone }</td>
					<td>
						<a href="UserShow?userid=${ user.userid }" title="详情">
								<img src="image/icon_show.gif" border="0"></a>&emsp;
								
						<a href="UserEdit?userid=${ user.userid }" title="修改">
								<img src="image/icon_edit.gif" border="0"></a>
					</td>
					<td>
						<input type="checkbox" name="userid" value="${ user.userid }" onchange="check()">
					</td>
				</tr>
			</c:forEach>
			  
			<tr>
				<td colspan="8" class="note" style="text-align:right; height:50px;">
					（<a href="UserAdd">新添用户</a>）
					&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<input type="submit" name="buttonDelete" value="删除" 
							onclick="return confirm('确认删除所选记录？');">
					&emsp;
					<label>全选:<input type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
				</td>
			</tr>
		</table>
		</div>
		
		<div class="divGrid note right">
			${ page }
		</div>
  	</c:if>
	</form>
		
	<div id="msg" class="msg">${ msg }</div>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">
function checkAll() {
	var checkboxList = document.getElementsByName("userid");			//获取复选框列表
	var checkboxAll  = document.getElementsByName("checkboxAll")[0];	//全选复选框
	
	for (var i = 0; i < checkboxList.length; i++) {						//对于列表中的每一个复选框
		checkboxList[i].checked = checkboxAll.checked;					//此复选框的勾选情况与全选复选框一致
	}
}

function check() {
	var checkboxList = document.getElementsByName("userid");			//获取复选框列表
	var checkboxAll  = document.getElementsByName("checkboxAll")[0];	//全选复选框
	var isChecked = true;
	
	for (var i = 0; i < checkboxList.length; i++) {						//对于列表中的每一个复选框
		if (checkboxList[i].checked == false) {							//如果没被勾选
			isChecked = false;
			break;
		}
	}
	
	if (isChecked) {
		checkboxAll.checked = true;										//全选复选框被勾选
	} else {
		checkboxAll.checked = false;									//全选复选框被取消勾选
	}
}
</script>
</html>
