package org.railway.com.trainplan.jdbcConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.StringAndTimeUtil;
import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.CmdTrainStn;

/**
 * 命令数据存取接口
 * 
 * @author ITC
 * 
 */
public class CmdDataFactory {

	private static CmdDataFactory instance;

	private CmdDataFactory() {

	}

	public static CmdDataFactory getInstance() {
		if (instance == null) {
			instance = new CmdDataFactory();
		}
		return instance;
	}

	/**
	 * 根据开始、终止日期查询符合条件的临客命令
	 * 
	 * @param paraModel
	 *            参数对象
	 * @param bureuaName
	 *            担当局简称
	 * @return
	 */
	public List<CmdInfoModel> findLKMlByStartAndDate(CmdInfoModel paraModel,
			String bureuaName) {

		List<CmdInfoModel> modelList = new ArrayList<CmdInfoModel>();

		/**
		 * v2.0 刘哲 20150126 高铁和既有命令类型和字段统一 通用： 项号--xianghao； 车次--content2；
		 * 命令号--mlh_name；总公司命令号--content30; 命令主表ID--txtmlid； 命令子表ID--txtmlitemid
		 * 加开命令：始发站名--content3；终到站名--content4；起始日期--time3；终止日期--time4
		 * 择日--content5；开行规律--content6；担当局--content7； 既有加开: 停运命令：担当局--content6；
		 * 停运日期--content5；停运始发站名--content4；停运终止站名--content7
		 */
		String sqlStr = "select t.txtmlitemid,t.txtmlid,t.ml_leixing,t.mlh_name,t.content2,t.content3,t.content4,t.content5,t.content6,t.content7,"
				+ "t.content30,t.faling_time,t.xianghao,t.time3,t.time4,t.content19 from text_ddml_items t "
				+ "where t.mlh_name is not null ";

		// 命令类型过滤
		String type = paraModel.getCmdType();

		if (type.equals(ConstantUtil.ALL_CMD_NAME)) {
			sqlStr += " and (t.ml_leixing = '" + ConstantUtil.JY_ADD_CMD_TYPE
					+ "' or t.ml_leixing = '" + ConstantUtil.GT_ADD_CMD_TYPE
					+ "' or t.ml_leixing = '" + ConstantUtil.JY_STOP_CMD_TYPE
					+ "' or t.ml_leixing = '" + ConstantUtil.GT_STOP_CMD_TYPE
					+ "') ";
		} else if (type.equals(ConstantUtil.GT_ADD_CMD_NAME)) {
			sqlStr += " and t.ml_leixing = '" + ConstantUtil.GT_ADD_CMD_TYPE
					+ "' ";
		} else if (type.equals(ConstantUtil.JY_ADD_CMD_NAME)) {
			sqlStr += " and t.ml_leixing = '" + ConstantUtil.JY_ADD_CMD_TYPE
					+ "' ";
		} else if (type.equals(ConstantUtil.GT_STOP_CMD_NAME)) {
			sqlStr += " and t.ml_leixing = '" + ConstantUtil.GT_STOP_CMD_TYPE
					+ "' ";
		} else if (type.equals(ConstantUtil.JY_STOP_CMD_NAME)) {
			sqlStr += " and t.ml_leixing = '" + ConstantUtil.JY_STOP_CMD_TYPE
					+ "' ";
		}

		// 开始、结束日期
		Date endDate = StringAndTimeUtil.computerLateDate(
				paraModel.getEndDate(), 1, 0);
		Date startDate = StringAndTimeUtil.computerLateDate(
				paraModel.getStartDate(), 0, -1);
		;
		String mlh_name = paraModel.getCmdNbrBureau();
		String content30 = paraModel.getCmdNbrSuperior();
		String content2 = paraModel.getTrainNbr();

		// 结束日期过滤
		if (endDate != null) {
			sqlStr += " and t.faling_time < to_date('";
			sqlStr += StringAndTimeUtil.yearMonthDayHourMinuteSecondSdf
					.format(endDate);
			sqlStr += "', 'yyyy-mm-dd hh24:mi:ss')";
		}
		// 开始日期过滤
		if (startDate != null) {
			sqlStr += " and t.faling_time > to_date('";
			sqlStr += StringAndTimeUtil.yearMonthDayHourMinuteSecondSdf
					.format(startDate);
			sqlStr += "', 'yyyy-mm-dd hh24:mi:ss')";
		}
		// 路局命令号过滤
		if (mlh_name != null && !mlh_name.trim().equals("")) {
			sqlStr += " and t.mlh_name = '";
			sqlStr += mlh_name;
			sqlStr += "'";
		}
		// 总公司命令号过滤
		if (content30 != null && !content30.trim().equals("")) {
			sqlStr += " and t.content30 = '";
			sqlStr += content30;
			sqlStr += "'";
		}
		// 车次过滤
		if (content2 != null && !content2.trim().equals("")) {
			sqlStr += " and t.content2 = '";
			sqlStr += content2;
			sqlStr += "'";
		}
		// 排序：发令时间倒排、项号顺排、车次顺排
		sqlStr += " order by t.faling_time desc, t.xianghao, t.content2";
		ResultSet rs = null;
		StringBuffer buffer = null;
		try {
			buffer = new StringBuffer();
			buffer.append("dbUri=" + DbUtilDdml.getInstance().DB_URL
					+ ", user=" + DbUtilDdml.getInstance().USER + ", password="
					+ DbUtilDdml.getInstance().PASSWORD);
			buffer.append("\r\nsql=" + sqlStr);

			rs = DbUtilDdml.getInstance().executeQuery(sqlStr, null);
			if (rs != null) {// 正常执行了SQL,但是结果集对象中可能没有数据
				rs.last();// 将指针移动到最后一行
				int i = rs.getRow();// 获得当前行的索引
				if (i > 0) {// 至少有一行数据
					rs.beforeFirst();// 将指针移动到第一行之前

					while (rs.next()) {
						CmdInfoModel model = new CmdInfoModel();
						model.setCmdTxtMlItemId(rs.getInt(1));
						model.setCmdTxtMlId(rs.getInt(2));
						String mllx = rs.getString(3);
						model.setCmdNbrBureau(rs.getString(4));
						model.setTrainNbr(rs.getString(5));
						model.setSelectedDate(rs.getString(8));
						buffer.append("\r\nMainId=" + model.getCmdTxtMlId()
								+ "; TrainNBR=" + model.getTrainNbr());
						if (model.getSelectedDate() == null
								|| (model.getSelectedDate() != null && model
										.getSelectedDate().trim()
										.equalsIgnoreCase("null"))) {
							model.setSelectedDate("");
						}

						if (mllx.equals(ConstantUtil.JY_ADD_CMD_TYPE)
								|| mllx.equals(ConstantUtil.GT_ADD_CMD_TYPE)) {

							model.setCmdType(mllx
									.equals(ConstantUtil.JY_ADD_CMD_TYPE) ? ConstantUtil.JY_ADD_CMD_NAME
									: ConstantUtil.GT_ADD_CMD_NAME);

							model.setStartStn(rs.getString(6));
							model.setEndStn(rs.getString(7));
							model.setRule(rs.getString(9));
							model.setCmdBureau(rs.getString(10));
						} else {

							model.setStartStn(rs.getString(7));
							model.setCmdType(mllx
									.equals(ConstantUtil.JY_STOP_CMD_TYPE) ? ConstantUtil.JY_STOP_CMD_NAME
									: ConstantUtil.GT_STOP_CMD_NAME);
							model.setCmdBureau(rs.getString(9));
							model.setEndStn(rs.getString(10));
						}

						model.setCmdNbrSuperior(rs.getString(11));
						model.setCmdTime(rs.getDate(12));
						model.setCmdItem(rs.getInt(13));

						model.setStartDate(rs.getDate(14));
						model.setEndDate(rs.getDate(15));
						String content19String = rs.getString(16);
						model.setImportCmdFlag((content19String != null && content19String
								.equals(ConstantUtil.IMPORT_CMD_STRING)) ? true
								: false);
						/**
						 * 2014-07-29
						 * 判断是否是本局担当的-因考虑分步骤实施，将所有发布的令设为本局担当,修改所有发布的命令为本局担当
						 * 2014-09-26修改为参数配置是否查看全部或只看本局担当，关闭下面两行代码
						 */
						// model.setCmdBureau(bureuaName);
						// modelList.add(model);

						if (!paraModel.getCmdBureau().isEmpty()) {

							if (model.getCmdBureau().equals(bureuaName)) {
								modelList.add(model);
							}
						} else {
							model.setCmdBureau(bureuaName);
							modelList.add(model);
						}
					}

				}
				rs.close();
			}
			DbUtilDdml.getInstance().clossPrepareStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 20150202 调试临客命令查不到情况。何宇阳
			// JDomUtil.writeFile(buffer.toString(), "/lkmllog/", "sql.txt");
		}

		return modelList;
	}

