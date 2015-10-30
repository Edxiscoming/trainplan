package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.BaseCrossTrainSubInfo;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.TrainTimeInfoJbt;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.PlanCrossDao;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPartOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCrossTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPartOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPhyCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPhyCrossTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainAlternateMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainRuleMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainStnMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmVersionMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.IComfirmedmapService;
import com.railway.passenger.transdispatch.operationplan.repository.PlanTrainNewMapper;
import com.railway.passenger.transdispatch.util.CrossInfoGenHelper;
import com.railway.passenger.transdispatch.util.RunDateGenHelper;
import com.railway.passenger.transdispatch.util.TimeUtils;

@Service
@Transactional
public class ComfirmedmapServiceImpl implements IComfirmedmapService {
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private CmCrossServiceImpl cmCrossServiceImpl;
	
	@Autowired
	private CmPhyCrossServiceImpl cmPhyCrossServiceImpl;
	
	@Autowired
	private CmOriginalCrossServiceImpl cmOriginalCrossServiceImpl;
	
	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private PlanCrossDao planCrossDao;
	
	@Autowired
	PlanTrainNewMapper planTrainNewMapper;
	
	@Autowired
	private TCmPartOriginalCrossMapper tCmPartOriginalCrossMapper;
	
	//根据原始对数表信息 删除其下面的逻辑交路-逻辑车次 物理交路-物理交路车次 开行计划-开行计划车次 
	@Override
	public boolean deleteAllInfoByTCmOriginalCross(TCmOriginalCross ocross){
		try{
			TCmCross delCross = cmCrossServiceImpl.selectByCmOriginalCross(ocross.getCmOriginalCrossId());
			if(delCross != null){
				//删除开行计划下的所有信息
				PlanCrossInfo plancross = planCrossDao.findByUnitCrossId(delCross.getCmCrossId());
				if(plancross != null){
					//删除开行计划下的车次信息
					planTrainNewMapper.deleteByPlanCrossId(plancross.getPlanCrossId());
					//删除开行计划cross
					planCrossDao.deleteById(plancross.getPlanCrossId());
				}
				//删除物理交路下的所有信息
				for(TCmPhyCross temp : cmPhyCrossServiceImpl.getTcmPhyCross(delCross.getCmCrossId())){
					//删除物理交路下的车次信息
					cmPhyCrossServiceImpl.delTCmPhyCrossTrain(temp.getCmPhyCrossId(), true);
					//删除物理交路cross
					cmPhyCrossServiceImpl.delTCmPhyCross(temp.getCmPhyCrossId(), true);
				}
				//删除逻辑交路下的所有信息
				List<TCmTrain> cmTrains = cmCrossServiceImpl.getTCmTrain(delCross);
				for(TCmTrain train : cmTrains){
					//删除车次的交替信息
					TCmTrainAlternate alternate = cmCrossServiceImpl.getTCmTrainAlternate(train.getCmTrainId());
					cmCrossServiceImpl.deleteTCmTrainAlternate(alternate);
					//删除车次的开行规律
					TCmTrainRule rule = cmCrossServiceImpl.getTCmTrainRule(train.getCmTrainId());
					cmCrossServiceImpl.deleteTCmTrainRule(rule);
					//删除车次
					cmCrossServiceImpl.deleteTCmTrain(train);
				}
				//删除逻辑交路
				cmCrossServiceImpl.delete(delCross);
			}
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public TCmCross generateTcmCrossInfo(TCmOriginalCross ocross) {
		try{
			deleteAllInfoByTCmOriginalCross(ocross);
			ShiroRealm.ShiroUser user = CrossInfoGenHelper.getUser();
			/*step 1 : 存TCmCross信息*/
			TCmCross cross = new TCmCross();
			cross.setCmCrossId(UUID.randomUUID().toString());
			cross.setCmCrossName(ocross.getCrossName());
			cross.setCreatPeople(user.getName());
			cross.setCreatPeopleOrg(user.getBureau());
			cross.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			cross.setUseStatus((short)0);
			cross.setCmOriginalCrossId(ocross.getCmOriginalCrossId());
			cross.setCheckFlag((short)1);
			cross.setCheckTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			cross.setCheckPeople(user.getName());
			cross.setCheckPeopleOrg(user.getBureau());
			cross.setCmVersionId(ocross.getCmVersionId());
			cross.setHighlineflag(ocross.getHighlineFlag());
			cmCrossServiceImpl.insertTCmCross(cross);
			
			/*step 2 : 解析对数表中的交路信息，解析为一个个的车次信息*/
			String [] trainNbrs = StringUtil.objToStr(ocross.getCrossName()).split("-");
			String [] alternateDates = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAlternateDate()).split("-"), trainNbrs.length);
			String [] alternateTrainNbrs = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAlternateTrainNbr()).split("-"), trainNbrs.length);
			String [] appointDays = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointDay()).split("-"), trainNbrs.length);
			String [] appointPeriods = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointPeriod()).split("-"), trainNbrs.length);
			String [] appointWeeks = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointWeek()).split("-"), trainNbrs.length);
			String [] commonlineRules = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getCommonlineRule()).split("-"), trainNbrs.length);
			String [] highlineRules = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getHighlineRule()).split("-"), trainNbrs.length);
			List<TCmTrain> trainlist =new ArrayList<TCmTrain>();
			final Map<String,TCmTrainAlternate> alternatemap=new HashMap<String, TCmTrainAlternate>();
			short trainSort = 1;
			for(int i = 0;i < trainNbrs.length;i++){
				String trainNbr = trainNbrs[i];
				/*step 3 : 存放每个车次的信息*/
				TCmTrain train = new TCmTrain();
				BaseTrainInfo baseTrain = baseDao.getBaseTrain(cmOriginalCrossServiceImpl.getTCmVersion(ocross.getCmVersionId()).getmTemplateScheme(), trainNbr).get(0);
				train.setCmTrainId(UUID.randomUUID().toString());
				train.setCmCrossId(cross.getCmCrossId());
				train.setTrainSort((short)trainSort++);
				train.setTrainNbr(trainNbrs[i]);
				train.setBaseTrainId(baseTrain.getBaseTrainId());
				train.setStartStn(baseTrain.getStartStn());
				train.setStartBureau(commonService.getStationPy(baseTrain.getStartBureauShortName()));
				train.setEndStn(baseTrain.getEndStn());
				train.setEndBureau(commonService.getStationPy(baseTrain.getEndBureanShortName()));
				train.setTokenVehBureau(ocross.getTokenVehBureau());
				train.setUseStatus((short)0);
				train.setEditFlag((short)0);
//				cmCrossServiceImpl.addTCmTrain(train);
				trainlist.add(train);
				/*step 3 : 存放每个车次的交替信息*/
				TCmTrainAlternate alternate = new TCmTrainAlternate();
				alternate.setCmTrainId(train.getCmTrainId());
				alternate.setCmTrainAlternateId(UUID.randomUUID().toString());
				alternate.setCreatPeople(user.getName());
				alternate.setCreatPeopleOrg(user.getBureauShortName());
				alternate.setUseStatus((short)0);
				alternate.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
				alternate.setCheckFlag((short)0);
				alternate.setAlternateDate(alternateDates[i]);
				alternate.setAlternateTrainNbr(alternateTrainNbrs[i]);
				cmCrossServiceImpl.addTCmTrainAlternate(alternate);
				alternatemap.put(train.getCmTrainId(), alternate);
				/*step 4 : 存放每个车次的开行规律*/
				TCmTrainRule rule = new TCmTrainRule();
				rule.setCmTrainRuleId(UUID.randomUUID().toString());
				rule.setCmTrainId(train.getCmTrainId());
				rule.setHighlineRule(highlineRules[i]);
				rule.setCommonlineRule(commonlineRules[i]);
				rule.setAppointWeek(appointWeeks[i]);
				rule.setAppointDay(appointDays[i]);
				rule.setAppointPeriod(appointPeriods[i]);
				rule.setUseStatus((short)0);
				cmCrossServiceImpl.addTCmTrainRule(rule);
			}
			
			/*按交替日期对交路中的车次进行排序*/
			Collections.sort(trainlist,new Comparator<TCmTrain>() {

				@Override
				public int compare(TCmTrain o1, TCmTrain o2) {
					Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, alternatemap.get(o1.getCmTrainId()).getAlternateDate());
					Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, alternatemap.get(o2.getCmTrainId()).getAlternateDate());
					if(o1Date.getTime() > o2Date.getTime()){
						return 1;
					}
					if(o1Date.getTime() == o2Date.getTime()){
						return 0;
					}
					if(o1Date.getTime() < o2Date.getTime()){
						return -1;
					}
					return 0;
					
				}
				
			});
			/*根据排序结果，取其中第一个车次为起始车次*/
			TCmTrain firstTrain = trainlist.get(0);
			/*按序号交路中的车次进行还原排序*/
			Collections.sort(trainlist,new Comparator<TCmTrain>() {

				@Override
				public int compare(TCmTrain o1, TCmTrain o2) {
					return o1.getTrainSort() - o2.getTrainSort();
				}
				
			});
			short temp_train_sort = 0;
			/*由取出来的车次开始 正向往后推重新定义trainSort*/
			for(int i = 0 ;i< trainlist.size();i++){
				if(temp_train_sort != 0){
					temp_train_sort = CrossInfoGenHelper.getNextTrainSort(temp_train_sort, trainNbrs.length);
					trainlist.get(i).setTrainSort(temp_train_sort);
				}
				if(trainlist.get(i).getCmTrainId().equals(firstTrain.getCmTrainId())){
					trainlist.get(i).setTrainSort((short)1);
					temp_train_sort = 1;
				}
			}
			temp_train_sort = 0;
			/*由取出来的车次开始 逆向往前推重新定义trainSort*/
			for(int i=trainlist.size()-1 ; i>=0;i--){
				if(temp_train_sort != 0){
					temp_train_sort = CrossInfoGenHelper.getPreTrainSort(temp_train_sort, trainNbrs.length);
					trainlist.get(i).setTrainSort(temp_train_sort);
				}
				if(trainlist.get(i).getCmTrainId().equals(firstTrain.getCmTrainId())){
					trainlist.get(i).setTrainSort((short)1);
					temp_train_sort = 1;
				}
			}
			/*保存tcmTrain信息*/
			for(TCmTrain train : trainlist){
				cmCrossServiceImpl.addTCmTrain(train);
			}
			return cross;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @Description: 查询出担当局的对数表信息
	 * @param @param cmPartOriginalCrossId
	 * @param @return   
	 * @return TCmPartOriginalCross  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月26日
	 */
	public TCmPartOriginalCross gettCmPartOriginalCross(String cmPartOriginalCrossId){
		return tCmPartOriginalCrossMapper.selectByPrimaryKey(cmPartOriginalCrossId);
	}
	
	public List<BaseCrossTrainInfo> getBaseCrossTrainInfo(String OriginalCrossid){
		TCmOriginalCross ocross=cmOriginalCrossServiceImpl.getTCmOriginalCross(OriginalCrossid);
		List<Object> li=getBaseInfo(ocross);
		return genernatePyCrossByRom(li);
	}
	public List<Object> getBaseInfo(TCmOriginalCross ocross) {
		List<Object> re=new ArrayList<Object>();
		try{
			ShiroRealm.ShiroUser user = CrossInfoGenHelper.getUser();
			/*step 1 : 存TCmCross信息*/
			TCmCross cross = new TCmCross();
			cross.setCmCrossId(UUID.randomUUID().toString());
			cross.setCmCrossName(ocross.getCrossName());
			cross.setCreatPeople(user.getName());
			cross.setCreatPeopleOrg(user.getBureau());
			cross.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			cross.setUseStatus((short)0);
			cross.setCmOriginalCrossId(ocross.getCmOriginalCrossId());
			cross.setCheckFlag((short)0);
//			cmCrossServiceImpl.insertTCmCross(cross);
			re.add(cross);
			re.add(ocross);
			/*step 2 : 解析对数表中的交路信息，解析为一个个的车次信息*/
			String [] trainNbrs = StringUtil.objToStr(ocross.getCrossName()).split("-");
			String [] alternateDates = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAlternateDate()).split("-"), trainNbrs.length);
			String [] alternateTrainNbrs = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAlternateTrainNbr()).split("-"), trainNbrs.length);
			String [] appointDays = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointDay()).split("-"), trainNbrs.length);
			String [] appointPeriods = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointPeriod()).split("-"), trainNbrs.length);
			String [] appointWeeks = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getAppointWeek()).split("-"), trainNbrs.length);
			String [] commonlineRules = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getCommonlineRule()).split("-"), trainNbrs.length);
			String [] highlineRules = CrossInfoGenHelper.analyseRules(StringUtil.objToStr(ocross.getHighlineRule()).split("-"), trainNbrs.length);
			short trainSort = 1;
			List<TCmTrain> trainlist =new ArrayList<TCmTrain>();
			final Map<String,TCmTrainAlternate> alternatemap=new HashMap<String, TCmTrainAlternate>();
			Map<String,TCmTrainRule>rulemap=new HashMap<String, TCmTrainRule>();
			for(int i = 0;i < trainNbrs.length;i++){
				List<Object> r=new ArrayList<Object>();
				String trainNbr = trainNbrs[i];
				/*step 3 : 存放每个车次的信息*/
				TCmTrain train = new TCmTrain();
				BaseTrainInfo baseTrain = baseDao.getBaseTrain(cmOriginalCrossServiceImpl.getTCmVersion(ocross.getCmVersionId()).getmTemplateScheme(), trainNbr).get(0);
				train.setCmTrainId(UUID.randomUUID().toString());
				train.setCmCrossId(cross.getCmCrossId());
				train.setTrainSort((short)trainSort++);
				train.setTrainNbr(trainNbrs[i]);
				train.setBaseTrainId(baseTrain.getBaseTrainId());
				train.setStartStn(baseTrain.getStartStn());
				train.setStartBureau(commonService.getStationPy(baseTrain.getStartBureauShortName()));
				train.setEndStn(baseTrain.getEndStn());
				train.setEndBureau(commonService.getStationPy(baseTrain.getEndBureanShortName()));
				train.setTokenVehBureau(ocross.getTokenVehBureau());
				train.setUseStatus((short)0);
				train.setEditFlag((short)0);
				train.setStartTime(baseTrain.getStartTime());
				train.setEndTime(baseTrain.getEndTime());
				train.setRunDays(baseTrain.getRunDays());
//				tCmTrainMapper.insertSelective(train);
				trainlist.add(train);
				/*step 3 : 存放每个车次的交替信息*/
				TCmTrainAlternate alternate = new TCmTrainAlternate();
				alternate.setCmTrainId(train.getCmTrainId());
				alternate.setCmTrainAlternateId(UUID.randomUUID().toString());
				alternate.setCreatPeople(user.getName());
				alternate.setCreatPeopleOrg(user.getBureauShortName());
				alternate.setUseStatus((short)1);
				alternate.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
				alternate.setCheckFlag((short)0);
				alternate.setAlternateDate(alternateDates[i]);
				alternate.setAlternateTrainNbr(alternateTrainNbrs[i]);
//				tCmTrainAlternateMapper.insertSelective(alternate);
				alternatemap.put(train.getCmTrainId(), alternate);
				/*step 4 : 存放每个车次的开行规律*/
				TCmTrainRule rule = new TCmTrainRule();
				rule.setCmTrainRuleId(UUID.randomUUID().toString());
				rule.setCmTrainId(train.getCmTrainId());
				rule.setHighlineRule(highlineRules[i]);
				rule.setCommonlineRule(commonlineRules[i]);
				rule.setAppointWeek(appointWeeks[i]);
				rule.setAppointDay(appointDays[i]);
				rule.setAppointPeriod(appointPeriods[i]);
				rule.setUseStatus((short)0);
//				tCmTrainRuleMapper.insertSelective(rule);
				rulemap.put(train.getCmTrainId(), rule);
			}
			/*按交替日期对交路中的车次进行排序*/
			Collections.sort(trainlist,new Comparator<TCmTrain>() {

				@Override
				public int compare(TCmTrain o1, TCmTrain o2) {
					Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, alternatemap.get(o1.getCmTrainId()).getAlternateDate());
					Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, alternatemap.get(o2.getCmTrainId()).getAlternateDate());
					if(o1Date.getTime() > o2Date.getTime()){
						return 1;
					}
					if(o1Date.getTime() == o2Date.getTime()){
						return 0;
					}
					if(o1Date.getTime() < o2Date.getTime()){
						return -1;
					}
					return 0;
					
				}
				
			});
			/*根据排序结果，取其中第一个车次为起始车次*/
			TCmTrain firstTrain = trainlist.get(0);
			/*按序号交路中的车次进行还原排序*/
			Collections.sort(trainlist,new Comparator<TCmTrain>() {

				@Override
				public int compare(TCmTrain o1, TCmTrain o2) {
					return o1.getTrainSort() - o2.getTrainSort();
				}
				
			});
			short temp_train_sort = 0;
			/*由取出来的车次开始 正向往后推重新定义trainSort*/
			for(int i = 0 ;i< trainlist.size();i++){
				if(temp_train_sort != 0){
					temp_train_sort = CrossInfoGenHelper.getNextTrainSort(temp_train_sort, trainNbrs.length);
					trainlist.get(i).setTrainSort(temp_train_sort);
				}
				if(trainlist.get(i).getCmTrainId().equals(firstTrain.getCmTrainId())){
					trainlist.get(i).setTrainSort((short)1);
					temp_train_sort = 1;
				}
			}
			temp_train_sort = 0;
			/*由取出来的车次开始 逆向往前推重新定义trainSort*/
			for(int i=trainlist.size()-1 ; i>=0;i--){
				if(temp_train_sort != 0){
					temp_train_sort = CrossInfoGenHelper.getPreTrainSort(temp_train_sort, trainNbrs.length);
					trainlist.get(i).setTrainSort(temp_train_sort);
				}
				if(trainlist.get(i).getCmTrainId().equals(firstTrain.getCmTrainId())){
					trainlist.get(i).setTrainSort((short)1);
					temp_train_sort = 1;
				}
			}
			
			re.add(trainlist);
			re.add(alternatemap);
			re.add(rulemap);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return re;
	}
	/**
	 * @Description: TODO
	 * @param @param ocross即通过对数表中的数据封装数据到TCmOriginalCross对象中，并在此对象中生成一个cmCrossId
	 * @param @param cmCrossIdcmTrains是一个以上面所生成的cmCrossId为键值，以TCmTrain的数据结构，并为每一个TCmTrain对象生成相对应的cmTrainId
	 * @param @param cmTrainIdalternate是一个以上面列车所对应的cmTrainId为键值，以alternate的数据结构。
	 * @param @param cmTrainIdrule是一个以上面列车所对应的cmTrainId为键值，以rule的数据结构。
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月25日
	 */
	public List<BaseCrossTrainInfo> genernatePyCrossByRom(List<Object> li){
		ShiroRealm.ShiroUser user = CrossInfoGenHelper.getUser();
		/*step1 获取所有需要的数据*/
		try{
		TCmOriginalCross ocross=(TCmOriginalCross) li.get(1);
		//逻辑交路下的车次信息
//		List<TCmTrain> cmTrains = cmCrossServiceImpl.getTCmTrain(cross);
		List<TCmTrain> cmTrains =(List<TCmTrain>) li.get(2);
		TCmCross cross=(TCmCross) li.get(0);
		/*step2 循环组数 生成各自组的车底交路数据*/
		List<TCmPhyCross> phyCrossList = new ArrayList<TCmPhyCross>();
		for(int groupNum = 1;groupNum <= ocross.getGroupTotalNbr();groupNum++){
			TCmPhyCross phyCross = new TCmPhyCross();
			phyCross.setCmCrossId(cross.getCmCrossId());
			phyCross.setCmPhyCrossId(groupNum+"");
			phyCross.setCreatPeople(user.getName());
			phyCross.setCreatPeopleOrg(user.getBureau());
			phyCross.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			phyCross.setUseStatus((short)0);
			phyCross.setCheckFlag((short)0);
			phyCross.setGroupSn((short)groupNum);
			phyCrossList.add(phyCross);
		}
		
		List<TCmPhyCrossTrain> phyTrainList = new ArrayList<TCmPhyCrossTrain>();
		
		/*step 3 生成当前组数下车次交路对应的车次数据(开行日期runDate) K1-K2-K3*/
		//记录每个车次的列表，按照key(车次在交路中的排序)-value(该车次在车底交路中的所有数据集合)存放
		Map<Short,List<TCmPhyCrossTrain>> sortTrainMap = new HashMap<Short, List<TCmPhyCrossTrain>>();
		for(TCmTrain train : cmTrains){
			if(!sortTrainMap.containsKey(train.getTrainSort())){
				sortTrainMap.put(train.getTrainSort(), new ArrayList<TCmPhyCrossTrain>());
			}
			String runDate = "";
			for(int cirNum=0;cirNum < ocross.getGroupTotalNbr(); cirNum++){//循环
				//车次的交替信息
				TCmTrainAlternate alternate = ((Map<String,TCmTrainAlternate> )li.get(3)).get(train.getCmTrainId());
				//车次的开行规律
				TCmTrainRule rule=((Map<String,TCmTrainRule>)li.get(4)).get(train.getCmTrainId());
				TCmPhyCrossTrain phyTrain = new TCmPhyCrossTrain();
				phyTrain.setCmPhyCrossTrainId(UUID.randomUUID().toString());
				if(train.getTrainSort() == 1){
					//第一个车次，给出组数信息
					phyTrain.setCmPhyCrossId(phyCrossList.get(cirNum).getCmPhyCrossId());
				} else {
					//其他车次 这里给不出组数
					phyTrain.setCmPhyCrossId("");
				}
				phyTrain.setCmTrainId(train.getCmTrainId());
				runDate = RunDateGenHelper.generateDate(runDate,alternate,rule);
				phyTrain.setRunDate(runDate);
				phyTrain.setUseStatus((short)0);
				phyTrain.setTrainSort(train.getTrainSort());
				sortTrainMap.get(train.getTrainSort()).add(phyTrain);
				phyTrainList.add(phyTrain);
			}
		}
		
		/*step 4 根据生成的车次信息，用算法选好其组数信息补全*/
		//第一次循环 按交路顺序与就近原则进行第一次连线，确定好一些phyTrain的组数
		for(Short sourceTrainSort : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> sourceTrains = sortTrainMap.get(sourceTrainSort);
			//循环每一个车次sort的所有train,与其下一个车次sort的train进行相同物理交路关联
			for(TCmPhyCrossTrain sourceTrain : sourceTrains){
				for(Short targetTrainSort : sortTrainMap.keySet()){
					//依次与下一轮车次进行比对 填写能够确定下来的物理交路ID
					if((targetTrainSort - sourceTrainSort) == 1){
						List<TCmPhyCrossTrain> targetTrains = sortTrainMap.get(targetTrainSort);
						for(TCmPhyCrossTrain targetTrain : targetTrains){
							TCmTrain target_cmTrain=null;
							for(int i=0;i<cmTrains.size();i++){
								if(cmTrains.get(i).getCmTrainId().equals(targetTrain.getCmTrainId())){
									target_cmTrain=cmTrains.get(i);
									break;
								}
							}
							TCmTrain source_cmTrain=null;
							for(int i=0;i<cmTrains.size();i++){
								if(cmTrains.get(i).getCmTrainId().equals(sourceTrain.getCmTrainId())){
									source_cmTrain=cmTrains.get(i);
									break;
								}
							}
							Date sourceEndDate =  getStartAndEndDateByPhyCrossTrain(source_cmTrain, sourceTrain.getRunDate()).get("endTime");
							Date targetStartDate = getStartAndEndDateByPhyCrossTrain(target_cmTrain, targetTrain.getRunDate()).get("startTime");
							if("".equals(targetTrain.getCmPhyCrossId()) && targetStartDate.getTime() > sourceEndDate.getTime()){
								//找最近的未被使用的下一组车次，给它与自己相同的物理交路ID，退出当前层的循环
								targetTrain.setCmPhyCrossId(sourceTrain.getCmPhyCrossId());
								break;
							}
						}
					}
				}
			}
		}
		//第二次循环 补充齐没有确定好组数的phyTrain的组数信息
		for(Short trainSort : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(trainSort);
			//正向推一次，将该车次按正向的逻辑交路ID补齐
			Integer tempSort = null;
			for(int i = 0;i < phyTrains.size();i++){
				TCmPhyCrossTrain phyTrain  = phyTrains.get(i);
				if(!"".equals(phyTrain.getCmPhyCrossId())){
					tempSort = this.getPhyCrossNumByCorssId(phyCrossList, phyTrain.getCmPhyCrossId());
				}
				if("".equals(phyTrain.getCmPhyCrossId()) && tempSort != null){
					tempSort = getNextSort(tempSort, ocross.getGroupTotalNbr());
					TCmPhyCross phyCross = phyCrossList.get(tempSort);
					phyTrain.setCmPhyCrossId(phyCross.getCmPhyCrossId());
				}
			}
			tempSort = null;
			//逆向推一次，将该车次按逆向的逻辑交路ID补齐
			for(int i= (phyTrains.size()-1);i>=0;i--){
				TCmPhyCrossTrain phyTrain  = phyTrains.get(i);
				if(!"".equals(phyTrain.getCmPhyCrossId())){
					tempSort = this.getPhyCrossNumByCorssId(phyCrossList, phyTrain.getCmPhyCrossId());
				}
				if("".equals(phyTrain.getCmPhyCrossId()) && tempSort != null){
					tempSort = getPreSort(tempSort, ocross.getGroupTotalNbr());
					TCmPhyCross phyCross = phyCrossList.get(tempSort);
					phyTrain.setCmPhyCrossId(phyCross.getCmPhyCrossId());
				}
			}
			
		}
		/**根据画图需要 将生成的物理车次的序号重新排序 start*/ 
		Map<String, List<TCmPhyCrossTrain>> phyCrossPhyTrainMap = new HashMap<String, List<TCmPhyCrossTrain>>();
		for(Short key : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(key);
			for(TCmPhyCrossTrain phyTrain:phyTrains){
				if(!phyCrossPhyTrainMap.containsKey(phyTrain.getCmPhyCrossId())){
					phyCrossPhyTrainMap.put(phyTrain.getCmPhyCrossId(), new ArrayList<TCmPhyCrossTrain>());
				}
				phyCrossPhyTrainMap.get(phyTrain.getCmPhyCrossId()).add(phyTrain);
			}
		}
		for(String phyCrossId : phyCrossPhyTrainMap.keySet()){
			List<TCmPhyCrossTrain> trains = phyCrossPhyTrainMap.get(phyCrossId);
			Collections.sort(trains,new Comparator<TCmPhyCrossTrain>() {

				@Override
				public int compare(TCmPhyCrossTrain o1, TCmPhyCrossTrain o2) {
					Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o1.getRunDate());
					Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o2.getRunDate());
					if(o1Date.getTime() > o2Date.getTime()){
						return 1;
					}
					if(o1Date.getTime() == o2Date.getTime()){
						return 0;
					}
					if(o1Date.getTime() < o2Date.getTime()){
						return -1;
					}
					return 0;
					
				}
				
			});
			//根据画交路图时的特殊需求，对不以第一个车开始的车次重新排序
			for(int i=1;i<=trains.size();i++){
				trains.get(i-1).setTrainSort((short)i);
			}
		}
		/**根据画图需要 将生成的物理车次的序号重新排序 end*/ 
		
		List<BaseCrossTrainInfo> bctifl = new ArrayList<BaseCrossTrainInfo>();
		//
		Map<String,List<TCmPhyCrossTrain>> lms = new HashMap<String,List<TCmPhyCrossTrain>>();
		for(Short key : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(key);
			for(TCmPhyCrossTrain phyTrain:phyTrains){
				if(lms!=null&&!lms.isEmpty()){
					if(lms.get(phyTrain.getCmPhyCrossId())==null){
						List<TCmPhyCrossTrain> l = new ArrayList<TCmPhyCrossTrain>();
						l.add(phyTrain);
						lms.put(phyTrain.getCmPhyCrossId(), l);
					}else{
						List<TCmPhyCrossTrain> l=lms.get(phyTrain.getCmPhyCrossId());
						l.add(phyTrain);
					}
				}else{
					List<TCmPhyCrossTrain> l = new ArrayList<TCmPhyCrossTrain>();
					l.add(phyTrain);
					lms.put(phyTrain.getCmPhyCrossId(), l);
				}
			}
		}
		Iterator it= (Iterator) lms.entrySet().iterator();
		int j=1;
		while(it.hasNext()){
			Map.Entry<String, List<TCmPhyCrossTrain>> entry = (Entry<String, List<TCmPhyCrossTrain>>) it.next();
			List<TCmPhyCrossTrain> lt=entry.getValue();
			BaseCrossTrainInfo bctif = new BaseCrossTrainInfo();
			bctif.setBaseCrossId(entry.getKey());
			List<BaseCrossTrainSubInfo> bf = new ArrayList<BaseCrossTrainSubInfo>();
			for (TCmPhyCrossTrain tCmPhyCrossTrain : lt) {
				
				BaseCrossTrainSubInfo b = new BaseCrossTrainSubInfo();
				
				b.setTrainSort(tCmPhyCrossTrain.getTrainSort());
				TCmTrain c=null;
				for (int i=0; i<cmTrains.size();i++) {
					c=cmTrains.get(i);
					if(c.getCmTrainId().equals(tCmPhyCrossTrain.getCmTrainId())){
						break;
					}
				}
				b.setBaseTrainId(c.getBaseTrainId());
				b.setTrainSort(tCmPhyCrossTrain.getTrainSort());
				b.setTrainNbr(c.getTrainNbr());
				b.setStartStn(c.getStartStn());
				b.setEndStn(c.getEndStn());
				if(StringUtil.isEmpty(c.getStartTime())){
					b.setStartTime(DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(tCmPhyCrossTrain.getRunDate(), TimeUtils.YYYYMMDD), c.getRunDays()), "yyyy-MM-dd")+" "+c.getEndTime());

				}else{
					b.setStartTime(DateUtil.formateDate(tCmPhyCrossTrain.getRunDate())+" "+c.getStartTime());
				}
				if(StringUtil.isEmpty(c.getEndTime())){
					b.setEndTime(DateUtil.formateDate(tCmPhyCrossTrain.getRunDate())+" "+c.getStartTime());
				}else{
					b.setEndTime(DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(tCmPhyCrossTrain.getRunDate(), TimeUtils.YYYYMMDD), c.getRunDays()), "yyyy-MM-dd")+" "+c.getEndTime());
	
				}
				b.setGroupSn(j+"");
				b.setBaseCrossTrainId(tCmPhyCrossTrain.getCmTrainId());
				List<BaseCrossTrainInfoTime> lb =new ArrayList<BaseCrossTrainInfoTime>();
				List<TrainTimeInfoJbt> ttfl= cmCrossServiceImpl.getTrainTimesNp(c.getBaseTrainId());
				for (int i=0;i<ttfl.size();i++) {
					BaseCrossTrainInfoTime ttf = new BaseCrossTrainInfoTime();
					TrainTimeInfoJbt jbt=(TrainTimeInfoJbt)ttfl.get(i);
					ttf.setPlanTrainId("");
					ttf.setStnName(jbt.getStnName());
					ttf.setStnSort(jbt.getChildIndex());
					ttf.setTrackName(jbt.getTrackName());
					if(jbt.getArrTime()==null||"".equals(jbt.getArrTime())){
						ttf.setArrTime(jbt.getDptTime());
					}else{
						ttf.setArrTime(jbt.getArrTime());
					}
					if(jbt.getDptTime()==null||"".equals(jbt.getDptTime())){
						ttf.setDptTime(jbt.getArrTime());
					}else{
						ttf.setDptTime(jbt.getDptTime());
					}
					ttf.setArrTime(DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(tCmPhyCrossTrain.getRunDate(), TimeUtils.YYYYMMDD), jbt.getArrRunDays()), "yyyy-MM-dd")+" "+ttf.getArrTime());

					if("ZDZ".equals(jbt.getStationFlag())){
						ttf.setDptTime(DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(tCmPhyCrossTrain.getRunDate(), TimeUtils.YYYYMMDD), jbt.getArrRunDays()), "yyyy-MM-dd")+" "+ttf.getDptTime());
					}
					else{
						ttf.setDptTime(DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(tCmPhyCrossTrain.getRunDate(), TimeUtils.YYYYMMDD), jbt.getRunDays()), "yyyy-MM-dd")+" "+ttf.getDptTime());
					}
					Date d=DateUtil.parseDate(ttf.getArrTime(), "yyyy-MM-dd hh:mm:ss");
					Date d2=DateUtil.parseDate(ttf.getDptTime(), "yyyy-MM-dd hh:mm:ss");
					Long t=d2.getTime()-d.getTime();
