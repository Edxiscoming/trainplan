package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.railway.com.trainplan.entity.CmdInfoModel;

import commonToolKit.ToolKit.CommonTools;

/**
 * 静态常量工具
 * @author ITC
 *
 */
public class ConstantUtil {

	/**
	 * 开行规律描述常量
	 */
	public static String RUN_RULES_EVERY_DAY = "每日";
	public static String RUN_RULES_INTERVAL_DAY = "隔日";
	public static String RUN_RULES_NULL = "";
	
	/**
	 * 高铁交路计划创建来源
	 */
	public static String HIGHLINE_CROSS_CREAT_REASON_BASEMAP = "基本图";
	
	/**
	 * 包括既有和高铁所有加开停运命令
	 */
	public static String ALL_CMD_NAME = "所有";
	
	/**
	 * 既有加开停运
	 */
	public static String JY_ADD_CMD_NAME = "既有加开命令";
	public static String JY_STOP_CMD_NAME = "既有停运命令";
	
	/**
	 * 高铁加开停运
	 */
	public static String GT_ADD_CMD_NAME = "高铁加开命令";
	public static String GT_STOP_CMD_NAME = "高铁停运命令";
	
	/**
	 * 文电加开停运
	 */
	public static String WD_ADD_CMD_NAME = "文电加开";
	public static String WD_STOP_CMD_NAME = "文电停运";
	
	/**
	 * 高铁日计划命令中文名
	 */
	public static String PLAN_CMD_NAME = "日计划命令";
	/**
	 * 既有线临客加开命令类型
	 */
	public static String GT_PLAN_CMD_TYPE = "LJ_DCD_DCKXJHML";
	/**
	 * 普速日计划命令中文名
	 */
	public static String JY_PLAN_CMD_NAME = "普速日计划命令";
	/**
	 * 普速日计划命令类型
	 */
	public static String JY_PLAN_CMD_TYPE = "LJ_KD_KDKXJHML";
	/**
	 * 既有线临客加开命令类型
	 */
	public static String JY_ADD_CMD_TYPE = "LJ_KD_KDLKJKML";
	/**
	 * 既有线临客停运命令类型
	 */
	public static String JY_STOP_CMD_TYPE = "LJ_KD_KDLKTYML";

	/**
	 * 高铁临客加开命令类型
	 */
	public static String GT_ADD_CMD_TYPE = "LJ_DCD_DCJKML";
	/**
	 * 高铁临客停运命令类型
	 */
	public static String GT_STOP_CMD_TYPE = "LJ_DCD_DCTYML";

	/**
	 * 既有加开文电类型
	 */
	public static String JY_ADD_TELEGRAPH_TYPE = "JY_ADD_TELEGRAPH";
	/**
	 * 既有文电停运类型
	 */
	public static String JY_STOP_TELEGRAPH_TYPE = "JY_STOP_TELEGRAPH";
	/**
	 * 高铁加开文电类型
	 */
	public static String GT_ADD_TELEGRAPH_TYPE = "GT_ADD_TELEGRAPH";
	/**
	 * 高铁文电停运类型
	 */
	public static String GT_STOP_TELEGRAPH_TYPE = "GT_STOP_TELEGRAPH";
	
	/**
	 * 列车开行计划数据来源--命令生成
	 */
	public static int PLAN_TRAIN_CREATE_TYPE_CMD = 3;
	/**
	 * 列车开行计划数据来源--文件电报生成
	 */
	public static int PLAN_TRAIN_CREATE_TYPE_TELEGRAPH = 2;
	
	/**
	 * 交路名中的间隔符号
	 */
	public static String CROSSNAME_TOKEN = "-";
	/**
	 * 调度命令子表content19内容，代表重点揭示命令
	 */
	public static String IMPORT_CMD_STRING = "zdjs";
	
	/**
	 * 临客加开-时刻信息中客运标记
	 */
	public static String START_STATION_FLAG = "<始发>";
	public static String END_STATION_FLAG = "<终到>";
	public static String KY_STATION_FLAG = "<客运营业>";
	public static String KY_STATION_FLAG_NO = "客运营业";
	
	/**
	 * 包括既有和高铁所有加开停运命令
	 */

