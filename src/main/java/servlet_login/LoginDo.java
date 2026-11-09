package servlet_login;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Db;
import service.LoginService;
import util.Md5;

@WebServlet("/LoginDo")
public class LoginDo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.sendRedirect("login.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		java.io.PrintWriter out = response.getWriter();	
		String msg = "";
		String url = "main.jsp";
		
		try {
			LoginService loginService=new LoginService();
			boolean result = loginService.loginDo(request);		//####【新添代码】：得到登录是否成功的结果

			if (result == false) {								//登录失败
				url = "";
				return;
			}

			//---------------------- 登录成功
			url ="@Redirect:"+url;		//####【新添代码】					//增加@Redirect用于通过JS代码实现网页跳转（自创协议）
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);

			//---- 由于页面login.jsp中是以Ajax方式提交表单，所以无论是在登录页输出错误消息，
			//---- 还是登录成功需跳转到用户功能页，这里都是调用out()方法输出内容，
			//---- 再由Ajax根据接收到的内容决定下一步如何处理。
			
			if(url.equals("")) {				//如果登录失败
				out.println(msg);				//输出错误消息
			}else {								//如果登录成功
				out.println(url);			//网页将转向，通过JS代码实现网页跳转（自创协议）
			}
		}
	}
}
