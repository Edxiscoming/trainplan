package org.railway.com.trainplan.jdbcConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import org.railway.com.trainplan.jdbcConnection.JDomUtil;;
/**
 * 数据库操作类
 * 
 * @author ITC
 * 
 */
public class DbUtilDdml {

	private DbUtilDdml() {

	}
	private static DbUtilDdml instance;

	public static DbUtilDdml getInstance() {
		if (instance == null){
			instance = new DbUtilDdml();
		}
		return instance;
	}

	/**
	 * 数据库参数
	 */
	public static String DRIVER = "";
	public static String DB_URL = "";
	public static String USER = "";
	public static String PASSWORD = "";
	public static HashMap<String, ConfigDataBase> dbCofigMap;
	
	static {
		dbCofigMap = JDomUtil.parserXml();
	}
	
	private Connection connection;
	private PreparedStatement ps;
	
	public void init(String bureau){

		ConfigDataBase dataBase = null;
		dataBase = dbCofigMap.get(bureau);
		if(dataBase == null){
			DRIVER = "";
			DB_URL = "";
			USER = "";
			PASSWORD = "";
		}else{
			DRIVER = dataBase.getDriver();
			DB_URL = dataBase.getUrl();
			USER = dataBase.getUser();
			PASSWORD = dataBase.getPassword();
		}
	}

	/**
	 * 获取连接对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {

		String message = "";
		if (connection == null) {
			Class.forName(DRIVER);
			java.util.Properties p = new java.util.Properties();
			p.put("user", USER);
			p.put("password", PASSWORD);
			connection = java.sql.DriverManager.getConnection(DB_URL, p);
			message = "dabaBase connect sucess!";
			// 20150202 调试临客命令查不到情况。何宇阳
//			JDomUtil.writeFile(message, "/lkmllog/", "sql.txt");
		}
		
		return connection;
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeConnection() {
		if (connection == null) {
			return;
		}
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			connection = null;
		}
		connection = null;
	}
	
	/**
	 * 关闭PrepareStatement
	 */
	public void clossPrepareStatement(){
		if(ps == null){
			return;
		}
		try{
			if(!ps.isClosed()) {
				ps.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			ps = null;
		}
		ps = null;
	}

	/**
	 * 执行查询操作
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @param parvalue
	 *            参数值
	 * @return 数据集合
	 * @throws Exception
	 *             执行过程中抛出的异常
	 */
	public ResultSet executeQuery(String sql, Object... parvalue)
			throws Exception {
		
		ps = getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		if (parvalue != null && parvalue.length > 0) {
			int index = 1;
			for (Object value : parvalue) {
				ps.setObject(index++, value);
			}
		}
		
		return ps.executeQuery();
	}

	/**
	 * 执行更新操作
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @param parvalue
	 *            参数值
	 * @return 是否执行成功
	 * @throws Exception
	 *             执行过程中抛出的异常
	 */
	public int executeUpdate(String sql, Object... parvalue) throws Exception {

		ps = getConnection().prepareStatement(sql);
		if (parvalue != null && parvalue.length > 0) {
			int index = 1;
			for (Object value : parvalue) {
				ps.setObject(index++, value);
			}
		}
		
		return ps.executeUpdate();
	}
	
	/**
	 * 获取日计划命令岗位名称
	 * @param bureauCode
	 * @return
	 */
	public static String getPlanCmdPostName(String bureauCode){
		String postName = "";
		ConfigDataBase configDb = dbCofigMap.get(bureauCode);
		postName = configDb == null ? "" : configDb.getPlanCmdPostName();
		return postName;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		DbUtilDdml util = DbUtilDdml.getInstance();

		Iterator<String> itera = DbUtilDdml.dbCofigMap.keySet().iterator();
		while(itera.hasNext()){
			String ljpym = itera.next();
			util.init(ljpym);
			if(DbUtilDdml.DB_URL.equals(""))
				continue;
			try {
				Connection con = util.getConnection();
				if (util.executeQuery("select * from dual", null) == null)
					System.out.println(ljpym + " query failed!");
				else
					System.out.println(ljpym + " query sucess");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				util.closeConnection();
			}
		}
		
		
	}
}
