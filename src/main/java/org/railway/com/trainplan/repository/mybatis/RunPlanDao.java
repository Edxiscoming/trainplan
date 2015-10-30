package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.LevelCheck;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.VaildPlanTrainData;
import org.railway.com.trainplan.entity.VaildPlanTrainTemp;
import org.railway.com.trainplan.service.dto.PlanTrainDTO2;
import org.railway.com.trainplan.service.dto.PlanTrainDTOForModify;
import org.railway.com.trainplan.service.dto.PlanTrainDto;

import java.util.List;
import java.util.Map;

/**
 * 计划dao Created by star on 5/12/14.
 */
 
public interface RunPlanDao {

	/**
	 * 校验列车信息
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> checkTrainInfo(Map<String, Object> map);

	/**
	 * 查询客运计划时刻表数据
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划时刻表
	 */
	List<Map<String, Object>> findPlanTimeTableByPlanId(String planId);

	/**
	 * 查询客运计划时刻表数据
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划时刻表
	 */
	List<Map<String, Object>> findPlanTimeTableByPlanId2(String planId);

	/**
	 * 查询客运计划时刻表数据 通过基本图中的时刻表
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划时刻表
	 */
	List<Map<String, Object>> findPlanTimeTableByPlanIdFromjbt(String planId);

	/**
	 * 查询客运计划时刻表数据 通过基本图中的时刻表
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划时刻表
	 */
	List<Map<String, Object>> findPlanTimeTableByPlanIdFromjbt2(String planId);

	/**
	 * 查询客运计划主体信息
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划主体信息
	 */
	Map<String, Object> findPlanInfoByPlanId(String planId);

	/**
	 * 查询客运计划主体信息
	 * 
	 * @param planId
	 *            计划id
	 * @return 计划主体信息
	 */
	Map<String, Object> findPlanInfoByPlanId2(String planId);

	/**
	 * 一级审核
	 * 
	 * @param list
	 *            审核列表
	 * @return 审核结果
	 */
	int addCheckHis(List<LevelCheck> list);

	/**
	 * 更新审核状态和已审核局
	 * 
	 * @param map
	 *            审核信息
	 * @return 更新结果
	 */
	int updateCheckInfo(Map<String, Object> map);

	/**
	 * 根据计划id列表查询列车信息列表
	 * 
	 * @param params
	 *            计划id列表
	 * @return 查询结果
	 */
	List<Map<String, Object>> findPlanInfoListByPlanId(List<String> params);

	int addRunPlanList(List<RunPlan> list);

	int addRunPlan(RunPlan runPlan);

	/**
	 * 根据日期和担当局查询始发终到
	 * 
	 * @param map
	 *            date: 日期，格式 yyyymmdd, bureau: 路局简称
	 * @return 计划列表
	 */
	List<Map<String, Object>> findRunPlan_sfzd(Map<String, Object> map);

	/**
	 * 根据日期和担当局查询始发交出
	 * 
	 * @param map
	 *            date: 日期，格式 yyyymmdd, bureau: 路局简称
	 * @return 计划列表
	 */
	List<Map<String, Object>> findRunPlan_sfjc(Map<String, Object> map);

	/**
	 * 根据日期和担当局查询接入终到
	 * 
	 * @param map
	 *            date: 日期，格式 yyyymmdd, bureau: 路局简称
	 * @return 计划列表
	 */
	List<Map<String, Object>> findRunPlan_jrzd(Map<String, Object> map);

	/**
	 * 根据日期和担当局查询接入交出
	 * 
	 * @param map
	 *            date: 日期，格式 yyyymmdd, bureau: 路局简称
	 * @return 计划列表
	 */
	List<Map<String, Object>> findRunPlan_jrjc(Map<String, Object> map);

	/**
	 * 根据日期和担当局查询所有类型
	 * 
	 * @param map
	 *            date: 日期，格式 yyyymmdd, bureau: 路局简称
	 * @return 计划列表
	 */
	// List<Map<String, Object>> findRunPlan_all(Map<String, Object> map);
	List<PlanTrainDTO2> findRunPlan_allGT(Map<String, Object> map);

	List<PlanTrainDTO2> findRunPlan_all(Map<String, Object> map);

