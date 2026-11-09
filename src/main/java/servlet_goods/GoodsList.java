package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;
import service.CategoryService;

@WebServlet("/GoodsList")									//控制层、控制器层，servlet
public class GoodsList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();		
		String msg = "";
		String url = "goods/goodsList.jsp";							//商品列表（表格式）
				
		try {		
			if (util.LoginCheck.verify(request) == false) {			//如果登录已失效，或权限不足
				url = "login.jsp";
				return;
			}	

			GoodsService goodsService=new GoodsService();
			goodsService.queryAll(request);					//****生成该页的记录列表

			CategoryService categoryService = new CategoryService();
            categoryService.queryAll(request);  // 改为使用CategoryService
			
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
