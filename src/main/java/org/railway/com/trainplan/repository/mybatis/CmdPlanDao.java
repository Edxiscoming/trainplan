package org.railway.com.trainplan.repository.mybatis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.CompareModel;
import org.railway.com.trainplan.entity.DicRelaCrossPost;
import org.railway.com.trainplan.entity.HighlineCrossCmd;
import org.railway.com.trainplan.entity.HighlineCrossTrain;
import org.railway.com.trainplan.entity.PlanCrossCmd;
import org.railway.com.trainplan.entity.PlanTrainCMD;


/**加载高铁交路
 * @author suntao
 *
 */
 
public interface CmdPlanDao {
	
	/**
	 * @param bureau
	 * @param runDate
	 * @return PlanTrainCMD
	 */
	public List<PlanTrainCMD> findPlanTrainListByBureauAndRunDate(Map map);

	public CompareModel findCompareModelByBureauAndRunDate(
			Map<String, Object> map);

	public CompareModel findCompareModelByBureauAndRunDateBaseId(
			Map<String, Object> map);

	public List<DicRelaCrossPost> findCrossNameFromDicRelaCrossPostByOffset(
			String bureuaCode);

	public PlanTrainCMD findPlanTrainByCrossFirstTrainNbrAndRunDate(
			Map<String, Object> map);

	public List<PlanCrossCmd> findPlanCrossByPlanCrossId(Map<String, Object> map);
	public List<PlanCrossCmd> findPlanCrossByPlanCrossId2(Map<String, Object> map);

	public PlanTrainCMD findPlanTrainListByPlanTrainId(String trainId);

	public void insertHighlineCross(HighlineCrossCmd highlineCross);

	public void insertHighlineCrossTrain(HighlineCrossTrain crossTrain);

	public void updateHighlineCrossVehicleByHistory(Map<String, Object> map);

	public void deleteHighlineDataByRundateAndBureauCode(Map<String, Object> map);

}
