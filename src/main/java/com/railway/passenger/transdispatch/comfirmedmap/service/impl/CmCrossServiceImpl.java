package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;

import com.railway.passenger.transdispatch.comfirmedmap.entity.Ljzd;

import org.railway.com.trainplan.entity.TrainTimeInfoJbt;
import org.railway.com.trainplan.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.common.entity.Page;
import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCrossAndOriginaCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainAlternateMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainRuleMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmCrossService;
import com.railway.passenger.transdispatch.util.PageUtil;

@Service
public class CmCrossServiceImpl implements ICmCrossService{
	
	private static final Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private TCmCrossMapper tCmCrossMapper;
	
	@Autowired
	private TCmTrainMapper tCmTrainMapper;
	
	@Autowired
	private TCmTrainAlternateMapper tCmTrainAlternateMapper;
	
	@Autowired
	private TCmTrainRuleMapper tCmTrainRuleMapper;
	
	@Autowired
	private TCmOriginalCrossMapper tCmOriginalCrossMapper;
	
	@Override
	public int insertTCmCross(TCmCross cross) {
		return tCmCrossMapper.insertSelective(cross);
	}

	@Override
	public TCmCross getTCmCross(String cmCrossId) {
		return tCmCrossMapper.selectByPrimaryKey(cmCrossId);
	}

	@Override
	public Result<TCmTrain> getTCmTrain(Map<String, Object> reqMap) {
		new PageUtil(reqMap);
		List<TCmTrain> list = tCmTrainMapper.selectByCmCrossId(reqMap);
		Result<TCmTrain> result = new Result<TCmTrain>();
		
		Page<TCmTrain> pageInfo = new Page<TCmTrain>();
		pageInfo.setList(list);
		pageInfo.setNum(list.size());
		pageInfo.setTotalCount((int) getCmUnitCrossTrainInfoCount(reqMap));
		pageInfo.setPageIndex(Integer.parseInt(String.valueOf(reqMap.get("pageIndex"))));
		pageInfo.setPageSize(Integer.parseInt(String.valueOf(reqMap.get("pageSize"))));
		result.setPageInfo(pageInfo);
		return result; 
	}

	@Override
	public TCmTrainAlternate getTCmTrainAlternate(String trainId) {
		List<TCmTrainAlternate> list=tCmTrainAlternateMapper.getTCmTrainAlternate(trainId);
		if(!list.isEmpty()&&list!=null){
			return list.get(0);
		}
		return null;
	}

	@Override
	public TCmTrainRule getTCmTrainRule(String trainId) {
		List<TCmTrainRule> list=tCmTrainRuleMapper.getTCmTrainRule(trainId);
		if(!list.isEmpty()&&list!=null){
			return list.get(0);
		}
		return null;
	}
	
	
	@Override
	public TCmOriginalCross getOriginalCrossInfoByCmCrossId(String trainId) {
		
		return tCmOriginalCrossMapper.selectByPrimaryKey_Cross(trainId);
	}
	
	/**
	 * @param reqMap
	 * @return
	 * 根据方案或者车次获取交路信息
	 */
	public Result<TCmCrossAndOriginaCross> getCmUnitCrossInfo(Map<String, Object> reqMap) {
		new PageUtil(reqMap);
		List<TCmCrossAndOriginaCross> list = tCmCrossMapper.getCmUnitCrossInfo(reqMap);
		Result<TCmCrossAndOriginaCross> result = new Result<TCmCrossAndOriginaCross>();
		
		Page<TCmCrossAndOriginaCross> pageInfo = new Page<TCmCrossAndOriginaCross>();
		pageInfo.setList(list);
		pageInfo.setNum(list.size());
		pageInfo.setTotalCount((int) getCmUnitCrossInfoCount(reqMap));
		pageInfo.setPageIndex(Integer.parseInt(String.valueOf(reqMap.get("pageIndex"))));
		pageInfo.setPageSize(Integer.parseInt(String.valueOf(reqMap.get("pageSize"))));
		result.setPageInfo(pageInfo);
		return result; 
	}
	
