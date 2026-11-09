package servlet_category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CategoryService;

@WebServlet("/CategoryAddDo")
public class CategoryAddDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果以GET方式访问此Servlet，返回到CategoryAdd页面
        response.sendRedirect("CategoryAdd");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        java.io.PrintWriter out = response.getWriter();    
        String msg = "";
        String url = "CategoryList";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {            //如果登录已失效，或权限不足
                url = "login.jsp";
                return;
            }
            
            CategoryService categoryService = new CategoryService();
            boolean result = categoryService.addCategory(request);    //****添加分类信息
            
            if (result == false) {                                //信息添加失败
                url = "";
                out.print(msg);  // 直接输出错误消息
                return;
            }
            
            //---------------------- 信息添加成功
            url = "CategoryList";                //目标网址：分类列表页
            url = "@Redirect:" + url;        //增加@Redirect用于通过JS代码实现网页跳转（自创协议）
            out.print(url);
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();    
            e.printStackTrace();
            out.print(msg);
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            request.setAttribute("msg", msg);
        }
    }
}