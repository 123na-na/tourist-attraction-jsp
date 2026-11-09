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
            padding: 20px;
        }
        .upload-box {
            background: white;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .preview-box {
            text-align: center;
            margin: 20px 0;
        }
        .preview-img {
            max-width: 300px;
            max-height: 200px;
            border: 1px solid #ccc;
            padding: 5px;
            background: #f9f9f9;
        }
        .form-group {
            margin: 15px 0;
        }
        .file-input {
            padding: 8px;
            border: 1px solid #ddd;
            width: 100%;
            box-sizing: border-box;
        }
        .btn {
            padding: 8px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary {
            background: #007bff;
            color: white;
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        .btn-danger {
            background: #dc3545;
            color: white;
        }
    </style>
</head>
<body>
    
    
    <div class="container">
        <h3>商品图片管理</h3>
        
         <!-- 显示当前图片 -->
        <div class="current-image-section">
            <h4>当前图片：</h4>
            <c:choose>
                <c:when test="${not empty goods.image and goods.image != ''}">
                    <div>
                        <img src="fileUpload/goodsImage/${goods.image}" 
                             alt="商品图片" class="current-image">
                        <p>文件名：${goods.image}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <p style="color:#666;">当前没有商品图片</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- 上传表单 -->
        <div class="upload-box">
            <form action="GoodsImageUploadDo" method="post" enctype="multipart/form-data" id="uploadForm">
                <input type="hidden" name="goodsId" value="${param.goodsId}">
                <input type="hidden" name="image" value="${param.image}">
                
                <div class="form-group">
                    <label for="imageFile"><strong>选择图片文件：</strong></label>
                    <input type="file" name="file" id="imageFile" class="file-input" required 
                           accept=".jpg,.jpeg,.png,.gif,.bmp">
                </div>
                
                <div class="form-group">
                    <p style="color:#666; font-size:12px; margin-top:5px;">
                        支持格式：JPG、JPEG、PNG、GIF、BMP<br>
                        最大文件大小：2MB<br>
                        建议尺寸：800x600像素
                    </p>
                </div>
                
                <div class="form-group" style="text-align:center;">
                    <input type="submit" value="上传图片" class="btn btn-primary">
                    <a href="GoodsShow?goodsId=${param.goodsId}" style="margin-left:10px;">
                        <button type="button" class="btn btn-secondary">返回商品详情</button>
                    </a>
                </div>
            </form>
        </div>
        
         <!-- 删除当前图片 -->
        <c:if test="${not empty goods.image and goods.image != ''}">
            <div class="delete-section">
                <form action="GoodsImageDeleteDo" method="get" 
                      style="margin-top: 15px;">
                    <input type="hidden" name="goodsId" value="${goods.goodsId}">
                    <input type="hidden" name="image" value="${goods.image}">
                    <button type="submit" class="btn-delete"
                            onclick="return confirm('确定要删除这张图片吗？删除后将无法恢复！');">
                        删除当前图片
                    </button>
                </form>
            </div>
        </c:if>
        
        <!-- 显示操作消息 -->
        <div id="msg" class="msg">
            <c:if test="${not empty msg}">
                <div style="background: #e9f7e9; padding: 10px; border-radius: 4px; margin: 10px 0;">
                    ${msg}
                </div>
            </c:if>
        </div>
    </div>
    
    
</body>
</html>