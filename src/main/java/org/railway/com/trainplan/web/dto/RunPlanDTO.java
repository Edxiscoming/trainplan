package org.railway.com.trainplan.web.dto;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PlanTrainDTO2;
import org.railway.com.trainplan.service.dto.PlanTrainStnDto;

/**
 * Created by star on 5/12/14. 审核界面-客运开行计划dto
 */
public class RunPlanDTO {
	
	public String getCheckedBureau() {
		return checkedBureau;
	}

	public void setCheckedBureau(String checkedBureau) {
		this.checkedBureau = checkedBureau;
	}

	public RunPlanDTO(){
		super();
	}
	

	// 列车ID
	private String id;

	// 车次
	private String serial = "";

	// 始发站

	// 终到站

	// private String startTime = "";

	// private String endTime = "";

	// 开行日期 MM-dd
	private String runDate = "";

	// 命令号
	private String command = "";

	// 文电号
	private String tele = "";

	// 上图标记 0: yes, 1: no
	private int dailyLineFlag;

	// 上图时间 MM-dd hh:mm
	private String dailyLineTime = "";

	// 来源类型
	private String sourceType = "";

	// 备用及停运标记
	private String spareFlag = "";

	// 线路类型
	private String highLineFlag = "";

	// 一级审核状态
	private int checkLev1;

	// 二级审核状态
	private int checkLev2;

	// 生成运行线id
	private String dailyLineId;

	// 是否一级审核
	private int lev1Checked;

	// 是否二级审核
	private int lev2Checked;
	private int lev2Checked2;

	// 列车类型（始发终到、始发交出、接入终到、接入交出）
	private String trainType;
	private String planTrainId;// ID;
	private String runType;// 运行方式 (SFJC：始发交出 JRZD：接入终到 JRJC：接入交出 SFZD：始发终到)
	private String trainNbr;// 车次;
	private String startStn;// 始发站;
	private String startTime;// 始发时间yyyy-MM-dd HH:mm:ss
	private String startTimeAll;
	private String jrStn;// 接入站;
	private String jrTime;// 接入时间yyyy-MM-dd HH:mm:ss
	private String jcStn;// 接出站;
	private String jcTime;// 接出时间yyyy-MM-dd HH:mm:ss
	private String endStn;// 终到站;
	private String endTime;// 终到时间yyyy-MM-dd HH:mm:ss
	private String endTimeAll;
	private String passBureau;// 途经局（按顺序列出途经路局简称）
	private String checkedBureau;// 落成计划审核局（P，V）

	private String isValid = "0";// 默认该数据有效

	private String creatType;
	
	private int vaildStatus;
	/**
	 * 运行线审核局（简称）.
	 */
	private String dailyplan_check_bureau;
	
