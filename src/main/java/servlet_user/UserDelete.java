package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.UserService;

@WebServlet("/UserDelete")
public class UserDelete extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doDelete(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doDelete(request, response);
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        java.io.PrintWriter out = response.getWriter();    
        String msg = "";
        String url = "UserList";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {  // 如果登录已失效，或权限不足
                url = "login.jsp";
                response.sendRedirect(url);
                return;
            }
            
            // 获取要删除的用户ID
            String userId = request.getParameter("userid");
            if (userId == null) {
                userId = request.getParameter("userId");
            }
            
            if (userId == null || userId.trim().isEmpty()) {
                out.print("错误：用户ID不能为空！");
                return;
            }
            
            UserService userService = new UserService();
            boolean result = userService.deleteByUserId(request);  // 调用删除方法
            
            if (result == false) {  // 删除失败
                out.print("删除失败：" + msg);
                return;
            }
            
            // 删除成功
            response.sendRedirect(url);
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();    
            e.printStackTrace();
            out.print(msg);
        }
    }
}