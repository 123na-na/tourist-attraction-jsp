package service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import bean.Category;
import dao.CategoryDao;

public class CategoryService {
    
    public boolean queryAll(HttpServletRequest request) {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categoryList = categoryDao.queryAll(request);
        
        if (categoryList != null) {
            request.setAttribute("categoryList", categoryList);
            return true;
        }
        return false;
    }
    
    public Category queryByCategoryId(HttpServletRequest request, String category_id) {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.queryByCategoryId(request, category_id);
    }
    
    // 新增的添加分类方法
    public boolean addCategory(HttpServletRequest request) {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.addCategory(request);
    }
    
    // 新增的更新分类方法
    public boolean updateCategory(HttpServletRequest request) {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.updateCategory(request);
    }
    
    // 新增的删除分类方法
    public boolean deleteCategory(HttpServletRequest request, String categoryId) {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.deleteCategory(request, categoryId);
    }
    
    // 新方法：删除分类并将商品设为未分类状态
    public boolean deleteCategoryWithUncategorized(HttpServletRequest request, String categoryId) {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.deleteCategoryWithUncategorized(request, categoryId);
    }
    
//    // 新方法：获取分类下的商品数量
//    public int getGoodsCountByCategoryId(HttpServletRequest request, String categoryId) {
//        CategoryDao categoryDao = new CategoryDao();
//        return categoryDao.getGoodsCountByCategoryId(request, categoryId);
//    }
}