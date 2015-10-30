package org.railway.com.trainplan.jdbcConnection;

import java.util.List;

import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.CmdTrainStn;

import trainTrack.Ui.Panel.maintain.trainPlan.readExcelToSchedule.Train;




/**
 * 调度命令适配器服务接口
 * @author ITC
 *
 */
public interface ICmdAdapterService {
	
	/**
	 * 根据局码初始化数据库环境
	 * @param bureuaCode 局码
	 */
	public void initilize(String bureuaCode);
	
	/**
	 *根据开始、结束日期 查询符合条件的临客命令
	 * @param paraModel   参数对象 包括：开始、结束日期、路局命令号、总公司命令号、车次
	 * @return
	 */
	public List<CmdInfoModel> findCmdInfoModelListByDateAndBureau(CmdInfoModel paraModel);
	
	/**
	 *根据担当局局码、命令子表ID 查询相关局临客命令的命令内容和发令人
	 * @param bureauCode   担当局局码
	 * @param cmdItemId 命令子表ID
	 * @return
	 */
	public CmdInfoModel findCmdInfoModelByBureauAndItemId(String bureauCode, String cmdItemId);
	
	

	/**
	 * 根据高铁日计划命令内容、岗位名、创建人姓名生成日计划命令
	 * 
	 * @param cmdContent  命令内容
	 * @param accontName  岗位名
	 * @param peopleName  创建人姓名
	 * 
	 * @return  流水号
	 */
	public String buildPlanCmd(List<String> cmdContent, String accontName, String peopleName);
	/**
	 * 根据既有日计划命令内容、岗位名、创建人姓名生成日计划命令
	 * 
	 * @param cmdContent  命令内容
	 * @param accontName  岗位名
	 * @param peopleName  创建人姓名
	 * 
	 * @return  流水号
	 */
	public String buildOrdinaryPlanCmd(List<String> cmdContent, String accontName, String peopleName);
	
	/**
	 * 根据命令子表ID在命令时刻表中查询时刻信息
	 * @param cmdItemId  命令子表ID
	 * @return
	 */
	public List<CmdTrainStn> findCmdTrainStnByCmdItemId(String cmdItemId);
	
}
