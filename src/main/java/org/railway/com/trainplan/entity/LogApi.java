package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应log_api.
 * 
 * @author zhangPengDong
 *
 *         2015年7月7日 下午3:53:46
 */
public class LogApi implements Serializable {
	private static final long serialVersionUID = 4256504879521207814L;

	/**
	 * 
	 */
	private String id;
	/**
	 * 接口类型.
	 */
	private String api_type;
	/**
	 * 
	 */
	private String create_time;
	/**
	 * 接口用户.
	 */
	private String api_user;
	/**
	 * 接口ip.
	 */
	private String api_ip;
	/**
	 * 接口json内容.
	 */
	private String api_json;
	/**
	 * 返回状态.
	 */
	private String api_status;
	/**
	 * 返回内容.
	 */
	private String api_msg;
	private String api_time;
	private Date update_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApi_type() {
		return api_type;
	}

	public void setApi_type(String api_type) {
		this.api_type = api_type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getApi_user() {
		return api_user;
	}

	public void setApi_user(String api_user) {
		this.api_user = api_user;
	}

	public String getApi_json() {
		return api_json;
	}

	public void setApi_json(String api_json) {
		this.api_json = api_json;
	}

	public String getApi_status() {
		return api_status;
	}

	public void setApi_status(String api_status) {
		this.api_status = api_status;
	}

	public String getApi_ip() {
		return api_ip;
	}

	public void setApi_ip(String api_ip) {
		this.api_ip = api_ip;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public void setApi_time(String api_time) {
		this.api_time = api_time;
	}

	public String getApi_time() {
		return api_time;
	}

	public String getApi_msg() {
		return api_msg;
	}

	public void setApi_msg(String api_msg) {
		this.api_msg = api_msg;
	}

}