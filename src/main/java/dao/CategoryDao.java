package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Category;

public class CategoryDao {
    
    Db db = new Db();
    String sql = "";
    ResultSet rs = null;
    
    /**------------------------------ 获取所有分类记录 */
    public List<Category> queryAll (HttpServletRequest request) {
        
        List<Category> categoryList = new ArrayList<Category>();
        String msg = "";
        
        try {
            String category_id;
            String category_name;
            
            sql = "select * from category order by category_name";
            rs = db.select(sql);
            
            if (rs == null) {           
                msg += "数据库操作发生错误！";
                return categoryList;
            }
            
            Category category = null;
            
            while (rs.next()) {
                category_id = rs.getString("category_id");      // 改为getString
                category_name = rs.getString("category_name");
                
                category = new Category();
                category.setCategory_id(category_id);
                category.setCategory_name(category_name);
                
                categoryList.add(category);
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

        return categoryList;
    }
    
    /**------------------------------ 根据ID查询分类记录 */
    public Category queryByCategoryId (HttpServletRequest request, String category_id) {
        
        Category category = null;
        String msg = "";
        
        try {
            String category_name;
            
            sql = "select * from category where category_id = ?";
            rs = db.select(sql, category_id);
            
            if (rs == null) {           
                msg += "数据库操作发生错误！";
                return category;
            }
            
            if (rs.next() == false) {
                db.close();
                return category;
            }
            
            category_id = rs.getString("category_id");
            category_name = rs.getString("category_name");
            db.close();

            category = new Category();
            category.setCategory_id(category_id);
            category.setCategory_name(category_name);
            
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
    
    /**------------------------------ 添加分类记录 */
    public boolean addCategory(HttpServletRequest request) {
        
        String msg = "";
        boolean result = false;
        
        try {
            String categoryName = request.getParameter("categoryName");
            
            if (categoryName == null || categoryName.trim().isEmpty()) {
                msg += "分类名称不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 检查分类名称是否已存在
            sql = "select count(*) from category where category_name = ?";
            rs = db.select(sql, categoryName);
            
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    msg += "分类名称已存在，请使用其他名称！";
                    request.setAttribute("msg", msg);
                    db.close();
                    return false;
                }
            }
            db.close();
            
            // 插入新分类
            sql = "insert into category (category_name) values (?)";
            int rows = db.update(sql, categoryName);
            
            if (rows > 0) {
                result = true;
                msg += "分类添加成功！";
            } else {
                msg += "分类添加失败！";
            }
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();  
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return result;
    }
    
    /**------------------------------ 更新分类记录 */
    public boolean updateCategory(HttpServletRequest request) {
        
        String msg = "";
        boolean result = false;
        
        try {
            String categoryId = request.getParameter("categoryId");
            String categoryName = request.getParameter("categoryName");
            
            if (categoryId == null || categoryId.trim().isEmpty()) {
                msg += "分类ID不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            if (categoryName == null || categoryName.trim().isEmpty()) {
                msg += "分类名称不能为空！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 检查分类名称是否已存在（排除当前分类）
            sql = "select count(*) from category where category_name = ? and category_id != ?";
            rs = db.select(sql, categoryName, categoryId);
            
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    msg += "分类名称已存在，请使用其他名称！";
                    request.setAttribute("msg", msg);
                    db.close();
                    return false;
                }
            }
            db.close();
            
            // 更新分类
            sql = "update category set category_name = ? where category_id = ?";
            int rows = db.update(sql, categoryName, categoryId);
            
            if (rows > 0) {
                result = true;
                msg += "分类更新成功！";
            } else {
                msg += "分类更新失败！";
            }
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();  
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return result;
    }
    
    /**------------------------------ 删除分类记录 */
    public boolean deleteCategory(HttpServletRequest request, String categoryId) {
        
        String msg = "";
        boolean result = false;
        
        try {
            // 检查分类是否被商品表引用 - 无论商品状态如何
            sql = "select count(*) from tb_goods where category_id = ?";
            rs = db.select(sql, categoryId);
            
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    // 获取商品信息用于显示
                    sql = "select goodsName, status from tb_goods where category_id = ? limit 5";
                    rs = db.select(sql, categoryId);
                    
                    StringBuilder goodsInfo = new StringBuilder();
                    while (rs != null && rs.next()) {
                        String goodsName = rs.getString("goodsName");
                        String status = rs.getString("status");
                        goodsInfo.append(goodsName).append("(").append(status).append(")").append("、");
                    }
                    db.close();
                    
                    if (goodsInfo.length() > 0) {
                        goodsInfo.deleteCharAt(goodsInfo.length() - 1); // 移除最后一个顿号
                    }
                    
                    msg = "该分类已被商品引用，无法删除！引用商品：" + goodsInfo.toString();
                    request.setAttribute("msg", msg);
                    return false;
                }
            }
            db.close();
            
            // 删除分类
            sql = "delete from category where category_id = ?";
            int rows = db.update(sql, categoryId);
            
            if (rows > 0) {
                result = true;
                msg = "分类删除成功！";
            } else {
                msg = "分类删除失败！";
            }
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();  
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return result;
    }
    
 // 添加到CategoryDao.java中

    /**------------------------------ 删除分类并将商品设为未分类状态 */
    public boolean deleteCategoryWithUncategorized(HttpServletRequest request, String categoryId) {
        
        String msg = "";
        boolean result = false;
        
        try {
            System.out.println("=== 开始删除分类ID: " + categoryId + " ===");
            
            // 1. 先检查分类是否存在
            sql = "select count(*) as count from category where category_id = ?";
            rs = db.select(sql, categoryId);
            
            int categoryExists = 0;
            if (rs != null && rs.next()) {
                categoryExists = rs.getInt("count");
            }
            System.out.println("分类存在检查结果: " + categoryExists);
            
            if (categoryExists == 0) {
                msg = "该分类不存在！";
                request.setAttribute("msg", msg);
                return false;
            }
            
            // 2. 获取该分类下的商品数量
            int goodsCount = 0;
            sql = "select count(*) as goodsCount from tb_goods where category_id = ?";
            rs = db.select(sql, categoryId);
            
            if (rs != null && rs.next()) {
                goodsCount = rs.getInt("goodsCount");
            }
            System.out.println("分类下的商品数量: " + goodsCount);
            
            // 3. 如果存在商品，先将商品设为未分类
            int updatedGoods = 0;
            if (goodsCount > 0) {
                // 使用NULL表示未分类，或者使用'0'
                sql = "update tb_goods set category_id = NULL where category_id = ?";
                updatedGoods = db.update(sql, categoryId);
                System.out.println("更新商品数量: " + updatedGoods);
            }
            
            // 4. 删除分类
            sql = "delete from category where category_id = ?";
            int deletedRows = db.update(sql, categoryId);
            System.out.println("删除分类行数: " + deletedRows);
            
            if (deletedRows > 0) {
                result = true;
                if (goodsCount > 0) {
                    msg = "分类删除成功！已将 " + updatedGoods + " 个商品设置为未分类状态。";
                } else {
                    msg = "分类删除成功！";
                }
            } else {
                msg = "分类删除失败！";
            }
            
            System.out.println("删除结果: " + msg);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();  
            System.out.println("删除异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
        
        return result;
    }
}