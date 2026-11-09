package dao;

import java.sql.*;

public class Db {

	//----------------------------------- 需将mysql-connector-java-8.0.26.jar复制到src/main/webapp/WEB-INF/lib文件夹
	// 连接信息不写进源码：优先读环境变量，未配置时回落到本机默认值（密码必须自行配置）
	private static String driver 		= "com.mysql.cj.jdbc.Driver";
	private static String connString	= env("TOURISM_DB_URL",
			"jdbc:mysql://localhost:3316/tourism_system"
			+ "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
	private static String username 		= env("TOURISM_DB_USER", "root");
	private static String password 		= env("TOURISM_DB_PASSWORD", "");

	private static String env(String key, String fallback) {
		String v = System.getenv(key);
		return (v == null || v.isEmpty()) ? fallback : v;
	}

private Connection	conn; 								//数据库连接
	private Statement 	stmt;  								//SQL执行接口对象
	private ResultSet 	rs;    								//结果集，记录集
	
	private PreparedStatement pstmt;						//预编译的SQL执行接口对象
	private boolean canShowSql = false;						//是否输出最终执行的SQL语句到控制台
	private String  msg = "";								//消息
	
	/**
	 * 创建数据库连接
	 */
	private Connection getConnection() {					//**此方法是private类型的，只供在类内部调用
		Connection myConn = null; 
		
		try {
			Class.forName(driver);   												//加载驱动程序
			myConn = DriverManager.getConnection(connString, username, password); 	//创建数据库连接
		
		} catch (Exception e) {
			msg = "\n创建数据库连接失败: " + e.getMessage() + "\n";	//错误信息
			System.err.println(msg);								//实时输出错误信息到控制台
			//转义符：\r是回车，\n是换行，前者使光标回到行首（return），后者使光标下移一行（next）
		}
		
		return myConn;												//返回null表示创建连接失败
	}	

	/**
	 * 查询数据。需要调用db.close()。除非查询时报错，即当rs=null时，才不需要调用db.close()。
	 * @param sql - 参数sql为准备执行的SQL语句。
	 * 				样例：select('select * from tb_student where studentId='888')。
	 * @return ResultSet - 记录集。
	 */
	public ResultSet select (String sql) {					//如果只有一个参数，则优先执行此方法（明确了参数个数）
		rs = null;

		try {
			if (conn == null || conn.isClosed()) {			//如果尚未创建连接，或连接已关闭
				conn = getConnection(); 					//连接数据库
				
				stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
															//创建SQL执行接口对象stmt，数据游标只能向前移动，只读。效率更高
				//stmt = conn.createStatement();    		//创建SQL执行接口对象stmt，以默认方式。效率稍低
				
			} else if (stmt == null || stmt.isClosed()) {	//如果尚未创建SQL执行接口对象，或对象已被关闭
				stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			}
			
			rs = stmt.executeQuery(sql);        			//执行查询此SQL语句
			
			if (canShowSql) {								//是否输出最终执行的SQL语句
				System.out.println("查询：" + sql);
			}
			
		} catch (Exception e) {
			close();										//出错则关闭连接
			msg = "\n查询数据失败: " + e.getMessage() + "（单参数）\n";
			System.err.println(msg);
		}

		return rs; 											//返回记录集。即使没查到数据，也不是null。当出错时才会是null
	}
	
	/**
	 * 查询数据。 需要调用db.close()。除非查询时报错，即当rs=null时，才不需要调用db.close()。
	 * @param args - args[0]是准备执行的SQL语句，后面的参数是sql所包含的参数值列表。
	 * 				 样例：select('select * from tb_student where studentId=?', '888')。
	 * @return ResultSet - 记录集。
	 */
	public ResultSet select (String... args) {				//使用了可变参数（参数个数大于或等于0）
		rs = null;
		
		try {				
			if (args == null || args.length == 0) {			//无参数
				return rs;
			}
			
			String sql = args[0];							//第1个参数是SQL语句

			if (conn == null || conn.isClosed()) {			//如果尚未创建连接，或连接已关闭
				conn = getConnection(); 					//连接数据库
			}	
			
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
															//预编译的SQL执行接口对象，数据游标只能向前移动，只读。效率更高
			
			for (int i = 1; i < args.length; i++) {			//从第2个参数开始
				pstmt.setObject(i, args[i]);				//从第1个占位符?开始，将占位符?替换成参数值
			}
			
			rs = pstmt.executeQuery();        				//执行查询。SQL语句已在前面合成
			
			if (canShowSql) {								//是否输出最终执行的SQL语句
				sql = pstmt.toString().substring(pstmt.toString().indexOf(":") + 2);
				System.out.println("查询：" + sql);
			}

		} catch (Exception e) {
			close();										//出错则关闭连接
			msg = "\n查询数据失败: " + e.getMessage() + "（可变参数）\n";
			System.err.println(msg);
		}

		return rs; 											//返回记录集。即使没查到数据，也不是null。当出错时才会是null
	}

	/**
	 * 更新数据。 已经调用了db.close()。
	 * @param args - args[0]是准备执行的SQL语句，后面的参数是sql所包含的参数值列表。
	 * 				  样例：update('update tb_student set studentName=? where studentId=?', '张三', '888')。
	 * @return int - 被更新的记录行数。
	 */
	public int update (String... args) {
		return executeUpdate(args); 						//返回被更新的记录行数。返回0表示没有记录被更新
	}

	/**
	 * 删除数据。 已经调用了db.close()。
	 * @param args - args[0]是准备执行的SQL语句，后面的参数是sql所包含的参数值列表。
	 * 				  样例：delete('delete from tb_student where studentId=?', '888')。
	 * @return int - 被删除的记录行数。
	 */
	public int delete (String... args) {
		return executeUpdate(args); 						//返回被删除的记录行数。返回0表示没有记录被删除
	}
	
	/**
	 * 执行数据更新或删除。已经调用了db.close()。
	 * @param args - args[0]是准备执行的SQL语句，后面的参数是sql所包含的参数值列表。
	 * @return int - 被更新或删除的记录行数。
	 */
	private int executeUpdate (String... args) {			//**此方法是private类型的，只供在类内部调用
		int count = 0;

		try {			
			if (args == null || args.length == 0) {			//无参数
				return count;
			}
			
			String sql = args[0];							//第1个参数是SQL语句
			
			if (conn == null || conn.isClosed()) {			//如果尚未创建连接，或连接已关闭
				conn = getConnection(); 					//连接数据库
			}	
			
			pstmt = conn.prepareStatement(sql);				//预编译的SQL执行接口对象
			
			for (int i = 1; i < args.length; i++) {			//从第2个参数开始
				pstmt.setObject(i, args[i]);				//从第1个占位符？开始，将占位符?替换成参数值
			}
			
			count = pstmt.executeUpdate(); 					//执行数据更新或删除，得到被影响的记录行数	
			
			if (canShowSql) {								//是否输出最终执行的SQL语句
				sql = pstmt.toString().substring(pstmt.toString().indexOf(":") + 2);
				System.out.println("更新或删除：" + sql);
			}
			
		} catch (Exception e) {
			msg = "\n数据更新或删除失败: " + e.getMessage() + "\n";
			System.err.println(msg);
		} finally {
			close();										//无论成功与否，都关闭连接
		}

		return count; 										//返回被影响的记录数。如果没有记录被更新或删除则，则为默认的0
	}
	
	/**
	 * 插入数据。 已经调用了db.close()。
	 * @param args - args[0]是准备执行的SQL语句，后面的参数是sql所包含的参数值列表。
	 * 				  样例：insert('insert into tb_student set (studentNo, studentName) values (?, ?)', '006', '张三')。
	 * @return String - 新添记录的id。如果返回"0"则表示新添记录失败。
	 */
	public String insert (String... args) {
		String id = "0";
		int count = 0;

		try {			
			if (args == null || args.length == 0) {			//无参数
				return id;
			}
			
			String sql = args[0];							//第1个参数是SQL语句
			
			if (conn == null || conn.isClosed()) {			//如果尚未创建连接，或连接已关闭
				conn = getConnection(); 					//连接数据库
			}	
			
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
															//预编译的SQL执行接口对象。加第2个参数是为了获得新记录的id
			
			for (int i = 1; i < args.length; i++) {			//从第2个参数开始
				pstmt.setObject(i, args[i]);				//从第1个占位符？开始，将占位符?替换成参数值
			}
			
			count = pstmt.executeUpdate();					//执行数据插入，得到被影响的记录数
			
			if (canShowSql) {								//是否输出最终执行的SQL语句
				sql = pstmt.toString().substring(pstmt.toString().indexOf(":") + 2);	
				System.out.println("数据插入：" + sql);
			}
			
			if (count == 0)	{								//新添记录失败
				return id;									//返回"0"
			}

			rs = pstmt.getGeneratedKeys();					//获取新添记录的id列（id需为自动增长，且用同一个pstmt）
			
			if (rs == null) {
				return id;									//返回"0"
			}
			
			if (rs.next()) {								//如果读取下一条记录成功
				id = rs.getString(1);						//取第1个查询字段的值，即新添记录的id
			}
			
		} catch (Exception e) {
			msg = "\n数据新添失败: " + e.getMessage() + "\n";
			System.err.println(msg);
		} finally {
			close();										//无论成功与否，都关闭连接	
		}

		return id; 											//新添记录的id
	}

	/**
	 * 关闭数据库连接。在页面代码中，当执行了查询select()且rs != null时，才需要调用此方法db.close()。
	 */
	public void close() {
		if (rs != null) {									//注意关闭顺序：rs，stmt/pstmt，conn
			try {
				if (rs.isClosed() == false)	{				//如果尚未关闭
					rs.close();
				}
			} catch (Exception e) {
				msg = "\n关闭记录集rs失败: " + e.getMessage() + "\n";
				System.err.println(msg);
			}
		}
		if (stmt != null) {
			try {
				if (stmt.isClosed() == false) {
					stmt.close();
				}
			} catch (Exception e) {
				msg = "\n关闭SQL执行接口对象stmt失败: " + e.getMessage() + "\n";
				System.err.println(msg);
			}
		}
		if (pstmt != null) {
			try {
				if (pstmt.isClosed() == false) {
					pstmt.close();
				}
			} catch (Exception e) {
				msg = "\n关闭预编译的SQL执行接口对象pstmt失败: " + e.getMessage() + "\n";
				System.err.println(msg);
			}
		}
		if (conn != null) {
			try {
				if (conn.isClosed() == false) {
					conn.close();
				}
			} catch (Exception e) {
				msg = "\n关闭数据库连接conn失败: " + e.getMessage() + "\n";
				System.err.println(msg);
			}
		}
	}

	public void setCanShowSql (boolean canShowSql) {			//更改canShowSql为true后，能输出SQL语句
		this.canShowSql = canShowSql;
	}
}
