package servlet_category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CategoryService;

@WebServlet("/CategoryEditDo")
public class CategoryEditDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果以GET方式访问此Servlet，返回到CategoryEdit页面
        String categoryId = request.getParameter("category_id");
        if (categoryId == null) {
            categoryId = request.getParameter("categoryId");
        }
        response.sendRedirect("CategoryEdit?category_id=" + categoryId);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        java.io.PrintWriter out = response.getWriter();    
        String msg = "";
        String url = "CategoryShow";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {            //如果登录已失效，或权限不足
                url = "login.jsp";
                return;
            }
            
            CategoryService categoryService = new CategoryService();
            boolean result = categoryService.updateCategory(request);    //修改分类信息
            
            if (result == false) {                                //信息修改失败
                url = "";
                return;
            }
            
            //---------------------- 信息修改成功
            // 获取category_id参数
            String categoryId = request.getParameter("category_id");
            if (categoryId == null || categoryId.trim().isEmpty()) {
                categoryId = request.getParameter("categoryId");
            }
            
            // 跳转到分类详情页，使用category_id参数
            url = "CategoryShow?category_id=" + categoryId;
            url = "@Redirect:" + url;
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);

            if (msg.contains("成功")) {
                out.print(url);  // 如果修改成功，输出跳转URL
            } else {
                out.print(msg);  // 如果修改失败，输出错误消息
            }
        }
    }
}