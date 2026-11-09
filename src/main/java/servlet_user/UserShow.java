package servlet_user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import bean.User;
import service.GoodsImageService;
import service.UserService;

@WebServlet("/UserShow")
public class UserShow extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();
		String msg = "";
		String url = "user/userShow.jsp";
		
		try {
			
			UserService userService =new UserService();
			userService.queryByUserId(request);						//****根据ID获取记录
			
			//-------------------------- 生成商品图片的标签和链接
			User user = (User) request.getAttribute("user");	//获取在Service中已经生成的goods		
			
			if (user == null) {
				return;
			}
			
			/*String image = user.getImage();	
			
			GoodsImageService goodsImageService = new GoodsImageService();
			image = goodsImageService.getImageTag(request, image);		//****生成商品图片的标签和链接
			
			goods.setImage(image);
			request.setAttribute("goods", goods);		*/					//将image的新值更新到goods
			
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