	/**
	 * 备用参数.
	 */
	private String str1;

	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public String getStartStn() {
		return startStn;
	}

	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}

	public String getJrStn() {
		return jrStn;
	}

	public void setJrStn(String jrStn) {
		this.jrStn = jrStn;
	}

	public String getJrTime() {
		return jrTime;
	}

	public void setJrTime(String jrTime) {
		this.jrTime = jrTime;
	}

	public String getJcStn() {
		return jcStn;
	}

	public void setJcStn(String jcStn) {
		this.jcStn = jcStn;
	}

	public String getJcTime() {
		return jcTime;
	}

	public void setJcTime(String jcTime) {
		this.jcTime = jcTime;
	}

	public String getEndStn() {
		return endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	public String getPassBureau() {
		return passBureau;
	}

	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}

	/*
	 * //列车id private String planTrainId;//ID; private String runType;//运行方式
	 * (SFJC：始发交出 JRZD：接入终到 JRJC：接入交出 SFZD：始发终到) private String trainNbr;//车次;
	 * private String startStn;//始发站; private String startTime;//始发时间yyyy-MM-dd
	 * HH:mm:ss private String jrStn;//接入站; private String
	 * jrTime;//接入时间yyyy-MM-dd HH:mm:ss private String jcStn;//接出站; private
	 * String jcTime;//接出时间yyyy-MM-dd HH:mm:ss private String endStn;//终到站;
	 * private String endTime;//终到时间yyyy-MM-dd HH:mm:ss private String
	 * endDays;//终到运行天数 private String passBureau;//途经局（按顺序列出途经路局简称）
	 * 
	 * //以下为非界面显示元素 private String runDate;//开行日期 private String
	 * startBureau;//始发局简称 private String endBureau;//终到局简称 private String
	 * planCrossId;//交路ID（对应PLAN_CROSS中的交路ID） private String
	 * creatType;//"创建方式  TD（0:基本图；1:基本图滚动)；LK(2:文件电报；3:命令）" private int
	 * fjkCount; //列车经由点单 分界口总个数
	 */
	// TODO constructer
	public RunPlanDTO(PlanTrainDTO2 train,
			List<PlanTrainStnDto> planTrainStnList, String date) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		this.passBureau = train.getPassBureau();
		this.dailyplan_check_bureau = train.getDailyplan_check_bureau();
		this.str1 = train.getStr1();
		this.id = train.getPlanTrainId();
		this.serial = train.getTrainNbr();// 车次
		this.sourceType = train.getCreatType(); // 来源
		this.spareFlag = train.getSpareFlag();// 开行状态
		this.trainType = train.getRunType();// 始发交出
		this.creatType = train.getCreatType();//基本图
		// 4个站
		this.startStn = train.getStartStn();// 始发站
		this.startTime = DateUtil.parseDateTOmmddhhmm(train.getStartTime());
		this.startTimeAll = train.getStartTime();
		this.endTimeAll = train.getEndTime();
		this.dailyLineFlag = Integer.parseInt(train.getDailyLineFlag());
		this.dailyLineId = train.getDailyLineId();
		this.lev1Checked = Integer.parseInt(train.getCheckLev1());
		this.lev2Checked = Integer.parseInt(train.getCheckLev2());
		this.lev2Checked2 = Integer.parseInt(train.getCheckLev2());
		this.vaildStatus = train.getVaildStatus();
		
		this.checkedBureau = "";
		if(train.getCheckedBureau()!=null && !train.getCheckedBureau().equals("")){
			String[] checkedBureauArr = train.getCheckedBureau().split(",");
			for (int i = checkedBureauArr.length-1; i>-1 ; i--) {
				checkedBureau = checkedBureau + LjUtil.getLjByNameBs(checkedBureauArr[i], 1);
			}
		}
		// =========接入 交出 start
		// 2.2赋值接入、交出信息
		if ("SFZD".equals(train.getRunTypeCode())) {// 始发终到
			String start = train.getStartTime();
			String end = train.getEndTime();
//			if (start != null && !"".equals(start) && !"null".equals(start)
//					&& end != null && !"".equals(end) && !"null".equals(end)) {
//				if ("1".equals(train.getHighlineFlag())) {
//					this.isValid = "1";
//				} else {
//					if (DateUtil.isBeforeForyyyyMMddHHmmss(start, date
//							+ " 18:00:00")
//							&& DateUtil.isBeforeForyyyyMMddHHmmss(
//									DateUtil.mulDateOneDay(date) + " 18:00:00",
//									end))
//						this.isValid = "1";
//				}
//			}
			
			if (start.toString() != null && !"".equals(start.toString())
					&& !"null".equals(start.toString())) {
				if ("1".equals(train.getHighlineFlag())) {
					if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
							DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
							.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
									start.toString()))) {
						this.isValid = "1";
					}
					
				} else {
					if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
							date + " 18:00:00") && DateUtil
							.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date)
									+ " 18:00:00", start.toString()))) {// 既有线时间分界
																	// 18:00:00
						this.isValid = "1";
					}
					
				}
			}
			// this.fillInfoSFZD(pageRowObj, planTrainStnList,
			// queryMap.get("bureauName"), highLinePlanObj.getFjkCount(),date);
		} else if ("JRZD".equals(train.getRunTypeCode())) {// 接入终到
			// 有接入 没有交出
			this.jcStn = null;
			this.jcTime = null;

			this.fillInfoJRZD(train, planTrainStnList,
					user.getBureauShortName(), date);
		} else if ("SFJC".equals(train.getRunTypeCode())) {// 始发交出
			this.fillInfoSFJC(train, planTrainStnList,
					user.getBureauShortName(), train.getFjkCount(), date);
		} else if ("JRJC".equals(train.getRunTypeCode())) {// 接入交出
			this.fillInfoJRJC(train, planTrainStnList,
					user.getBureauShortName(), train.getFjkCount(), date);
		}

		// =========接入 交出 end

		this.endStn = train.getEndStn();//
		this.endTime = DateUtil.parseDateTOmmddhhmm(train.getEndTime());

		// this.runDate = MapUtils.getString(map, "RUN_DATE");
		/*
		 * this.dailyLineFlag = train.get; this.dailyLineTime =
		 * MapUtils.getString(map, "DAILYPLAN_TIME"); this.highLineFlag =
		 * MapUtils.getString(map, "HIGHLINE_FLAG"); this.checkLev1 =
		 * MapUtils.getIntValue(map, "CHECK_LEV1_TYPE"); this.checkLev2 =
		 * MapUtils.getIntValue(map, "CHECK_LEV2_TYPE"); this.dailyLineId =
		 * MapUtils.getString(map, "DAILYPLAN_ID");
		 */
	}
	public RunPlanDTO(PlanTrainDTO2 train,
			List<PlanTrainStnDto> planTrainStnList, String date,String str) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		this.passBureau = train.getPassBureau();
		this.dailyplan_check_bureau = train.getDailyplan_check_bureau();
		this.str1 = train.getStr1();
		this.id = train.getPlanTrainId();
		this.serial = train.getTrainNbr();// 车次
		this.sourceType = train.getCreatType(); // 来源
		this.spareFlag = train.getSpareFlag();// 开行状态
		this.trainType = train.getRunType();// 始发交出
		this.creatType = train.getCreatType();//基本图
		// 4个站
		this.startStn = train.getStartStn();// 始发站
		this.startTime = DateUtil.parseDateTOmmddhhmm1(train.getStartTime());
		this.startTimeAll = train.getStartTime();
		this.endTimeAll = train.getEndTime();
		this.dailyLineFlag = Integer.parseInt(train.getDailyLineFlag());
		this.dailyLineId = train.getDailyLineId();
		this.lev1Checked = Integer.parseInt(train.getCheckLev1());
		this.lev2Checked = Integer.parseInt(train.getCheckLev2());
		this.lev2Checked2 = Integer.parseInt(train.getCheckLev2());

		// =========接入 交出 start
		// 2.2赋值接入、交出信息
		if ("SFZD".equals(train.getRunTypeCode())) {// 始发终到
			String start = train.getStartTime();
			String end = train.getEndTime();
			//考虑开始时间在查询条件范围内
			if (start.toString() != null && !"".equals(start.toString())
					&& !"null".equals(start.toString())) {
				if ("1".equals(train.getHighlineFlag())) {
					if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
							DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
							.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
									start.toString()))) {
						this.isValid = "1";
					}
					
				} else {
					if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
							date + " 18:00:00") && DateUtil
							.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date)
									+ " 18:00:00", start.toString()))) {// 既有线时间分界
																	// 18:00:00
						this.isValid = "1";
					}
					
				}
			}
			//临客   满足
			
			// this.fillInfoSFZD(pageRowObj, planTrainStnList,
			// queryMap.get("bureauName"), highLinePlanObj.getFjkCount(),date);
		} else if ("JRZD".equals(train.getRunTypeCode())) {// 接入终到
			// 有接入 没有交出
			this.jcStn = null;
			this.jcTime = null;

			this.fillInfoJRZD(train, planTrainStnList,
					user.getBureauShortName(), date);
		} else if ("SFJC".equals(train.getRunTypeCode())) {// 始发交出
			this.fillInfoSFJC(train, planTrainStnList,
					user.getBureauShortName(), train.getFjkCount(), date);
		} else if ("JRJC".equals(train.getRunTypeCode())) {// 接入交出
			this.fillInfoJRJC(train, planTrainStnList,
					user.getBureauShortName(), train.getFjkCount(), date);
		}

		// =========接入 交出 end

		this.endStn = train.getEndStn();//
		this.endTime = DateUtil.parseDateTOmmddhhmm1(train.getEndTime());

		// this.runDate = MapUtils.getString(map, "RUN_DATE");
		/*
		 * this.dailyLineFlag = train.get; this.dailyLineTime =
		 * MapUtils.getString(map, "DAILYPLAN_TIME"); this.highLineFlag =
		 * MapUtils.getString(map, "HIGHLINE_FLAG"); this.checkLev1 =
		 * MapUtils.getIntValue(map, "CHECK_LEV1_TYPE"); this.checkLev2 =
		 * MapUtils.getIntValue(map, "CHECK_LEV2_TYPE"); this.dailyLineId =
		 * MapUtils.getString(map, "DAILYPLAN_ID");
		 */
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getDailyLineFlag() {
		return dailyLineFlag;
	}

	public void setDailyLineFlag(int dailyLineFlag) {
		this.dailyLineFlag = dailyLineFlag;
	}

	public String getDailyLineTime() {
		return dailyLineTime;
	}

	public void setDailyLineTime(String dailyLineTime) {
		this.dailyLineTime = dailyLineTime;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSpareFlag() {
		return spareFlag;
	}

	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}

	public String getHighLineFlag() {
		return highLineFlag;
	}

	public void setHighLineFlag(String highLineFlag) {
		this.highLineFlag = highLineFlag;
	}

	public int getCheckLev1() {

		return checkLev1;
	}

	public void setCheckLev1(int checkLev1) {
		this.checkLev1 = checkLev1;
	}

	public int getCheckLev2() {
		return checkLev2;
	}

	public void setCheckLev2(int checkLev2) {
		this.checkLev2 = checkLev2;
	}

	public String getDailyLineId() {
		return dailyLineId;
	}

	public void setDailyLineId(String dailyLineId) {
		this.dailyLineId = dailyLineId;
	}

	public int getLev1Checked() {
		return lev1Checked;
	}

	public void setLev1Checked(int lev1Checked) {
		this.lev1Checked = lev1Checked;
	}

	public int getLev2Checked() {
		return lev2Checked;
	}

	public void setLev2Checked(int lev2Checked) {
		this.lev2Checked = lev2Checked;
	}

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	/**
	 * 始发终到业务解析规则： otherFjkCount==0 视为管内运行车辆 直接return otherFjkCount>0
	 * other出现的次数序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]；
	 * other出现的次数序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoSFZD(PlanTrainDTO2 pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount, String date) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		if (otherFjkCount == 0) {
			return;
		}

		int otherIndex = 0;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("other".equals(obj.getIsfdz())) {
				otherIndex++;
				if (otherIndex % 2 == 0) {// other出现的次数序号为偶数：接入
					jrStnSbf.append(obj.getStnName()).append("/");
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 接入时间为到达时间
						jrTimeSbf.append(obj.getArrTime()).append("/");
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jrTimeSbf.append(obj.getDptTime()).append("/");
					}
				} else {// other出现的次数序号为奇数：交出
					jcStnSbf.append(obj.getStnName()).append("/");
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 交出时间为出发时间
						jcTimeSbf.append(obj.getDptTime()).append("/");
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jcTimeSbf.append(obj.getArrTime()).append("/");
					}
				}
			}
		}

		
		
		// 解析结束

		if (!"".equals(jrStnSbf.toString())) {
			this.setJrStn(jrStnSbf.substring(0, jrStnSbf.lastIndexOf("/")));
		}
		if (!"".equals(jrTimeSbf.toString())) {
			this.setJrTime(jrTimeSbf.substring(0, jrTimeSbf.lastIndexOf("/")));
		}
		if (!"".equals(jcStnSbf.toString())) {
			this.setJcStn(jcStnSbf.substring(0, jcStnSbf.lastIndexOf("/")));
		}
		if (!"".equals(jcTimeSbf.toString())) {
			this.setJcTime(jcTimeSbf.substring(0, jcTimeSbf.lastIndexOf("/")));
		}
	}

	/**
	 * 始发交出业务解析规则： otherFjkCount%2==0 isFjk==1出现的序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间
	 * 不相等时取出发时间）]； isFjk==1出现的序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * otherFjkCount%2!=0 other出现的次数序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]；
	 * other出现的次数序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoSFJC(PlanTrainDTO2 pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount, String date) {
		// StringBuffer jrStnSbf = new StringBuffer("");//接入站
		// StringBuffer jrTimeSbf = new StringBuffer("");//接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				jcStnSbf = new StringBuffer(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 交出时间为出发时间
					jcTimeSbf = new StringBuffer(obj.getDptTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jcTimeSbf = new StringBuffer(obj.getArrTime());
				}
			}
		}
/*		原来的需求，需要判断fjk站时间
 * 		if (jcTimeSbf.toString() != null && !"".equals(jcTimeSbf.toString())
				&& !"null".equals(jcTimeSbf.toString())) {
			if ("1".equals(pageRowObj.getHighlineFlag())) {// SFJC:比较 date <
															// jcTimeSbf
				// date:2014-01-01
				// jrTimeSbf.toString() 2014-01-01 01:01:01
				// gt OK 判断要求：交出时间大于 2014-01-01 00:00:00 并且小于2014-01-02 00:00:00
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(jcTimeSbf.toString(),
						DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
						.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
								jcTimeSbf.toString()))
						|| (DateUtil.isBeforeForyyyyMMddHHmmss(
								this.startTimeAll, DateUtil.addDateOneDay(date)
										+ " 00:00:00") && DateUtil
								.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
										this.startTimeAll))) {
					this.isValid = "1";
				}
				
			} else {
				// date-1 <jctime <date
				// gt OK 判断要求：交出时间大于 2014-01-01 18:00:00 并且小于2014-01-02 18:00:00
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(jcTimeSbf.toString(),
						date + " 18:00:00") && DateUtil
						.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date)
								+ " 18:00:00", jcTimeSbf.toString()))
						|| (DateUtil.isBeforeForyyyyMMddHHmmss(
								this.startTimeAll, date + " 18:00:00") && DateUtil
								.isBeforeForyyyyMMddHHmmss(
										DateUtil.mulDateOneDay(date)
												+ " 18:00:00",
										this.startTimeAll))) {// 既有线时间分界
																// 18:00:00
					this.isValid = "1";
				}
				
			}
		}else{
			if ("1".equals(pageRowObj.getHighlineFlag())) {// SFJC:比较 date <
				//如果是临客,加入没有fjk，直接显示
				if(!"基本图".equals(pageRowObj.getCreatType())){
						if(DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00", pageRowObj.getStartTime()) &&
								DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , DateUtil.addDateOneDay(date) + " 00:00:00"))
							this.isValid = "1";
				}
			}else{
				//如果是临客,加入没有fjk，直接显示
				if(!"基本图".equals(pageRowObj.getCreatType())){
						if(DateUtil.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date) + " 18:00:00", pageRowObj.getStartTime()) &&
								DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , date + " 18:00:00"))
						this.isValid = "1";
				}
			}
		}
		*/
		
		//新的需求   只需判断始发，接入
		if ("1".equals(pageRowObj.getHighlineFlag())) {// SFJC:比较 date <
			//如果是临客,加入没有fjk，直接显示
			if(DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00", pageRowObj.getStartTime()) &&
					DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , DateUtil.addDateOneDay(date) + " 00:00:00"))
				this.isValid = "1";
		}else{
			//如果是临客,加入没有fjk，直接显示
			if(DateUtil.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date) + " 18:00:00", pageRowObj.getStartTime()) &&
					DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , date + " 18:00:00"))
			this.isValid = "1";
		}
		
		this.setJcStn(jcStnSbf.toString());
		this.setJcTime(DateUtil.parseDateTOmmddhhmm(jcTimeSbf.toString()));
		
		
		
		// 解析结束

		// 以下方式未来可能会启用
		// if (otherFjkCount%2 == 0) {
		// int fjkIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("1".equals(obj.getIsfjk())) {
		// fjkIndex ++;
		// if (fjkIndex%2 == 0) {//fjk出现的序号为偶数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//fjk出现的序号为奇数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// } else {
		// int otherIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("other".equals(obj.getIsfdz())) {
		// otherIndex ++;
		// if (otherIndex%2 == 0) {//other出现的次数序号为偶数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//other出现的次数序号为奇数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// }
		//
		//
		//
		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }
		
	}

	/**
	 * 接入终到业务解析规则： isFjk==1出现的序号最小的为接入（暂时用该方法）
	 * 
	 * // * isFjk==1出现的序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]； // *
	 * isFjk==1出现的序号为偶数且不为发到站：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param this 页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoJRZD(PlanTrainDTO2 pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			String date) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		// StringBuffer jcStnSbf = new StringBuffer("");//交出站
		// StringBuffer jcTimeSbf = new StringBuffer("");//交出时间

		// int fjkIndex = 0;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				jrStnSbf.append(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 接入时间为到达时间
					jrTimeSbf.append(obj.getArrTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jrTimeSbf.append(obj.getDptTime());
				}
				break;

				// 以下方式未来可能会启用
				// fjkIndex ++;
				// if (fjkIndex%2 != 0) {//fjk出现的序号为奇数：接入
				// jrStnSbf.append(obj.getStnName()).append("/");
				// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等
				// 接入时间为到达时间
				// jrTimeSbf.append(obj.getArrTime()).append("/");
				// } else {//车站所属局与查询局相等 接入时间为到达时间
				// jrTimeSbf.append(obj.getDptTime()).append("/");
				// }
				// } else
				// if("other".equals(obj.getIsfdz())){//fjk出现的序号为偶数且不为发到站：交出
				// jcStnSbf.append(obj.getStnName()).append("/");
				// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等
				// 交出时间为出发时间
				// jcTimeSbf.append(obj.getDptTime()).append("/");
				// } else {//车站所属局与查询局相等 接入时间为到达时间
				// jcTimeSbf.append(obj.getArrTime()).append("/");
				// }
				// }
			}
		}
		
		if (jrTimeSbf.toString() != null && !"".equals(jrTimeSbf.toString())
				&& !"null".equals(jrTimeSbf.toString())) {
			if ("1".equals(pageRowObj.getHighlineFlag())) {// JRZD:比较jrTimeSbf <
															// x < endTime
				// date:2014-01-01
				// jrTimeSbf.toString() 2014-01-01 01:01:01
//				if ((DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
//						jrTimeSbf.toString()) && DateUtil
//						.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(),
//								DateUtil.addDateOneDay(date) + " 00:00:00"))
//						|| (DateUtil.isBeforeForyyyyMMddHHmmss(date
//								+ " 00:00:00", this.endTimeAll) && DateUtil
//									.isBeforeForyyyyMMddHHmmss(this.endTimeAll,
//											DateUtil.addDateOneDay(date)
//													+ " 00:00:00"))) {
//					this.isValid = "1";
//				}
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
						jrTimeSbf.toString()) && DateUtil
						.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(),
								DateUtil.addDateOneDay(date) + " 00:00:00"))) {
					this.isValid = "1";
				}
				
			} else {
				// date < jr < date+1
				// ok
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(
						DateUtil.mulDateOneDay(date) + " 18:00:00",
						jrTimeSbf.toString()) && DateUtil
						.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(), date
								+ " 18:00:00"))
								
//						|| (DateUtil.isBeforeForyyyyMMddHHmmss(
//								DateUtil.mulDateOneDay(date) + " 18:00:00",
//								this.endTimeAll) && DateUtil
//								.isBeforeForyyyyMMddHHmmss(this.endTimeAll,
//										date + " 18:00:00"))
										) {// 既有线时间分界
																// 18:00:00
					this.isValid = "1";
				}
				
			}
		}else{
			if ("1".equals(pageRowObj.getHighlineFlag())) {// JRZD:比较jrTimeSbf <
				//如果是临客,加入没有fjk，直接显示
				if(!"基本图".equals(pageRowObj.getCreatType())){
					if(DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getEndTime(), DateUtil.addDateOneDay(date) + " 00:00:00") && 
							DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00", pageRowObj.getEndTime())){
						this.isValid = "1";
					}
				}
			}else{
				//如果是临客,加入没有fjk，直接显示
				if(!"基本图".equals(pageRowObj.getCreatType())){
					if(DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getEndTime(), date + " 18:00:00") && 
							DateUtil.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date) + " 18:00:00", pageRowObj.getEndTime())){
					this.isValid = "1";
					}
				}
			}
		}
		this.setJrStn(jrStnSbf.toString());
		this.setJrTime(DateUtil.parseDateTOmmddhhmm(jrTimeSbf.toString()));
		
		
		// 解析结束

		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }
	
	}

	/**
	 * 接入交出业务解析规则： isFjk==1出现的序号最小的为接入（暂时用该方法） isFjk==1出现的序号最大的为交出（暂时用该方法）
	 * 
	 * // * otherFjkCount==0 始发信息即为接入信息，终到信息即为交出信息 return // * otherFjkCount!=0
	 * // * otherFjkCount%2==0 other出现的次数序号为偶数：交出[交出时间：（车站所属局==查询局 取出发时间
	 * 不相等时取到站时间）]； // * other出现的次数序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]
	 * // * otherFjkCount%2!=0 isFjk==1出现的序号为偶数：交出[交出时间：（车站所属局==查询局 取出发时间
	 * 不相等时取到站时间）]； // * isFjk==1出现的序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoJRJC(PlanTrainDTO2 pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount, String date) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		boolean isFirstFjkBoolean = true;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				if (isFirstFjkBoolean) {
					jrStnSbf.append(obj.getStnName());
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 接入时间为到达时间
						jrTimeSbf.append(obj.getArrTime());
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jrTimeSbf.append(obj.getDptTime());
					}
					isFirstFjkBoolean = false;
				}

				jcStnSbf = new StringBuffer(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 交出时间为出发时间
					jcTimeSbf = new StringBuffer(obj.getDptTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jcTimeSbf = new StringBuffer(obj.getArrTime());
				}
			}
		}
		/*if (jrTimeSbf.toString() != null && !"".equals(jrTimeSbf.toString())
				&& !"null".equals(jrTimeSbf.toString())
				&& jcTimeSbf.toString() != null
				&& !"".equals(jcTimeSbf.toString())
				&& !"null".equals(jcTimeSbf.toString())) {

			if ("1".equals(pageRowObj.getHighlineFlag())) {// JRJC:比较 date <
															// jcTimeSbf
				// date:2014-01-01
				// jrTimeSbf.toString() 2014-01-01 01:01:0
				//
				if (DateUtil.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(),
						date + " 23:59:59")
						&& DateUtil.isBeforeForyyyyMMddHHmmss(date
								+ " 00:00:00", jcTimeSbf.toString())) {
					this.isValid = "1";
				}
			} else {
				if (DateUtil.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(),
						date + " 18:00:00")
						&& DateUtil.isBeforeForyyyyMMddHHmmss(
								DateUtil.mulDateOneDay(date) + " 18:00:00",
								jcTimeSbf.toString())) {// 既有线时间分界 18:00:00
					this.isValid = "1";
				}
				
			}
		}*/
		if (jrTimeSbf.toString() != null && !"".equals(jrTimeSbf.toString())
				&& !"null".equals(jrTimeSbf.toString())) {
			if ("1".equals(pageRowObj.getHighlineFlag())) {// JRZD:比较jrTimeSbf <
															// x < endTime
				// date:2014-01-01
				// jrTimeSbf.toString() 2014-01-01 01:01:01
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
						jrTimeSbf.toString()) && DateUtil
						.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(),
								DateUtil.addDateOneDay(date) + " 00:00:00"))) {
					this.isValid = "1";
				}
				
			} else {
				// date < jr < date+1
				// ok
				if ((DateUtil.isBeforeForyyyyMMddHHmmss(
						DateUtil.mulDateOneDay(date) + " 18:00:00",
						jrTimeSbf.toString()) && DateUtil
						.isBeforeForyyyyMMddHHmmss(jrTimeSbf.toString(), date
								+ " 18:00:00"))) {// 既有线时间分界
																// 18:00:00
					this.isValid = "1";
				}
				
			}
		}
		else{
			if ("1".equals(pageRowObj.getHighlineFlag())) {
				if(!"基本图".equals(pageRowObj.getCreatType())){
					if((DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getEndTime(), DateUtil.addDateOneDay(date) + " 00:00:00") && 
							DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00", pageRowObj.getEndTime())) ||
							(DateUtil.isBeforeForyyyyMMddHHmmss(date + " 00:00:00", pageRowObj.getStartTime()) &&
							DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , DateUtil.addDateOneDay(date) + " 00:00:00"))){
					this.isValid = "1";
					}
				}
			}else{
				//如果是临客,加入没有fjk，直接显示
				if(!"基本图".equals(pageRowObj.getCreatType())){
					if((DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getEndTime(), date + " 18:00:00") && 
							DateUtil.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date) + " 18:00:00", pageRowObj.getEndTime())) ||
							(DateUtil.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date) + " 18:00:00", pageRowObj.getStartTime()) &&
							DateUtil.isBeforeForyyyyMMddHHmmss(pageRowObj.getStartTime() , date + " 18:00:00"))){
						this.isValid = "1";
					}
				}
			}
		}
		this.setJrStn(jrStnSbf.toString());
		this.setJrTime(DateUtil.parseDateTOmmddhhmm(jrTimeSbf.toString()));
		this.setJcStn(jcStnSbf.toString());
		this.setJcTime(DateUtil.parseDateTOmmddhhmm(jcTimeSbf.toString()));
		
		
		// 解析结束

		// 以下方法未来可能会启用
		// if (otherFjkCount == 0) {
		// pageRowObj.setJrStn(pageRowObj.getStartStn());
		// pageRowObj.setJrTime(pageRowObj.getStartTime());
		// pageRowObj.setJcStn(pageRowObj.getEndStn());
		// pageRowObj.setJcTime(pageRowObj.getEndTime());
		// return;
		// }
		//
		//
		// if (otherFjkCount%2 == 0) {
		// int otherIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("other".equals(obj.getIsfdz())) {
		// otherIndex ++;
		// if (otherIndex%2 != 0) {//other出现的次数序号为奇数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//other出现的次数序号为偶数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// } else {
		// int fjkIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("1".equals(obj.getIsfjk())) {
		// fjkIndex ++ ;
		// if (fjkIndex%2 != 0) {//fjk出现的序号为奇数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//fjk出现的序号为偶数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// }
		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }
		
	}

	public int getLev2Checked2() {
		return lev2Checked2;
	}

	public void setLev2Checked2(int lev2Checked2) {
		this.lev2Checked2 = lev2Checked2;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getStartTimeAll() {
		return startTimeAll;
	}

	public void setStartTimeAll(String startTimeAll) {
		this.startTimeAll = startTimeAll;
	}

	public String getEndTimeAll() {
		return endTimeAll;
	}

	public void setEndTimeAll(String endTimeAll) {
		this.endTimeAll = endTimeAll;
	}

	public String getDailyplan_check_bureau() {
		return dailyplan_check_bureau;
	}

	public void setDailyplan_check_bureau(String dailyplan_check_bureau) {
		this.dailyplan_check_bureau = dailyplan_check_bureau;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getCreatType() {
		return creatType;
	}

	public void setCreatType(String creatType) {
		this.creatType = creatType;
	}

	public int getVaildStatus() {
		return vaildStatus;
	}

	public void setVaildStatus(int vaildStatus) {
		this.vaildStatus = vaildStatus;
	}

}