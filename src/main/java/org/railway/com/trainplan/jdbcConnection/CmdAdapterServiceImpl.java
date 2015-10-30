package org.railway.com.trainplan.jdbcConnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.railway.com.trainplan.common.utils.StringAndTimeUtil;
import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.CmdTrainStn;

import trainTrack.Ui.Panel.maintain.trainPlan.readExcelToSchedule.ImportScheduleXls;
import trainTrack.Ui.Panel.maintain.trainPlan.readExcelToSchedule.Schedule;
import trainTrack.Ui.Panel.maintain.trainPlan.readExcelToSchedule.Train;

/**
 * 调度命令适配器服务实现
 * 
 * @author ITC
 * 
 */
public class CmdAdapterServiceImpl implements ICmdAdapterService {
	// 服务实例
	private static CmdAdapterServiceImpl instance;
	// 路局局码
	private String bureauCode = "";

	private CmdAdapterServiceImpl() {

	}

	/**
	 * 获取服务实例
	 * 
	 * @return
	 */
	public static CmdAdapterServiceImpl getInstance() {

		if (instance == null) {
			instance = new CmdAdapterServiceImpl();
		}
		return instance;
	}

	/**
	 * 根据开始、结束日期 查询符合条件的临客命令
	 * 
	 * @param paraModel
	 *            参数对象 包括：开始、结束日期、路局命令号、总公司命令号、车次
	 * @return
	 */
	public List<CmdInfoModel> findCmdInfoModelListByDateAndBureau(
			CmdInfoModel paraModel) {
		// TODO Auto-generated method stub
		StringBuffer message = new StringBuffer();
		List<CmdInfoModel> modelList = new ArrayList<CmdInfoModel>();
		String bureauName = CmdDataFactory.getInstance().findBureuaMap()
				.get(getBureau());
		message.append("\r\n路局码：" + getBureau());
		message.append("\r\n路局参数：" + bureauName);

		message.append("\r\n路局新参数：" + bureauName);
		modelList = CmdDataFactory.getInstance().findLKMlByStartAndDate(
				paraModel, bureauName);

		// 关闭数据库资源
		closeResource();

		message.append("[临客查询：局码="
				+ getBureau()
				+ ",局简称="
				+ bureauName
				+ ",开始日期="
				+ StringAndTimeUtil.yearMonthDayFullSdf.format(paraModel
						.getStartDate())
				+ ",结束日期="
				+ StringAndTimeUtil.yearMonthDayFullSdf.format(paraModel
						.getEndDate()) + ",个数" + modelList.size() + "]");

		// 20150202 调试临客命令查不到情况。何宇阳
		// JDomUtil.writeFile(message.toString(), "/lkmllog/", "ddml.txt");
		return modelList;
	}

	/**
	 * 根据局码初始化数据库环境
	 * 
	 * @param bureuaCode
	 *            局码
	 */
	@Override
	public void initilize(String bureuaCode) {
		// TODO Auto-generated method stub
		setBureau(bureuaCode);
		// DbUtilDdml db = new DbUtilDdml();
		// db.init(bureuaCode);
		DbUtilDdml.getInstance().init(bureuaCode);
	}

