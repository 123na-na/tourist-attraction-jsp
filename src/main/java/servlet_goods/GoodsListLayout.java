package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;
import service.CategoryService;

@WebServlet("/GoodsListLayout")
public class GoodsListLayout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		String msg = "";
		String url = "goods/goodsListLayout.jsp";		//商品平铺式列表
				
		try {		
			if (util.LoginCheck.verify(request) == false) {	//如果登录已失效，或权限不足
				url = "login.jsp";
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}	

			// 调用GoodsService的queryAll方法，该方法应该已经处理了筛选和搜索逻辑
			GoodsService goodsService = new GoodsService();
			goodsService.queryAll(request);		//****生成该页的记录列表，包含筛选和搜索功能

			// 查询分类列表
			CategoryService categoryService = new CategoryService();
			categoryService.queryAll(request);
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
			
			request.getRequestDispatcher(url).forward(request, response);	//转发
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}