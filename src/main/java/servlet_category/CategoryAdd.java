package servlet_category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CategoryService;

@WebServlet("/CategoryAdd")
public class CategoryAdd extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";
        String url = "category/CategoryAdd.jsp";
                
        try {            
            if (util.LoginCheck.verify(request) == false) {            //如果登录已失效，或权限不足
                url = "login.jsp";
                return;
            }    

            // 获取所有分类列表（用于显示现有分类）
            CategoryService categoryService = new CategoryService();
            boolean success = categoryService.queryAll(request);
            
            if (!success) {
                msg += "获取分类信息失败，请检查系统状态。";
            }
                        
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