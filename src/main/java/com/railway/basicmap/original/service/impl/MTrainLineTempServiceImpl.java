package com.railway.basicmap.original.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.basicmap.original.entity.BureauDic;
import com.railway.basicmap.original.entity.MTrainlineTemp;
import com.railway.basicmap.original.repository.BureauDicMapper;
import com.railway.basicmap.original.repository.MTrainlineTempMapper;
import com.railway.basicmap.original.service.IMTrainLineTempService;
import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmVersionMapper;

@Service
public class MTrainLineTempServiceImpl implements IMTrainLineTempService{
	private static final Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private MTrainlineTempMapper mTrainlineTempMapper;
	
	@Autowired
	private BureauDicMapper bureauMapper;
	
	@Autowired
	private TCmVersionMapper versionMapper;
	
	/* (non-Javadoc)
	 * @see com.railway.basicmap.original.service.IMTrainLineTemp#GetTrainLineInfo(java.util.Map)
	 * 基本图列表
	 */
	public Result<MTrainlineTemp> GetTrainLineInfo(Map<String, Object> map){
		Result<MTrainlineTemp> result = new Result<MTrainlineTemp>();
		String cmVersionId = String.valueOf(map.get("versionId"));
		int checkFlag = (int) map.get("checkFlag");
		try {
		
				TCmVersion version  = versionMapper.selectByPrimaryKey(cmVersionId);
				map.put("versionId", version.getmTemplateScheme());
				if(checkFlag == 1)
			{
				List<MTrainlineTemp> list = mTrainlineTempMapper.queryVagueAllTrainList(map);
				result.setList(list);
			}else
			{
				List<MTrainlineTemp> list = mTrainlineTempMapper.queryAllTrainList(map);
				result.setList(list);
			}
		} catch (Exception e) {
			logger.error("GetTrainLineInfo 查询基本图所有车次失败！",e);
			result.setCode(-1);
			result.setMessage("查询基本图车次信息失败！");
		}
		return result; 
	}
	
	@Override
	public Map<String,String> getBureauNameDic() {
		List<BureauDic> list = null;
		try {
			list = bureauMapper.getBureauDictMap();
		} catch (Exception e) {
			logger.error("查询基础数据库局码数据字典失败！",e);
			return null;
		}
		Map<String,String> map = null;
		if(list != null && list.size() > 0){
			map = new HashMap<String, String>();
			for(BureauDic dic : list){
				map.put(dic.getLjpym(), dic.getLjjc());
			}
		}
		return map;
	}
	
	@Override
	public Map<String, String> getBureauShortNameDic() {
		List<BureauDic> list = null;
		try {
			list = bureauMapper.getBureauDictMap();
		} catch (Exception e) {
			logger.error("查询基础数据库局码数据字典失败！",e);
			return null;
		}
		Map<String,String> map = null;
		if(list != null && list.size() > 0){
			map = new HashMap<String, String>();
			for(BureauDic dic : list){
				map.put(dic.getLjjc(), dic.getLjpym());
			}
		}
		return map;
	}
	
}
