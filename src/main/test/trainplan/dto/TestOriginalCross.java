package trainplan.dto;

import java.util.Date;
import java.util.UUID;

public class TestOriginalCross {
	//对象id
		private String crossId = UUID.randomUUID().toString() ;
		//高线开行规律
		private String highlineRule;
		//普线开行规律
		private String commonlineRule;
		//是否截断原交路
		private int cutOld;
		//组数
		private int groupTotalNbr;
		//供电
		private int elecSupply;
		//集便
		private int dejCollect;
		//空调
		private int airCondition;
		//客运担当局
		private String tokenPsgBureau;
		//车辆担当局
		private String tokenVehBureau;
		//对数
		private String pairNbr;
		//交替日期
		private String alterNateDate;
		//交易原车次
		private String alterNateTranNbr;
		//交路名
		private String crossName;
		//备用套跑交路名
		private String crossSpareName;
		//高线标记
		private String highlineFlag; 
		//经由铁路线
		private String throughline;
		//指定星期
		private String appointWeek;
		//指定日期
		private String appointDay;
		//创建人
		private String createPeople; 
		//创建人单位
		private String createPeopleOrg; 
		//运行区段
		private String crossSection;
		//审核人
		private String checkPeople; 
		//审核时间
		private Date checkTime; 
		//是否审核
		private String checkFlag;
		//指定周期
		private String appointPeriod;
		//审核人单位
		private String checkPeopleOrg;
		//动车组车型
		private String crhType;
		//备注
		private String note;
		//担当车辆段/动车段
		private String tokenVehDept;
		//担当动车所
		private String tokenVehDepot;
		//客运担当单位
		private String tokenPsgDept;
		//备用及停运标记
		private String spareFlag;
		//机车类型
		private String locoType;
		//创建时间
		private Date createTime ;
		//基本方案id
		private String chartId;
		//基本方案名
		private String chartName;
		public String getHighlineRule() {
			return highlineRule;
		}
		public void setHighlineRule(String highlineRule) {
			this.highlineRule = highlineRule;
		}
		public String getCommonlineRule() {
			return commonlineRule;
		}
		public void setCommonlineRule(String commonlineRule) {
			this.commonlineRule = commonlineRule;
		}
		public int getCutOld() {
			return cutOld;
		}
		public void setCutOld(int cutOld) {
			this.cutOld = cutOld;
		}
		public int getGroupTotalNbr() {
			return groupTotalNbr;
		}
		public void setGroupTotalNbr(int groupTotalNbr) {
			this.groupTotalNbr = groupTotalNbr;
		}
		public int getElecSupply() {
			return elecSupply;
		}
		public void setElecSupply(int elecSupply) {
			this.elecSupply = elecSupply;
		}
		public int getDejCollect() {
			return dejCollect;
		}
		public void setDejCollect(int dejCollect) {
			this.dejCollect = dejCollect;
		}
		public int getAirCondition() {
			return airCondition;
		}
		public void setAirCondition(int airCondition) {
			this.airCondition = airCondition;
		}
		public String getTokenPsgBureau() {
			return tokenPsgBureau;
		}
		public void setTokenPsgBureau(String tokenPsgBureau) {
			this.tokenPsgBureau = tokenPsgBureau;
		}
		public String getTokenVehBureau() {
			return tokenVehBureau;
		}
		public void setTokenVehBureau(String tokenVehBureau) {
			this.tokenVehBureau = tokenVehBureau;
		}
		public String getPairNbr() {
			return pairNbr;
		}
		public void setPairNbr(String pairNbr) {
			this.pairNbr = pairNbr;
		}
		public String getAlterNateDate() {
			return alterNateDate;
		}
		public void setAlterNateDate(String alterNateDate) {
			this.alterNateDate = alterNateDate;
		}
		public String getAlterNateTranNbr() {
			return alterNateTranNbr;
		}
		public void setAlterNateTranNbr(String alterNateTranNbr) {
			this.alterNateTranNbr = alterNateTranNbr;
		}
		public String getCrossName() {
			return crossName;
		}
		public void setCrossName(String crossName) {
			this.crossName = crossName;
		}
		public String getCrossSpareName() {
			return crossSpareName;
		}
		public void setCrossSpareName(String crossSpareName) {
			this.crossSpareName = crossSpareName;
		}
		public String getHighlineFlag() {
			return highlineFlag;
		}
		public void setHighlineFlag(String highlineFlag) {
			this.highlineFlag = highlineFlag;
		}
		public String getThroughline() {
			return throughline;
		}
		public void setThroughline(String throughline) {
			this.throughline = throughline;
		}
		public String getAppointWeek() {
			return appointWeek;
		}
		public void setAppointWeek(String appointWeek) {
			this.appointWeek = appointWeek;
		}
		public String getAppointDay() {
			return appointDay;
		}
		public void setAppointDay(String appointDay) {
			this.appointDay = appointDay;
		}
		public String getCreatePeople() {
			return createPeople;
		}
		public void setCreatePeople(String createPeople) {
			this.createPeople = createPeople;
		}
		public String getCreatePeopleOrg() {
			return createPeopleOrg;
		}
		public void setCreatePeopleOrg(String createPeopleOrg) {
			this.createPeopleOrg = createPeopleOrg;
		}
		public String getCrossSection() {
			return crossSection;
		}
		public void setCrossSection(String crossSection) {
			this.crossSection = crossSection;
		}
		public String getCheckPeople() {
			return checkPeople;
		}
		public void setCheckPeople(String checkPeople) {
			this.checkPeople = checkPeople;
		}
		public Date getCheckTime() {
			return checkTime;
		}
		public void setCheckTime(Date checkTime) {
			this.checkTime = checkTime;
		}
		public String getAppointPeriod() {
			return appointPeriod;
		}
		public void setAppointPeriod(String appointPeriod) {
			this.appointPeriod = appointPeriod;
		}
		public String getCheckPeopleOrg() {
			return checkPeopleOrg;
		}
		public void setCheckPeopleOrg(String checkPeopleOrg) {
			this.checkPeopleOrg = checkPeopleOrg;
		}
		public String getCrhType() {
			return crhType;
		}
		public void setCrhType(String crhType) {
			this.crhType = crhType;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getTokenVehDept() {
			return tokenVehDept;
		}
		public void setTokenVehDept(String tokenVehDept) {
			this.tokenVehDept = tokenVehDept;
		}
		public String getTokenVehDepot() {
			return tokenVehDepot;
		}
		public void setTokenVehDepot(String tokenVehDepot) {
			this.tokenVehDepot = tokenVehDepot;
		}
		public String getTokenPsgDept() {
			return tokenPsgDept;
		}
		public void setTokenPsgDept(String tokenPsgDept) {
			this.tokenPsgDept = tokenPsgDept;
		}
		public String getSpareFlag() {
			return spareFlag;
		}
		public void setSpareFlag(String spareFlag) {
			this.spareFlag = spareFlag;
		}
		public String getLocoType() {
			return locoType;
		}
		public void setLocoType(String locoType) {
			this.locoType = locoType;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getChartId() {
			return chartId;
		}
		public void setChartId(String chartId) {
			this.chartId = chartId;
		}
		public String getChartName() {
			return chartName;
		}
		public void setChartName(String chartName) {
			this.chartName = chartName;
		}
		public String getCheckFlag() {
			return checkFlag;
		}
		public void setCheckFlag(String checkFlag) {
			this.checkFlag = checkFlag;
		}
		public String getCrossId() {
			return crossId;
		}
		public void setCrossId(String crossId) {
			this.crossId = crossId;
		}
}
