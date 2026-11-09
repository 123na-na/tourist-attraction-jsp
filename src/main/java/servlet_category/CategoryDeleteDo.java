package servlet_category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CategoryService;

@WebServlet("/CategoryDeleteDo")
public class CategoryDeleteDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";
        String url = "CategoryList";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {
                url = "login.jsp";
                response.sendRedirect(url);
                return;
            }
            
            // 获取要删除的分类ID
            String categoryId = request.getParameter("category_id");
            if (categoryId == null || categoryId.trim().isEmpty()) {
                categoryId = request.getParameter("categoryId");
            }
            
            System.out.println("=== 接收到删除分类请求 ===");
            System.out.println("参数category_id: " + request.getParameter("category_id"));
            System.out.println("参数categoryId: " + request.getParameter("categoryId"));
            System.out.println("最终categoryId: " + categoryId);
            
            if (categoryId == null || categoryId.trim().isEmpty()) {
                msg = "分类ID不能为空！";
                response.sendRedirect("CategoryList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }
            
            CategoryService categoryService = new CategoryService();
            
            // 执行删除操作
            boolean result = categoryService.deleteCategoryWithUncategorized(request, categoryId);
            
            if (result) {
                msg = "分类删除成功！";
            } else {
                // 获取具体的失败原因
                if (request.getAttribute("msg") != null) {
                    msg = request.getAttribute("msg").toString();
                } else {
                    msg = "分类删除失败！";
                }
            }
            
            System.out.println("最终消息: " + msg);
            
            // 重定向到分类列表页面
            response.sendRedirect(url + "?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();    
            System.out.println("Servlet异常: " + e.getMessage());
            e.printStackTrace();
            try {
                response.sendRedirect("CategoryList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}