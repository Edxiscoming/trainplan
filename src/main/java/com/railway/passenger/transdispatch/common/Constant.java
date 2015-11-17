package com.railway.passenger.transdispatch.common;

public class Constant {
	public static final int CHECK_FLAG_PASS = 1;	//审核状态：已通过
	public static final int CHECK_FLAG_RETURN = 0;	//审核状态：未审核
	public static final int USE_STATUS_NORMAL = 0;	//使用状态：正常
	public static final int USE_STATUS_DELETE = 1;	//使用状态：删除
	public static final int USE_STATUS_UNLOAD = 2;	//使用状态：未入库
	public static final int CREATE_CROSS_YES = 1;	//生成交路状态：已生成
	public static final int CREATE_CROSS_NO = 0;	//生成交路状态：未生成
	public static final String HIGHLINE_FLAG_COMMON = "0";	//高线标志：普通
	public static final String HIGHLINE_FLAG_HIGHLINE = "1";	//高线标志：高线
	
	
	//******************************* 异常标志 ***********************************//
	public static final int EXCEPTION_FLAG_ERROR = -1;	//异常
	public static final int EXCEPTION_FLAG_NORMAL = 0;	//正常
	
	//******************************* 自动设置标志 ***********************************//
		public static final int AUTO_FLAG_BATCH = 1;	//批量
		public static final int AUTO_FLAG_MANUAL = 2;	//手动
	
	//******************************* 开行规律 ***********************************//
	
	public static final String COMMONLINE_RULE_EVERYDAY = "1";	//普通规律：每日
	public static final String COMMONLINE_RULE_SUBDAY = "2";	//普通规律：隔日
	
	//******************************* 开行状态 ***********************************//
	
	public static final String SPARE_FLAG_RUN = "1";	//开行状态：开行
	public static final String SPARE_FLAG_SPARE = "2";	//开行状态：备用
	public static final String SPARE_FLAG_STOP = "0";	//开行状态：停用
	
	//******************************* 错误码返回值 ***********************************//
	
	public static final int RETURN_CODE_ERROR = -1;	//错误
	public static final int RETURN_CODE_SUCCESS = 0;	//成功
	public static final int RETURN_CODE_EXIST = 1;	//已存在
	public static final int RETURN_CODE_WARNING = 2;	//警告提示
}
