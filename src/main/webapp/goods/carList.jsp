<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>我的购物车 - 走着瞧旅游商城</title>
    <link rel="stylesheet" type="text/css" href="../css/css.css">
    <style type="text/css">
        /* 重置样式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
            background: #f8f9fa;
            color: #333;
        }
        
        /* 顶部导航 */
        .top-nav {
            background: white;
            border-bottom: 1px solid #eaeaea;
            padding: 15px 0;
        }
        
        .nav-container {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
        }
        
        .logo {
            font-size: 24px;
            font-weight: bold;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-decoration: none;
        }
        
        .user-info {
            font-size: 14px;
            color: #666;
        }
        
        .user-info a {
            color: #667eea;
            text-decoration: none;
            margin-left: 15px;
        }
        
        /* 页面标题 */
        .page-header {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .page-title {
            font-size: 28px;
            font-weight: 300;
            color: #333;
            margin-bottom: 10px;
        }
        
        .page-subtitle {
            color: #666;
            font-size: 14px;
        }
        
        /* 购物车内容 */
        .cart-container {
            max-width: 1200px;
            margin: 0 auto 40px;
            padding: 0 20px;
        }
        
        /* 购物车表格 */
        .cart-table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
            overflow: hidden;
            margin-bottom: 20px;
        }
        
        .cart-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .cart-table thead {
            background: #f8f9fa;
        }
        
        .cart-table th {
            padding: 18px 15px;
            text-align: left;
            font-weight: 500;
            color: #555;
            font-size: 14px;
            border-bottom: 2px solid #eaeaea;
        }
        
        .cart-table tbody tr {
            border-bottom: 1px solid #f0f0f0;
            transition: background 0.2s;
        }
        
        .cart-table tbody tr:hover {
            background: #fafafa;
        }
        
        .cart-table td {
            padding: 20px 15px;
            vertical-align: middle;
        }
        
        /* 商品信息 */
        .product-cell {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .product-img {
            width: 80px;
            height: 60px;
            border-radius: 8px;
            overflow: hidden;
            flex-shrink: 0;
        }
        
        .product-img img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .product-info {
            flex: 1;
        }
        
        .product-name {
            font-size: 16px;
            font-weight: 500;
            color: #333;
            margin-bottom: 5px;
            text-decoration: none;
            display: block;
        }
        
        .product-name:hover {
            color: #667eea;
        }
        
        .product-location {
            font-size: 13px;
            color: #888;
        }
        
        /* 价格样式 */
        .price {
            font-size: 16px;
            font-weight: 600;
            color: #ff6b6b;
        }
        
        /* 日期样式 */
        .date-badge {
            background: linear-gradient(135deg, #667eea20 0%, #764ba220 100%);
            color: #667eea;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 500;
        }
        
        /* 数量控制 */
        .quantity-control {
            display: inline-flex;
            align-items: center;
            border: 1px solid #e0e0e0;
            border-radius: 6px;
            overflow: hidden;
        }
        
        .qty-btn {
            width: 32px;
            height: 32px;
            background: #f8f9fa;
            border: none;
            color: #666;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.2s;
        }
        
        .qty-btn:hover {
            background: #e9ecef;
        }
        
        .qty-input {
            width: 50px;
            height: 32px;
            border: none;
            border-left: 1px solid #e0e0e0;
            border-right: 1px solid #e0e0e0;
            text-align: center;
            font-size: 14px;
            font-weight: 500;
            color: #333;
        }
        
        /* 复选框 */
        .select-checkbox {
            width: 20px;
            height: 20px;
            cursor: pointer;
            accent-color: #667eea;
        }
        
        /* 操作按钮 */
        .action-btn {
            background: none;
            border: 1px solid #ff6b6b;
            color: #ff6b6b;
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 13px;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .action-btn:hover {
            background: #ff6b6b;
            color: white;
        }
        
        /* 底部操作栏 */
        .cart-footer {
            background: white;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .selection-area {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .select-all {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            color: #666;
        }
        
        .footer-buttons {
            display: flex;
            gap: 10px;
        }
        
        .footer-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .delete-btn {
            background: #ff6b6b;
            color: white;
        }
        
        .delete-btn:hover {
            background: #ff5252;
        }
        
        .update-btn {
            background: #667eea;
            color: white;
        }
        
        .update-btn:hover {
            background: #5a6fd8;
        }
        
        /* 购物车汇总 */
        .cart-summary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            padding: 30px;
            color: white;
            margin-bottom: 30px;
        }
        
        .summary-content {
            display: flex;
            justify-content: space-around;
            align-items: center;
            text-align: center;
        }
        
        .summary-item {
            flex: 1;
        }
        
        .summary-label {
            font-size: 14px;
            opacity: 0.9;
            margin-bottom: 8px;
        }
        
        .summary-value {
            font-size: 24px;
            font-weight: 600;
        }
        
        .total-amount .summary-value {
            font-size: 32px;
            color: #ffd93d;
        }
        
        /* 结算按钮 */
        .checkout-section {
            text-align: center;
            margin-top: 20px;
        }
        
        .checkout-btn {
            background: #ff6b6b;
            color: white;
            border: none;
            padding: 16px 50px;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }
        
        .checkout-btn:hover {
            background: #ff5252;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255,107,107,0.3);
        }
        
        .continue-btn {
            background: white;
            color: #666;
            border: 1px solid #ddd;
            padding: 16px 30px;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.2s;
            margin-right: 20px;
        }
        
        .continue-btn:hover {
            background: #f8f9fa;
            border-color: #ccc;
        }
        
        /* 空购物车 */
        .empty-cart {
            background: white;
            border-radius: 12px;
            padding: 80px 40px;
            text-align: center;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }
        
        .empty-icon {
            font-size: 80px;
            margin-bottom: 20px;
            opacity: 0.3;
        }
        
        .empty-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 15px;
            font-weight: 400;
        }
        
        .empty-text {
            color: #666;
            font-size: 14px;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .shop-btn {
            display: inline-block;
            padding: 12px 35px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 500;
            transition: all 0.2s;
        }
        
        .shop-btn:hover {
            background: #5a6fd8;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102,126,234,0.3);
        }
        
        /* 消息提示 */
        .message-box {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }
        
        .message {
            padding: 12px 20px;
            border-radius: 8px;
            font-size: 14px;
            display: none;
        }
        
        .message.show {
            display: block;
        }
        
        .message.success {
            background: #e8f5e9;
            color: #2e7d32;
            border: 1px solid #c8e6c9;
        }
        
        .message.error {
            background: #ffebee;
            color: #c62828;
            border: 1px solid #ffcdd2;
        }
        
        .message.info {
            background: #e3f2fd;
            color: #1976d2;
            border: 1px solid #bbdefb;
        }
        
        /* 响应式 */
        @media (max-width: 768px) {
            .cart-table {
                display: block;
                overflow-x: auto;
            }
            
            .summary-content {
                flex-direction: column;
                gap: 20px;
            }
            
            .cart-footer {
                flex-direction: column;
                gap: 20px;
                align-items: flex-start;
            }
            
            .footer-buttons {
                width: 100%;
                justify-content: flex-end;
            }
        }
    </style>
</head>
<body>
    <!-- 顶部导航 -->
    <div class="top-nav">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/" class="logo">走着瞧旅游</a>
            <div class="user-info">
                <span style="color:#800;">欢迎，${ myUser.username }</span>&emsp;
                <a href="${pageContext.request.contextPath}/main.jsp">用户功能</a>&emsp;
                <a href="${pageContext.request.contextPath}/Logout">退出登录</a>
            </div>
        </div>
    </div>
    
    <!-- 页面标题 -->
    <div class="page-header">
        <h1 class="page-title">我的购物车</h1>
        <p class="page-subtitle">精心挑选的旅程，从这里开始</p>
    </div>
    
    <!-- 消息提示 -->
    <c:if test="${not empty msg}">
        <div class="message-box">
            <div class="message ${msg.contains('成功') ? 'success' : msg.contains('失败') ? 'error' : 'info'} show">
                ${msg}
            </div>
        </div>
    </c:if>
    
    <!-- 购物车内容 -->
    <div class="cart-container">
        <c:if test="${not empty cartItemList and cartItemList.size() > 0}">
            <form action="CartItemList" method="post">
                <!-- 购物车表格 -->
                <div class="cart-table-container">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th style="width: 50px;">
                                    <input type="checkbox" class="select-checkbox" 
                                           onchange="checkAll(this)">
                                </th>
                                <th style="width: 60px;">序号</th>
                                <th>商品信息</th>
                                <th style="width: 120px;">价格</th>
                                <th style="width: 150px;">出行日期</th>
                                <th style="width: 140px;">出行人数</th>
                                <th style="width: 140px;">数量</th>
                                <th style="width: 120px;">小计</th>
                                <th style="width: 100px;">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cartItem" items="${cartItemList}" varStatus="status">
                                <tr>
                                    <td>
                                        <input type="checkbox" name="cartItemId" 
                                               value="${cartItem.cart_item_id}" class="select-checkbox"
                                               onchange="updateSummary()">
                                    </td>
                                    <td>${status.index + 1}</td>
                                    <td>
                                        <div class="product-cell">
                                            <div class="product-img">
                                                <img src="fileUpload/goodsImage/${cartItem.goods.image}" 
                                                     alt="${cartItem.goodsName}"
                                                     onerror="this.src='../image/default_product.jpg'"> 
                                            </div>
                                            <div class="product-info">
                                                <a href="GoodsShow?goodsId=${cartItem.goodsId}" class="product-name">
                                                    ${cartItem.goodsName}
                                                </a>
                                                <div class="product-location">
                                                    ${cartItem.goodsLocation}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="price">
                                        <fmt:formatNumber value="${cartItem.goodsPrice}" pattern="¥#,##0.00"/>
                                    </td>
                                    <td>
                                        <span class="date-badge">${cartItem.selected_date}</span>
                                    </td>
                                    <td>
                                        <div class="quantity-control">
                                            <button type="button" class="qty-btn" 
                                                    onclick="changeTravelerCount(${cartItem.cart_item_id}, -1)">-</button>
                                            <input type="text" id="travelers_${cartItem.cart_item_id}" 
                                                   value="${cartItem.travelers_count}" readonly 
                                                   class="qty-input">
                                            <button type="button" class="qty-btn" 
                                                    onclick="changeTravelerCount(${cartItem.cart_item_id}, 1)">+</button>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="quantity-control">
                                            <button type="button" class="qty-btn" 
                                                    onclick="changeQuantity(${cartItem.cart_item_id}, -1)">-</button>
                                            <input type="text" id="quantity_${cartItem.cart_item_id}" 
                                                   value="${cartItem.quantity}" readonly 
                                                   class="qty-input">
                                            <button type="button" class="qty-btn" 
                                                    onclick="changeQuantity(${cartItem.cart_item_id}, 1)">+</button>
                                        </div>
                                    </td>
                                    <td class="price" id="subtotal_${cartItem.cart_item_id}">
                                        <fmt:formatNumber value="${cartItem.subtotal}" pattern="¥#,##0.00"/>
                                    </td>
                                    <td>
                                        <button type="button" class="action-btn" 
                                                onclick="deleteCartItem(${cartItem.cart_item_id})">
                                            删除
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <!-- 底部操作栏 -->
                <div class="cart-footer">
                    <div class="selection-area">
                        <div class="select-all">
                            <input type="checkbox" class="select-checkbox" 
                                   onchange="checkAll(this)">
                            <span>全选</span>
                        </div>
                        <div class="footer-buttons">
							<button type="button" onclick="return deleteSelectedItems();" class="footer-btn delete-btn">
							    删除选中
							</button>
                        </div>
                    </div>
                </div>
                
                <!-- 购物车汇总 -->
                <div class="cart-summary">
                    <div class="summary-content">
                        <div class="summary-item">
                            <div class="summary-label">已选择商品</div>
                            <div class="summary-value" id="selectedCount">0</div>
                        </div>
                        <div class="summary-item">
                            <div class="summary-label">总数量</div>
                            <div class="summary-value" id="totalQuantity">0</div>
                        </div>
                        <div class="summary-item total-amount">
                            <div class="summary-label">合计总金额</div>
                            <div class="summary-value" id="totalAmount">¥0.00</div>
                        </div>
                    </div>
                </div>
                
                <!-- 结算按钮 -->
                <div class="checkout-section">
                    <button type="button" class="continue-btn" onclick="continueShopping()">
                        ← 继续购物
                    </button>
                    <button type="button" class="checkout-btn" onclick="return confirm('确认结算商品吗？');">
                        去结算 →
                    </button>
                </div>
            </form>
        </c:if>
        
        <c:if test="${empty cartItemList or cartItemList.size() == 0}">
            <div class="empty-cart">
                <div class="empty-icon">🛒</div>
                <h2 class="empty-title">购物车空空如也</h2>
                <p class="empty-text">
                    还没有添加任何商品到购物车<br>
                    去发现心仪的旅行路线，让旅程从这里开始吧！
                </p>
                <a href="${pageContext.request.contextPath}/GoodsList" class="shop-btn">
                    去发现精彩旅行
                </a>
            </div>
        </c:if>
    </div>
    
    <script type="text/javascript">

    
        // 全选功能
        function checkAll(checkbox) {
            const checkboxes = document.querySelectorAll('input[name="cartItemId"]');
            checkboxes.forEach(cb => cb.checked = checkbox.checked);
            updateSummary();
        }
        
        // 更新汇总信息
        function updateSummary() {
            const checkboxes = document.querySelectorAll('input[name="cartItemId"]:checked');
            const selectedCount = checkboxes.length;
            let totalQuantity = 0;
            let totalAmount = 0;
            
            checkboxes.forEach(checkbox => {
                const cartItemId = checkbox.value;
                const quantity = parseInt(document.getElementById('quantity_' + cartItemId).value);
                const travelers = parseInt(document.getElementById('travelers_' + cartItemId).value);
                const subtotalText = document.getElementById('subtotal_' + cartItemId).textContent;
                const subtotal = parseFloat(subtotalText.replace(/[^0-9.-]+/g, ""));
                
                totalQuantity += quantity;
                totalAmount += subtotal;
            });
            
            document.getElementById('selectedCount').textContent = selectedCount;
            document.getElementById('totalQuantity').textContent = totalQuantity;
            document.getElementById('totalAmount').textContent = '¥' + totalAmount.toFixed(2);
        }
        
        // 修改数量
        function changeQuantity(cartItemId, change) {
            const input = document.getElementById('quantity_' + cartItemId);
            let value = parseInt(input.value) + change;
            if (value < 1) value = 1;
            updateCartItem(cartItemId, value, 'quantity');
        }
        
        // 修改出行人数
        function changeTravelerCount(cartItemId, change) {
            const input = document.getElementById('travelers_' + cartItemId);
            let value = parseInt(input.value) + change;
            if (value < 1) value = 1;
            updateCartItem(cartItemId, value, 'travelers_count');
        }
        
       // AJAX更新
        function updateCartItem(cartItemId, value, field) {
            console.log("更新购物车项 - ID: " + cartItemId + ", 值: " + value + ", 字段: " + field);
            
            // 构建请求参数
            const params = new URLSearchParams();
            params.append('cart_item_id', cartItemId);
            params.append(field, value);
            
            console.log("发送请求参数: " + params.toString());
            
            // 静默更新，不显示提示信息
            fetch('CartItemUpdate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
            .then(response => {
                console.log("收到响应，状态码: " + response.status);
                if (!response.ok) {
                    throw new Error('网络响应错误: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log("响应数据: ", data);
                if (data.success) {
                    // 静默更新界面，不显示提示信息
                    if (field === 'quantity') {
                        document.getElementById('quantity_' + cartItemId).value = value;
                    } else {
                        document.getElementById('travelers_' + cartItemId).value = value;
                    }
                    
                    if (data.subtotal) {
                        document.getElementById('subtotal_' + cartItemId).textContent = 
                            '¥' + parseFloat(data.subtotal).toFixed(2);
                    }
                    
                    // 静默更新汇总信息
                    updateSummary();
                    
                    // 不显示成功提示
                    // showMessage('更新成功！', 'success');
                } else {
                    // 只在失败时显示错误信息
                    console.error("更新失败: " + data.message);
                    showMessage(data.message || '更新失败', 'error');
                }
            })
            .catch(error => {
                console.error('更新请求错误:', error);
                // 只在网络错误时显示提示
                showMessage('网络错误，请重试', 'error');
            });
        }
        
        // 删除购物车项
     function deleteCartItem(cartItemId) {
         console.log("尝试删除购物车项ID: " + cartItemId);
         
         if (!confirm('确定要删除这个商品吗？')) {
             console.log("用户取消删除");
             return;
         }
         
         // 显示加载提示
         showMessage('正在删除...', 'info');
         
         // 构建请求参数
         const params = new URLSearchParams();
         params.append('cart_item_id', cartItemId);
         
         console.log("发送删除请求到CartItemDelete, 参数: cart_item_id=" + cartItemId);
         
         fetch('CartItemDelete', {
             method: 'POST',
             headers: {
                 'Content-Type': 'application/x-www-form-urlencoded',
             },
             body: params.toString()
         })
         .then(response => {
             console.log("收到响应, 状态: " + response.status);
             if (!response.ok) {
                 throw new Error('网络响应错误: ' + response.status);
             }
             return response.json();
         })
         .then(data => {
             console.log("响应数据: ", data);
             if (data.success) {
                 showMessage('删除成功！', 'success');
                 // 延迟1秒后刷新页面
                 setTimeout(() => {
                     console.log("刷新页面");
                     location.reload();
                 }, 1000);
             } else {
                 showMessage(data.message || '删除失败', 'error');
                 console.error("删除失败: " + data.message);
             }
         })
         .catch(error => {
             console.error('删除请求错误:', error);
             showMessage('网络错误，请重试: ' + error.message, 'error');
         });
     }

     // 批量删除功能
     function deleteSelectedItems() {
         const checkboxes = document.querySelectorAll('input[name="cartItemId"]:checked');
         
         if (checkboxes.length === 0) {
             showMessage('请选择要删除的商品！', 'error');
             return false;
         }
         
         if (!confirm(`确定要删除选中的 ${checkboxes.length} 件商品吗？`)) {
             return false;
         }
         
         // 收集选中的ID
         const selectedIds = Array.from(checkboxes).map(cb => cb.value);
         
         // 显示加载提示
         showMessage('正在删除选中的商品...', 'info');
         
         // 使用Form提交
         const form = document.querySelector('form[action="CartItemList"]');
         const hiddenInput = document.createElement('input');
         hiddenInput.type = 'hidden';
         hiddenInput.name = 'cartItemId';
         
         // 为每个选中的ID创建一个隐藏input
         checkboxes.forEach(checkbox => {
             const input = document.createElement('input');
             input.type = 'hidden';
             input.name = 'cartItemId';
             input.value = checkbox.value;
             form.appendChild(input);
         });
         
         // 设置删除操作标识
         const deleteBtn = document.createElement('input');
         deleteBtn.type = 'hidden';
         deleteBtn.name = 'buttonDelete';
         deleteBtn.value = '删除选中';
         form.appendChild(deleteBtn);
         
         // 提交表单
         form.submit();
         
         return false;
     }

     // 修改表单的onsubmit事件
     document.addEventListener('DOMContentLoaded', function() {
         const form = document.querySelector('form[action="CartItemList"]');
         if (form) {
             form.onsubmit = function(e) {
                 const deleteBtn = this.querySelector('input[name="buttonDelete"]');
                 if (deleteBtn && deleteBtn.value === '删除选中') {
                     const checkboxes = this.querySelectorAll('input[name="cartItemId"]:checked');
                     if (checkboxes.length === 0) {
                         e.preventDefault();
                         showMessage('请选择要删除的商品！', 'error');
                         return false;
                     }
                 }
                 return true;
             };
         }
     });
        
        
        // 继续购物
        function continueShopping() {
            window.location.href = 'GoodsList';
        }
        
        // 去结算
        function checkout() {
            const selectedIds = Array.from(
                document.querySelectorAll('input[name="cartItemId"]:checked')
            ).map(cb => cb.value);
            
            if (selectedIds.length === 0) {
                showMessage('请选择要结算的商品', 'error');
                return;
            }
            
            window.location.href = 'OrderCreate?cartItemIds=' + selectedIds.join(',');
        }
        
        // 显示消息
        function showMessage(message, type) {
            const messageDiv = document.createElement('div');
            messageDiv.className = `message ${type} show`;
            messageDiv.textContent = message;
            
            const container = document.querySelector('.cart-container');
            container.insertBefore(messageDiv, container.firstChild);
            
            setTimeout(() => {
                messageDiv.remove();
            }, 3000);
        }
        
        // 页面加载时初始化
        document.addEventListener('DOMContentLoaded', () => {
            updateSummary();
        });
    </script>
</body>
</html>