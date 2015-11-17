package org.railway.com.trainplan.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mor.railway.cmd.adapter.model.CmdInfoModel;
import mor.railway.cmd.adapter.service.ICmdAdapterService;
import mor.railway.cmd.adapter.service.impl.CmdAdapterServiceImpl;
import mor.railway.cmd.adapter.util.ConstantUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.MTrainLine;
import org.railway.com.trainplan.entity.MTrainLineStn;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.PlanTrainInFoTemp;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
import org.railway.com.trainplan.entity.runline.TrainTypeModel;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.MTrainLineDao;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.controller.RunPlanLkController;
import org.railway.com.trainplanv2.dto.TrainTypeModel2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 存生成运行线时临时数据
 * @author chengc
 *
 */
@Component
@Transactional
@Monitored
public class PlanTrainInfoTempService {

	@Autowired
	private BaseDao baseDao;
	
	
	
	  
	  /**
	   * 保存表PLAN_TRAIN_INFO_TEMP中数据
	   * @param cmdTrainStn
	   * @return
	   */
	  public int insertPlanTrainInFoTemp(PlanTrainInFoTemp planTrainInFoTemp){
		  return baseDao.insertBySql(Constants.PLANTRAININFOTEMPDAO_INSERT_PLANTRAININFOTEMP, planTrainInFoTemp);
	  }

	 
	  public int deletePlanTrainInFoTemp(String id){
		  return baseDao.deleteBySql(Constants.PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP, id);
	  }
	  
	  public int deletePlanTrainInfoTempByRequestId(String requestid){
		  return baseDao.deleteBySql(Constants.PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP_BYREQUESTID, requestid);
	  }
	  
	  public int deletePlanTrainInFoTempByDate(String date){
		  return baseDao.deleteBySql(Constants.PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP_BYDATE, date);
	  }
	  
	  /**
	   * 根据id更新PLAN_TRAIN_INFO_TEMP
	   * @param id
	   * @param id
	   * @return
	   */
	  public int updateCmdTrainForCmdTraindId(PlanTrainInFoTemp planTrainInFoTemp){
		  return baseDao.updateBySql(Constants.PLANTRAININFOTEMPDAO_UPDATE_PLANTRAININFOTEMP, planTrainInFoTemp);
	  }
	  
	  /**
	   * 根据多条件查询表PLAN_TRAIN_INFO_TEMP数据对象
	   * @param PlanTrainInFoTemp
	   * @return
	   */
	  public List<PlanTrainInFoTemp> getPlanTrainInFoTemp(PlanTrainInFoTemp planTrainInFoTemp){
		  return baseDao.selectListBySql(Constants.PLANTRAININFOTEMPDAO_LIST_PLANTRAININFOTEMP, planTrainInFoTemp);
	  }
}
