package servlet_category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CategoryService;

@WebServlet("/CategoryList")
public class CategoryList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String msg = "";
        String url = "category/CategoryList.jsp";

        try {
            if (util.LoginCheck.verify(request) == false) {
                url = "login.jsp";
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            CategoryService categoryService = new CategoryService();
            categoryService.queryAll(request); // 调用CategoryService的查询方法
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();
            e.printStackTrace();
            request.setAttribute("msg", msg);
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String msg = "";
        String url = "category/CategoryList.jsp";

        try {
            if (util.LoginCheck.verify(request) == false) {
                url = "login.jsp";
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // 检查是否是删除操作
            String buttonDelete = request.getParameter("buttonDelete");
            if (buttonDelete != null && buttonDelete.equals("删除")) {
                // 处理删除操作
                String[] categoryIds = request.getParameterValues("category_id");
                
                if (categoryIds == null || categoryIds.length == 0) {
                    msg = "请选择要删除的分类！";
                } else {
                    CategoryService categoryService = new CategoryService();
                    int successCount = 0;
                    int failCount = 0;
                    
                    // 逐个删除选中的分类
                    for (String categoryId : categoryIds) {
                        boolean result = categoryService.deleteCategory(request, categoryId);
                        if (result) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    }
                    
                    if (successCount > 0) {
                        msg += "成功删除 " + successCount + " 个分类。";
                    }
                    if (failCount > 0) {
                        msg += "有 " + failCount + " 个分类删除失败（可能被商品引用）。";
                    }
                }
            }
            
            // 无论是否删除，都执行查询
            CategoryService categoryService = new CategoryService();
            categoryService.queryAll(request);
            
            // 设置消息
            if (msg != null && !msg.isEmpty()) {
                request.setAttribute("msg", msg);
            }
            
        } catch (Exception e) {
            msg += "系统发生错误：" + e.getMessage();
            e.printStackTrace();
            request.setAttribute("msg", msg);
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}