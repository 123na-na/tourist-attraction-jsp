package service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.CartItem;
import bean.User; 
import dao.CartItemDao;
import dao.GoodsDao;

public class CartItemService {
    
    CartItemDao cartItemDao = new CartItemDao();
    
    /**------------------------------ 获取用户购物车列表 */
    public boolean queryCartList(HttpServletRequest request) {
        
        String msg = "";
        
        try {
            System.out.println("========== CartItemService.queryCartList ==========");
            
            // 直接从session获取myUser
            User myUser = (User) request.getSession().getAttribute("myUser");
            System.out.println("DEBUG: 从session获取myUser: " + (myUser != null ? "存在, 用户名: " + myUser.getUsername() : "null"));
            
            if (myUser == null) {
                msg = "请先登录！";
                System.out.println("DEBUG: 用户未登录");
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 获取用户ID - 字符串形式
            String userIdStr = myUser.getUserid();
            System.out.println("DEBUG: 从myUser获取用户ID(字符串): " + userIdStr);
            
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                msg = "无法获取用户信息，请重新登录！";
                System.out.println("DEBUG: 用户ID为空");
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 转换为整数
            int userId = 0;
            try {
                userId = Integer.parseInt(userIdStr.trim());
                System.out.println("DEBUG: 转换为整数用户ID: " + userId);
            } catch (NumberFormatException e) {
                msg = "用户ID格式错误，请重新登录！";
                System.out.println("DEBUG: 用户ID不是有效的数字: " + userIdStr);
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (userId <= 0) {
                msg = "无效的用户ID，请重新登录！";
                System.out.println("DEBUG: 用户ID无效: " + userId);
                request.setAttribute("msg", msg);
                return false;
            }
            
            System.out.println("DEBUG: 最终用户ID: " + userId);
            
            // 获取购物车列表
            System.out.println("DEBUG: 开始查询购物车，用户ID: " + userId);
            List<CartItem> cartItemList = cartItemDao.queryByUserId(request, userId);
            System.out.println("DEBUG: 购物车查询完成，列表大小: " + cartItemList.size());
            
            // 设置到request属性中
            request.setAttribute("cartItemList", cartItemList);
            
            // 计算总金额和总数量
            double totalAmount = 0;
            int totalQuantity = 0;
            int totalItems = cartItemList.size();
            
            for (CartItem item : cartItemList) {
                double subtotal = item.getSubtotal();
                totalAmount += subtotal;
                totalQuantity += item.getQuantity();
                
                // 调试输出
                System.out.println("购物车项: " + 
                                 "商品ID=" + item.getGoodsId() + 
                                 ", 商品名=" + item.getGoodsName() + 
                                 ", 数量=" + item.getQuantity() + 
                                 ", 小计=" + subtotal);
            }
            
            System.out.println("DEBUG: 汇总信息 - 总金额: " + totalAmount + 
                             ", 总数量: " + totalQuantity + 
                             ", 总项数: " + totalItems);
            
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("totalQuantity", totalQuantity);
            request.setAttribute("totalItems", totalItems);
            
            // 设置session中的用户信息（确保存在）
            request.getSession().setAttribute("myUser", myUser);
            
            return true;
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("DEBUG: 查询购物车异常: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return false;
    }
    
    /**------------------------------ 添加商品到购物车 */
    public boolean addToCart(HttpServletRequest request) {
        
        String msg = "";
        
        try {
            System.out.println("========== CartItemService.addToCart ==========");
            
            // 获取当前用户
            User myUser = (User) request.getSession().getAttribute("myUser");
            if (myUser == null) {
                msg = "请先登录！";
                System.out.println("DEBUG: 用户未登录");
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 获取用户ID
            String userIdStr = myUser.getUserid();
            System.out.println("DEBUG: 获取用户ID(字符串): " + userIdStr);
            
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                msg = "无法获取用户信息，请重新登录！";
                System.out.println("DEBUG: 用户ID为空");
                request.setAttribute("msg", msg);
                return false;
            }
            
            int userId = 0;
            try {
                userId = Integer.parseInt(userIdStr.trim());
                System.out.println("DEBUG: 转换为整数用户ID: " + userId);
            } catch (NumberFormatException e) {
                msg = "用户ID格式错误，请重新登录！";
                System.out.println("DEBUG: 用户ID不是有效的数字: " + userIdStr);
                request.setAttribute("msg", msg);
                return false;
            }
            
            String goodsId = request.getParameter("goodsId");
            String quantityStr = request.getParameter("quantity");
            
            System.out.println("DEBUG: 添加购物车 - 用户ID: " + userId + 
                             ", 商品ID: " + goodsId + 
                             ", 数量: " + quantityStr);
            
            if (goodsId == null || goodsId.trim().isEmpty()) {
                msg = "商品ID不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 验证商品ID是否为数字
            int goodsIdInt = 0;
            try {
                goodsIdInt = Integer.parseInt(goodsId.trim());
                System.out.println("DEBUG: 商品ID(整数): " + goodsIdInt);
            } catch (NumberFormatException e) {
                msg = "商品ID格式错误！";
                System.out.println("DEBUG: 商品ID不是有效的数字: " + goodsId);
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 设置默认数量
            int quantity = 1;
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityStr.trim());
                    if (quantity < 1) quantity = 1;
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: 数量格式错误，使用默认值1");
                }
            }
            
            // 创建购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setGoodsId(goodsIdInt);
            cartItem.setQuantity(quantity);
            cartItem.setSelected_date(null); // 默认为空
            cartItem.setTravelers_count(1); // 默认1人
            
            // 添加到购物车
            boolean added = cartItemDao.addCartItem(request, cartItem);
            if (added) {
                msg = "商品已成功加入购物车！";
                System.out.println("DEBUG: 添加购物车成功");
            } else {
                msg = "加入购物车失败！";
                System.out.println("DEBUG: 添加购物车失败");
            }
            
            request.setAttribute("msg", msg);
            return added;
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
            request.setAttribute("msg", msg);
            return false;
        }
    }
    
    /**------------------------------ 更新购物车项（AJAX调用） */
    public String updateCartItemAjax(HttpServletRequest request) {
        String msg = "";
        
        try {
            String cartItemIdStr = request.getParameter("cart_item_id");
            String quantityStr = request.getParameter("quantity");
            String travelersCountStr = request.getParameter("travelers_count");
            
            System.out.println("DEBUG: AJAX更新参数 - cartItemId: " + cartItemIdStr + 
                             ", quantity: " + quantityStr + 
                             ", travelersCount: " + travelersCountStr);
            
            if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
                return "{\"success\": false, \"message\": \"购物车项ID不能为空！\"}";
            }
            
            int cartItemId = Integer.parseInt(cartItemIdStr);
            
            // 获取原始购物车项
            CartItem cartItem = cartItemDao.queryByCartItemId(request, cartItemId);
            if (cartItem == null) {
                return "{\"success\": false, \"message\": \"购物车项不存在！\"}";
            }
            
            // 更新数量
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityStr.trim());
                    if (quantity < 1) quantity = 1;
                    cartItem.setQuantity(quantity);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: 数量格式错误");
                }
            }
            
            // 更新出行人数
            if (travelersCountStr != null && !travelersCountStr.trim().isEmpty()) {
                try {
                    int travelersCount = Integer.parseInt(travelersCountStr.trim());
                    if (travelersCount < 1) travelersCount = 1;
                    cartItem.setTravelers_count(travelersCount);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: 出行人数格式错误");
                }
            }
            
            boolean updated = cartItemDao.updateCartItem(request, cartItem);
            
            if (updated) {
                double subtotal = cartItem.getSubtotal();
                System.out.println("DEBUG: 更新成功，小计: " + subtotal);
                return String.format("{\"success\": true, \"message\": \"更新成功！\", \"subtotal\": %.2f}", subtotal);
            } else {
                return "{\"success\": false, \"message\": \"更新失败！\"}";
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        }
        
        return "{\"success\": false, \"message\": \"系统错误！\"}";
    }

