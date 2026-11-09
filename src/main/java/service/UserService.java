package service;

import java.io.File;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bean.Goods;
import bean.Type;
import bean.User;
import dao.Db;
import dao.GoodsDao;
import dao.UserDao;

public class UserService {									//业务层、业务逻辑层，service或biz
	
	UserDao userDao = new UserDao();						//创建对象

	public boolean queryAll (HttpServletRequest request) {
		
		List<User> userList = null; 						//记录列表
		String search = ""; 								//搜索的内容
		int indexStart = 0;	 								//查询的起始位置
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				String[] userIdArray = request.getParameterValues("userid"); 	//获取ID列表			
				
				if (userIdArray != null) {
					deleteByUserIdLot(request, userIdArray);					//****删除所选。在本类中定义的方法
				}
			}

			String buttonSearch	= request.getParameter("buttonSearch");	//搜索按钮
			String buttonPage  	= request.getParameter("buttonPage"); 	//页码提交按钮
			String pageInput  	= "1";									//输入的页码
			
			if (buttonSearch != null && request.getParameter("search") != null) { 		//如果按下了搜索按钮
				search = request.getParameter("search").trim(); 						//搜索内容
			} else if (buttonPage != null && request.getParameter("search") != null) { 	//如果按下了页码提交按钮
				search 	  = request.getParameter("search").trim(); 						//搜索内容
				pageInput = request.getParameter("pageShow"); 							//页码输入框中的值
			} else { 													//点击了页码链接，或者刚打开此页
				if (request.getParameter("searchUrl") != null) { 
					search = request.getParameter("searchUrl"); 		//不需要进行解码操作，系统会自动解码
				}
				
				if (request.getParameter("pageUrl") != null) {			//地址栏中的页码
					pageInput = request.getParameter("pageUrl");
				}
			}	
			
			// 添加角色筛选逻辑
			if (request.getParameter("buttonRoleFilter") != null) {
			    String roleFilter = request.getParameter("roleFilter");
			    if (roleFilter != null && !roleFilter.trim().equals("")) {
			        // 如果已经有搜索内容，则追加角色筛选条件
			        if (search != null && !search.trim().equals("")) {
			            // 这里我们优先使用角色筛选，覆盖原有搜索
			            search = roleFilter; // 直接传递角色值，不添加前缀
			        } else {
			            search = roleFilter; // 直接传递角色值
			        }
			    }
			}
			int countRow = userDao.queryCount(request, search);		//****获得记录总数
					
			int pageSize  = 6;  										//每页6条记录
			int pageCount = 0; 											//预设总页数为0
		
			if (countRow % pageSize == 0) {								//如果余数为0，即能整除
				pageCount = countRow / pageSize; 						//总页数
			} else {
				pageCount = countRow / pageSize + 1;					//不能整除则加1页。如果除数为小数，将自动去除小数部分得到整数			
			}
			
			try { 									 
				pageShow = Integer.parseInt(pageInput);					//如果是数字，返回字符串对应的整数
			} catch (Exception e) {
				//showPage = 1; 										//如果抛出异常，则取预设值
			}				
		
			if (pageShow < 1) {											//如果当前页码小于1
				pageShow = 1;
			} else if (pageShow > pageCount && pageCount >= 1) { 		//如果当前页码大于总页数，且总页数>=1
				pageShow = pageCount;
			}
			
			String searchUrl = "";
			
			if (search.equals("") == false) {
				searchUrl = URLEncoder.encode(search, "UTF-8");			//进行URL编码，以便在地址栏传递		
			}
			
			String mapName = request.getServletPath();					//当前的请求名称   /GoodsListLayout
			mapName = mapName.substring(1);								//删除最前面的斜杠  GoodsListLayout
			//System.out.println(mapName);
			
			if (pageShow <= 1) {
				page += "<span style='color:gray;'>首页&ensp;";
				page += "上一页&ensp;</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=1"
						+ "&searchUrl=" + searchUrl + "'>首页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow - 1) 
						+ "&searchUrl=" + searchUrl + "'>上一页</a>&ensp;";
			}
	
