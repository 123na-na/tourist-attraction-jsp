package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Goods;
import bean.User;
import service.TypeService;

@WebServlet("/UserAdd")
public class UserAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();	
		String msg = "";
		String url = "user/userAdd.jsp";
				
		try {			
			if (util.LoginCheck.verify(request) == false) {			//如果登录已失效，或权限不足
				url = "login.jsp";
				return;
			}	

			TypeService typeService	= new TypeService();				//****生成商品分类列表typeList
			typeService.queryAllForGOODS(request);
			

			
			
			User user = new User();
			
			
			
			request.setAttribute("user", user);
			
			
												//用于给网页的iframe的src和隐藏域控件赋值
						
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