    /**------------------------------ 删除购物车项 */
    public boolean deleteCartItem(HttpServletRequest request) {
        
        String msg = "";
        
        try {
            System.out.println("========== CartItemService.deleteCartItem ==========");
            
            // 从多个位置尝试获取cart_item_id
            String cartItemIdStr = null;
            
            // 尝试1：从request参数获取
            cartItemIdStr = request.getParameter("cart_item_id");
            System.out.println("DEBUG: 从request.getParameter获取cart_item_id: " + cartItemIdStr);
            
            // 尝试2：如果为空，从attribute获取
            if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
                Object cartItemIdObj = request.getAttribute("cart_item_id");
                if (cartItemIdObj != null) {
                    cartItemIdStr = cartItemIdObj.toString();
                    System.out.println("DEBUG: 从request.getAttribute获取cart_item_id: " + cartItemIdStr);
                }
            }
            
            // 尝试3：从session获取（如果是批量删除的情况）
            if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
                Object cartItemIdObj = request.getSession().getAttribute("cart_item_id");
                if (cartItemIdObj != null) {
                    cartItemIdStr = cartItemIdObj.toString();
                    System.out.println("DEBUG: 从session获取cart_item_id: " + cartItemIdStr);
                }
            }
            
            System.out.println("DEBUG: 最终获取的cartItemIdStr: " + cartItemIdStr);
            
            if (cartItemIdStr == null || cartItemIdStr.trim().isEmpty()) {
                msg = "购物车项ID不能为空！";
                System.out.println("DEBUG: 购物车项ID为空");
                request.setAttribute("msg", msg);
                return false;
            }
            