	// @Override
	// public List<CmdInfoModel> findCmdInfoModelListByDateAndBureau(
	// CmdInfoModel paraModel) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public CmdInfoModel findCmdInfoModelByBureauAndItemId(String bureauCode,
			String cmdItemId) {
		// TODO Auto-generated method stub
		CmdInfoModel model = CmdDataFactory.getInstance().findCmdContentAndReleasePeopleByItemId(cmdItemId);
		return model;
	}

	@Override
	public String buildPlanCmd(List<String> cmdContent, String accontName,
			String peopleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildOrdinaryPlanCmd(List<String> cmdContent,
			String accontName, String peopleName) {
		// TODO Auto-generated method stub
		return null;
	}

	// /**
	// * 根据高铁日计划命令内容、岗位名、创建人姓名生成日计划命令
	// *
	// * @param cmdContent
	// * 命令内容
	// * @param accontName
	// * 岗位名
	// * @param peopleName
	// * 创建人姓名
	// *
	// * @return 流水号
	// */
	// @Override
	// public String buildPlanCmd(List<String> cmdContent, String accontName,
	// String peopleName) {
	// String liushuihao = null;
	// Date bianzhiTime = Calendar.getInstance().getTime();
	//
	// PlanCmdModel cmdModel = new PlanCmdModel();
	// cmdModel.setBianzhiPeople(peopleName);
	// cmdModel.setBianzhiTime(bianzhiTime);
	// liushuihao = StringAndTimeUtil.yearMonthDayHourMinuteSecondMicroSecondSdf
	// .format(bianzhiTime) + "-" + peopleName;
	// cmdModel.setLiushuihao(liushuihao);
	// cmdModel.setMlDanwei(accontName);
	// cmdModel.setMlLeixing(ConstantUtil.GT_PLAN_CMD_TYPE);
	// cmdModel.setMlMingcheng(ConstantUtil.GT_PLAN_CMD_NAME);
	//
	// PlanCmdModel resultModel = CmdDataFactory.getInstance().insertTextDdml(
	// cmdModel);
	// if (resultModel != null) {
	//
	// for (String largetContent : cmdContent) {
	//
	// for (int i = 0; i < largetContent.length(); i = i + 2000) {
	// cmdModel.setLargeContent(largetContent.substring(i,
	// Math.min(i + 2000, largetContent.length())));
	// CmdDataFactory.getInstance().insertTextDdmlItems(
	// resultModel.getTxtMlId(), cmdModel);
	// }
	//
	// }
	// }
	// // 关闭数据库资源
	// closeResource();
	// String message = "[生成高铁日计划命令：拟令人=" + peopleName + "，流水号" + liushuihao
	// + "]";
	// // 20150202 调试临客命令查不到情况。何宇阳
	// // JDomUtil.writeFile(message, "/lkmllog/", "ddml.txt");
	// return liushuihao;
	// }
	//
	// /**
	// * 根据普速日计划命令内容、岗位名、创建人姓名生成日计划命令
	// *
	// * @param cmdContent
	// * 命令内容
	// * @param accontName
	// * 岗位名
	// * @param peopleName
	// * 创建人姓名
	// *
	// * @return 流水号
	// */
	// @Override
	// public String buildOrdinaryPlanCmd(List<String> cmdContent,
	// String accontName, String peopleName) {
	// String liushuihao = null;
	// Date bianzhiTime = Calendar.getInstance().getTime();
	//
	// PlanCmdModel cmdModel = new PlanCmdModel();
	// cmdModel.setBianzhiPeople(peopleName);
	// cmdModel.setBianzhiTime(bianzhiTime);
	// liushuihao = StringAndTimeUtil.yearMonthDayHourMinuteSecondMicroSecondSdf
	// .format(bianzhiTime) + "-" + peopleName;
	// cmdModel.setLiushuihao(liushuihao);
	// cmdModel.setMlDanwei(accontName);
	// cmdModel.setMlLeixing(ConstantUtil.JY_PLAN_CMD_TYPE);
	// cmdModel.setMlMingcheng(ConstantUtil.JY_PLAN_CMD_NAME);
	//
	// PlanCmdModel resultModel = CmdDataFactory.getInstance().insertTextDdml(
	// cmdModel);
	// if (resultModel != null) {
	//
	// for (String largetContent : cmdContent) {
	//
	// for (int i = 0; i < largetContent.length(); i = i + 2000) {
	// cmdModel.setLargeContent(largetContent.substring(i,
	// Math.min(i + 2000, largetContent.length())));
	// CmdDataFactory.getInstance().insertTextDdmlItems(
	// resultModel.getTxtMlId(), cmdModel);
	// }
	//
	// }
	// }
	// // 关闭数据库资源
	// closeResource();
	// String message = "[生成普速日计划命令：拟令人=" + peopleName + "，流水号" + liushuihao
	// + "]";
	// // 20150202 调试临客命令查不到情况。何宇阳
	// // JDomUtil.writeFile(message, "/lkmllog/", "ddml.txt");
	// return liushuihao;
	// }
	//
	/**
	 * 关闭数据库资源
	 */
	private void closeResource() {
		// TODO Auto-generated method stub
		DbUtilDdml.getInstance().closeConnection();
	}

	private String getBureau() {
		return bureauCode;
	}

	private void setBureau(String bureau) {
		this.bureauCode = bureau;
	}

	//
	// @Override
	// public CmdInfoModel findCmdInfoModelByBureauAndItemId(String bureauCode,
	// String cmdItemId) {
	// // TODO Auto-generated method stub
	// initilize(bureauCode);
	// return CmdDataFactory.getInstance()
	// .findCmdContentAndReleasePeopleByItemId(cmdItemId);
	// }
	//
	// @Override
	// public List<mor.railway.cmd.adapter.model.CmdInfoModel>
	// findCmdInfoModelListByDateAndBureau(
	// mor.railway.cmd.adapter.model.CmdInfoModel paraModel) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public mor.railway.cmd.adapter.model.CmdInfoModel
	// findCmdInfoModelByBureauAndItemId(
	// String bureauCode, String cmdItemId) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public List<CmdTrainStn> findCmdTrainStnByCmdItemId(String cmdItemId) {
		// TODO Auto-generated method stub
		List<CmdTrainStn> trainStnList = new ArrayList<CmdTrainStn>();
		trainStnList = CmdDataFactory.getInstance().findCmdTrainStnByCmdItemId(
				cmdItemId);
		// 关闭数据库资源
		closeResource();
		return trainStnList;
	}

	/**
	 * 导入Excel文件中列车客运时刻表信息
	 * @param InputStream  文件流
	 * @param trainNbr  车次名
	 * @param startStn  始发站
	 * @param endStn    终到站
	 * @return List<CmdTrainStn>
	 */
	public static List<CmdTrainStn> importCmdTrainStnByExcel(InputStream inputStream, String trainNbr,
			String startStn, String endStn) {
		
	    List<CmdTrainStn> trainStnList = new ArrayList<CmdTrainStn>();
		try {

			LinkedHashMap<String, Train> trains = CmdAdapterServiceImpl.loadCmdTrainStnFromExcel(inputStream);
			Train trainTemp = null;
			List<Train> trainTempList = new ArrayList<Train>();
			List<Train> trainList = new ArrayList<Train>();

			for (Iterator<String> rs = trains.keySet().iterator(); rs.hasNext();) {
				String key = (String) rs.next();
				trainTemp = (Train) trains.get(key);
				if (trainTemp.getCc().equals(trainNbr)) {
					trainTempList.add(trainTemp);
				}
			}
			// Excel文件中有相同车次的多个记录时，判定始发终到站是否相同
			if (trainTempList.size() > 1) {
				for (Train t : trainTempList) {
					if (t.getSfz().equals(startStn)
							&& t.getZdz().equals(endStn)) {
						trainList.add(t);
					}
				}
			} else if(trainTempList.size() == 1){
				trainList.add(trainTempList.get(0));
			}
			if (trainList.isEmpty()) {
				return trainStnList;
			}
			List<Schedule> schList = trainList.get(0).getSchedules();
			for (Schedule sch : schList) {
				CmdTrainStn trainStn = new CmdTrainStn();
				trainStn.setArrTime(StringAndTimeUtil.getFormatedTimeText(sch
						.getZdsj()));
				trainStn.setDptTime(StringAndTimeUtil.getFormatedTimeText(sch
						.getSfsj()));
				trainStn.setNodeName(sch.getZm());
				trainStnList.add(trainStn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trainStnList;
	}

	/**
	 * 从Excel文件中解析出客运列车时刻
	 * @param inputStream
	 * @return LinkedHashMap<String, Train>
	 */
	public static LinkedHashMap<String, Train> loadCmdTrainStnFromExcel(
			InputStream inputStream) {

		try {
			HSSFWorkbook wb = new HSSFWorkbook(inputStream);
			HSSFSheet[] sheet = new HSSFSheet[wb.getNumberOfSheets()];
			ArrayList<String[][]> sheetDatas = new ArrayList<String[][]>(wb.getNumberOfSheets());
			//Excel文件数据获取
			for (int sheetI = 0; sheetI < wb.getNumberOfSheets(); ++sheetI) {
				sheet[sheetI] = wb.getSheetAt(sheetI);
				int rows = sheet[sheetI].getPhysicalNumberOfRows();
				if (rows > 0) {
					int cols = 0;
					for (int i = 0; i < sheet[sheetI].getPhysicalNumberOfRows(); ++i) {
						int tmpCols = 0;
						if (sheet[sheetI].getRow(i) == null)
							continue;

						int cols1 = sheet[sheetI].getRow(i).getLastCellNum()
								- sheet[sheetI].getRow(i).getFirstCellNum() + 1;
						int cols2 = sheet[sheetI].getRow(i)
								.getPhysicalNumberOfCells();
						tmpCols = (cols1 > cols2) ? cols1 : cols2;
						cols = (tmpCols > cols) ? tmpCols : cols;
					}

					String[][] pageDatas = new String[rows][cols];
					if (cols > 0) {
						for (int i = 0; i < rows; ++i)
							for (int j = 0; j < cols; ++j) {
								if (sheet[sheetI].getRow(i) == null)
									continue;

								String msg = "";
								HSSFCell cell = sheet[sheetI].getRow(i)
										.getCell((short) j);
								if (cell != null)
									switch (cell.getCellType()) {
									case 0:
										int hour = Integer
												.parseInt(StringAndTimeUtil.hourSdf.format(cell
														.getDateCellValue()));
										int minute = Integer
												.parseInt(StringAndTimeUtil.minuteSdf.format(cell
														.getDateCellValue()));
										if ((hour != 0) || (minute != 0))
											msg = StringAndTimeUtil.hourMinuteSdf
													.format(cell
															.getDateCellValue());
										else
											msg = String.valueOf((int) cell
													.getNumericCellValue());

										break;
									default:
										msg = cell.getRichStringCellValue().getString();
									}

								msg = ImportScheduleXls.getInstance()
										.trimCellValue(msg);

								pageDatas[i][j] = msg;
							}

						sheetDatas.add(pageDatas);
					}
				}
			}
			//开始分析数据
			ArrayList rs = ImportScheduleXls.getInstance().analysisDatas(
					sheetDatas);
			ArrayList trains = ImportScheduleXls.getInstance().createTrains(rs);

			LinkedHashMap<String, Train> datas = ImportScheduleXls.getInstance()
					.getTrainsResult(trains);
			CmdAdapterServiceImpl.correctTrains(datas);
			return datas;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException in importExcel()\n"
					+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException in importExcel()\n"
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static void correctTrainSchedule(Train train) {
		@SuppressWarnings("unchecked")
		ArrayList<Schedule> zms = train.getSchedules();
		int loop = zms.size();
		for (int i = 1; i < loop; ++i) {
			Schedule schedule = (Schedule) zms.get(i);
			Schedule schedulepre = (Schedule) zms.get(i - 1);
			if (schedule.getZdsj().length() == 2)
				schedule.setZdsj(schedulepre.getSfsj().substring(0, 2)
						+ schedule.getZdsj());

			if (schedule.getSfsj().length() == 2)
				schedule.setSfsj(schedule.getZdsj().substring(0, 2)
						+ schedule.getSfsj());
		}
	}

	private static void correctTrains(LinkedHashMap<String, Train> trains) {
		Iterator<String> keys = trains.keySet().iterator();
		while (keys.hasNext()) {
			Train train = (Train) trains.get(keys.next());
			correctTrainSchedule(train);
		}
	}

}
