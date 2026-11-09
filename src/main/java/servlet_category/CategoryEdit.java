package servlet_category;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import service.CategoryService;

@WebServlet("/CategoryEdit")
public class CategoryEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";    
        String url = "category/CategoryEdit.jsp";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {            //如果登录已失效，或权限不足
                url = "login.jsp";
                return;
            }
            
            // 获取分类ID参数 - 使用category_id
            String categoryIdStr = request.getParameter("category_id");
            
            // 如果category_id为空，再尝试获取categoryId参数（兼容性）
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                categoryIdStr = request.getParameter("categoryId");
            }
            
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                msg += "分类ID不能为空！";
                request.setAttribute("msg", msg);
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // 根据分类ID查询记录
            CategoryService categoryService = new CategoryService();
            
            // 先获取所有分类
            categoryService.queryAll(request);
            
            // 从所有分类中找到要编辑的分类
            List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
            Category category = null;
            
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
            
            // 将分类对象设置到request中
            request.setAttribute("category", category);
            
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