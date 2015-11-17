package org.railway.com.trainplan.common.constants;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.Validate;

/**
 * 定义返回报文中常用的返回码
 * @author join
 *
 */
public enum StaticCodeType {
	SYSTEM_SUCCESS				("0","成功"),
	SYSTEM_ERR_YS				("1","失败，提示框显示橙色"),
	SYSTEM_ERROR				("M01","系统错误"),
	SYSTEM_DATA_FORMAT_ERROR	("M02","不是合法的JSON数据"),
	SYSTEM_PARAM_LOST			("M03","缺少必要的请求参数"),
	SYSTEM_PARAM_TYPE_ERROR		("M04","请求参数类型错误"),
	SYSTEM_DATA_ISNULL  		("M05","获取数据为空"),
	SYSTEM_SIGN_EQUALS  		("M06","Sign不匹配"),
	SYSTEM_USER_ISNULL  		("M07","查询不到用户"),
	
	/** 关于数据 **/
	SYSTEM_DATA_BUFEN  			("M11","成功修改部分数据"),
	SYSTEM_DATA_STATUS_ERROR  	("M12","数据状态错误"),
//	SYSTEM_DATA__ERROR  	("M13","数据内容有误，请检查您所选中的数据"),
	SYSTEM_DATA__ERROR  	("M13","对数表交路中的车次与基本图中的车次不一致"),
	SYSTEM_DATA__IS_EXIST  	("M14","数据已存在，请勿重复添加"),
	
	/** 消息 **/
	MSG_ERROR  	("M20","消息储存失败!");
	
	private String code;
	private String description;
	
	
	private static final Map<String, StaticCodeType> DICT = new HashMap<String, StaticCodeType>();
	private static final Map<String, StaticCodeType> DICT2 = new HashMap<String, StaticCodeType>();
	
	
	static {
		for (StaticCodeType item : values()) {
			DICT.put(item.code, item);
			DICT2.put(item.description, item);
			
		}
	}
	
	private StaticCodeType(String code,String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}
	
	public String getDescription(){
		return description;
	}
	
	public static StaticCodeType parseCode(String code){
		try {
			
			StaticCodeType found = DICT.get(code);
			Validate.notNull(found, "The value of the role is null");
			return found;

		} catch (NullPointerException ex) {
			
			return null;
		}
		
	}
	
	public static StaticCodeType parseDescription(String description){
		try {
			StaticCodeType found = DICT2.get(description);
			Validate.notNull(found, "The value of the role is null");
			return found;

		} catch (NullPointerException ex) {
			
			return null;
		}
		
	}
}
