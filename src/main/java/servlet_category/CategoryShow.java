package servlet_category;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import service.GoodsService;
import service.CategoryService;

@WebServlet("/CategoryShow")
public class CategoryShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";
        String url = "category/CategoryShow.jsp";
        
        try {
            // 首先尝试获取categoryId参数
            String categoryIdStr = request.getParameter("categoryId");
            
            // 如果categoryId为空，再尝试获取category_id参数
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                categoryIdStr = request.getParameter("category_id");
            }
            
            // 再次检查是否为空
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                msg += "分类ID不能为空！";
                request.setAttribute("msg", msg);
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // 获取分类详情
            CategoryService categoryService = new CategoryService();
            Category category = null;
            
            // 先获取所有分类，然后找到对应的分类
            categoryService.queryAll(request);
            
            List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
            if (categoryList != null) {
                for (Category c : categoryList) {
                    if (Integer.parseInt(c.getCategory_id()) == categoryId) {
                        category = c;
                        break;
                    }
                }
            }
            
            if (category == null) {
                msg += "该分类不存在！";
                request.setAttribute("msg", msg);
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            // 将找到的分类设置到request中
            request.setAttribute("category", category);
            
            // 获取该分类下的商品列表
            GoodsService goodsService = new GoodsService();
            boolean hasGoods = goodsService.queryByCategoryId(request, categoryId);
            
            if (!hasGoods) {
                // 如果没有商品，消息已经在Service中设置了
            }
            
        } catch (NumberFormatException e) {
            msg += "分类ID格式不正确！";
            e.printStackTrace();
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }            
            request.setAttribute("msg", msg);
            
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}