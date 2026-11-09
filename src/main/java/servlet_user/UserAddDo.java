package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.UserService;

@WebServlet("/UserAddDo")
public class UserAddDo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect("UserAdd");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		java.io.PrintWriter out = response.getWriter();
		String msg = "";
		String url = "UserShow";									//目标网址
		
		try {			
			if (util.LoginCheck.verify(request) == false) {			//如果登录已失效，或权限不足
				url = "login.jsp";
				return;
			}
			
			UserService userService = new UserService();			
			String userid = userService.addUser(request);		//****新添商品
			
			if (userid == null || userid.equals("0")) {								//新添失败
				url = "";
				return;
			}
		
			//---------------------- 新添成功
			url = "UserShow?userid=" + userid;					//目标网址
			url = "@Redirect:" + url;								//增加@Redirect用于通过JS代码实现网页跳转（自创协议）
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);

			//---- 由于页面goodsAdd.jsp中是以Ajax方式提交表单，所以无论是输出错误消息，
			//---- 还是新添成功需跳转到详情页面，这里都是调用out()方法输出内容，
			//---- 再由页面中的JavaScript代码根据接收到的内容决定下一步如何处理。
			
			if (url.equals("")) {			//如果新添失败
				out.print(msg);				//输出错误消息
			} else {						//如果新添成功
				out.print(url);				//网页将转向
			}
		}
	}
}
