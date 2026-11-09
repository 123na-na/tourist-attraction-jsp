package servlet_user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String buttonSubmit = request.getParameter("buttonSubmit");
        
        if (buttonSubmit != null) {
            UserService userService = new UserService();
            String userId = userService.addUser(request);
            
            if (!userId.equals("0")) {
                response.sendRedirect("login.jsp?msg=注册成功，请登录");
                return;
            }
        }
        
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}