            int cartItemId = 0;
            try {
                cartItemId = Integer.parseInt(cartItemIdStr.trim());
                System.out.println("DEBUG: 解析为整数的cartItemId: " + cartItemId);
            } catch (NumberFormatException e) {
                msg = "购物车项ID格式错误！";
                System.out.println("DEBUG: cartItemId格式错误: " + cartItemIdStr);
                request.setAttribute("msg", msg);
                return false;
            }
            
            boolean deleted = cartItemDao.deleteCartItem(request, cartItemId);
            
            if (deleted) {
                msg = "删除成功！";
                System.out.println("DEBUG: 删除成功");
            } else {
                msg = "删除失败！";
                System.out.println("DEBUG: 删除失败");
            }
            
            request.setAttribute("msg", msg);
            return deleted;
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("DEBUG: 删除购物车项异常: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return false;
    }
    

    /**------------------------------ 批量删除购物车项 */
    public boolean deleteCartItems(HttpServletRequest request) {
        
        String msg = "";
        
        try {
            System.out.println("========== CartItemService.deleteCartItems ==========");
            
            String[] cartItemIdArray = request.getParameterValues("cartItemId");
            
            System.out.println("DEBUG: 批量删除 - 参数数组: " + 
                             (cartItemIdArray != null ? cartItemIdArray.length : "null"));
            
            if (cartItemIdArray == null || cartItemIdArray.length == 0) {
                msg = "请选择要删除的商品！";
                System.out.println("DEBUG: 没有选中任何商品");
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 构建ID字符串
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < cartItemIdArray.length; i++) {
                try {
                    int id = Integer.parseInt(cartItemIdArray[i].trim());
                    if (ids.length() > 0) {
                        ids.append(",");
                    }
                    ids.append(id);
                    System.out.println("DEBUG: 添加有效ID: " + id);
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: 忽略无效ID: " + cartItemIdArray[i]);
                }
            }
            
            if (ids.length() == 0) {
                msg = "没有有效的商品ID！";
                System.out.println("DEBUG: 所有ID都无效");
                request.setAttribute("msg", msg);
                return false;
            }
            
            System.out.println("DEBUG: 批量删除ID: " + ids.toString());
            boolean deleted = cartItemDao.deleteCartItems(request, ids.toString());
            
            if (deleted) {
                msg = "已成功删除选中的商品！";
                System.out.println("DEBUG: 批量删除成功");
            } else {
                msg = "删除失败！";
                System.out.println("DEBUG: 批量删除失败");
            }
            
            request.setAttribute("msg", msg);
            return deleted;
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("DEBUG: 批量删除异常: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return false;
    }
    
    /**------------------------------ 清空购物车 */
    public boolean clearCart(HttpServletRequest request) {
        
        String msg = "";
        
        try {
            System.out.println("========== CartItemService.clearCart ==========");
            
            // 获取当前登录用户ID
            Object userIdObj = request.getSession().getAttribute("userId");
            if (userIdObj == null) {
                msg = "请先登录！";
                System.out.println("DEBUG: 用户未登录");
                request.setAttribute("msg", msg);
                return false;
            }
            
            int userId = Integer.parseInt(userIdObj.toString());
            boolean cleared = cartItemDao.clearUserCart(request, userId);
            
            if (cleared) {
                msg = "购物车已清空！";
                System.out.println("DEBUG: 清空购物车成功");
            } else {
                msg = "清空购物车失败！";
                System.out.println("DEBUG: 清空购物车失败");
            }
            
            request.setAttribute("msg", msg);
            return cleared;
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
            request.setAttribute("msg", msg);
        }
        
        return false;
    }
    
    /**------------------------------ 获取购物车商品数量（用于header显示） */
    public int getCartCount(HttpServletRequest request) {
        
        try {
            // 获取当前登录用户ID
            Object userIdObj = request.getSession().getAttribute("userId");
            if (userIdObj == null) {
                System.out.println("DEBUG: 获取购物车数量 - 用户未登录");
                return 0;
            }
            
            int userId = Integer.parseInt(userIdObj.toString());
            int count = cartItemDao.getCartCount(request, userId);
            System.out.println("DEBUG: 购物车商品数量: " + count);
            return count;
            
        } catch (Exception e) {
            System.out.println("DEBUG: 获取购物车数量异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**------------------------------ 检查购物车是否有商品 */
    public boolean hasCartItems(HttpServletRequest request) {
        
        try {
            // 获取当前登录用户ID
            Object userIdObj = request.getSession().getAttribute("userId");
            if (userIdObj == null) {
                return false;
            }
            
            int userId = Integer.parseInt(userIdObj.toString());
            return cartItemDao.hasCartItems(request, userId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
}