			if (pageShow >= pageCount) {
				page += "<span style='color:gray;'>下一页&ensp;";
				page += "尾页</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow + 1) 
						+ "&searchUrl=" + searchUrl + "'>下一页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + pageCount 
						+ "&searchUrl=" + searchUrl + "'>尾页</a>";
			}
			
			page += "&emsp;&emsp;";
			page += "页码：" + pageShow + "/" + pageCount + "&emsp;";
			page += "记录数：" + countRow + "&emsp;&emsp;";	
			
			page += "输入页码:";
			page += "	<input type='text' name='pageShow' value='" + pageShow 
					+ "' style='width:40px; text-align:center;'>";
			page += "	<input type='submit' name='buttonPage' value='提交'>&emsp;";

			if (pageShow > 0) {
				indexStart = (pageShow - 1) * pageSize;  							//查询的起始位置
			}
			
			userList = userDao.queryAll(request, search, indexStart, pageSize);	//****获取当前页的记录列表
			
			if (userList == null || userList.size() == 0) {
				msg += "（查无记录）";
				return false;
			}

			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。在详情页删除记录后保存的消息
				msg += request.getSession().getAttribute("msg").toString();		//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}

			request.setAttribute("search", search);					//搜索内容
			request.setAttribute("indexStart", indexStart);  		//查询的起始位置
			request.setAttribute("page", page);						//页码相关的链接
			request.setAttribute("userList", userList);			//商品对象列表
		}
		
		return false;			
	}

	public boolean queryByUserId (HttpServletRequest request) {		
		
		User user = null;
		String msg = "";
		
		try {
			String userid = request.getParameter("userid"); 		//获取地址栏参数goodsId的值 
			
			try { 									 
				Integer.parseInt(userid);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg += "参数userId错误！";
				return false;
			}	
			
			user = userDao.queryByUserId(request, userid);		//****根据ID获取记录
			
			if (user == null) {
				msg += "（查无此商品记录）";
				return false;
			}
			
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。新添或修改记录后保存的消息
				msg += request.getSession().getAttribute("msg").toString();		//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}
			
			request.setAttribute("user", user);								//通过request对象传递goods对象
		}
		
		return false;
	}	
	
	public String addUser (HttpServletRequest request) {		

	    String userid = "0";
	    String msg = "";
	    
	    try {
	        String  username, password, role, email, phone, create_time;
	        
	        // 移除userid参数
	        username = request.getParameter("username");
	        password = request.getParameter("password");
	        role = request.getParameter("role");
	        email = request.getParameter("email");
	        phone = request.getParameter("phone");
	        create_time = request.getParameter("create_time");
	        
	        // 修改参数检查，移除userid
	        if (username == null || password == null || role == null || 
	            email == null || phone == null || create_time == null) {
	            msg = "输入不正确！";
	            return "0";
	        }
	        
	        username = username.trim();
	        password = password.trim();
	        role = role.trim();
	        email = email.trim();
	        phone = phone.trim();
	        create_time = create_time.trim();
	        
	        // 验证必填字段
	        if (username.equals("")) {
	            msg += "请输入用户名称。";
	            return "0";
	        }
	        
	        if (password.equals("")) {
	            msg += "请输入密码。";
	            return "0";
	        }
	        
	        if (role.equals("")) {
	            msg += "请输入用户角色。";
	            return "0";
	        }
	        
	        if (phone.equals("")) {
	            msg += "请输入手机号。";
	            return "0";
	        }
	        
	        if (email.equals("")) {
	            msg += "请输入邮箱。";
	            return "0";
	        }
	        
	        // 验证邮箱格式
	        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
	            msg += "请输入正确的邮箱格式！";
	            return "0";
	        }
	        
	        // 验证手机号格式
	        if (!phone.matches("^1[3-9]\\d{9}$")) {
	            msg += "请输入正确的手机号格式！";
	            return "0";
	        }
	        
	        // 验证创建时间格式
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            create_time = sdf.format(sdf.parse(create_time));
	        } catch (Exception e) {
	            msg += "请输入正确的创建时间格式（如：2023-01-01 08:00:00）！";
	            return "0";
	        }
	        
	        // 移除检查用户ID是否已存在的代码
	        
	        User user = new User();
	        
	        // 不设置userid，由数据库自动生成
	        user.setUsername(username);
	        user.setPassword(password);  // 设置密码
	        user.setRole(role);
	        user.setEmail(email);
	        user.setPhone(phone);
	        user.setCreate_time(create_time);

	        userid = userDao.addUser(request, user);                    //****新添记录

	        if (userid.equals("0")) {
	            msg += "新添记录失败！请重试。";
	            return userid;
	        }
	        
	        msg += "新添记录成功。";    
	        request.getSession().setAttribute("msg", msg);
	        return userid;
	        
	    } catch (Exception e) {
	        msg += "系统发生错误：" + e.getMessage();    
	        e.printStackTrace();
	    } finally {
	        if (request.getAttribute("msg") != null) {
	            msg += request.getAttribute("msg").toString();
	        }
	        request.setAttribute("msg", msg);
	    }
	    
	    return userid;
	}
	
	public boolean editUser (HttpServletRequest request) {		

		String msg = "";
		
		try {			
			String userid = request.getParameter("userid"); 			//获取地址栏参数id的值
			
			try { 									 
				Integer.parseInt(userid);					
			} catch (Exception e) {
				msg += "参数userId错误！";
				return false;
			}
			
			String username = request.getParameter("username");
			String role = request.getParameter("role");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String create_time = request.getParameter("create_time");
			
			// 验证参数
			if (username == null || role == null || email == null || 
				phone == null || create_time == null) {
				msg = "输入不正确！";
				return false;
			}
			// 去除空格
			username = username.trim();
			role = role.trim();
			email = email.trim();
			phone = phone.trim();
			create_time = create_time.trim();
			
			// 验证必填字段
			if (username.equals("")) {
				msg += "请输入用户名称。";
				return false;
			}
			
			if (role.equals("")) {
				msg += "请输入用户角色。";
				return false;
			}
			
			// 验证邮箱格式
			if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				msg += "请输入正确的邮箱格式！";
				return false;
			}
			
			// 验证手机号格式
			if (!phone.matches("^1[3-9]\\d{9}$")) {
				msg += "请输入正确的手机号格式！";
				return false;
			}
			
			// 验证创建时间格式
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				create_time = sdf.format(sdf.parse(create_time));
			} catch (Exception e) {
				msg += "请输入正确的创建时间格式！";
				return false;
			}
			
			// 检查用户是否存在
			User existingUser = userDao.queryByUserId(request, userid);
			if (existingUser == null) {
				msg += "用户ID对应的记录已不存在！";
				return false;
			}
			
			// 创建User对象
			User user = new User();
			user.setUserid(userid);
			user.setUsername(username);
			user.setRole(role);
			user.setEmail(email);
			user.setPhone(phone);
			user.setCreate_time(create_time);
			
			// 更新用户信息
			int count = userDao.editUser(request, user);
			
			if (count == 0) {
				msg += "用户信息修改失败！请重试。";
				return false;
			}

			msg += "用户信息修改成功！";
			request.getSession().setAttribute("msg", msg);
			return true;
			
		} catch (Exception e) {
			msg += "系统发生错误：" + e.getMessage();	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}	
		return false;
	}
	
	// 新增：删除单个用户的方法
	public boolean deleteUser(HttpServletRequest request) {
		String msg = "";
		
		try {	
			String userId = request.getParameter("userId"); 			// 注意：这里使用userId，与editUser中的userid保持一致
			
			if (userId == null || userId.trim().isEmpty()) {
				msg += "用户ID不能为空！";
				return false;
			}
			
			try { 									 
				Integer.parseInt(userId);				
			} catch (Exception e) {
				msg += "参数userId错误！";
				return false;
			}

			User user = userDao.queryByUserId(request, userId);	//****	
			
			if (user == null) {
				msg += "userId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			
			boolean result = userDao.deleteByUserId(request, userId);		//****删除这条记录
			
			if (result == false) {
				msg += "删除记录失败！请重试。";
				return false;
			}

			msg += "删除记录成功。";			

			request.getSession().setAttribute("msg", msg);
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return false;
	}
	
	public boolean deleteByUserId (HttpServletRequest request) {
	    String msg = "";
	    
	    try {    
	        // 调试：打印所有参数
	        System.out.println("=== 开始删除操作 ===");
	        System.out.println("获取的userId参数: " + request.getParameter("userId"));
	        System.out.println("获取的userid参数: " + request.getParameter("userid"));
	        System.out.println("所有参数名: " + request.getParameterMap().keySet());
	        
	        // 尝试两种参数名
	        String userId = request.getParameter("userId");
	        if (userId == null || userId.trim().isEmpty()) {
	            userId = request.getParameter("userid");
	            System.out.println("使用小写userid: " + userId);
	        }
	        
	        if (userId == null || userId.trim().isEmpty()) {
	            msg += "用户ID不能为空！";
	            System.out.println("错误: " + msg);
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        System.out.println("要删除的用户ID: " + userId);
	        
	        // 检查用户是否存在
	        User user = userDao.queryByUserId(request, userId);
	        System.out.println("查询到的用户: " + (user != null ? user.getUsername() : "null"));
	        
	        if (user == null) {
	            msg += "用户ID" + userId + "对应的记录已不存在，请刷新页面后重试！";
	            System.out.println("错误: " + msg);
	            request.setAttribute("msg", msg);
	            return false;
	        }
	        
	        // 执行删除
	        System.out.println("开始调用DAO删除...");
	        boolean result = userDao.deleteByUserId(request, userId);
	        System.out.println("DAO删除结果: " + result);
	        
	        if (result == false) {
	            msg += "从数据库删除记录失败！请重试。";
	            System.out.println("错误: " + msg);
	            request.setAttribute("msg", msg);
	            return false;
	        }

	        msg += "删除用户 [" + user.getUsername() + "] 成功！";
	        System.out.println("成功: " + msg);
	        
	        request.getSession().setAttribute("msg", msg);
	        return true;
	        
	    } catch (Exception e) {
	        msg += "系统发生错误：" + e.getMessage();
	        System.out.println("异常: " + msg);
	        e.printStackTrace();
	        request.setAttribute("msg", msg);
	        return false;
	    }
	}
	
	public boolean deleteByUserIdLot (HttpServletRequest request, String[] userIdArray) {		//被本类中的queryAll()方法调用
		
		String msg = "";
		
		try {
			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			String uploadFolder = "fileUpload\\goodsImage\\";					//存放文件的子目录
			path += uploadFolder; 
			File file = null;
			User user = null;
			String userid = "";
			
			String userIdLot = "";
			
			for (int i = 0; i < userIdArray.length; i++) {
				try { 									 
					userid = String.valueOf(Integer.parseInt(userIdArray[i]));		   
				} catch (Exception e) {
					continue;													//如果转换失败，即不是整数，则略过此项
				}
				
				userIdLot += "," + userid;	
				
				user = userDao.queryByUserId(request, userid);				//****	
				
				if (user == null) {											//如果查无记录
					continue;
				}
			
				/*String image = user.getImage();
				
				if (image == null || image.trim().equals("")) {					//如果image无效
					continue;
				}
				
				file = new File(path, image);									//创建File对象
				
				if (file.exists()) {											//如果已存在同名文件，应先删除它
					if (file.delete() == false) {								//如果删除失败
						msg += "将文件" + image + "删除时失败。";
						continue;
					}
				}*/
			}
			
			if (userIdLot.equals("")) {
				return false;
			}
			userIdLot = userIdLot.substring(1);								//去除最前面的逗号
			
			boolean result = userDao.deleteByUserIdLot(request, userIdLot);	//****删除这些记录

			if (result == false) {
				msg += "删除记录失败！请重试。";
				return false;
			}
			
			msg += "删除记录成功。";
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return false;	
	}	
	
}