<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
    <title>分类列表</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <style type="text/css">
        .categoryName {
            display: inline-block; width: 350px; vertical-align: middle;
            white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
        }
    </style>
  </head>
  <body>
    <%@ include file="../header.jsp" %>

    <!-- 使用绝对路径，避免路径问题 -->
    <form action="<%= request.getContextPath() %>/CategoryList" method="post" onsubmit="return confirmDelete()">
    <div class="divGrid note right" style="margin-top:-3px;">
        &emsp;&emsp;搜索：
        <input type="text" name="search" value="${ param.search }" style="width:120px;">
        <input type="submit" name="buttonSearch" value="搜索">
        <span class="note">（在分类名称中搜索）</span>
    </div>    
    
    <h3>分类列表</h3>
    
    <!-- 先检查categoryList是否存在，再检查size -->
    <c:choose>
        <c:when test="${ not empty categoryList and categoryList.size() > 0 }">
            <div class="divGrid">
            <table style="width:900px; margin:0 auto;" class="table_border table_border_bg table_hover">
                <tr class="tr_header">
                    <td style="width:46px;">序号</td>
                    <td>分类名称</td>
                    <td>分类ID</td>
                    <td style="width:95px;">操作</td>
                    <td style="width:46px;">选择</td>
                </tr>
                
                <c:forEach var="category" items="${ categoryList }" varStatus="status">
                    <tr>
                        <td class="note">${status.index + 1}</td>
                        <td style="font-weight:bold; text-align:left;">
                            <a href="CategoryShow?category_id=${ category.category_id }" style="text-decoration:none;">
                                <span title="${ category.category_name }" class="categoryName">${ category.category_name }</span>
                            </a>
                        </td>
                        <td>${ category.category_id }</td>
                        <td>
                            <a href="CategoryShow?category_id=${ category.category_id }" title="详情">
                                <img src="image/icon_show.gif" border="0" alt="详情"></a>&emsp;
                                <c:if test="${ myUser.role != 'user'}">
		                            <a href="CategoryEdit?category_id=${ category.category_id }" title="修改">
		                                <img src="image/icon_edit.gif" border="0" alt="修改"></a>
                                </c:if>
                        </td>
                        <td>
                            <input type="checkbox" name="category_id" value="${ category.category_id }" onchange="check()">
                        </td>
                    </tr>
                </c:forEach>
                  
                <tr>
                    <td colspan="5" class="note" style="text-align:right; height:50px;">
                        （<a href="CategoryAdd">新添分类</a>）
                        &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                        <input type="submit" name="buttonDelete" value="删除">
                        &emsp;
                        <label>全选:<input type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
                    </td>
                </tr>
            </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="msg" style="text-align:center; padding:20px;">
                暂无分类数据
            </div>
        </c:otherwise>
    </c:choose>
    </form>
        
    <!-- 显示消息 -->
    <c:if test="${ not empty msg }">
        <div id="msg" class="msg">${ msg }</div>
    </c:if>
    
    <%@ include file="../footer.jsp" %>
  </body>
  
<script type="text/javascript">
function checkAll() {
    var checkboxList = document.getElementsByName("category_id");
    var checkboxAll = document.getElementsByName("checkboxAll")[0];
    
    if (!checkboxAll) return;
    
    for (var i = 0; i < checkboxList.length; i++) {
        checkboxList[i].checked = checkboxAll.checked;
    }
}

function check() {
    var checkboxList = document.getElementsByName("category_id");
    var checkboxAll = document.getElementsByName("checkboxAll")[0];
    
    if (!checkboxAll || checkboxList.length === 0) return;
    
    var isChecked = true;
    for (var i = 0; i < checkboxList.length; i++) {
        if (!checkboxList[i].checked) {
            isChecked = false;
            break;
        }
    }
    
    checkboxAll.checked = isChecked;
}

function confirmDelete() {
    // 检查是否点击了删除按钮
    var form = event.target.form || document.forms[0];
    var buttonName = event.submitter ? event.submitter.name : "";
    
    if (buttonName === "buttonDelete") {
        // 检查是否有选中的分类
        var checkboxes = document.getElementsByName("category_id");
        var checked = false;
        
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                checked = true;
                break;
            }
        }
        
        if (!checked) {
            alert("请至少选择一个要删除的分类！");
            return false;
        }
        
        return confirm("确认删除所选分类？");
    }
    
    return true; // 对于其他按钮（如搜索），不进行确认
}

// 页面加载时初始化
document.addEventListener('DOMContentLoaded', function() {
    var msgDiv = document.getElementById('msg');
    if (msgDiv && msgDiv.innerHTML.trim() === '') {
        msgDiv.style.display = 'none';
    }
});
</script>
</html>