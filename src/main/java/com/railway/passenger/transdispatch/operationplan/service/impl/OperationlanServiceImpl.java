package com.railway.passenger.transdispatch.operationplan.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.PlanCrossDao;
import org.railway.com.trainplan.service.SchemeService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.basicmap.original.entity.MTrainlineTemp;
import com.railway.basicmap.original.repository.MTrainlineTempMapper;
import com.railway.basicmap.original.service.impl.MTrainLineTempServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCrossTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPhyCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmCrossServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmOriginalCrossServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmPhyCrossServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.TCmVersionServiceImpl;
import com.railway.passenger.transdispatch.operationplan.entity.PlanTrainNew;
import com.railway.passenger.transdispatch.operationplan.entity.TCmPlanAutogenerate;
import com.railway.passenger.transdispatch.operationplan.repository.PlanTrainNewMapper;
import com.railway.passenger.transdispatch.operationplan.repository.TCmPlanAutogenerateMapper;
import com.railway.passenger.transdispatch.operationplan.service.IOperationlanService;
import com.railway.passenger.transdispatch.util.CrossInfoGenHelper;
import com.railway.passenger.transdispatch.util.LogUtil;
import com.railway.passenger.transdispatch.util.RunDateGenHelper;
import com.railway.passenger.transdispatch.util.TimeUtils;

@Service
public class OperationlanServiceImpl implements IOperationlanService {
	
	private static Log logger = LogFactory.getLog(OperationlanServiceImpl.class.getName());
	
	@Autowired
	SchemeService schemeService;
	
	@Autowired
	private CmCrossServiceImpl cmCrossServiceImpl;
	
	@Autowired
	private CmPhyCrossServiceImpl cmPhyCrossServiceImpl;
	
	@Autowired
	TCmPhyCrossMapper tCmPhyCrossMapper;
	
	@Autowired
	private CmOriginalCrossServiceImpl cmOriginalCrossServiceImpl;
	
	@Autowired
	private TCmTrainMapper tCmTrainMapper;
	
	@Autowired
	private MTrainlineTempMapper mTrainlineTempMapper;
	
	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private PlanCrossDao planCrossDao;
	
	@Autowired
	PlanTrainNewMapper planTrainNewMapper;
	
	@Autowired
	TCmPlanAutogenerateMapper tCmPlanAutogenerateMapper;
	
	@Autowired
	private MTrainLineTempServiceImpl mTrainLineTempServiceImpl;
	
	@Autowired
	TCmVersionServiceImpl tCmVersionServiceImpl;
	
