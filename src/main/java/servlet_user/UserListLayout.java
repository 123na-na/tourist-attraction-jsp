package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.TypeService;
import service.UserService;

@WebServlet("/UserListLayout")
public class UserListLayout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();		
		String msg = "";
		String url = "user/userListLayout.jsp";					//商品平铺式列表
				
		try {

			UserService userService = new UserService();
			userService.queryAll(request);						//****生成该页的记录列表

			TypeService typeService = new TypeService();
			typeService.queryAllForGOODS(request);						//****生成分类列表		
	
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
			
			request.getRequestDispatcher(url).forward(request, response);		//转发
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
