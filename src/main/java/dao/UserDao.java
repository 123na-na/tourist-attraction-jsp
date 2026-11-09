package dao;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import bean.Goods;
import bean.Type;
import bean.User;

public class UserDao {	
	
	Db db = new Db();													//新建实例（该实例实现了数据的增删改查）
	String sql = "";
	ResultSet rs = null;
	
	/**------------------------------ 根据用户名和密码查询用户。登录验证用 */
	public User queryByUsernameAndPassword (HttpServletRequest request, String username, String password) {
		
		User user = null;
		String msg = "";
		
		try {	
			sql="select userid,role from user where username = ? and password = ?";//sql语句	
			rs=db.select(sql,username,password);//执行查询	
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return user;
			}
			
			if (rs.next() == false) {
				db.close();
				//msg += "用户名或密码错误！";
				return user;
			}
					
			String userid	= rs.getString("userid");		//读取字段
			String role 	= rs.getString("role");	
			db.close();										//用完及时关闭与数据库连接
	
			user =new User();								//创建user对象	
			user.setUserid(userid);							//给属性赋值
			user.setUsername(username);
			user.setRole(role);
			
			
			return user;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}		

		return user;
	}
	
	public int queryCount (HttpServletRequest request, String search) {

		int count = 0;
		String msg = "";
		
		try {															//使用try-catch，便于传递消息
			// 修改 queryCount 方法中的类似代码：
			// 修改后（正确的代码）：
			String sqlWhere = "";
			String param = null;  // 改为单个 String 变量

			if (search != null && !search.trim().equals("")) {
			    List<String> roleList = Arrays.asList("admin", "user", "group");
			    if (roleList.contains(search.trim())) {
			        sqlWhere = " where role = ?";
			        param = search.trim();  // 直接存储 String
			    } else {
			        sqlWhere = " where concat_ws(',', userid, username, role) like ?";
			        param = "%" + search + "%";  // 直接存储 String
			    }
			}

			if (search == null || search.trim().equals("")) {
			    sql = "select count(*) from user";
			    rs = db.select(sql);
			} else {
			    sql = "select count(*) from user " + sqlWhere;
			    if (param != null) {
			        rs = db.select(sql, param);  // 传递单个 String 参数
			    }
			}
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return count;
			}
			
			if (rs.next() == false) {
				db.close();
				return count;
			}			
			count = rs.getInt(1);
			db.close();
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();							//输出异常信息到控制台
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}

		return count;										//满足条件的记录数可能为0
	}	
	
	public User queryByUserId (HttpServletRequest request, String userid) {
		
		User user = null;
		String msg = "";
		
		try {
			String  username, email, phone, role, create_time;
			
			sql = " select * from user where userid = ?";		//####【新添代码】
			rs = db.select(sql, userid);
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return user;
			}
			
			if (rs.next() == false) {
				db.close();
				//msg += "goodsId号对应的记录已不存在，请刷新页面后重试！";
				return user;
			}
				
			userid 	= rs.getString("userid");		
			username 	= rs.getString("username");		
			email = rs.getString("email");
			phone = rs.getString("phone");
			role = rs.getString("role");
			create_time = rs.getString("create_time");	//在连接MySQL 8时指定了时区地点，则获取到的时间不需格式化
			db.close();
	
			user = new User();						//创建对象
			
			user.setUserid(userid);
			user.setUsername(username);
			user.setEmail(email);
			user.setPhone(phone);			
			user.setRole(role);
			user.setCreate_time(create_time);
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}		

		return user;
	}
	
	/**------------------------------ 获取满足查询条件的要显示的页中的记录列表 */
	public List<User> queryAll (HttpServletRequest request, String search, int indexStart, int pageSize) {
		
		List<User> userList = new ArrayList<User>();					//创建Goods对象列表
		String msg = "";
		
		try {
			String userid, username, email, phone, role, create_time;
			
			String sqlWhere = "";
			String param = null;  // 改为单个 String 变量

			if (search != null && !search.trim().equals("")) {
			    List<String> roleList = Arrays.asList("admin", "user", "group");
			    if (roleList.contains(search.trim())) {
			        sqlWhere = " where role = ?";
			        param = search.trim();
			    } else {
			        sqlWhere = " where concat_ws(',', userid, username, role) like ?";
			        param = "%" + search + "%";
			    }
			}

			if (search == null || search.trim().equals("")) {
			    sql = "select * from user order by username"
			            + " limit " + indexStart + ", " + pageSize;
			    rs = db.select(sql);
			} else {
			    sql = "select * from user " + sqlWhere + " order by username"
			            + " limit " + indexStart + ", " + pageSize;
			    if (param != null) {
			        rs = db.select(sql, param);  // 传递单个 String 参数
			    }
			}
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return userList;
			}
			
			User user = null;
			long time = (new Date()).getTime();
			
			while (rs.next()) {
				userid 	= rs.getString("userid");		
				username 	= rs.getString("username");		
				email = rs.getString("email");
				phone = rs.getString("phone");
				role = rs.getString("role");
				create_time = rs.getString("create_time");
				
				user = new User();
	
				user.setUserid(userid);
				user.setUsername(username);
				user.setEmail(email);
				user.setPhone(phone);			
				user.setRole(role);
				user.setCreate_time(create_time);
	
				userList.add(user);					//添加到对象列表
			}
			db.close();
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}		
	
		return userList;			//返回对象列表
	}	

	public String addUser (HttpServletRequest request, User user) {		
	    
	    String msg = "";
	    
	    try {
	        String username = user.getUsername();
	        String password = user.getPassword();  // 获取密码
	        String email = user.getEmail();
	        String phone = user.getPhone();
	        String role = user.getRole();
	        String create_time = user.getCreate_time();
	        
	        // 修改SQL，不包含userid字段，但包含password字段
	        sql = "INSERT INTO user (username, password, email, phone, role, create_time) " +
	                  "VALUES (?, ?, ?, ?, ?, ?)";                //####【修改代码】      
	        String newUserId = db.insert(sql, username, password, email, phone, role, create_time);
	        
	        if (newUserId.equals("0")) {
	            msg += "用户新添失败！";
	            return "0";
	        }
	    
	        msg = "用户新添成功！";        
	        return newUserId;
	    
	    } catch (Exception e) {
	        msg += "系统发生错误。";    
	        e.printStackTrace();
	    } finally {
	        if (request.getAttribute("msg") != null) {
	            msg += request.getAttribute("msg").toString();
	        }
	        request.setAttribute("msg", msg);
	    }        
	    
	    return "0";
	}

	public int editUser (HttpServletRequest request, User user) {		
		
		int count = 0;
		String msg = "";
		
		try {
			String userid, username, email, phone,role,create_time;
			
			userid = user.getUserid();
			username = user.getUsername();
	        email = user.getEmail();
	        phone = user.getPhone();
	        role = user.getRole();
	        create_time = user.getCreate_time();
			
			sql = "UPDATE user SET username=?, email=?, phone=?, role=?, create_time=? WHERE userid=?";				//####【新添代码】		
			count = db.update(sql, username, email, phone, role, create_time, userid);
			
			if (count == 0) {
				//msg = "商品信息修改失败！";
				return count;
			}	
		
			//msg = "商品信息修改成功！";		
			return count;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}		
	
		return count;
	}

	/**------------------------------ 是否存在商品编码相同而与ID不同的记录 */
	public boolean existByUserNoExptId (HttpServletRequest request, String userid) {
	
		String msg = "";
		
		try {			
			sql = "SELECT COUNT(*) FROM user WHERE userid = ? AND userid != ?";		//####【新添代码】
			rs = db.select(sql, userid, userid);
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return false;
			}
			
			if (rs.next() == false) {
				db.close();
				return false;
			}
			db.close();			
			
			//msg += "已存在相同的商品编码！";
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

	/**------------------------------ 是否存在商品编码相同的记录 */
	public boolean existByUserNo (HttpServletRequest request, String userid) {
	
		String msg = "";
		
		try {			
			sql = " SELECT COUNT(*) FROM user WHERE userid = ? ";		//####【新添代码】
			rs = db.select(sql, userid);
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return false;
			}
			
			if (rs.next() == false) {
				db.close();
				return false;
			}
			db.close();
	
			//msg += "已存在相同的商品编码！";
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

	/**------------------------------ 删除ID对应的记录 */
	public boolean deleteByUserId (HttpServletRequest request, String userid) {
	    String msg = "";
	    
	    try {    
	        System.out.println("DAO层收到删除请求，userid=" + userid);
	        
	        sql = "DELETE FROM user WHERE userid = ?";
	        System.out.println("执行的SQL: " + sql);
	        
	        int count = db.delete(sql, userid);
	        System.out.println("影响的行数: " + count);
	        
	        if (count == 0) {
	            msg += "数据库删除操作未影响任何行！";
	            System.out.println("DAO错误: " + msg);
	            return false;
	        }   
	        
	        System.out.println("DAO删除成功！");
	        return true;
	        
	    } catch (Exception e) {
	        msg += "数据库操作发生错误：" + e.getMessage();
	        System.out.println("DAO异常: " + msg);
	        e.printStackTrace();
	        return false;
	    } finally {
	        request.setAttribute("msg", msg);
	    }       
	}

	/**------------------------------ 删除ID列表中的记录 */
	public boolean deleteByUserIdLot (HttpServletRequest request, String userIdLot) {
	
		String msg = "";
		
		try {			
			sql = "delete from user where userid in ("+ userIdLot +")";			//####【新添代码】		//批量删除记录
			int count = db.delete(sql);
			
			if (count == 0) {
				//msg += "商品批量删除失败！";
				return false;
			}
			
			//msg = "商品批量删除成功！";
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
