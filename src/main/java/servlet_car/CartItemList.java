package servlet_car;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CartItemService;

@WebServlet("/CartItemList")
public class CartItemList extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
   // 在 CartItemList.java 中修复批量删除的处理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("========== CartItemList Servlet ==========");
        System.out.println("请求方法: " + request.getMethod());
        
        CartItemService service = new CartItemService();
        
        // 检查操作类型
        String buttonDelete = request.getParameter("buttonDelete");
        
        System.out.println("DEBUG: 操作参数 - buttonDelete=" + buttonDelete);
        
        if (buttonDelete != null && !buttonDelete.isEmpty()) {
            // 处理批量删除
            System.out.println("DEBUG: 执行批量删除操作");
            
            // 获取选中的购物车项ID数组
            String[] cartItemIdArray = request.getParameterValues("cartItemId");
            
            if (cartItemIdArray != null && cartItemIdArray.length > 0) {
                System.out.println("DEBUG: 批量删除，选中的ID数量: " + cartItemIdArray.length);
                for (String id : cartItemIdArray) {
                    System.out.println("  选中的ID: " + id);
                }
            } else {
                System.out.println("DEBUG: 批量删除，没有选中任何商品");
            }
            
            boolean deleted = service.deleteCartItems(request);
            System.out.println("DEBUG: 批量删除结果: " + deleted);
        }
        
        // 获取购物车列表
        System.out.println("DEBUG: 开始查询购物车列表");
        boolean success = service.queryCartList(request);
        System.out.println("DEBUG: 查询购物车列表结果: " + success);
        
        // 转发到购物车页面
        request.getRequestDispatcher("/goods/carList.jsp").forward(request, response);
    }
    
}