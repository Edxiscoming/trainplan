package com.railway.passenger.transdispatch.operationplan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.common.entity.Page;
import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanTrain;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TPlanCheckMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TPlanModifyMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TPlanSentMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TPlanTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TPlanTrainStnMapper;
import com.railway.passenger.transdispatch.operationplan.service.IRunPlanService;

@Service
public class RunPlanServiceImpl implements IRunPlanService {
	
	private static Log logger = LogFactory.getLog(RunPlanServiceImpl.class);
	
	@Autowired
	TPlanCheckMapper tPlanCheckMapper;

	@Autowired
	TPlanModifyMapper tPlanModifyMapper;

	@Autowired
	TPlanSentMapper tPlanSentMapper;

	@Autowired
	TPlanTrainMapper tPlanTrainMapper;

	@Autowired
	TPlanTrainStnMapper tPlanTrainStnMapper;
	
	@Autowired
	private BaseDao baseDao;
		
	@Override
	public Result<TCmCross> getPlanCrosses(Map<String, Object> map){
		Result<TCmCross> result=new Result<TCmCross>();		
			try {
				List<TCmCross> list = tPlanTrainMapper.PageQueryPlanCross(map);
				int count = tPlanTrainMapper.getPlanCrossInfoTotalCount(map);
				if (list != null && list.size() > 0) {
					Page<TCmCross> page = new Page<TCmCross>(
							count,
							Integer.parseInt(String.valueOf(map.get("pageSize"))),
							Integer.parseInt(String.valueOf(map
									.get("pageIndex"))), list.size(), list);
					;
					result.setPageInfo(page);
				} else {
					result.setCode(-1);
					result.setMessage("未查出相关开行计划信息");
				}
			} catch (Exception e) {
				logger.error("分页查询开行计划信息失败 pageQueryPlanCross:",e);
				result.setCode(-1);
				result.setMessage("分页查询开行计划信息失败！");
			}
			return result;
	}

	
	
	@Override
	public Result<TCmCross> deletePlanCross(String crossIds) {
		Result<TCmCross> result=new Result<TCmCross>();		
		try {
			//将ids解析成字符串数组，字符串数组直接传给后台作为IN的查询条件
			String[] ids = crossIds.split(",");
			
			StringBuffer bf = new StringBuffer();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			int size = ids.length;
			for (int i = 0; i < size; i++) {
				bf.append("'").append(ids[i]).append("'");
				if (i != size - 1) {
					bf.append(",");
				}
			}
			//这个对象后台SQL可以直接用在IN查询条件里面
			reqMap.put("crossIds", bf.toString());
			
		} catch (Exception e) {
			logger.error("删除开行计划失败: crossIds = " + crossIds ,e);
			result.setCode(-1);
			result.setMessage("删除开行计划失败");
		}
		return result;
	}

	@Override
	public Result<TCmCross> getPlanCrossDetail(String id) {
		Result<TCmCross> result=new Result<TCmCross>();		
		try {
			//根据TCmCross的id，查询出TCmCross以及关联的对数表和planTrain信息
			
		} catch (Exception e) {
			logger.error("查询计划交路失败: id = " + id,e);
			result.setCode(-1);
			result.setMessage("查询计划交路失败");
		}
		return result;
	}

	@Override
	public Result<TPlanTrain> checkPlanTrain(String ids) {
		Result<TPlanTrain> result=new Result<TPlanTrain>();		
		try {
			//将ids解析成字符串数组，字符串数组直接传给后台作为IN的查询条件
			
			//获取用户信息，用户信息作为审核字段存入数据
			
		} catch (Exception e) {
			logger.error("审核开行计划失败: ids = " + ids,e);
			result.setCode(-1);
			result.setMessage("审核开行计划失败");
		}
		return result;
	}

	@Override
	public Result<TCmCross> sendPlanTrainMsg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	

}