	/**
	 * 根据plancrossid查询已生成的、group_serial_nbr值最大的所有列车的信息，用来继续生成后面的开行计划
	 * 
	 * @param crossName
	 *            交路名
	 * @return
	 */
	List<RunPlan> findPreRunPlanByPlanCrossName(String crossName);

	List<RunPlan> findPreRunPlanByParam(Map<String, Object> map);

	/**
	 * 删除某个时间点之后的列车
	 * 
	 * @param map
	 *            参数2
	 */
	void deleteRunPlanByStartTime(Map<String, Object> map);
	
	/**
	 * 删除已经没有planTrain的plancross
	 * 
	 * @param map
	 *            参数2
	 */
	void deletePlanCrossIfNoPlanTrain(Map<String, Object> map);

	/**
	 * 保存后续车id
	 * 
	 * @param runPlan
	 */
	void updateNextTrainId(RunPlan runPlan);

	/**
	 * 根据PLAN_TRAIN_ID（列车ID）查询开行计划信息（唯一）
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)列车ID
	 * @author denglj
	 * @date 2014-10-26
	 */
	PlanTrainDto findPlanByPlanIdForTrainTime(String planTrainId);

	/**
	 * 根据trainNbr、runDate、startStn、endStn查询PLAN_TRAIN开行计划信息（唯一） 2015-1-30
	 * 19:36:27,唯一什么唯一,查出2条数据
	 * 
	 * @param queryMap
	 *            { trainNbr 车次 (检索条件 必填) runDate 开行日期yyyyMMdd (检索条件 必填)
	 *            startStn 始发站 (检索条件 必填) endStn 终到站 (检索条件 必填) }
	 * @author denglj
	 * @date 2014-10-26
	 */
	List<PlanTrainDto> findPlanByOther(Map<String, Object> queryMap);

	/**
	 * 修改plan_train表中的落成局.
	 * 
	 * @param sent_bureau
	 *            当前落成局.
	 * @param sent_bureau_his
	 *            历史落成局.
	 * @param id
	 * @return
	 */
	Integer updPlanTrainBureau(Map<String, String> map);

	/**
	 * 根据列车号和运行时间查询trainplan
	 * 
	 * @param bureau
	 * @param runDate
	 * @return
	 */
	List<PlanTrainDTOForModify> findPlanInfoByNBrAndRunDate(
			Map<String, Object> map);

	List<Map<String, Object>> getGroupSerialNbrByPlanCrossId(String planCrossId);

	/**
	 * cross_name中有相同车次.
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getTrainRunPlans2(Map<String, Object> map);

	/**
	 * 修改滚动状态.
	 * 
	 * @param map
	 * @return
	 */
	int updateIsAutoGenerateByMap(Map<String, Object> map);

	/**
	 * 查询列车信息.
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getTrainByMap(Map<String, Object> params);

	/**
	 * 检查列车是否存在重复数据.
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> checkTrainCfMatch(Map<String, Object> map);

	/**
	 * 检查列车是否存在重复数据.
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> checkTrainCfData(Map<String, Object> map);

	/**
	 * 查询plantrain,返回PlanTrainDto对象；getTrainByMap返回Map.
	 * 
	 * @param map
	 * @return
	 */
	List<PlanTrainDto> getTrainByMapReturnObj(Map<String, Object> map);

	/**
	 * 差别数据：第二个日期没有的数据.
	 * 
	 * @param m
	 * @return
	 */
	List<PlanTrainDto> getCbPlansQ(Map<String, Object> map);

	/**
	 * 差别数据：第一个日期没有的数据.
	 * 
	 * @param m
	 * @return
	 */
	List<PlanTrainDto> getCbPlansH(Map<String, Object> map);

	/**
	 * 差别数据：2个日期都有的.
	 * 
	 * @param m
	 * @return
	 */
	List<PlanTrainDto> getCbPlans(Map<String, Object> map);

	void insertVaildPlanTrainTemp(VaildPlanTrainTemp vaildPlanTrainTemp);
	
	List<VaildPlanTrainData> getPlanTrainByIdRenturnVPTD(Map<String, Object> m);
}