	/**
	 * 既有加开停运文电
	 */
	public static String JY_ADD_TELEGRAPH_NAME = "既有加开文电";
	public static String JY_STOP_TELEGRAPH_NAME = "既有停运文电";
	
	/**
	 * 高铁加开停运文电
	 */
	public static String GT_ADD_TELEGRAPH_NAME = "高铁加开文电";
	public static String GT_STOP_TELEGRAPH_NAME = "高铁停运文电";

	/**
	 * 高铁日计划命令中文名
	 */
	public static String GT_PLAN_CMD_NAME = "高铁日计划命令";

	
	
	/**
	 * 路局动车调度角色名称
	 */
	public static String CYCLE_POST_ROLES = "铁路局动车调"; 
	
	
	/**
	 * 根据命令内容对象计算日期
	 * 
	 * @param model
	 * @return
	 */
	public static List<String> getSelectedDateStrList(CmdInfoModel model) {
		
		List<String> dateStrList = new ArrayList<String>();
		
		// 已选择日期--加开命令、加开文电、停运文电：择日； 停运命令：停运日期
		//
		String selectedDateStr = model.getSelectedDate();
		
		// 停运命令择日特殊处理
		if (!isStringValueNull(model.getCmdType())
				&& (model.getCmdType().equals(JY_STOP_CMD_NAME) || model
						.getCmdType().equals(GT_STOP_CMD_NAME))) {
			
			if (selectedDateStr != null) {
				
				selectedDateStr = CommonTools.getDateRule(selectedDateStr);
				dateStrList = StringAndTimeUtil.getSplitedString(
						selectedDateStr, ",");
			}
			
		} else {
			// 择日处理
			// 已选择日期不为空
			if (!isStringValueNull(selectedDateStr)) {
				selectedDateStr = CommonTools.getDateRule(selectedDateStr
						.trim());
				dateStrList = StringAndTimeUtil.getSplitedString(
						selectedDateStr, ",");
				
			} else {
				// 根据开行规律判断开行日期
				int runRules = getRunRulesIntegerValues(model.getRule());
				// 隔日标记
				boolean intervalFlag = false;
				switch (runRules) {
				case 0:
				case 1:
					break;
				case 2:
					intervalFlag = true;
					break;
				}
				
				Date sourceDate = StringAndTimeUtil.computerIntervalDate(
						model.getStartDate(), 0);
				Date targetDate = StringAndTimeUtil.computerIntervalDate(
						model.getEndDate(), 0);
				;
				;
				Calendar calTemp = Calendar.getInstance();
				calTemp.setTime(sourceDate);
				
				// 日期顺序
				int index = 1;
				while (!calTemp.getTime().after(targetDate)) {
					
					if (intervalFlag && index++ % 2 == 0) {
						calTemp.add(Calendar.DAY_OF_MONTH, 1);
						continue;
					}
					
					dateStrList.add(StringAndTimeUtil.yearMonthDaySdf.format(calTemp.getTime()));
					calTemp.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
		}
		return dateStrList;
	}

	
	/**
	 * 根据命令内容对象计算日期
	 * @param model
	 * @return
	 */
	public static List<Date> getSelectedDateList(CmdInfoModel model){
		
		List<Date> dateList = new ArrayList<Date>();
		
		// 已选择日期--加开命令：择日； 停运命令--停运日期
		String selectedDateStr = model.getSelectedDate();
		/**
		 *  临客加开命令处理
		 */
		if(model.getCmdType().equals(JY_ADD_CMD_NAME) || model.getCmdType().equals(GT_ADD_TELEGRAPH_NAME)){
			
			
			
			List<String> dateStrList = null;
			if (selectedDateStr != null && !selectedDateStr.trim().equalsIgnoreCase("null") && ! selectedDateStr.trim().equals("")) {
				selectedDateStr = CommonTools.getDateRule(selectedDateStr.trim());
				dateStrList = StringAndTimeUtil.getSplitedString(selectedDateStr,
						",");

				for (String dateStr : dateStrList) {
					Date date = StringAndTimeUtil.getDateByStringText(dateStr,
							StringAndTimeUtil.yearMonthDaySdf);
					dateList.add(date);
				}
			} else {
				// 根据开行规律判断开行日期
				int runRules = getRunRulesIntegerValues(model.getRule());
				// 隔日标记
				boolean intervalFlag = false;
				switch (runRules) {
				case 0:
				case 1:
					break;
				case 2:
					intervalFlag = true;
					break;
				}

				Date sourceDate = StringAndTimeUtil.computerIntervalDate(model.getStartDate(), 0);
				Date targetDate = StringAndTimeUtil.computerIntervalDate(model.getEndDate(), 0);;
				;
				Calendar calTemp = Calendar.getInstance();
				calTemp.setTime(sourceDate);

				// 日期顺序
				int index = 1;
				while (!calTemp.getTime().after(targetDate)) {

					if (intervalFlag && index++ % 2 == 0) {
						calTemp.add(Calendar.DAY_OF_MONTH, 1);
						continue;
					}

					dateList.add(calTemp.getTime());
					calTemp.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
		}
		else if(model.getCmdType().equals(JY_ADD_TELEGRAPH_NAME) || model.getCmdType().equals(GT_ADD_CMD_NAME)){
			
			
			
			List<String> dateStrList = null;
			if (selectedDateStr != null && !selectedDateStr.trim().equalsIgnoreCase("null") && ! selectedDateStr.trim().equals("")) {
				selectedDateStr = CommonTools.getDateRule(selectedDateStr.trim());
				dateStrList = StringAndTimeUtil.getSplitedString(selectedDateStr,
						",");
				
				for (String dateStr : dateStrList) {
					Date date = StringAndTimeUtil.getDateByStringText(dateStr,
							StringAndTimeUtil.yearMonthDaySdf);
					dateList.add(date);
				}
			} else {
				// 根据开行规律判断开行日期
				int runRules = getRunRulesIntegerValues(model.getRule());
				// 隔日标记
				boolean intervalFlag = false;
				switch (runRules) {
				case 0:
				case 1:
					break;
				case 2:
					intervalFlag = true;
					break;
				}
				
				Date sourceDate = StringAndTimeUtil.computerIntervalDate(model.getStartDate(), 0);
				Date targetDate = StringAndTimeUtil.computerIntervalDate(model.getEndDate(), 0);;
				;
				Calendar calTemp = Calendar.getInstance();
				calTemp.setTime(sourceDate);
				
				// 日期顺序
				int index = 1;
				while (!calTemp.getTime().after(targetDate)) {
					
					if (intervalFlag && index++ % 2 == 0) {
						calTemp.add(Calendar.DAY_OF_MONTH, 1);
						continue;
					}
					
					dateList.add(calTemp.getTime());
					calTemp.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
		}
		else{
			/**
			 *  临客加开命令处理
			 */
			if (selectedDateStr != null) {
				List<String> dateStrList = null;
				
				selectedDateStr = CommonTools.getDateRule(selectedDateStr);
				dateStrList = StringAndTimeUtil.getSplitedString(selectedDateStr,
						",");

				for (String dateStr : dateStrList) {
					Date date = StringAndTimeUtil.getDateByStringText(dateStr,
							StringAndTimeUtil.yearMonthDaySdf);
					dateList.add(date);
				}
			} 
		}
		return dateList;
	}
	
	/**
	 * 根据开行规律字符串返回数值
	 * @param runRules
	 * @return
	 */
	public static int getRunRulesIntegerValues(String runRules){
		
		if(runRules.equals(RUN_RULES_NULL))
			return 0;
		else if(runRules.equals(RUN_RULES_EVERY_DAY))
			return 1;
		else 
			return 2;
	}
	/**
	 * 判断字符串是否值为空
	 * 
	 * @param tempStr
	 * @return true 值为空；false 值不为空
	 */
	public static boolean isStringValueNull(String tempStr) {
		boolean valueNull = true;
		if (tempStr != null && !tempStr.trim().equalsIgnoreCase("null")
				&& !tempStr.trim().equals("")) {
			valueNull = false;
		}
		return valueNull;
	}
	/**
	 * 根据日期创集合拼接日期字符串
	 * 
	 * @param dateStrList
	 * @return
	 */
	public static String getSelectedDateStrListToString(List<String> dateStrList) {
		if (dateStrList.size() > 0) {
			StringBuffer bf = new StringBuffer();
			int index = 0;
			for (String str : dateStrList) {
				if (index++ > 0)
					bf.append(",");
				bf.append(str);
			}
			return CommonTools.getDateSimple(bf.toString());
		}
		return "";
	}
}