	/**
	 * 查询 cmcross信息总条数
	 */
	public int getCmUnitCrossInfoCount(Map<String, Object> reqMap) {
		List<Map<String, Object>> list = tCmCrossMapper.getUnitCrossInfoCount(reqMap);
		int count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).intValue();  
		}
		return count;
	}
	
	/**
	 * 查询 cmcross信息总条数
	 */
	public int getCmUnitCrossTrainInfoCount(Map<String, Object> reqMap) {
		List<Map<String, Object>> list = tCmCrossMapper.getUnitCrossTrainInfoCount(reqMap);
		int count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).intValue();  
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.railway.passenger.transdispatch.comfirmedmap.service.ICmCrossService#getCrossInfoForCmCrossid_Phy(java.lang.String)
	 * 物理交路查询车次（之前的对数表查询）
	 */
	public List<TCmCross> getCrossInfoForCmCrossid_Phy(String cmCrossId){
		return (List<TCmCross>) tCmCrossMapper.selectPhyCrossByCmCrossId(cmCrossId);
	}
	
	/* (non-Javadoc)
	 * @see com.railway.passenger.transdispatch.comfirmedmap.service.ICmCrossService#deleteCmUnitCrossInfoForCorssIds(java.util.List)
	 * 删除交路
	 */
	public int deleteCmUnitCrossInfoForCorssIds(List<String> unitCrossIds){
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("cmCrossId", bf.toString());

		return  tCmCrossMapper.deleteCmUnitCrossInfoForCorssIds(reqMap);
	}
	
	/**
	 * 通过base_train_id查询列车时刻表，用于对数表和交路单元功能显示详点和简点
	 * 
	 * @param baseTrainId
	 *            列车id
	 * @return
	 */
	public List<TrainTimeInfoJbt> getTrainTimes(String baseTrainId) {
		return tCmCrossMapper.getTrainTimeInfoByTrainIdFromTrainlineJbt(baseTrainId);
	}
	
	public List<TrainTimeInfoJbt> getTrainTimesNp(String baseTrainId) {
		return tCmCrossMapper.getTrainTimeInfoByTrainIdFromTrainlineJbt(baseTrainId);
	}
	
	public List<TCmTrain> getTCmTrain(TCmCross cross) {
		return tCmTrainMapper.selectByCmCrossId(cross);
	}
	
	public TCmTrain getTCmTrain(String cmTrainId) {
		return tCmTrainMapper.selectByPrimaryKey(cmTrainId);
	}
	
	public int deleteTCmTrain(TCmTrain train){
		return tCmTrainMapper.deleteByPrimaryKey(train.getCmTrainId());
	}
	
	public int addTCmTrain(TCmTrain train){
		return tCmTrainMapper.insertSelective(train);
	}
	
	public TCmCross selectByCmOriginalCross(String cmOriginalCrossId){
		return tCmCrossMapper.selectByCmOriginalCross(cmOriginalCrossId);
	}
	
	public void delete(TCmCross cross){
		tCmCrossMapper.deleteByPrimaryKey(cross.getCmCrossId());
	}
	
	public int deleteTCmTrainAlternate(TCmTrainAlternate alternate){
		return tCmTrainAlternateMapper.deleteByPrimaryKey(alternate.getCmTrainAlternateId());
	}
	
	public int deleteTCmTrainRule(TCmTrainRule rule){
		return tCmTrainRuleMapper.deleteByPrimaryKey(rule.getCmTrainRuleId());
	}
	
	public int addTCmTrainAlternate(TCmTrainAlternate alternate){
		return tCmTrainAlternateMapper.insertSelective(alternate);
	}
	
	public int addTCmTrainRule(TCmTrainRule rule){
		return tCmTrainRuleMapper.insertSelective(rule);
	}
	
	/**
	    * 从基础数据库中获取18个路局的基本信息
	    * @return
	    */ 
    public List<Ljzd> getFullStationInfo(){
    	List<Ljzd> ljzdList = new ArrayList<Ljzd>();
    	if (ljzdList == null || ljzdList.size()==0) {
    		ljzdList = tCmCrossMapper.selectListBySql();
    	}
    	return ljzdList;
    }
    
    public List<TCmCross> selectAll(){
		return tCmCrossMapper.selectAll();
	}
    
    public TCmCross selectByCrossNameAndScheme(Map<String,Object> param){
    	return tCmCrossMapper.selectByCrossNameAndScheme(param);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List queryHomePage() {
		List l = new ArrayList();
		List listztjl = tCmCrossMapper.getHomePageSumZtjl();
		List listztysc = tCmCrossMapper.getHomePageSumZtysc();
		List listgljl = tCmCrossMapper.getHomePageSumGljl();
		List listglysc = tCmCrossMapper.getHomePageSumGlysc();
		Map mapr = new HashMap();
		Map map = new HashMap();
		map.put("B", "哈");
		map.put("T", "沈");
		map.put("P", "京");
		map.put("V", "太");
		map.put("C", "呼");
		map.put("F", "郑");
		map.put("N", "武");
		map.put("Y", "西");
		map.put("K", "济");
		map.put("H", "上");
		map.put("G", "南");
		map.put("Q", "广");
		map.put("Z", "宁");
		map.put("W", "成");
		map.put("M", "昆");
		map.put("J", "兰");
		map.put("R", "乌");
		map.put("O", "青");
		
		Iterator it= map.entrySet().iterator();
		Integer allhj=0;
		Integer ztxjhj=0;
		Integer ztyschj=0;
		Integer glxjhj=0;
		Integer glyschj=0;
		while(it.hasNext()){
			Entry entry=(Entry) it.next();
			String key=(String) entry.getKey();
			Map p = new HashMap();
			boolean f=true;
			for (int i = 0; i < listztjl.size(); i++) {
				Map m=(Map) listztjl.get(i);
				System.out.println(m.get("TOKEN_VEH_BUREAU"));
				if(m.get("TOKEN_VEH_BUREAU").equals(key)){
					p.put("ztjl", m.get("ZTJL"));
					f=false;
				}
			}
			if(f){
				p.put("ztjl", "0");
			}
			ztxjhj=ztxjhj+Integer.valueOf(p.get("ztjl").toString());
			boolean f2=true;
			for (int i = 0; i < listztysc.size(); i++) {
				Map m=(Map) listztysc.get(i);
				if(m.get("TOKEN_VEH_BUREAU").equals(key)){
					p.put("ztysc", m.get("ZTYSC"));
					f2=false;
				}
			}
			if(f2){
				p.put("ztysc", "0");
			}
			ztyschj=ztyschj+Integer.valueOf(p.get("ztysc").toString());
			boolean f3=true;
			for (int i = 0; i < listgljl.size(); i++) {
				Map m=(Map) listgljl.get(i);
				if(m.get("TOKEN_VEH_BUREAU").equals(key)){
					p.put("gljl", m.get("GLJL"));
					f3=false;
				}
			}
			if(f3){
				p.put("gljl","0");
			}
			glxjhj=glxjhj+Integer.valueOf(p.get("gljl").toString());
			boolean f4=true;
			for (int i = 0; i < listglysc.size(); i++) {
				Map m=(Map) listglysc.get(i);
				if(m.get("TOKEN_VEH_BUREAU").equals(key)){
					p.put("glysc", m.get("GLYSC"));
					f4=false;
				}
			}
			if(f4){
				p.put("glysc", "0");
			}
			glyschj=glyschj+Integer.valueOf(p.get("glysc").toString());
			p.put("hj", Integer.valueOf(p.get("ztjl")==null?"0":p.get("ztjl").toString())+Integer.valueOf(p.get("gljl")==null?"0":p.get("gljl").toString()));
			allhj=allhj+Integer.valueOf(p.get("hj").toString());
			p.put("jc", entry.getValue());
			p.put("key", entry.getKey());
			l.add(p);
		}
		Map a=new HashMap();
		a.put("jc", "合计");
		a.put("ztjl", ztxjhj);
		a.put("ztysc", ztyschj);
		a.put("gljl", glxjhj);
		a.put("glysc", glyschj);
		a.put("hj", allhj);
		l.add(a);
		return l;
	}
}
