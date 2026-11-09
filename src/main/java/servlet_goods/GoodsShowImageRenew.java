package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;
import service.GoodsImageService;

@WebServlet("/GoodsShowImageRenew")
public class GoodsShowImageRenew extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		String msg = "";
		String url = "goods/goodsImageUpload.jsp"; // 改为使用 goodsImageUpload.jsp
		
		try {				
			if (util.LoginCheck.verify(request) == false) {
				response.sendRedirect("login.jsp");
				return;
			}
			
			// 获取商品ID
			String goodsId = request.getParameter("goodsId");
			
			if (goodsId == null || goodsId.trim().isEmpty()) {
				msg = "商品ID不能为空。";
				request.setAttribute("msg", msg);
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
			
			// 获取商品信息
			GoodsService goodsService = new GoodsService();
			goodsService.queryByGoodsId(request);
			
			// 传递goodsId参数给JSP
			request.setAttribute("goodsId", goodsId);
			
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

