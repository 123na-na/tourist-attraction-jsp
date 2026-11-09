package servlet_image;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsImageService;

@WebServlet("/GoodsImageUpload")
public class GoodsImageUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
	    String msg = "";
	    String url = "goods/goodsImageUpload.jsp";
	    
	    try {
	        // 从URL参数获取
	        String goodsId = request.getParameter("goodsId");
	        String image = request.getParameter("image");
	        String msgParam = request.getParameter("msg");
	        
	        System.out.println("=== GoodsImageUpload.doGet ===");
	        System.out.println("URL参数 - goodsId: " + goodsId + ", image: " + image);
	        
	        if (msgParam != null) {
	            msg = java.net.URLDecoder.decode(msgParam, "UTF-8");
	        }
	        
	        // 验证参数
	        if (goodsId == null || goodsId.trim().isEmpty()) {
	            msg = "错误：商品ID不能为空";
	            request.setAttribute("msg", msg);
	            request.getRequestDispatcher(url).forward(request, response);
	            return;
	        }
	        
	        // 如果image参数为空，直接使用空值
	        if (image == null || image.trim().isEmpty()) {
	            image = "";
	        }
	        
	        // 清理可能的时间戳
	        if (image != null && image.contains("?t=")) {
	            image = image.substring(0, image.indexOf("?t="));
	        }
	        
	        System.out.println("最终图片名: " + image);
	        
	        // 生成图片标签
	        GoodsImageService goodsImageService = new GoodsImageService();
	        String imageTag = "";
	        
	        if (image != null && !image.trim().isEmpty() && !image.equals("null")) {
	            imageTag = goodsImageService.getImageTag(request, image);
	        } else {
	            imageTag = "（无图片）";
	        }
	        
	        // 创建Goods对象并设置属性
	        bean.Goods goods = new bean.Goods();
	        goods.setGoodsId(goodsId);
	        goods.setImage(image);
	        
	        // 设置到request中
	        request.setAttribute("goods", goods);  // 关键：设置goods对象
	        request.setAttribute("goodsId", goodsId);
	        request.setAttribute("image", image);
	        request.setAttribute("imageTag", imageTag);
	        request.setAttribute("msg", msg);
	        
	    } catch (Exception e) {
	        msg = "系统发生错误：" + e.getMessage();
	        System.out.println("ERROR: " + msg);
	        e.printStackTrace();
	        request.setAttribute("msg", msg);
	    } finally {
	        request.getRequestDispatcher(url).forward(request, response);
	    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
