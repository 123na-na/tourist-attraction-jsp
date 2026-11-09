package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;
import service.CategoryService;

@WebServlet("/GoodsEdit")
public class GoodsEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        String msg = "";    
        String url = "goods/goodsEdit.jsp";
        boolean canForward = true; // 标记是否可以转发
        
        try {            
            // 检查权限
            if (!util.LoginCheck.verify(request)) {
                // 权限不足，直接重定向到登录页
                response.sendRedirect("login.jsp");
                canForward = false;
                return;
            }
            
            // 根据商品ID查询记录
            String goodsId = request.getParameter("goodsId");
            if (goodsId == null || goodsId.trim().isEmpty()) {
                msg = "商品ID不能为空";
                request.setAttribute("msg", msg);
                // 使用重定向而不是转发
                response.sendRedirect("GoodsList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                canForward = false;
                return;
            }
            
            // 直接使用GoodsService的queryByGoodsId方法
            GoodsService goodsService = new GoodsService();
            boolean goodsResult = goodsService.queryByGoodsId(request); // 这个方法会设置goods属性到request
            
            if (!goodsResult) {
                msg = "商品不存在";
                request.setAttribute("msg", msg);
                // 使用重定向而不是转发
                response.sendRedirect("GoodsList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                canForward = false;
                return;
            }
            
            // 生成分类对象列表
            CategoryService categoryService = new CategoryService();
            boolean categoryResult = categoryService.queryAll(request); // 这个方法会设置categoryList属性到request
            
            if (!categoryResult) {
                msg += "获取分类列表失败";
            }
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();    
            e.printStackTrace();
            // 错误处理：重定向回商品列表
            try {
                response.sendRedirect("GoodsList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                canForward = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        // 只有在没有重定向的情况下才执行转发
        if (canForward) {
            try {
                if (request.getAttribute("msg") != null) {
                    msg += request.getAttribute("msg").toString();
                }
                request.setAttribute("msg", msg);
                request.getRequestDispatcher(url).forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}