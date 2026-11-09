package util;

import javax.servlet.http.HttpServletRequest;

import bean.User;

public class LoginCheck {

	/**
	 * 如果用户已登录，将返回true。<br>
	 * 返回false时，出错消息会保存到request
	 */	
	public static boolean isLogined(HttpServletRequest request) {			//static类型的方法，可以直接调用
		String msg = "";
		
		try {		
			User myUser = (User) request.getSession().getAttribute("myUser");
			
			if (myUser == null) {											//如果此session属性不存在
				msg = "您的登录已失效！请重新登录。";
				return false;
			}
				
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return true;
	}
	
	/**
	 * 如果用户已登录，且角色是admin或user，将返回true。<br>
	 * 返回false时，出错消息会保存到request
	 */	
	public static boolean verify(HttpServletRequest request) {				//static类型的方法，可以直接调用
		String msg = "";
		
		try {	
			User myUser = (User) request.getSession().getAttribute("myUser");
			
			if (myUser == null) {											//如果此session属性不存在
				msg = "您的登录已失效！请重新登录。";
				return false;
			}
			
			if (myUser.getRole().equals("guest")) {	
				msg = "您的权限不足！";
				return false;
			}	
				
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return true;
	}
	
	/**
	 * 如果是管理员角色admin，返回true。<br>
	 * 若参数isSelf=true，当显示、修改自己的个人信息，或访问main.jsp页时，才返回true。<br>
	 * 返回false时，出错消息会保存到request
	 */
	public static boolean verify(HttpServletRequest request, boolean isSelf) {
		String msg = "";
		
		try {		
			User myUser = (User) request.getSession().getAttribute("myUser");
			
			if (myUser == null) {								//如果此session属性不存在
				msg = "您的登录已失效！请重新登录。";
				return false;
			}	
			
			if (myUser.getRole().equals("admin")) {				//如果是管理员角色，无论isSelf是true或false
				return true;			
			}		
	
			if (isSelf == false) {								//如果不是管理员，且isSelf为false，却想拥有用户管理等权限
				msg = "程序错误！请与管理员联系。";
				return false;
			}			
	
			String servlet = request.getServletPath();			//得到“/main.jsp”，当前网页的路径
			servlet = servlet.substring(1);						//得到“main.jsp”
			
			if (servlet.equals("main.jsp")) {					//所有用户都可以访问main.jsp页
				return true;
			}
			
			String myUserId = myUser.getUserid();							//session中的值
			String userid   = (String) request.getParameter("userid");		//地址栏参数值，显示或修改用户时
			
			if (myUserId == null || userid == null) {
				msg = "发生错误！请重新登录。";
				return false;
			}
			
			if (myUserId.equals(userid) == false) {				//显示或修改的不是自己的信息
				msg = "用户ID不匹配！请重新登录。";
				return false;
			}
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}

		return true;											//将允许显示或修改自己的信息
	}
	
	 
	/**
     * 购物车专用 - 获取当前登录用户的ID（字符串形式）
     * 返回null表示未登录
     */
    public static String getCartUserId(HttpServletRequest request) {
        try {
            User myUser = (User) request.getSession().getAttribute("myUser");
            if (myUser == null) {
                System.out.println("DEBUG: getCartUserId - myUser为null");
                return null;
            }
            
            String userId = myUser.getUserid();
            System.out.println("DEBUG: getCartUserId - 获取用户ID: " + userId);
            
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("DEBUG: 用户ID为空");
                return null;
            }
            
            return userId.trim();
            
        } catch (Exception e) {
            System.out.println("DEBUG: getCartUserId异常: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 购物车专用 - 获取当前登录用户的ID（整数形式）
     * 返回0表示未登录或转换失败
     */
    public static int getCartUserIdInt(HttpServletRequest request) {
        try {
            String userIdStr = getCartUserId(request);
            if (userIdStr == null) {
                return 0;
            }
            
            return Integer.parseInt(userIdStr);
            
        } catch (NumberFormatException e) {
            System.out.println("DEBUG: 用户ID不是有效的数字: " + e.getMessage());
            return 0;
        } catch (Exception e) {
            System.out.println("DEBUG: getCartUserIdInt异常: " + e.getMessage());
            return 0;
        }
    }
}
