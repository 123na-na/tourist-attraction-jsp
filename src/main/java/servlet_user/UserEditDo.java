package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;


@WebServlet("/UserEditDo")
public class UserEditDo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//由于修改商品所输入的商品信息是以POST方式提交的，将访问doPost()方法，
		//如果以GET方式访问此Servlet，即访问doGet()方法，那么在request中应没有输入的商品信息，
		//故此处需返回到Servlet类GoodsEdit，以便在商品修改页修改商品信息。
		response.sendRedirect("UserEdit?userId=" + request.getParameter("userId"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");        
	    java.io.PrintWriter out = response.getWriter();    
	    String msg = "";
	    String url = "UserList";
	    boolean result = false;  // 添加result变量声明
	    
	    try {            
            if (util.LoginCheck.verify(request) == false) {  // 如果登录已失效，或权限不足
                url = "login.jsp";
                response.sendRedirect(url);
                return;
            }
            
            UserService userService = new UserService();
            result = userService.deleteUser(request);  // 调用删除方法，而不是editUser
            
            if (result == false) {  // 删除失败
                out.print("删除失败：" + msg);
                return;
            }
            
            // 删除成功
            url = "@Redirect:" + "UserList";  // 增加@Redirect用于通过JS代码实现网页跳转
            
        } catch (Exception e) {
            msg += "系统发生错误。";    
            e.printStackTrace();
        } finally {
            if (request.getAttribute("msg") != null) {
                msg += request.getAttribute("msg").toString();
            }
            
            // 输出结果
            if (!result) {  // 如果删除失败
                out.print(msg);  // 输出错误消息
            } else {  // 如果删除成功
                out.print(url);  // 网页将转向
            }
        }
    }
}
