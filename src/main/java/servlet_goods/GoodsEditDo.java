package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;

@WebServlet("/GoodsEditDo")
public class GoodsEditDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果是GET请求，重定向到GoodsEdit页面
        String goodsId = request.getParameter("goodsId");
        if (goodsId != null && !goodsId.trim().equals("")) {
            response.sendRedirect("GoodsEdit?goodsId=" + goodsId);
        } else {
            response.sendRedirect("GoodsList");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        java.io.PrintWriter out = response.getWriter();    
        String msg = "";
        String url = "GoodsShow";
        boolean result = false;
        
        try {            
            if (util.LoginCheck.verify(request) == false) {            // 如果登录已失效，或权限不足
                url = "login.jsp";
                response.sendRedirect(url);
                return;
            }
            
            GoodsService goodsService = new GoodsService();
            result = goodsService.editGoods(request);    // 修改商品信息
            
            if (result == false) {                                // 信息修改失败
                out.print(msg);
                return;
            }
            
            // ---------------------- 信息修改成功
            String goodsId = request.getParameter("goodsId");        
            url = "GoodsShow?goodsId=" + goodsId;                // 目标网址
            url = "@Redirect:" + url;                            // 增加@Redirect用于通过JS代码实现网页跳转
            
            out.print(url);
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
            out.print(msg);
        }
    }
}