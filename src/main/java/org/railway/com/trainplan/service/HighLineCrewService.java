package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.HighLineCrewDao;
import org.railway.com.trainplan.service.dto.OptionDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 乘务计划服务
 * Created by speeder on 2014/6/27.
 */
@Monitored
@Component
@Transactional
public class HighLineCrewService {
	
	private static List<HighLineCrewInfo> highLineCrewInfolist = new ArrayList<HighLineCrewInfo>();
	
    @Autowired
    private HighLineCrewDao highLineCrewDao;
    
    @Autowired
    private BaseDao baseDao;

    public HighLineCrewInfo findHighLineCrew(Map<String, Object> map) {
        return highLineCrewDao.findOne(map);
    }

    public List<HighLineCrewInfo> findList(String crewDate,String crewType,String recordPeopleOrg) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if("all".equals(crewType)){
    		crewType = null;
    	}
    	map.put("crewDate",crewDate);
    	map.put("crewType",crewType);
    	map.put("recordPeopleOrg",recordPeopleOrg);
        return highLineCrewDao.findList(map);
    }

    public List<HighLineCrewInfo> findListAll(String crewDate,String crewType) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if("all".equals(crewType)){
    		crewType = null;
    	}
    	map.put("crewDate",crewDate);
    	map.put("crewType",crewType);
        return highLineCrewDao.findListAll(map);
    }
    
    public void addCrew(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.addCrew(crewHighlineInfo);
    }

    public int  getMaxSortId() {
        return highLineCrewDao.getMaxSortId();
    }

    
    public void update(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.update(crewHighlineInfo);
    }

    public void delete(String crewHighlineId) {
    	Map<String,String> reqMap = new HashMap<String,String>();
    	reqMap.put("crewHighlineId", crewHighlineId);
        highLineCrewDao.delete(reqMap);
    }
    
    /**
	 * 查询PLAN_TRAIN信息
	 * @param 
	 * @return
	 * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public QueryResult<RunPlan>  getRunLineListForRunDate(String runDate,String trainNbr,String rownumstart,String rownumend,
			String highlineFlag, String bureau) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("runDate",runDate );
		reqMap.put("trainNbr",trainNbr );
		reqMap.put("rownumstart",rownumstart );
		reqMap.put("rownumend",rownumend );
		reqMap.put("highlineFlag",highlineFlag );
		reqMap.put("bureau",bureau );
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_FIND_RUNPLAN_LIST,reqMap);
	}
    
    /**
	 * 查询PLAN_TRAIN信息
	 * @param 
	 * @return
	 * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public QueryResult<RunPlan>  getRunLineListForCurrentBureau(String runDate,String rownumstart,String rownumend, String bureau ,String highlineFlag) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("runDate",runDate );
		reqMap.put("rownumstart",rownumstart );
		reqMap.put("rownumend",rownumend );
		reqMap.put("bureau",bureau );
		reqMap.put("highlineFlag",highlineFlag );
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_FIND_RUNPLAN_LIST_BUREAU,reqMap);
	}
    
	
	 /**
	 * 查询乘务计划信息
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public QueryResult<HighLineCrewInfo>  getHighlineCrewListForRunDate(String crewGroup,String xwyName,String crewDate,String crewType,String trainNbr,String rownumstart,String rownumend ) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
		if(!"all".equals(crewType)){
			reqMap.put("crewType",crewType);
			reqMap.put("recordPeopleOrg",user.getDeptName());
		}
		reqMap.put("name1",xwyName );
		reqMap.put("crewGroup",crewGroup );
		reqMap.put("crewDate",crewDate );
		reqMap.put("rownumstart",rownumstart );
		reqMap.put("rownumend",rownumend );
		if(trainNbr != null && !"".equals(trainNbr.trim())){
			reqMap.put("trainNbr","%-" +trainNbr + "-%");
		}
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST,reqMap);
	}
    
	public List<HighLineCrewInfo> getHighlineCrewListForWeek(String startDate,String endDate) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("startDate",startDate );
		reqMap.put("endDate",endDate);
		 List<HighLineCrewInfo> 	highLineCrewInfolistFowWeek = baseDao.selectListBySql(Constants.HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST_FOR_WEEK, reqMap);
    	 return highLineCrewInfolistFowWeek;
	}
	
	
	public List<HighLineCrewInfo> getFullHighLineCrewInfo() throws Exception{
		if (highLineCrewInfolist == null || highLineCrewInfolist.size()==0) {
			highLineCrewInfolist = baseDao.selectListBySql(Constants.HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST_FOR_ALL, null);
    	}
    	return highLineCrewInfolist;
	}
	
	/**
	 * 更新submitType字段值为1
	 * @param crewDate 格式yyyy-MM-dd
	 * @param crewType 乘务类型（1车长、2司机、3机械师）
	 * @return
	 */
	public int updateSubmitType(String crewDate,String crewType,String id){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("crewDate",crewDate );
		reqMap.put("crewType", crewType);
		reqMap.put("id", id);
		return baseDao.updateBySql(Constants.HIGHLINECREWDAO_UPDATE_SUBMIT_TYPE, reqMap);
	}
	
	/**
	 * 根据crewDate和crewType删除表highline_crew表中数据
	 * @param crewDate 格式：yyyyMMdd
	 * @param crewType
	 * @param recordPepole 
	 * @return 成功删除数据的条数
	 */
	public int deleteHighlineCrewForCrewDate(String crewDate,String crewType,String recordPepole ){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crewDate",crewDate);
		reqMap.put("crewType", crewType);
		reqMap.put("recordPepole", recordPepole);
		
		return baseDao.deleteBySql(Constants.HIGHLINECREWDAO_DELETE_HIGHLINE_CREW_FOR_CREWDATE, reqMap);
		
	}
	
	/**
	 * 获取表highline_crew中RecordPeopleOrg字段的值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OptionDto> getRecordPeopleOrgList(){
		return baseDao.selectListBySql(Constants.HIGHLINECREWDAO_GET_RECORD_PEOPLE_ORG, "");
	}
	
	/**
	 * 对表highline_crew进行条件分页查询
	 * @param reqMap
	 * 主要有这些字段：
	 * crewStartDate;crewEndDate;crewType;
       crewBureau;recordPeopleOrg;trainNbr;name;rownumstart;rownumend
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public QueryResult<HighLineCrewInfo>   getHighlineCrewBaseInfoForPage(Map<String,Object> reqMap)throws Exception{
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_GET_HIGHLINE_CREW_BASE_INFO_FOR_PAGE, reqMap);
	}
	
	/**
	 * 对表highline_crew进行条件查询
	 * @param reqMap
	 * 主要有这些字段：
	 * crewStartDate;crewEndDate;crewType;
       crewBureau;recordPeopleOrg;trainNbr;name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighLineCrewInfo> getHighlineCrewBaseInfo(Map<String,Object> reqMap){
			return baseDao.selectListBySql(Constants.HIGHLINECREWDAO_GET_HIGHLINE_CREW_BASE_INFO, reqMap);
	}
	
	
	/**
	 * 通过crewDate和trainNbr查询HighLineCrewInfo对象列表
	 * @param crewDate 时间格式：yyyyMMdd
	 * @param trainNbr 车次
	 * @return
	 */
	public List<HighLineCrewInfo> getHighlineCrewForCrewDateAndTrainNbr(String crewDate,String trainNbr,String submitType){
		   Map<String,Object> reqMap = new HashMap<String,Object>();
		   reqMap.put("crewDate",crewDate);
		   reqMap.put("trainNbr",trainNbr);
		   reqMap.put("submitType", submitType);
		   return baseDao.selectListBySql(Constants.GET_HIGHLINE_CREW_FOR_CREWDATE_AND_TRAINNBR, reqMap);
	}
}
