package org.railway.com.trainplan.jdbcConnection;

/**
 * 数据库配置对象
 * @author ITC
 *
 */
public class ConfigDataBase {
	
	private String bureau;
	private String driver;
	private String url;
	private String user;
	private String password;
	private String planCmdPostName;
	
	
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getUrl() {
		return url;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlanCmdPostName() {
		return planCmdPostName;
	}
	public void setPlanCmdPostName(String planCmdPostName) {
		this.planCmdPostName = planCmdPostName;
	}

}
