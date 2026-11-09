package service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Type;
import dao.TypeDao;

public class TypeService {
	
	TypeDao typeDao = new TypeDao();	//创建对象
	
	//------------------用大写的GOODS，是为了在查找替换时不被更改
	public boolean queryAllForGOODS (HttpServletRequest request) {		//----实现商品列表、新添输入、修改输入时要调用
		
		List<Type> typeList = null; 									//记录列表
		String msg = "";
		
		try {			
			typeList =typeDao.queryAll(request, "", 0, 9999) ;	//####【新添代码】		//****获取当前页的记录列表。获取第0条之后的9999条记录，相当于获取所有记录
			
			if (typeList.size() == 0) {
				msg += "（查无分类记录）";
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
			
			request.setAttribute("typeList", typeList);		//传递记录列表
		}
		
		return false;
	}
	
	
	
}
