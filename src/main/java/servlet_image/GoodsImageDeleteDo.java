package servlet_image;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Goods;
import service.GoodsImageService;

@WebServlet("/GoodsImageDeleteDo")
public class GoodsImageDeleteDo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//java.io.PrintWriter out = response.getWriter();
		String msg = "";
		String url = "GoodsImageUpload";
				
		try {			
			if (util.LoginCheck.verify(request) == false) {							//如果登录已失效，或权限不足
				msg += "（<a href='login.jsp' target='_top'>用户登录</a>）</p>";	//前往最外层的框架集，即整个浏览器窗口
				return;
			}		

			GoodsImageService goodsImageService = new GoodsImageService();
			Goods goods = goodsImageService.imageDeleteDo(request);				//****删除图片 
			
			String goodsId = goods.getGoodsId();
			String image   = goods.getImage();	
			
			url = "GoodsImageUpload?goodsId=" + goodsId + "&image=" + image;
						
		} catch (Exception e) {
			msg += "&emsp;&emsp;系统发生错误。";	
			e.printStackTrace();
		} finally {			
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		
			response.sendRedirect(url);						//网页重定向
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
