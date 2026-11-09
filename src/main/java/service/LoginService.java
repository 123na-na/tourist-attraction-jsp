package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDao;
import util.Md5;

public class LoginService {
	
	public boolean loginDo (HttpServletRequest request) {			//登录验证

		String msg = "";
		HttpSession session = request.getSession();	
		
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String code 	= request.getParameter("code");			//输入的验证码
			
			if (username == null || username.trim().equals("")) {
				msg = "请输入用户名！";
				return false;										//即使这里有return，也会在程序结束之前执行finally中的代码
			}
			username = username.trim();
			
			if (password == null || password.equals("")) {
				msg = "请输入密码！";
				return false;
			}
			
			if (code == null || code.trim().equals("")) {			//对输入的验证码进行校验
				msg = "@RefreshCode:请输入验证码！";					//将通过JS代码在浏览器端刷新验证码图片
				return false;
			}
			code = code.trim().toLowerCase();						//转换为小写字母
			
			if (session.getAttribute("code") == null) {				//session中的验证码
				msg = "@RefreshCode:验证码已过期。请重新输入。";
				return false;
			}
			
			String codeSession = session.getAttribute("code").toString();
			codeSession = codeSession.toLowerCase();
			
			if (code.equals(codeSession) == false) {
				msg = "@RefreshCode:您输入的验证码错误。请重新输入。";
				return false;
			}

			//String salt = "login_fdsfj45349fd";						//加点料（放点盐）。此料应与登录页login.jsp中加的料相同
			//password = Md5.getMd5(username+password+salt);			//****对用户名+密码+料进行MD5加密

			UserDao userDao =new UserDao();			//创建Dao对象
			User user =userDao.queryByUsernameAndPassword(request, username, password);
			
			if (user == null)	{									//如果未读到记录	
				msg = "登录失败！您输入的用户名或者密码不正确。";
				return false;
			}
			
			//msg = "登录成功！";
			session.setAttribute("myUser", user); 					//用户对象user，添加到session
			session.setMaxInactiveInterval(20 * 60);				//有效时长20分钟。在生成验证码时设置的时长是30秒
			return true;

		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
	
		return false;
	}
}
