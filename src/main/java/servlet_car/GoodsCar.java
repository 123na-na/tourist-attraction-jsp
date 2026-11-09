package servlet_car;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import service.CartItemService;

@WebServlet("/GoodsCar")
public class GoodsCar extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("========== GoodsCar Servlet ==========");
        System.out.println("请求URL: " + request.getRequestURL());
        System.out.println("商品ID参数: " + request.getParameter("goodsId"));
        
        // 检查用户是否登录
        User myUser = (User) request.getSession().getAttribute("myUser");
        if (myUser == null) {
            System.out.println("DEBUG: 用户未登录");
            request.setAttribute("msg", "请先登录！");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        System.out.println("DEBUG: 当前用户: " + myUser.getUsername() + 
                         ", 用户ID: " + myUser.getUserid() + 
                         ", 角色: " + myUser.getRole());
        
        // 获取商品ID
        String goodsId = request.getParameter("goodsId");
        if (goodsId == null || goodsId.trim().isEmpty()) {
            System.out.println("DEBUG: 商品ID为空");
            request.setAttribute("msg", "商品ID不能为空！");
            response.sendRedirect(request.getContextPath() + "/GoodsList");
            return;
        }
        
        // 添加商品到购物车
        CartItemService service = new CartItemService();
        boolean success = service.addToCart(request);
        
        System.out.println("DEBUG: 添加到购物车结果: " + success);
        
        // 获取操作结果消息
        String msg = (String) request.getAttribute("msg");
        if (msg == null) {
            msg = success ? "商品已加入购物车！" : "操作失败！";
        }
        
        System.out.println("DEBUG: 操作消息: " + msg);
        
        // 根据结果重定向
        if (success) {
            // 成功：跳转到购物车页面
            System.out.println("DEBUG: 跳转到购物车页面");
            response.sendRedirect(request.getContextPath() + "/CartItemList");
        } else {
            // 失败：返回商品列表
            System.out.println("DEBUG: 返回商品列表");
            request.setAttribute("msg", msg);
            response.sendRedirect(request.getContextPath() + "/GoodsList?msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
        }
    }
}