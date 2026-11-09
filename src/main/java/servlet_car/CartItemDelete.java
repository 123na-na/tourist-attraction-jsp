//// servlet_car/CartItemDelete.java
//package servlet_car;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import service.CartItemService;
//
//@WebServlet("/CartItemDelete")
//public class CartItemDelete extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
//            throws ServletException, IOException {
//        
//        response.setContentType("application/json;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        
//        String cartItemId = request.getParameter("cart_item_id");
//        
//        if (cartItemId == null || cartItemId.trim().isEmpty()) {
//            out.write("{\"success\": false, \"message\": \"参数错误\"}");
//            out.flush();
//            return;
//        }
//        
//        CartItemService service = new CartItemService();
//        boolean success = service.deleteCartItem(request);
//        
//        if (success) {
//            out.write("{\"success\": true, \"message\": \"删除成功\"}");
//        } else {
//            out.write("{\"success\": false, \"message\": \"删除失败\"}");
//        }
//        
//        out.flush();
//    }
//}

// servlet_car/CartItemDelete.java - 修复版
package servlet_car;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CartItemService;

@WebServlet("/CartItemDelete")
public class CartItemDelete extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        System.out.println("========== CartItemDelete Servlet ==========");
        
        String cartItemId = request.getParameter("cart_item_id");
        System.out.println("接收到的cart_item_id参数: " + cartItemId);
        
        if (cartItemId == null || cartItemId.trim().isEmpty()) {
            System.out.println("参数错误: cart_item_id为空");
            out.write("{\"success\": false, \"message\": \"参数错误: 购物车项ID不能为空！\"}");
            out.flush();
            return;
        }
        
        try {
            // 设置参数到request中，让CartItemService能够获取到
            request.setAttribute("cart_item_id", cartItemId);
            
            CartItemService service = new CartItemService();
            boolean success = service.deleteCartItem(request);
            
            System.out.println("删除操作结果: " + success);
            
            if (success) {
                out.write("{\"success\": true, \"message\": \"删除成功！\"}");
                System.out.println("返回成功响应");
            } else {
                out.write("{\"success\": false, \"message\": \"删除失败！\"}");
                System.out.println("返回失败响应");
            }
            
        } catch (Exception e) {
            System.out.println("删除操作异常: " + e.getMessage());
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"系统错误: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
    
    // 添加doGet方法，方便测试
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}