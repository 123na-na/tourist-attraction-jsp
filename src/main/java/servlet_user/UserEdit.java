package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.TypeService;
import service.UserService;

@WebServlet("/UserEdit")
public class UserEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();
		String msg = "";	
		String url = "user/userEdit.jsp";
		
		try {			
			if (util.LoginCheck.verify(request) == false) {			//如果登录已失效，或权限不足
				url = "login.jsp";
				return;
			}
			
			//####添加代码 			//****根据商品ID查询记录
			UserService userService = new UserService();
            boolean result = userService.queryByUserId(request);
			//####添加代码 			//****生成分类对象列表typeList
			
            
            if (!result) {
                // 如果查询失败，可能是用户不存在，跳转到用户列表
                url = "UserList";
                request.getSession().setAttribute("msg", "用户不存在或查询失败！");
                return;
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
