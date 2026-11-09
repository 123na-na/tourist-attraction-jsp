package servlet_goods;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import bean.Goods;
import service.GoodsImageService;
import service.GoodsService;

@WebServlet("/GoodsShow")
public class GoodsShow extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");		
	    String msg = "";
	    String url = "goods/goodsShow.jsp";
	    
	    try {
	        
	        GoodsService goodsService = new GoodsService();
	        goodsService.queryByGoodsId(request);  // 获取商品信息
	        
	        Goods goods = (Goods) request.getAttribute("goods");
	        
	        if (goods == null) {
	            msg += "商品不存在。";
	            request.setAttribute("msg", msg);
	            request.getRequestDispatcher(url).forward(request, response);
	            return;
	        }
	        
	        // 使用 GoodsImageService 的 getImageTag 方法
	        GoodsImageService goodsImageService = new GoodsImageService();
	        String imageTag = goodsImageService.getImageTag(request, goods.getImage());  // 调用现有的getImageTag方法
	        
	        // 将生成的HTML标签保存到request中，供JSP使用
	        request.setAttribute("imageTag", imageTag);
	        
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
