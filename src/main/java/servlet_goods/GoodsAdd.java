package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Goods;
import service.CategoryService;

@WebServlet("/GoodsAdd")
public class GoodsAdd extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";
        String url = "goods/goodsAdd.jsp";
                
        try {            
            if (util.LoginCheck.verify(request) == false) {
                url = "login.jsp";
                return;
            }    

            // 查询分类列表
            CategoryService categoryService = new CategoryService();
            categoryService.queryAll(request);
            
            // 生成临时ID用于图片上传
            String goodsUUID = java.util.UUID.randomUUID().toString();
            
            Goods goods = new Goods();
            goods.setGoodsId(goodsUUID);
            goods.setImage("");
            goods.setStatus("计划中");  // 默认上架状态
            
            request.setAttribute("goods", goods);
                    
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