	/**
	 * 根据命令子表ID查询
	 * 
	 * @param cmdItemId
	 * @return
	 */
	public CmdInfoModel findCmdContentAndReleasePeopleByItemId(String cmdItemId) {
		CmdInfoModel resultModel = null;

		if (cmdItemId == null || cmdItemId.equals(""))
			return resultModel;

		String sqlStr = "select t.faling_people, t.content20 from text_ddml_items t where t.txtmlitemid = '";
		sqlStr += cmdItemId;
		sqlStr += "'";
		ResultSet rs = null;
		try {
			System.out.println("sql = " + sqlStr);
			rs = DbUtilDdml.getInstance().executeQuery(sqlStr, null);

			while (rs.next()) {
				resultModel = new CmdInfoModel();
				String releasePeople = rs.getString("faling_people");
				String largeContent = rs.getString("content20");
				resultModel.setCmdReleasePeople(releasePeople == null ? ""
						: releasePeople);
				resultModel.setLargeContent(largeContent == null ? ""
						: largeContent);
			}

			rs.close();
			DbUtilDdml.getInstance().clossPrepareStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

		return resultModel;
	}

	/**
	 * 查询路局字典，构造局码简称映射
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<String, String> findBureuaMap() {

		Map<String, String> bureuaMap = new HashMap<String, String>();

		bureuaMap.put("B", "哈局");
		bureuaMap.put("T", "沈局");
		bureuaMap.put("P", "京局");
		bureuaMap.put("V", "太局");
		bureuaMap.put("C", "呼局");
		bureuaMap.put("F", "郑局");
		bureuaMap.put("N", "武局");
		bureuaMap.put("Y", "西局");
		bureuaMap.put("K", "济局");
		bureuaMap.put("H", "上局");
		bureuaMap.put("G", "南局");
		bureuaMap.put("Q", "广铁");
		bureuaMap.put("Z", "宁局");
		bureuaMap.put("W", "成局");
		bureuaMap.put("M", "昆局");
		bureuaMap.put("J", "兰局");
		bureuaMap.put("R", "乌局");
		bureuaMap.put("O", "青藏");

		return bureuaMap;
	}

	/**
	 * 根据命令子表ID查询命令时刻信息
	 * 
	 * @param cmdItemId
	 * @return
	 */
	public List<CmdTrainStn> findCmdTrainStnByCmdItemId(String cmdItemId) {
		
		List<CmdTrainStn> trainStnList = new ArrayList<CmdTrainStn>();
		if(cmdItemId == null || cmdItemId.trim().equals("")){
			return trainStnList;
		}
		String sqlStr = "select t.item_sort, t.sta_sort, t.sta_name,t.arrive_time, t.start_time, t.run_days, t.subbur_code from lkcmd_schedule t where t.item_sort = ? order by to_number(t.sta_sort)";
		ResultSet rs = null;
		try {
			System.out.println("sql = " + sqlStr);
			rs = DbUtilDdml.getInstance().executeQuery(sqlStr, cmdItemId);
			int index = 0;
			while (rs.next()) {
				CmdTrainStn trainStn = new CmdTrainStn();
				trainStn.setCmdTrainStnId(rs.getInt("item_sort") + "");
				trainStn.setStnSort(rs.getString("sta_sort") == null ? 0 : Integer.parseInt(rs.getString("sta_sort")));
				trainStn.setNodeName(rs.getString("sta_name") == null ? "" : rs.getString("sta_name"));
//				String bureauCode = rs.getString("bur_code") == null ? "" : rs.getString("bur_code"); 
//				trainStn.setStnBureau(LjUtil.getLjByNameBs(bureauCode, 2));
				String arrTimeStr = rs.getString("arrive_time") == null ? "" : rs.getString("arrive_time");
				trainStn.setArrTime(StringAndTimeUtil.getFormatedTimeText(arrTimeStr));
				String deptTimeStr =  rs.getString("start_time") == null ? "" : rs.getString("start_time");
				trainStn.setDptTime(StringAndTimeUtil.getFormatedTimeText(deptTimeStr));
				trainStn.setTrackName(rs.getString("subbur_code"));
				//其它字段
				if (!trainStn.getArrTime().equals(
						trainStn.getDptTime())) {
					trainStn.setJobs(ConstantUtil.KY_STATION_FLAG);
				}
				if (index == 0) {
					trainStn.setStnType("1");
					trainStn.setPsgFlg(1);
					trainStn.setJobs(ConstantUtil.START_STATION_FLAG);
				}
				index ++;
				trainStnList.add(trainStn);
			}

			rs.close();
			DbUtilDdml.getInstance().clossPrepareStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		if(trainStnList.size() > 1){
			CmdTrainStn lastStn = trainStnList.get(trainStnList.size() - 1);
		    lastStn.setStnType("2");
			lastStn.setPsgFlg(1);
			lastStn.setJobs(ConstantUtil.END_STATION_FLAG);
		}
		
		return trainStnList;
	}
	
}
