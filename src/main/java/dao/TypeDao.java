package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Type;

public class TypeDao {	
	
	Db db = new Db();													//新建实例（该实例实现了数据的增删改查）
	String sql = "";
	ResultSet rs = null;
	
	/**------------------------------ 获取满足查询条件的要显示的页中的记录列表 */	
	public List<Type> queryAll (HttpServletRequest request, String search, int indexStart, int pageSize) {
		
		List<Type> typeList = new ArrayList<Type>();
		String msg = "";
		
		try {	
			String typeId, typeName, note, timeRenew;
						
			String sqlWhere = "";
			
			if (search.equals("") == false) {								//如果有搜索内容
				sqlWhere = " where concat_ws(',', typeName, note) like ?";	//多字段模糊查询
			}

			if (search.equals("")) {
				sql = "select * from tb_type order by typeName" 
						+ " limit " + indexStart + "," + pageSize; 		//只获取该页的记录
				rs = db.select(sql);
			} else {
				sql = "select * from tb_type " + sqlWhere + " order by typeName" 
						+ " limit " + indexStart + "," + pageSize;
				rs = db.select(sql, "%" + search + "%"); 					//模糊查询
			}
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return typeList;
			}
			
			Type type = null;
			
			while (rs.next()) {				
				typeId 		= rs.getString("typeId");	
				typeName 	= rs.getString("typeName");		//在连接MySQL 8时指定了时区地点，则获取到的时间不需格式化
				
				type = new Type();
		
				type.setTypeId(typeId);
				type.setTypeName(typeName);
				
				typeList.add(type);
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

		return typeList;
	}		

	/**------------------------------ 根据ID查询记录 */
	public Type queryByTypeId (HttpServletRequest request, String typeId) {
		
		Type type = null;
		String msg = "";
		
		try {
			String typeName, note, timeRenew;
			
			sql = "select * from tb_type where typeId = ?";
			rs = db.select(sql, typeId);
			
			if (rs == null) {			
				msg += "数据库操作发生错误！";
				return type;
			}
			
			if (rs.next() == false) {
				db.close();
				//msg += "typeId号对应的记录已不存在，请刷新页面后重试！";
				return type;
			}
				
			typeName 	= rs.getString("typeName");		//在连接MySQL 8时指定了时区地点，则获取到的时间不需格式化
			db.close();

			type = new Type();								//创建对象
			
			type.setTypeId(typeId);
			type.setTypeName(typeName);
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}		

		return type;
	}


}
