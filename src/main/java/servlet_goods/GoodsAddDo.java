package servlet_goods;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.GoodsService;

@WebServlet("/GoodsAddDo")
public class GoodsAddDo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("GoodsAdd");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");        
        java.io.PrintWriter out = response.getWriter();
        String msg = "";
        String url = "GoodsShow";
        
        try {            
            if (util.LoginCheck.verify(request) == false) {
                msg = "请先登录！";
                out.print(msg);
                return;
            }
            
            // 获取表单参数
            String goodsName = request.getParameter("goodsName");
            String category_id = request.getParameter("category_id");
            String price = request.getParameter("price");
            String start_date = request.getParameter("start_date");
            String end_date = request.getParameter("end_date");
            String location = request.getParameter("location");
            String guide = request.getParameter("guide");
            String status = request.getParameter("status");
            String goodsUUID = request.getParameter("goodsUUID");
            String image = request.getParameter("image");
            
            // 参数验证
            if (goodsName == null || goodsName.trim().isEmpty()) {
                msg = "商品名称不能为空";
                out.print(msg);
                return;
            }
            if (category_id == null || category_id.trim().isEmpty()) {
                msg = "请选择商品分类";
                out.print(msg);
                return;
            }
            if (price == null || price.trim().isEmpty()) {
                msg = "价格不能为空";
                out.print(msg);
                return;
            }
            if (start_date == null || start_date.trim().isEmpty()) {
                msg = "起售时间不能为空";
                out.print(msg);
                return;
            }
            
            // 验证日期格式（简化版）
            if (!start_date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                msg = "起售时间格式不正确，应为：yyyy-MM-dd";
                out.print(msg);
                return;
            }
            if (end_date != null && !end_date.trim().isEmpty() && !end_date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                msg = "结束时间格式不正确，应为：yyyy-MM-dd";
                out.print(msg);
                return;
            }
            
            // 验证价格格式
            try {
                Float.parseFloat(price);
            } catch (NumberFormatException e) {
                msg = "请输入正确的价格格式";
                out.print(msg);
                return;
            }
            
            // 设置请求参数（使用setParameter）
            request.setAttribute("goodsName", goodsName);
            request.setAttribute("category_id", category_id);
            request.setAttribute("price", price);
            request.setAttribute("start_date", start_date);
            request.setAttribute("end_date", end_date);
            request.setAttribute("location", location);
            request.setAttribute("guide", guide);
            request.setAttribute("status", status);
            request.setAttribute("goodsUUID", goodsUUID);
            request.setAttribute("image", image);
            
            GoodsService goodsService = new GoodsService();            
            String goodsId = goodsService.addGoods(request);
            
            if (goodsId == null || goodsId.equals("0")) {
                msg = "商品添加失败！";
                if (request.getAttribute("msg") != null) {
                    msg = request.getAttribute("msg").toString();
                }
                out.print(msg);
                return;
            }
        
            url = "GoodsShow?goodsId=" + goodsId;
            url = "@Redirect:" + url;
            out.print(url);
            
        } catch (Exception e) {
            msg = "系统发生错误：" + e.getMessage();    
            e.printStackTrace();
            out.print(msg);
        }
    }
}