//					t/1000/60
					ttf.setStayTime((int)(t/1000/60));
					ttf.setNodeId(jbt.getNodeId());
					ttf.setBureauShortName(jbt.getBureauShortName());
					ttf.setStationType(jbt.getStationFlag());//
					lb.add(ttf);
				}
				b.setStationList(lb);
				bf.add(b);
			}
			bctif.setTrainList(bf);
			bctifl.add(bctif);
			j++;
		}
		System.out.println(bctifl.toArray());
		return bctifl;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean generateTcmPhyCrossInfo(TCmCross cross) {
		/**生成数据之前先删除之前的数据 start*/
		for(TCmPhyCross temp : cmPhyCrossServiceImpl.getTcmPhyCross(cross.getCmCrossId())){
			cmPhyCrossServiceImpl.delTCmPhyCross(temp.getCmPhyCrossId(), true);
			cmPhyCrossServiceImpl.delTCmPhyCrossTrain(temp.getCmPhyCrossId(), true);
		}
		/**生成数据之前先删除之前的数据 end*/
		ShiroRealm.ShiroUser user = CrossInfoGenHelper.getUser();
		/*step1 获取所有需要的数据*/
		TCmOriginalCross ocross = cmOriginalCrossServiceImpl.getTCmOriginalCross(cross.getCmOriginalCrossId());
		//逻辑交路下的车次信息
		List<TCmTrain> cmTrains = cmCrossServiceImpl.getTCmTrain(cross);
		/*step2 循环组数 生成各自组的车底交路数据*/
		List<TCmPhyCross> phyCrossList = new ArrayList<TCmPhyCross>();
		for(int groupNum = 1;groupNum <= ocross.getGroupTotalNbr();groupNum++){
			TCmPhyCross phyCross = new TCmPhyCross();
			phyCross.setCmCrossId(cross.getCmCrossId());
			phyCross.setCmPhyCrossId(UUID.randomUUID().toString());
			phyCross.setCreatPeople(user.getName());
			phyCross.setCreatPeopleOrg(user.getBureau());
			phyCross.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			phyCross.setUseStatus((short)1);
			phyCross.setCheckFlag((short)1);
			phyCross.setCheckTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
			phyCross.setCheckPeople(user.getName());
			phyCross.setCheckPeopleOrg(user.getBureau());
			phyCross.setGroupSn((short)groupNum);
			phyCrossList.add(phyCross);
			cmPhyCrossServiceImpl.insertTCmPhyCross(phyCross);
		}
		
		List<TCmPhyCrossTrain> checkList = new ArrayList<TCmPhyCrossTrain>();
		
		/*step 3 生成当前组数下车次交路对应的车次数据(开行日期runDate) K1-K2-K3*/
		//记录每个车次的列表，按照key(车次在交路中的排序)-value(该车次在车底交路中的所有数据集合)存放
		Map<Short,List<TCmPhyCrossTrain>> sortTrainMap = new HashMap<Short, List<TCmPhyCrossTrain>>();
		for(TCmTrain train : cmTrains){
			if(!sortTrainMap.containsKey(train.getTrainSort())){
				sortTrainMap.put(train.getTrainSort(), new ArrayList<TCmPhyCrossTrain>());
			}
			String runDate = "";
			for(int cirNum=0;cirNum < ocross.getGroupTotalNbr(); cirNum++){//循环
				//车次的交替信息
				TCmTrainAlternate alternate = cmCrossServiceImpl.getTCmTrainAlternate(train.getCmTrainId());
				//车次的开行规律
				TCmTrainRule rule = cmCrossServiceImpl.getTCmTrainRule(train.getCmTrainId());
				TCmPhyCrossTrain phyTrain = new TCmPhyCrossTrain();
				phyTrain.setCmPhyCrossTrainId(UUID.randomUUID().toString());
				if(train.getTrainSort() == 1){
					//第一个车次，给出组数信息
					phyTrain.setCmPhyCrossId(phyCrossList.get(cirNum).getCmPhyCrossId());
				} else {
					//其他车次 这里给不出组数
					phyTrain.setCmPhyCrossId("");
				}
				phyTrain.setCmTrainId(train.getCmTrainId());
				runDate = RunDateGenHelper.generateDate(runDate,alternate,rule);
				phyTrain.setRunDate(runDate);
				phyTrain.setUseStatus((short)1);
				phyTrain.setTrainSort(train.getTrainSort());
				sortTrainMap.get(train.getTrainSort()).add(phyTrain);
				checkList.add(phyTrain);
			}
		}
		
		/*step 4 根据生成的车次信息，用算法选好其组数信息补全*/
		//第一次循环 按交路顺序与就近原则进行第一次连线，确定好一些phyTrain的组数
		for(Short sourceTrainSort : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> sourceTrains = sortTrainMap.get(sourceTrainSort);
			//循环每一个车次sort的所有train,与其下一个车次sort的train进行相同物理交路关联
			for(TCmPhyCrossTrain sourceTrain : sourceTrains){
				for(Short targetTrainSort : sortTrainMap.keySet()){
					//依次与下一轮车次进行比对 填写能够确定下来的物理交路ID
					if((targetTrainSort - sourceTrainSort) == 1){
						List<TCmPhyCrossTrain> targetTrains = sortTrainMap.get(targetTrainSort);
						for(TCmPhyCrossTrain targetTrain : targetTrains){
							Date sourceEndDate =  getStartAndEndDateByPhyCrossTrain(sourceTrain.getCmTrainId(), sourceTrain.getRunDate()).get("endTime");
							Date targetStartDate = getStartAndEndDateByPhyCrossTrain(targetTrain.getCmTrainId(), targetTrain.getRunDate()).get("startTime");
							if("".equals(targetTrain.getCmPhyCrossId()) && targetStartDate.getTime() > sourceEndDate.getTime()){
								//找最近的未被使用的下一组车次，给它与自己相同的物理交路ID，退出当前层的循环
								targetTrain.setCmPhyCrossId(sourceTrain.getCmPhyCrossId());
								break;
							}
						}
					}
				}
			}
		}
		//第二次循环 补充齐没有确定好组数的phyTrain的组数信息
		for(Short trainSort : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(trainSort);
			//正向推一次，将该车次按正向的逻辑交路ID补齐
			Integer tempSort = null;
			for(int i = 0;i < phyTrains.size();i++){
				TCmPhyCrossTrain phyTrain  = phyTrains.get(i);
				if(!"".equals(phyTrain.getCmPhyCrossId())){
					tempSort = this.getPhyCrossNumByCorssId(phyCrossList, phyTrain.getCmPhyCrossId());
				}
				if("".equals(phyTrain.getCmPhyCrossId()) && tempSort != null){
					tempSort = getNextSort(tempSort, ocross.getGroupTotalNbr());
					TCmPhyCross phyCross = phyCrossList.get(tempSort);
					phyTrain.setCmPhyCrossId(phyCross.getCmPhyCrossId());
					
				}
			}
			tempSort = null;
			//逆向推一次，将该车次按逆向的逻辑交路ID补齐
			for(int i= (phyTrains.size()-1);i>=0;i--){
				TCmPhyCrossTrain phyTrain  = phyTrains.get(i);
				if(!"".equals(phyTrain.getCmPhyCrossId())){
					tempSort = this.getPhyCrossNumByCorssId(phyCrossList, phyTrain.getCmPhyCrossId());
				}
				if("".equals(phyTrain.getCmPhyCrossId()) && tempSort != null){
					tempSort = getPreSort(tempSort, ocross.getGroupTotalNbr());
					TCmPhyCross phyCross = phyCrossList.get(tempSort);
					phyTrain.setCmPhyCrossId(phyCross.getCmPhyCrossId());
				}
			}
			
		}
		/**根据画图需要 将生成的物理车次的序号重新排序 start*/ 
		Map<String, List<TCmPhyCrossTrain>> phyCrossPhyTrainMap = new HashMap<String, List<TCmPhyCrossTrain>>();
		for(Short key : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(key);
			for(TCmPhyCrossTrain phyTrain:phyTrains){
				if(!phyCrossPhyTrainMap.containsKey(phyTrain.getCmPhyCrossId())){
					phyCrossPhyTrainMap.put(phyTrain.getCmPhyCrossId(), new ArrayList<TCmPhyCrossTrain>());
				}
				phyCrossPhyTrainMap.get(phyTrain.getCmPhyCrossId()).add(phyTrain);
			}
		}
		for(String phyCrossId : phyCrossPhyTrainMap.keySet()){
			List<TCmPhyCrossTrain> trains = phyCrossPhyTrainMap.get(phyCrossId);
			Collections.sort(trains,new Comparator<TCmPhyCrossTrain>() {

				@Override
				public int compare(TCmPhyCrossTrain o1, TCmPhyCrossTrain o2) {
					Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o1.getRunDate());
					Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o2.getRunDate());
					if(o1Date.getTime() > o2Date.getTime()){
						return 1;
					}
					if(o1Date.getTime() == o2Date.getTime()){
						return 0;
					}
					if(o1Date.getTime() < o2Date.getTime()){
						return -1;
					}
					return 0;
					
				}
				
			});
			//根据画交路图时的特殊需求，对不以第一个车开始的车次重新排序
			for(int i=1;i<=trains.size();i++){
				trains.get(i-1).setTrainSort((short)i);
			}
		}
		/**根据画图需要 将生成的物理车次的序号重新排序 end*/ 
		
		//第三次循环 将生成好的数据插入到数据库中
		for(Short key : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyTrains = sortTrainMap.get(key);
			for(TCmPhyCrossTrain phyTrain:phyTrains){
				cmPhyCrossServiceImpl.insertTCmPhyCrossTrain(phyTrain);
			}
		}
		return true;
	}
	
	private int getPhyCrossNumByCorssId(List<TCmPhyCross> phyCrossList, String cmPhyCrossId){
		for(TCmPhyCross phyCross : phyCrossList){
			if(cmPhyCrossId.equals(phyCross.getCmPhyCrossId())){
				return phyCrossList.indexOf(phyCross);
			}
		}
		return 0;
	}
	
	private int getNextSort(int sort,int groupTotalNbr){
		if(sort == groupTotalNbr-1){
			return 0;
		} else {
			return sort+1;
		}
	}
	
	private int getPreSort(int sort,int groupTotalNbr){
		if(sort == 0){
			return groupTotalNbr-1;
		} else {
			return sort-1;
		}
	}
	
	//根据逻辑车路id以及物理交路车次开行时间获取该车次的起始和结束日期
	public Map<String, Date> getStartAndEndDateByPhyCrossTrain(String cmTrainId, String runDate){
		TCmTrain cmTrain = cmCrossServiceImpl.getTCmTrain(cmTrainId);
		BaseTrainInfo baseTrain = baseDao.getBaseTrain(cmTrain.getBaseTrainId());
		return CrossInfoGenHelper.getStartAndEndDateByBaseTrain(baseTrain, runDate);
	}
	public Map<String, Date> getStartAndEndDateByPhyCrossTrain(TCmTrain cmTrain, String runDate){
		BaseTrainInfo baseTrain = baseDao.getBaseTrain(cmTrain.getBaseTrainId());
		return CrossInfoGenHelper.getStartAndEndDateByBaseTrain(baseTrain, runDate);
	}

}
