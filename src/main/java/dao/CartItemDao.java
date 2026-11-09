package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.CartItem;
import bean.Goods;
import bean.Category;

public class CartItemDao {
    
    Db db = new Db();
    String sql = "";
    ResultSet rs = null;
    
    /** 根据用户ID获取购物车列表（包含商品信息）*/
    public List<CartItem> queryByUserId(HttpServletRequest request, int userId) {
        
        List<CartItem> cartItemList = new ArrayList<>();
        String msg = "";
        
        try {
            System.out.println("========== CartItemDao.queryByUserId ==========");
            System.out.println("查询用户ID: " + userId);
            
            // 使用JOIN查询，同时获取购物车和商品信息
            sql = "SELECT c.*, g.goodsName, g.price, g.location, g.image " +
                  "FROM shopping_cart c " +
                  "LEFT JOIN tb_goods g ON c.goodsId = g.goodsId " +
                  "WHERE c.userid = ? " +
                  "ORDER BY c.add_time DESC";
            
            System.out.println("执行SQL: " + sql);
            rs = db.select(sql, String.valueOf(userId));
            
            if (rs == null) {
                System.out.println("DEBUG: 查询结果集为null");
                msg = "数据库查询失败";
                request.setAttribute("msg", msg);
                return cartItemList;
            }
            
            int cartCount = 0;
            
            while (rs.next()) {
                cartCount++;
                int goodsId = rs.getInt("goodsId");
                System.out.println("购物车记录 " + cartCount + ": cart_item_id=" + rs.getInt("cart_item_id") + 
                                 ", goodsId=" + goodsId + ", goodsName=" + rs.getString("goodsName"));
                
                CartItem cartItem = new CartItem();
                cartItem.setCart_item_id(rs.getInt("cart_item_id"));
                cartItem.setUserId(rs.getInt("userid"));
                cartItem.setGoodsId(goodsId);
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setSelected_date(rs.getString("selected_date"));
                cartItem.setTravelers_count(rs.getInt("travelers_count"));
                cartItem.setAdd_time(rs.getTimestamp("add_time"));
                cartItem.setUpdate_time(rs.getTimestamp("update_time"));
                
                // 创建商品对象并设置属性
                Goods goods = new Goods();
                goods.setGoodsId(String.valueOf(goodsId));
                goods.setGoodsName(rs.getString("goodsName"));
                goods.setPrice(rs.getString("price"));
                goods.setLocation(rs.getString("location"));
                goods.setImage(rs.getString("image"));
                
                cartItem.setGoods(goods);
                cartItemList.add(cartItem);
            }
            
            System.out.println("找到购物车记录: " + cartCount + " 条");
            
            if (cartCount == 0) {
                System.out.println("用户 " + userId + " 的购物车为空");
                msg += "您的购物车是空的; ";
            }
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();
            System.out.println("CartItemDao.queryByUserId异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return cartItemList;
    }
    
    /**------------------------------ 根据购物车项ID查询 */
    public CartItem queryByCartItemId(HttpServletRequest request, int cartItemId) {
        
        CartItem cartItem = null;
        String msg = "";
        
        try {
            sql = "SELECT * FROM shopping_cart WHERE cart_item_id = ?";
            rs = db.select(sql, String.valueOf(cartItemId));
            
            if (rs == null) {
                msg = "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return cartItem;
            }
            
            if (rs.next()) {
                cartItem = new CartItem();
                cartItem.setCart_item_id(rs.getInt("cart_item_id"));
                cartItem.setUserId(rs.getInt("userid"));
                cartItem.setGoodsId(rs.getInt("goodsId"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setSelected_date(rs.getString("selected_date"));
                cartItem.setTravelers_count(rs.getInt("travelers_count"));
                cartItem.setAdd_time(rs.getTimestamp("add_time"));
                cartItem.setUpdate_time(rs.getTimestamp("update_time"));
                
                // 查询商品信息
                GoodsDao goodsDao = new GoodsDao();
                Goods goods = goodsDao.queryByGoodsId(request, String.valueOf(rs.getInt("goodsId")));
                cartItem.setGoods(goods);
            } else {
                msg = "购物车项不存在！";
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return cartItem;
    }
    
    /**------------------------------ 根据用户ID和商品ID查询购物车项 */
    public CartItem queryByUserIdAndGoodsId(HttpServletRequest request, int userId, String goodsId) {
        
        CartItem cartItem = null;
        String msg = "";
        
        try {
            sql = "SELECT * FROM shopping_cart WHERE userid = ? AND goodsId = ?";
            rs = db.select(sql, String.valueOf(userId), goodsId);
            
            if (rs == null) {
                msg = "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return cartItem;
            }
            
            if (rs.next()) {
                cartItem = new CartItem();
                cartItem.setCart_item_id(rs.getInt("cart_item_id"));
                cartItem.setUserId(rs.getInt("userid"));
                cartItem.setGoodsId(rs.getInt("goodsId"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setSelected_date(rs.getString("selected_date"));
                cartItem.setTravelers_count(rs.getInt("travelers_count"));
                cartItem.setAdd_time(rs.getTimestamp("add_time"));
                cartItem.setUpdate_time(rs.getTimestamp("update_time"));
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return cartItem;
    }
    
    /**------------------------------ 添加购物车项 */
    public boolean addCartItem(HttpServletRequest request, CartItem cartItem) {
        
        String msg = "";
        boolean success = false;
        
        try {
            System.out.println("CartItemDao.addCartItem - 用户ID: " + cartItem.getUserId() + 
                             ", 商品ID: " + cartItem.getGoodsId() +
                             ", 数量: " + cartItem.getQuantity());
            
            // 检查是否已存在
            CartItem existing = queryByUserIdAndGoodsId(request, cartItem.getUserId(), 
                                                       String.valueOf(cartItem.getGoodsId()));
            
            if (existing != null) {
                // 更新数量
                sql = "UPDATE shopping_cart SET quantity = quantity + ?, update_time = NOW() " +
                      "WHERE userid = ? AND goodsId = ?";
                int result = db.update(sql, 
                    String.valueOf(cartItem.getQuantity()),
                    String.valueOf(cartItem.getUserId()),
                    String.valueOf(cartItem.getGoodsId()));
                success = result > 0;
                System.out.println("更新购物车项结果: " + success + ", 影响行数: " + result);
            } else {
                // 新增 - 需要同时添加用户姓名和商品名称
                // 先查询用户信息
                sql = "SELECT username FROM user WHERE userid = ?";
                rs = db.select(sql, String.valueOf(cartItem.getUserId()));
                String userName = "";
                if (rs != null && rs.next()) {
                    userName = rs.getString("username");
                }
                if (rs != null) rs.close();
                
                // 查询商品信息
                sql = "SELECT goodsName, price FROM tb_goods WHERE goodsId = ?";
                rs = db.select(sql, String.valueOf(cartItem.getGoodsId()));
                String goodsName = "";
                String goodsPrice = "";
                if (rs != null && rs.next()) {
                    goodsName = rs.getString("goodsName");
                    goodsPrice = rs.getString("price");
                }
                if (rs != null) rs.close();
                
                // 插入购物车记录
                sql = "INSERT INTO shopping_cart " +
                      "(userid, user_name, goodsId, goods_name, goods_price, quantity, selected_date, travelers_count) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                System.out.println("DEBUG: 插入SQL: " + sql);
                System.out.println("DEBUG: 参数: userid=" + cartItem.getUserId() + 
                                 ", user_name=" + userName + 
                                 ", goodsId=" + cartItem.getGoodsId() + 
                                 ", goods_name=" + goodsName + 
                                 ", goods_price=" + goodsPrice);
                
                int result = db.update(sql, 
                    String.valueOf(cartItem.getUserId()),
                    userName,
                    String.valueOf(cartItem.getGoodsId()),
                    goodsName,
                    goodsPrice,
                    String.valueOf(cartItem.getQuantity()),
                    cartItem.getSelected_date(),
                    String.valueOf(cartItem.getTravelers_count()));
                
                success = result > 0;
                System.out.println("新增购物车项结果: " + success + ", 影响行数: " + result);
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("添加购物车异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
    
    /**------------------------------ 更新购物车项数量 */
    public boolean updateQuantity(HttpServletRequest request, int cartItemId, int quantity) {
        
        String msg = "";
        boolean success = false;
        
        try {
            sql = "UPDATE shopping_cart SET quantity = ?, update_time = NOW() WHERE cart_item_id = ?";
            int result = db.update(sql, String.valueOf(quantity), String.valueOf(cartItemId));
            success = result > 0;
            System.out.println("更新数量结果: " + success + ", 影响行数: " + result);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
	
    /**------------------------------ 更新购物车项信息 */
    public boolean updateCartItem(HttpServletRequest request, CartItem cartItem) {
        
        String msg = "";
        boolean success = false;
        
        try {
            System.out.println("========== CartItemDao.updateCartItem ==========");
            System.out.println("DEBUG: 更新购物车项 - ID: " + cartItem.getCart_item_id() + 
                             ", 数量: " + cartItem.getQuantity() + 
                             ", 出行人数: " + cartItem.getTravelers_count());
            
            sql = "UPDATE shopping_cart SET quantity = ?, selected_date = ?, travelers_count = ?, " +
                  "update_time = NOW() WHERE cart_item_id = ?";
            
            int result = db.update(sql, 
                String.valueOf(cartItem.getQuantity()),
                cartItem.getSelected_date(),
                String.valueOf(cartItem.getTravelers_count()),
                String.valueOf(cartItem.getCart_item_id()));
            
            success = result > 0;
            System.out.println("更新购物车项结果: " + success + ", 影响行数: " + result);
            
            if (success) {
                // 重新查询并更新商品信息（确保小计计算正确）
                GoodsDao goodsDao = new GoodsDao();
                Goods goods = goodsDao.queryByGoodsId(request, String.valueOf(cartItem.getGoodsId()));
                if (goods != null) {
                    cartItem.setGoods(goods);
                    System.out.println("DEBUG: 重新加载商品信息: " + goods.getGoodsName() + ", 价格: " + goods.getPrice());
                }
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            System.out.println("DEBUG: 更新购物车项异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
    /**------------------------------ 删除购物车项 */
    public boolean deleteCartItem(HttpServletRequest request, int cartItemId) {
        
        String msg = "";
        boolean success = false;
        
        try {
            sql = "DELETE FROM shopping_cart WHERE cart_item_id = ?";
            int result = db.delete(sql, String.valueOf(cartItemId));
            success = result > 0;
            System.out.println("删除购物车项结果: " + success + ", 影响行数: " + result);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
    
    /**------------------------------ 批量删除购物车项 */
    public boolean deleteCartItems(HttpServletRequest request, String cartItemIds) {
        
        String msg = "";
        boolean success = false;
        
        try {
            sql = "DELETE FROM shopping_cart WHERE cart_item_id IN (" + cartItemIds + ")";
            System.out.println("批量删除SQL: " + sql);
            int result = db.delete(sql);
            success = result > 0;
            System.out.println("批量删除结果: " + success + ", 影响行数: " + result);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
    
    /**------------------------------ 清空用户购物车 */
    public boolean clearUserCart(HttpServletRequest request, int userId) {
        
        String msg = "";
        boolean success = false;
        
        try {
            sql = "DELETE FROM shopping_cart WHERE userid = ?";
            int result = db.delete(sql, String.valueOf(userId));
            success = result > 0;
            System.out.println("清空购物车结果: " + success + ", 影响行数: " + result);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return success;
    }
    
    /**------------------------------ 获取购物车商品总数 */
    public int getCartCount(HttpServletRequest request, int userId) {
        
        int count = 0;
        String msg = "";
        
        try {
            sql = "SELECT COALESCE(SUM(quantity), 0) as total FROM shopping_cart WHERE userid = ?";
            rs = db.select(sql, String.valueOf(userId));
            
            if (rs != null && rs.next()) {
                count = rs.getInt("total");
                System.out.println("购物车总数查询结果: " + count);
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return count;
    }
    
    /**------------------------------ 检查用户是否有购物车记录 */
    public boolean hasCartItems(HttpServletRequest request, int userId) {
        
        boolean hasItems = false;
        String msg = "";
        
        try {
            sql = "SELECT COUNT(*) as count FROM shopping_cart WHERE userid = ?";
            rs = db.select(sql, String.valueOf(userId));
            
            if (rs != null && rs.next()) {
                int count = rs.getInt("count");
                hasItems = count > 0;
                System.out.println("检查购物车记录: userId=" + userId + ", count=" + count + ", hasItems=" + hasItems);
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
        }
        
        return hasItems;
    }
    
    
 // 在CategoryDao.java中添加以下方法：

    /**------------------------------ 根据分类ID查询分类 */
    public Category queryByCategoryId(HttpServletRequest request, String category_id) {
        
        Category category = null;
        String msg = "";
        
        try {
            sql = "select * from tb_category where category_id = ?";
            rs = db.select(sql, category_id);
            
            if (rs == null) {            
                msg += "数据库操作发生错误！";
                request.setAttribute("msg", msg);
                return category;
            }
            
            if (rs.next()) {
                category = new Category();
                category.setCategory_id(rs.getString("category_id"));
                category.setCategory_name(rs.getString("category_name"));
            }
            db.close();
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return category;
    }
}