	public Map<String, Object> autoGeneratePlan(ShiroRealm.ShiroUser user){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String userBureau = null;
		if(user != null){
			userBureau = user.getBureau();
		}
		Map<String, String> jcBureauMap = mTrainLineTempServiceImpl.getBureauNameDic();
		Map<String, List<TCmPlanAutogenerate>> bureauAutogenerateMap = getBureauTCmPlanAutogenerateMap();
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		CompletionService<String> completion = new ExecutorCompletionService<String>(pool);
		long startTime = System.currentTimeMillis();
		LogUtil.info(logger,TimeUtils.date2String(new Date(), TimeUtils.DEFAULTFORMAT) + " start auto generate plan!");
		Map<String, Object> map = new HashMap<String, Object>();
		int pageSize = 20;
		map.put("pageSize", pageSize);
		map.put("checkFlag", 1);//只对已审的原始对数表进行操作
		map.put("tokenVehBureau", userBureau);//指定了用户 则只查该用户所在担当局下的对数表信息
		int total = cmOriginalCrossServiceImpl.queryTotalCount(map);
		int totalPage = total/pageSize + 1;
		int allNum = 0;
		//分页查询数据进行处理
		for(int i=1;i<=totalPage;i++){
			map.put("pageIndex", i);
			com.railway.common.entity.Result<TCmOriginalCross> result = cmOriginalCrossServiceImpl.pageQueryCross(map);
			if(result.getPageInfo() == null || result.getPageInfo().getList() == null){
				LogUtil.info(logger,"auto generate plan no data,do nothing!");
				return resultMap;
			}
			
			for(final TCmOriginalCross ocross : result.getPageInfo().getList()){
				try{
					final TCmCross cross = cmCrossServiceImpl.selectByCmOriginalCross(ocross.getCmOriginalCrossId());
					String bureau = (String)jcBureauMap.get(ocross.getTokenVehBureau());
					TCmPlanAutogenerate autoGen = null;
					if(bureauAutogenerateMap.size()>0){
						//获取当前交路对应的自动生成开行计划设置
						for(TCmPlanAutogenerate obj : bureauAutogenerateMap.get(bureau)){
							if(ocross.getHighlineFlag().equals(obj.getHighlineFlag()+"")){
								autoGen = obj;
								break;
							}
						}
					}
					final TCmPlanAutogenerate fAutoGen = autoGen;
					if(cross != null && CrossInfoGenHelper.canGeneratePlan(cross)){
						allNum++;
						completion.submit(new Callable<String>() {
							@Override
							public String call() throws Exception {
								String result = "success";
								CrossInfoGenHelper.lockPlan(cross);
								LogUtil.info(logger,"【" +cross.getCmCrossId() + "-" + cross.getCmCrossName() + "】 start!");
								try{
									String startDate = TimeUtils.date2String(new Date(), TimeUtils.YYYYMMDD);
									String endDate = TimeUtils.getNextDateString(startDate, 40, TimeUtils.YYYYMMDD);
									if(fAutoGen != null){
										endDate = TimeUtils.getNextDateString(startDate, fAutoGen.getMaintainDays(), TimeUtils.YYYYMMDD);
									}
									generatePlanCrossInfo(cross, startDate, endDate);
									CrossInfoGenHelper.unlockPlan(cross);
								}catch(Exception e){
									result = "fail";
								}
								LogUtil.info(logger,"【" +cross.getCmCrossId() + "-" + cross.getCmCrossName() + "】 finished!");
								result = cross.getCmCrossName() + "," + result;
								return result;
							}
						});
					}
				} catch(Exception e){
					e.printStackTrace();
					logger.error(e.getMessage(),e);
				}
			}
		}
		// 基于ExecutorCompletionService
		// 线程管理模式，必须把结果集线程中的所有结果都显示的处理一次才认为当前线程完成了，当所有线程都被处理才表示当前线程组完成了
		int successNum = 0;
		int failNum = 0;
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < allNum; i++) {
			try {
				String result = completion.take().get();
				if(result.contains("success")){
					successNum++;
				}
				if(result.contains("fail")){
					sb.append((failNum++ + 1) + ". " + result.split(",")[0] + " ; ");
				}
				System.out.println("【"+result+"】");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultMap.put("totalNum", allNum);
		resultMap.put("successNum", successNum);
		resultMap.put("failNum", failNum);
		resultMap.put("failCrossInfo", sb.toString());
		pool.shutdown();
		long endTime = System.currentTimeMillis();
		System.out.println(TimeUtils.date2String(new Date(), TimeUtils.DEFAULTFORMAT) + "auto generate plan finished in 【" + (endTime-startTime) + "】millseconds");
		return resultMap;
	}
	
	public Map<String, List<TCmPlanAutogenerate>> getBureauTCmPlanAutogenerateMap(){
		Map<String, List<TCmPlanAutogenerate>> map = new HashMap<String, List<TCmPlanAutogenerate>>();
		for(TCmPlanAutogenerate tpa : getAll()){
			if(!map.containsKey(tpa.getTokenVehBureau())){
				map.put(tpa.getTokenVehBureau(), new ArrayList<TCmPlanAutogenerate>());
			}
			map.get(tpa.getTokenVehBureau()).add(tpa);
		}
		return map;
	}
	
	public List<TCmPlanAutogenerate> getAll(){
		Map<String, Object> map = new HashMap<String, Object>();
		return tCmPlanAutogenerateMapper.selectByParam(map);
	}
	
	public TCmPlanAutogenerate selectByParam(String tokenVehBureau,String highlineFlag){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tokenVehBureau", tokenVehBureau);
		map.put("highlineFlag", highlineFlag);
		List<TCmPlanAutogenerate> records = tCmPlanAutogenerateMapper.selectByParam(map);
		if(records != null && records.size()>0){
			return records.get(0);
		}
		return null;
	}
	
	public int update(TCmPlanAutogenerate record){
		return tCmPlanAutogenerateMapper.updateByPrimaryKeySelective(record);
	}
	
	public int save(TCmPlanAutogenerate record){
		return tCmPlanAutogenerateMapper.insertSelective(record);
	}
	
	public Map<String,Object> countByParams(Map<String,Object> param){
		Map<String,String> bureaus = mTrainLineTempServiceImpl.getBureauNameDic();
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = planTrainNewMapper.countByParams(param);
		if(list != null){
			for(Map<String,Object> temp : list){
				String bureau = (String)temp.get("START_BUREAU");
				String bureauJC = (String)bureaus.get(bureau);
				map.put(bureauJC,temp.get("NUM"));
			}
		}
		return map;
	}
	
	@Override
	public boolean generatePlanCrossInfo(TCmCross cross, String startDate, String endDate) {
		try{
			ShiroRealm.ShiroUser user = CrossInfoGenHelper.getUser();
			//原始对数表信息
			TCmOriginalCross ocross = cmOriginalCrossServiceImpl.getTCmOriginalCross(cross.getCmOriginalCrossId());
			PlanCrossInfo plancross = findByCmCrossId(cross.getCmCrossId());
			//组号-车次ID-车次序号
			Map<Short,Map<String,Short>> groupSnTrainNbrTrainSortMap = new HashMap<Short, Map<String,Short>>();
			prepareGroupSnTrainNbrTrainSortMap(cross, groupSnTrainNbrTrainSortMap);
			//计算得到计划结束日期用的List
			List<String> endDateList = new ArrayList<String>();
			//存放生成好的所有开行计划下的车次信息
			List<PlanTrainNew> allPlanTrainList = new ArrayList<PlanTrainNew>();
			//基本图以及对数表改变后需要进行交替的数据
			Map<Short,TCmTrainAndAlternateVo> newSchemeAlternateMap = new HashMap<Short, TCmTrainAndAlternateVo>();
			prepareNextCrossAlternateData(ocross,plancross,newSchemeAlternateMap);
			Map<Short,PlanTrainNew> preSchemeAlternateMap = new HashMap<Short, PlanTrainNew>();
			preparePreCrossAlternateData(ocross, plancross, preSchemeAlternateMap);
			//根据传入的参数确定要生成的计划截止日期，按照该日期来生成数据
			String planEndDate = endDate;
			if(plancross == null){
				/**准备拼装车底交路生成开行计划所需数据*/
				//记录每个车次的列表，按照key(车次在交路中的排序)-value(该车次在车底交路中的所有数据集合)存放
				Map<Short,List<TCmPhyCrossTrain>> sortTrainMap = new HashMap<Short, List<TCmPhyCrossTrain>>();
				preparePhyInfoData(cross, sortTrainMap);
				/**根据准备好的数据生成开行计划*/
				/*step 1 生成planCross*/
				TCmVersion version = tCmVersionServiceImpl.selectByPrimaryKey(ocross.getCmVersionId());
				plancross = new PlanCrossInfo();
				//沿用原始对数表中的部分数据
				BeanUtils.copyProperties(plancross, ocross);
				plancross.setPlanCrossId(UUID.randomUUID().toString());
				plancross.setBaseCrossId("");
				plancross.setUnitCrossId(cross.getCmCrossId());
				plancross.setBaseChartId(version.getmTemplateScheme());
				plancross.setBaseChartName(version.getName());
				plancross.setCrossStartDate(startDate);
				plancross.setCrossSpareName("");
				plancross.setSpareFlag("1");
				plancross.setCreatePeople(user.getName());
				plancross.setCreatePeopleOrg(user.getBureau());
				plancross.setCreateTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
				plancross.setCheckType(0);
				plancross.setIsAutoGenerate(1);
				plancross.setCmCrossId(cross.getCmCrossId());
				plancross.setHighlineFlag(ocross.getHighlineFlag());
				/*step 2 根据车底交路的车次信息 生成planCrossTrain*/
				for(Short sort : sortTrainMap.keySet()){
					List<TCmPhyCrossTrain> phyCrossTrainList = sortTrainMap.get(sort);
					PlanTrainNew temp_plantrain = null;
					while(true){
						String runDate = "";
						if(temp_plantrain == null){
							/*生成第一条数据,根据phyCrossTrain及其相关数据生成开行计划的train*/
							TCmPhyCrossTrain phyTrain = phyCrossTrainList.get(0);//排序后第一条数据就是该车次生成开行计划的起始车次
							//逻辑交路车次信息
							TCmTrain cmTrain = tCmTrainMapper.selectByPrimaryKey(phyTrain.getCmTrainId());
							//车次的交替信息
							TCmTrainAlternate alternate = cmCrossServiceImpl.getTCmTrainAlternate(cmTrain.getCmTrainId());
							//车次的开行规律
							TCmTrainRule rule = cmCrossServiceImpl.getTCmTrainRule(cmTrain.getCmTrainId());
							runDate = phyTrain.getRunDate();//RunDateGenHelper.generateDate(phyTrain.getRunDate(), alternate, rule);
							//生成出来的开行日期在planDate之后 结束循环
							if(TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, runDate).getTime() - TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, planEndDate).getTime() > 0){
								break;
							}
							PlanTrainNew plantrain = generatePlanTrainNew(plancross, phyTrain, cmTrain, rule, groupSnTrainNbrTrainSortMap, runDate);
							plantrain.setRunDate(runDate);//开行日期
							plantrain.setOrgCmTrainId(plantrain.getCmTrainId());
							
							/**交替简单连线*/
							if(preSchemeAlternateMap.containsKey(cmTrain.getTrainSort())){
								PlanTrainNew linkTrain = preSchemeAlternateMap.get(cmTrain.getTrainSort());
								plantrain.setPreTrainId(linkTrain.getPlanTrainId());
							}
							/**交替简单连线*/
							
							temp_plantrain = plantrain;
							allPlanTrainList.add(plantrain);
							endDateList.add(runDate);
						} else {
							/*生成第一条以后的数据*/
							
							/*检测交替 start*/
							//逻辑交路车次信息
							TCmTrain cmTrain = tCmTrainMapper.selectByPrimaryKey(temp_plantrain.getCmTrainId());
							TCmTrainAndAlternateVo vo = newSchemeAlternateMap.get(cmTrain.getTrainSort());
							if(vo != null && vo.getAlternate().getAlternateDate().equals(temp_plantrain.getRunDate())){
								//有交替了 则该计划不往后滚动生成了
								break;
								//需要交替,交替车次为新方案以及新对数表中的车次
//								temp_plantrain.setCmTrainId(vo.getCmTrain().getCmTrainId());
//								planTrainNewMapper.updateByPrimaryKeySelective(temp_plantrain);
							}
							/*检测交替 end*/
							
							//车次的交替信息
							TCmTrainAlternate alternate = cmCrossServiceImpl.getTCmTrainAlternate(temp_plantrain.getCmTrainId());
							//车次的开行规律
							TCmTrainRule rule = cmCrossServiceImpl.getTCmTrainRule(temp_plantrain.getCmTrainId());
							runDate = RunDateGenHelper.generateDate(temp_plantrain.getRunDate(), alternate, rule);
							//生成出来的开行日期在planDate之后 结束循环
							if(TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, runDate).getTime() - TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, planEndDate).getTime() > 0){
								break;
							}
							PlanTrainNew plantrain = generatePlanTrainNew(plancross, temp_plantrain, groupSnTrainNbrTrainSortMap, runDate);
							plantrain.setRunDate(runDate);//开行日期
							temp_plantrain = plantrain;
							allPlanTrainList.add(plantrain);
							endDateList.add(runDate);
						}
					}
				}
				/*step last 将生成好的数据写入数据库*/
				//排序 得到计划交路结束日期
				Collections.sort(endDateList,new Comparator<String>() {
					//将时间降序排列
					@Override
					public int compare(String o1, String o2) {
						Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o1);
						Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o2);
						if(o1Date.getTime() > o2Date.getTime()){
							return -1;
						}
						if(o1Date.getTime() == o2Date.getTime()){
							return 0;
						}
						if(o1Date.getTime() < o2Date.getTime()){
							return 1;
						}
						return 0;
					}
					
				});
				//取生成的计划中最后一个日期作为计划交路结束日期
				if(endDateList != null && endDateList.size()>0){
					plancross.setCrossEndDate(endDateList.get(0));
					planCrossDao.save(plancross);
				}
			} else {
				/**根据以前生成好的开行计划车次信息生成开行计划*/
				for(TCmTrain cmTrain : tCmTrainMapper.selectByCmCrossId(cross.getCmCrossId())){
					//获取每个车次最后一个rundate的车次信息 用此为基准产生开行计划
					PlanTrainNew temp_plantrain = selectLatestRunDatePlanTrain(plancross.getPlanCrossId(), cmTrain.getCmTrainId());
					while(true){
						
						/*检测交替 start*/
						TCmTrainAndAlternateVo vo = newSchemeAlternateMap.get(cmTrain.getTrainSort());
						if(vo != null && vo.getAlternate().getAlternateDate().equals(temp_plantrain.getRunDate())){
							//有交替了 则该计划不往后滚动生成了
							break;
							//需要交替,交替车次为新方案以及新对数表中的车次
//							temp_plantrain.setCmTrainId(vo.getCmTrain().getCmTrainId());
//							planTrainNewMapper.updateByPrimaryKeySelective(temp_plantrain);
						}
						/*检测交替 end*/
						
						String runDate = "";
						/*生成第一条以后的数据*/
						//车次的交替信息
						TCmTrainAlternate alternate = cmCrossServiceImpl.getTCmTrainAlternate(temp_plantrain.getCmTrainId());
						//车次的开行规律
						TCmTrainRule rule = cmCrossServiceImpl.getTCmTrainRule(temp_plantrain.getCmTrainId());
						runDate = RunDateGenHelper.generateDate(temp_plantrain.getRunDate(), alternate, rule);
						//生成出来的开行日期在planDate之后 结束循环
						if(TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, runDate).getTime() - TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, planEndDate).getTime() > 0){
							break;
						}
						PlanTrainNew plantrain = generatePlanTrainNew(plancross, temp_plantrain, groupSnTrainNbrTrainSortMap, runDate);
						plantrain.setRunDate(runDate);//开行日期
						temp_plantrain = plantrain;
						endDateList.add(runDate);
						allPlanTrainList.add(plantrain);
					}
				}
				
				/*step last 将生成好的数据写入数据库*/
				//排序 得到计划交路结束日期
				Collections.sort(endDateList,new Comparator<String>() {
					//将时间降序排列
					@Override
					public int compare(String o1, String o2) {
						Date o1Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o1);
						Date o2Date = TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, o2);
						if(o1Date.getTime() > o2Date.getTime()){
							return -1;
						}
						if(o1Date.getTime() == o2Date.getTime()){
							return 0;
						}
						if(o1Date.getTime() < o2Date.getTime()){
							return 1;
						}
						return 0;
					}
					
				});
				if(endDateList.size()>0){
					//取生成的计划中最后一个日期作为计划交路结束日期
					plancross.setCrossEndDate(endDateList.get(0));
					planCrossDao.update(plancross);
				}
			}
			/*存放生成好的开行计划车次数据*/
			//为planTrain添加preTrainId和nextTrainId
			genereatePreAndNextTrainId(allPlanTrainList);
			for(PlanTrainNew plantrain : allPlanTrainList){
				planTrainNewMapper.insertSelective(plantrain);
			}
			return true;
			
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void genereatePreAndNextTrainId(List<PlanTrainNew> allPlanTrainList){
		Map<Short, List<PlanTrainNew>> groupSnTrainsMap = new HashMap<Short, List<PlanTrainNew>>();
		//将所有车次按照组数进行分组
		for(PlanTrainNew train : allPlanTrainList){
			if(!groupSnTrainsMap.containsKey(train.getGroupSerialNbr())){
				groupSnTrainsMap.put(train.getGroupSerialNbr(), new ArrayList<PlanTrainNew>());
			}
			groupSnTrainsMap.get(train.getGroupSerialNbr()).add(train);
		}
		//对分组好的数据按时间进行排序，排序出来的结果 就是具有前后续关系的数据
		for(Short groupSn : groupSnTrainsMap.keySet()){
			List<PlanTrainNew> planTrainList = groupSnTrainsMap.get(groupSn);
			//排序 得到计划交路结束日期
			Collections.sort(planTrainList,new Comparator<PlanTrainNew>() {
				//将时间升排列
				@Override
				public int compare(PlanTrainNew o1, PlanTrainNew o2) {
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
			PlanTrainNew temp = null;
			//顺推一次 填写preTrainId
			for(int i=0;i<planTrainList.size();i++){
				PlanTrainNew train = planTrainList.get(i);
				if(temp != null){
					train.setPreTrainId(temp.getPlanTrainId());
				}
				temp = train;
			}
			//逆推一次 填写nextTrainId
			temp = null;
			for(int i=planTrainList.size()-1;i>=0;i--){
				PlanTrainNew train = planTrainList.get(i);
				if(temp != null){
					train.setNextTrainId(temp.getPlanTrainId());
				}
				temp = train;
			}
		}
		
	}
	
	//从物理交路车次生成开行计划车次
	private PlanTrainNew generatePlanTrainNew(PlanCrossInfo plancross, TCmPhyCrossTrain phyTrain, TCmTrain cmTrain, TCmTrainRule rule
			, Map<Short,Map<String,Short>> groupSnTrainNbrTrainSortMap, String runDate) 
			throws IllegalAccessException, InvocationTargetException{
		PlanTrainNew plantrain = new PlanTrainNew();
		BeanUtils.copyProperties(plantrain, rule);
		
		plantrain.setEndBureau(cmTrain.getEndBureau());
		plantrain.setEndStn(cmTrain.getEndStn());
		plantrain.setStartBureau(cmTrain.getStartBureau());
		plantrain.setStartStn(cmTrain.getStartStn());
		plantrain.setTrainNbr(cmTrain.getTrainNbr());
		plantrain.setBaseTrainId(cmTrain.getBaseTrainId());
		plantrain.setTokenVehBureau(cmTrain.getTokenVehBureau());
		
		BeanUtils.copyProperties(plantrain, phyTrain);
		plantrain.setPlanTrainId(UUID.randomUUID().toString());
		plantrain.setPlanCrossId(plancross.getPlanCrossId());
		plantrain.setBaseChartId(plancross.getBaseChartId());
		//组号 设置为物理交路中的groupSn组序号信息，由物理交路生成的第一条数据，保持组号与原数据相同
		plantrain.setGroupSerialNbr(tCmPhyCrossMapper.selectByPrimaryKey(phyTrain.getCmPhyCrossId()).getGroupSn());
		plantrain.setCmTrainId(cmTrain.getCmTrainId());
		//train序号，根据车底交路中车次的序号来定 每一组的每个车次都有一个固定的序号
		plantrain.setTrainSort(groupSnTrainNbrTrainSortMap.get(plantrain.getGroupSerialNbr()).get(plantrain.getCmTrainId()));
		//补充填写基本图中的数据
		MTrainlineTemp baseTrain = mTrainlineTempMapper.selectByPrimaryKey(cmTrain.getBaseTrainId());
		setBaseTrainInfo(baseTrain, plantrain);
		
		Map<String, Date> startAndEndTimeMap = CrossInfoGenHelper.getStartAndEndDateByBaseTrain(baseDao.getBaseTrain(cmTrain.getBaseTrainId()), runDate);
		plantrain.setStartTime(startAndEndTimeMap.get("startTime"));
		plantrain.setEndTime(startAndEndTimeMap.get("endTime"));
		//创建人信息
		plantrain.setCreatPeople(CrossInfoGenHelper.getUser().getName());
		plantrain.setCreatPeopleOrg(CrossInfoGenHelper.getUser().getBureau());
		
		plantrain.setHighlineFlag(Short.valueOf(plancross.getHighlineFlag()));
		plantrain.setSpareFlag(Short.valueOf(plancross.getSpareFlag()));
		
		generatePlanTrainSign(plantrain);
		return plantrain;
	}
	
	private void setBaseTrainInfo(MTrainlineTemp baseTrain, PlanTrainNew plantrain){
//		BaseTrainInfo baseTrain = baseDao.getBaseTrain(cmTrain.getBaseTrainId());
		plantrain.setBusiness(baseTrain.getBusiness());
		plantrain.setStartBureauId(baseTrain.getSourceBureauId());
		plantrain.setStartStnId(baseTrain.getSourceNodeId());
		plantrain.setEndBureauId(baseTrain.getTargetBureauId());
		plantrain.setEndStnId(baseTrain.getTargetNodeId());
		plantrain.setEndDays(baseTrain.getTargetTimeScheduleDates()+"");
		plantrain.setCreatTime(new Date());
		//创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；4:人工添加）
		plantrain.setCreatType((short)0);
		plantrain.setRouteId(baseTrain.getRouteId());
		plantrain.setTrainTypeId(baseTrain.getTypeId());
		
		plantrain.setStartStationStnId(baseTrain.getSourceNodeStationId());
		plantrain.setStartStationStnName(baseTrain.getSourceNodeStationName());
		plantrain.setStartStnTdcsId(baseTrain.getSourceNodeTdcsId());
		plantrain.setStartStnTdcsName(baseTrain.getSourceNodeTdcsName());
		plantrain.setEndStationStnId(baseTrain.getTargetNodeStationId());
		plantrain.setEndStationStnName(baseTrain.getTargetNodeStationName());
		plantrain.setEndStnTdcsId(baseTrain.getTargetNodeTdcsId());
		plantrain.setEndStnTdcsName(baseTrain.getTargetNodeTdcsName());
		
		plantrain.setStartBureauFull(baseTrain.getSourceBureauName());
		plantrain.setEndBureauFull(baseTrain.getTargetBureauName());
		plantrain.setPassBureau(baseTrain.getRouteBureauShortNames());
	}
	
	//从开行计划车次生成开行计划车次
	private PlanTrainNew generatePlanTrainNew(PlanCrossInfo plancross, PlanTrainNew oldPlanTrainNew
			, Map<Short,Map<String,Short>> groupSnTrainNbrTrainSortMap, String runDate) 
			throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException{
		PlanTrainNew plantrain = new PlanTrainNew();
		plantrain = (PlanTrainNew)BeanUtils.cloneBean(oldPlanTrainNew);
		plantrain.setPlanTrainId(UUID.randomUUID().toString());
		//组号 设置为物理交路中的groupSn组序号信息
		plantrain.setGroupSerialNbr(getNextGroupSn(oldPlanTrainNew.getGroupSerialNbr(), plancross.getGroupTotalNbr()));
		//train序号，根据车底交路中车次的序号来定 每一组的每个车次都有一个固定的序号
		plantrain.setTrainSort(groupSnTrainNbrTrainSortMap.get(plantrain.getGroupSerialNbr()).get(plantrain.getOrgCmTrainId()));
		
		Map<String, Date> startAndEndTimeMap = CrossInfoGenHelper.getStartAndEndDateByBaseTrain(baseDao.getBaseTrain(plantrain.getBaseTrainId()), runDate);
		plantrain.setStartTime(startAndEndTimeMap.get("startTime"));
		plantrain.setEndTime(startAndEndTimeMap.get("endTime"));
		
		//创建人信息
		plantrain.setCreatPeople(CrossInfoGenHelper.getUser().getName());
		plantrain.setCreatPeopleOrg(CrossInfoGenHelper.getUser().getBureau());
		plantrain.setCreatTime(new Date());
		
		plantrain.setHighlineFlag(Short.valueOf(plancross.getHighlineFlag()));
		plantrain.setSpareFlag(Short.valueOf(plancross.getSpareFlag()));
		
		generatePlanTrainSign(plantrain);
		return plantrain;
	}
	
	//列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
	private void generatePlanTrainSign(PlanTrainNew plantrain){
		plantrain.setPlanTrainSign(plantrain.getRunDate() 
				+ "-" + plantrain.getTrainNbr() 
				+ "-" + plantrain.getStartStn() 
				+ "-" + TimeUtils.date2String(plantrain.getStartTime(), TimeUtils.DEFAULTFORMAT));
	}
	
	//获取下一组的组号
	private Short getNextGroupSn(Short correntGroupSn, int groupTotalNbr){
		if(correntGroupSn == groupTotalNbr){
			return 1;
		}
		return ++correntGroupSn;
	}
	
	private void prepareGroupSnTrainNbrTrainSortMap(TCmCross cross, Map<Short,Map<String,Short>> groupSnTrainNbrTrainSortMap){
		List<TCmPhyCross> phyCrossList = cmPhyCrossServiceImpl.getTcmPhyCross(cross.getCmCrossId());
		for(TCmPhyCross phyCross : phyCrossList){
			groupSnTrainNbrTrainSortMap.put(phyCross.getGroupSn(), new HashMap<String, Short>());
			//车底交路下车次列表
			List<TCmPhyCrossTrain> phyTrainList = cmPhyCrossServiceImpl.getTcmPhyCrossTrain(phyCross.getCmPhyCrossId());
			for(TCmPhyCrossTrain phyTrain : phyTrainList){
				//逻辑交路车次信息
				TCmTrain train = tCmTrainMapper.selectByPrimaryKey(phyTrain.getCmTrainId());
				groupSnTrainNbrTrainSortMap.get(phyCross.getGroupSn()).put(train.getCmTrainId(), phyTrain.getTrainSort());
			}
		}
	}
	
	private void preparePhyInfoData(TCmCross cross, Map<Short,List<TCmPhyCrossTrain>> sortTrainMap){
		List<TCmPhyCross> phyCrossList = cmPhyCrossServiceImpl.getTcmPhyCross(cross.getCmCrossId());
		/*step 1 获取车底交路下的车次信息并按每个车次进行存放*/
		for(TCmPhyCross phyCross : phyCrossList){
			//车底交路下车次列表
			List<TCmPhyCrossTrain> phyTrainList = cmPhyCrossServiceImpl.getTcmPhyCrossTrain(phyCross.getCmPhyCrossId());
			for(TCmPhyCrossTrain phyTrain : phyTrainList){
				//逻辑交路车次信息
				TCmTrain train = tCmTrainMapper.selectByPrimaryKey(phyTrain.getCmTrainId());
				if(!sortTrainMap.containsKey(train.getTrainSort())){
					sortTrainMap.put(train.getTrainSort(), new ArrayList<TCmPhyCrossTrain>());
				}
				sortTrainMap.get(train.getTrainSort()).add(phyTrain);
			}
		}
		
		/*step 2 按时间升序排序每个车次列表*/
		for(Short sort : sortTrainMap.keySet()){
			List<TCmPhyCrossTrain> phyCrossTrainList = sortTrainMap.get(sort);
			Collections.sort(phyCrossTrainList,new Comparator<TCmPhyCrossTrain>() {
				
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
		}
	}
	
	//准备下一个方案对应的交替数据
	private void prepareNextCrossAlternateData(TCmOriginalCross ocross, PlanCrossInfo plancross, Map<Short,TCmTrainAndAlternateVo> newSchemeAlternateMap){
		TCmVersion target_version = tCmVersionServiceImpl.getNextVersion(ocross.getCmVersionId());
		/*判断是否需要交替*/
		if(target_version != null){
			/*查找target_scheme下是否有相应的tcmCross*/
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("cmCrossName", ocross.getCrossName());
			if(StringUtils.isNotBlank(ocross.getAlternateTrainNbr())){
				param.put("cmCrossName", ocross.getAlternateTrainNbr());
			}
			param.put("mTemplateScheme", target_version.getmTemplateScheme());
			TCmCross newCmCross = cmCrossServiceImpl.selectByCrossNameAndScheme(param);
			if(newCmCross != null){
				/*获取新交路下车次信息*/
				List<TCmTrain> newCmTrainList = tCmTrainMapper.selectByCmCrossId(newCmCross.getCmCrossId());
				List<TCmTrain> oldCmTrainList = null;
				if(plancross !=null ){
					oldCmTrainList = tCmTrainMapper.selectByCmCrossId(plancross.getCmCrossId());
				}
				for(int i = 0;i < newCmTrainList.size();i++){
					TCmTrain newTrain  = newCmTrainList.get(i);
					TCmTrainAlternate newAlternate = cmCrossServiceImpl.getTCmTrainAlternate(newTrain.getCmTrainId());
					
					/*获取新车次的交替信息*/
					/*获取该车次在已生成的开行计划中最后一个rundate的数据*/
					if(plancross != null){
						TCmTrain oldTrain  = oldCmTrainList.get(i);
//						PlanTrainNew latestTrain = selectLatestRunDatePlanTrain(plancross.getPlanCrossId(), oldTrain.getCmTrainId());
//						/*查询出来的最后一条数据的cmTrainId与新方案对数表中车次的cmTrainId不同的时候，执行删除操作*/
						//删除交替日期
						param.clear();
						param.put("planCrossId", plancross.getPlanCrossId());
						param.put("orgCmTrainId", oldTrain.getCmTrainId());
						param.put("runDate", newAlternate.getAlternateDate());
						//删除该交路已产生的，开行日期在新对数表交路中交替日期之后的，该车次的信息
						planTrainNewMapper.deleteByParams(param);
					}
					newSchemeAlternateMap.put(newTrain.getTrainSort(), new TCmTrainAndAlternateVo(newTrain, newAlternate));
				}
			}
		}
		
	}
	
		/**
		 * 
		 * @Description: 方案交替，简单连线策略
		 * @param @param ocross
		 * @param @param plancross
		 * @param @param preSchemeAlternateMap   
		 * @return void  
		 * @throws
		 * @author qs
		 * @date 2015年11月9日
		 */
		private void preparePreCrossAlternateData(TCmOriginalCross ocross, PlanCrossInfo plancross, Map<Short,PlanTrainNew> preSchemeAlternateMap){
			TCmVersion target_version = tCmVersionServiceImpl.getPreVersion(ocross.getCmVersionId());
			/*判断是否需要交替*/
			if(target_version != null){
				/*查找target_scheme下是否有相应的tcmCross*/
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("cmCrossName", ocross.getCrossName());
				if(StringUtils.isNotBlank(ocross.getAlternateTrainNbr())){
					param.put("cmCrossName", ocross.getAlternateTrainNbr());
				}
				param.put("mTemplateScheme", target_version.getmTemplateScheme());
				TCmCross preCmCross = cmCrossServiceImpl.selectByCrossNameAndScheme(param);
				TCmCross nowCmCross = cmCrossServiceImpl.selectByCmOriginalCross(ocross.getCmOriginalCrossId());
				List<TCmTrain> nowCmTrainList = null;
				if(nowCmCross !=null ){
					nowCmTrainList = tCmTrainMapper.selectByCmCrossId(nowCmCross.getCmCrossId());
				}
				if(preCmCross != null){
					PlanCrossInfo pre_planCross = planCrossDao.findByUnitCrossId(preCmCross.getCmCrossId());
					/*获取新交路下车次信息*/
					List<TCmTrain> preCmTrainList = tCmTrainMapper.selectByCmCrossId(preCmCross.getCmCrossId());
					for(int i = 0;i < preCmTrainList.size();i++){
						TCmTrain preTrain  = preCmTrainList.get(i);
						TCmTrain nowTrain  = nowCmTrainList.get(i);
						TCmTrainAlternate nowAlternate = cmCrossServiceImpl.getTCmTrainAlternate(nowTrain.getCmTrainId());
						param.put("planCrossId", pre_planCross.getPlanCrossId());
						param.put("cmTrainId", preTrain.getCmTrainId());
						param.put("runDate", nowAlternate.getAlternateDate());
						PlanTrainNew planTrain = planTrainNewMapper.selectLinkedPlanTrain(param);
						if(planTrain != null && StringUtils.isNotBlank(planTrain.getPreTrainId())){
							PlanTrainNew linkTrain = planTrainNewMapper.selectByPrimaryKey(planTrain.getPreTrainId());
							preSchemeAlternateMap.put(preTrain.getTrainSort(), linkTrain);
						}
					}
				}
			}
			
		}
	
	/**
	 * @Description: 获取车次最后一个rundate的信息
	 * @param @param planCrossId
	 * @param @param cmTrainId
	 * @param @return   
	 * @return PlanTrainNew  
	 * @throws
	 * @author qs
	 * @date 2015年10月5日
	 */
	public PlanTrainNew selectLatestRunDatePlanTrain(String planCrossId, String orgCmTrainId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("planCrossId", planCrossId);
		param.put("orgCmTrainId", orgCmTrainId);
		PlanTrainNew plantrain = planTrainNewMapper.selectLatestRunDatePlanTrain(param);
		return plantrain;
	}
	
	public PlanTrainNew selectFirstRunDatePlanTrain(String planCrossId){
		return planTrainNewMapper.selectFirstRunDatePlanTrain(planCrossId);
	}
	
	public PlanCrossInfo findByCmCrossId(String cmCrossId){
		return planCrossDao.findByUnitCrossId(cmCrossId);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.gc();
	}

}

class TCmTrainAndAlternateVo{
	private TCmTrain cmTrain;
	private TCmTrainAlternate alternate;
	
	public TCmTrainAndAlternateVo(){};
	
	public TCmTrainAndAlternateVo(TCmTrain cmTrain, TCmTrainAlternate alternate){
		this.cmTrain = cmTrain;
		this.alternate = alternate;
	};
	
	public TCmTrain getCmTrain() {
		return cmTrain;
	}
	public void setCmTrain(TCmTrain cmTrain) {
		this.cmTrain = cmTrain;
	}

	public TCmTrainAlternate getAlternate() {
		return alternate;
	}

	public void setAlternate(TCmTrainAlternate alternate) {
		this.alternate = alternate;
	}
	
}
