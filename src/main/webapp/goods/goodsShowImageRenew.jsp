<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
<head>
    <title>商品图片管理</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
    <style>
        .container {
            width: 600px;
            margin: 20px auto;
        }
        .goods-info {
            background: #f5f5f5;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .upload-form {
            background: white;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .current-image {
            max-width: 200px;
            max-height: 150px;
            border: 1px solid #ccc;
            margin: 10px 0;
        }
        .delete-section {
            margin-top: 20px;
            padding-top: 15px;
            border-top: 1px solid #eee;
        }
        .btn-delete {
            background: #dc3545;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-delete:hover {
            background: #c82333;
        }
    </style>
</head>
<body>
    <%@ include file="../header.jsp" %>
    
    <div class="container">
        <h3>商品图片管理</h3>
        
        <!-- 保留原有的上传图片表单 -->
        <div class="upload-form">
            <form action="GoodsImageUploadDo" method="post" enctype="multipart/form-data">
                <input type="hidden" name="goodsId" value="${goods.goodsId}">
                
                <p><strong>选择新图片文件：</strong></p>
                <input type="file" name="image" required>
                
                <p style="color:#666; font-size:12px; margin-top:5px;">
                    （支持JPG、JPEG、PNG、GIF、BMP格式，最大2MB）
                </p>
                
                <div style="margin-top:20px; text-align:center;">
                    <input type="submit" value="上传图片">
                    &nbsp;&nbsp;
                    <a href="GoodsShow?goodsId=${goods.goodsId}">
                        <input type="button" value="返回">
                    </a>
                </div>
            </form>
        </div>
        
        <!-- 新增的删除图片功能 -->
        <c:if test="${not empty goods.image and goods.image != 'noImage.png'}">
            <div class="delete-section">
                <p><strong>删除当前图片：</strong></p>
                <form action="GoodsImageDeleteDo" method="get" style="display:inline;">
                    <input type="hidden" name="goodsId" value="${goods.goodsId}">
                    <input type="hidden" name="image" value="${goods.image}">
                    <input type="submit" value="删除图片" class="btn-delete" 
                           onclick="return confirm('确定要删除这张图片吗？')">
                </form>
                <p style="color:#666; font-size:12px; margin-top:5px;">
                    删除后，商品将显示为默认无图片状态
                </p>
            </div>
        </c:if>
        
        <!-- 显示消息 -->
        <div id="msg" class="msg">${msg}</div>
        
        <div style="margin-top:20px; text-align:center;">
            <a href="GoodsShow?goodsId=${goods.goodsId}">返回商品详情</a>
            &nbsp;|&nbsp;
            <a href="GoodsList">返回商品列表</a>
        </div>
    </div>
    
    <%@ include file="../footer.jsp" %>
</body>
</html>