<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户注册</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        
        .container {
            max-width: 500px;
            margin: 50px auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        
        input[type="text"],
        input[type="password"],
        input[type="email"],
        input[type="tel"],
        select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        
        .btn-group {
            text-align: center;
            margin-top: 20px;
        }
        
        .btn {
            padding: 10px 25px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin: 0 5px;
        }
        
        .btn-submit {
            background-color: #4CAF50;
            color: white;
        }
        
        .btn-reset {
            background-color: #f44336;
            color: white;
        }
        
        .error {
            color: red;
            font-size: 14px;
            margin: 10px 0;
            text-align: center;
        }
        
        .back-link {
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <%@ include file="../header.jsp" %>	
    
    <div class="container">
        <h1>用户注册</h1>
        
        <c:if test="${not empty msg}">
            <div class="error">${msg}</div>
        </c:if>
        
        <form action="Register" method="post">
            <div class="form-group">
                <label>用户名 *</label>
                <input type="text" name="username" value="${param.username}" required>
            </div>
            
            <div class="form-group">
                <label>密码 *</label>
                <input type="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label>邮箱 *</label>
                <input type="email" name="email" value="${param.email}" required>
            </div>
            
            <div class="form-group">
                <label>手机号 *</label>
                <input type="tel" name="phone" value="${param.phone}" required>
            </div>
            
            <div class="form-group">
                <label>用户角色 *</label>
                <select name="role" required>
                    <option value="">请选择角色</option>
                    <option value="user" ${param.role == 'user' ? 'selected' : ''}>普通用户</option>
                    <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>管理员</option>
                    <option value="group" ${param.role == 'group' ? 'selected' : ''}>旅游团</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>创建时间 *</label>
                <input type="text" name="create_time" value="${param.create_time}" 
                       placeholder="格式：2023-01-01 08:00:00" required>
            </div>
            
            <div class="btn-group">
                <button type="submit" name="buttonSubmit" class="btn btn-submit">注册</button>
                <button type="reset" class="btn btn-reset">重置</button>
            </div>
        </form>
        
        <div class="back-link">
            <a href="login.jsp">已有账号？去登录</a>
        </div>
    </div>
    
    <script>
        // 设置创建时间的默认值为当前时间
        window.onload = function() {
            var createTimeInput = document.querySelector('input[name="create_time"]');
            if (!createTimeInput.value) {
                var now = new Date();
                var year = now.getFullYear();
                var month = String(now.getMonth() + 1).padStart(2, '0');
                var day = String(now.getDate()).padStart(2, '0');
                var hours = String(now.getHours()).padStart(2, '0');
                var minutes = String(now.getMinutes()).padStart(2, '0');
                var seconds = String(now.getSeconds()).padStart(2, '0');
                createTimeInput.value = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
            }
        };
    </script>
</body>
</html>