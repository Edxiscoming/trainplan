package org.railway.com.trainplan.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.DicRelaCrossPost;
import org.railway.com.trainplan.entity.DicThroughLine;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.Node;
import org.railway.com.trainplan.entity.TrainType;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.LjzdMybatisDao;
import org.railway.com.trainplan.service.dto.OptionDto;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;




/**
 * 基本的service业务功能
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class CommonService {
	private static final Logger logger = Logger.getLogger(CommonService.class);
	
	private static Map<String, Ljzd> map = new HashMap<String, Ljzd>(); 
	private static Map<String, String> jcMap = new HashMap<String, String>();
	private static Map<String, Node> nodeNameMap = new HashMap<String, Node>();
	private static List<Ljzd> ljzdList = new ArrayList<Ljzd>();
	private static List<Node> nodeList = new ArrayList<Node>();
	
	private static List<OptionDto> crhTypes = new ArrayList<OptionDto>();  
	
	@Autowired
	private LjzdMybatisDao ljzdDao;
	
	@Autowired
	private BaseDao baseDao;
	
	
	/**根据京 得 P
	 * @param bureauShortName
	 * @return
	 */
	public String getLJPYMbyLJJC(String bureauShortName){
		Map<String, String> params = new HashMap<String, String>();  
		if(bureauShortName != null){
			params.put("bureauShortName", bureauShortName); 
		} 
		return (String) baseDao.selectOneBySql(Constants.BASEDAO_GET_LJPYMbyLJJC, params);
	}
	public List<OptionDto> getThroughLines(String bureauCode){
		Map<String, String> params = new HashMap<String, String>();  
		if(bureauCode != null){
			params.put("bureauCode", bureauCode); 
		} 
		return baseDao.selectListBySql(Constants.BASEDAO_GET_THROUGHLINE, params);
	}
	public List<DicThroughLine> getThroughLineName(String bureauCode){
		Map<String, String> params = new HashMap<String, String>();  
		if(bureauCode != null){
			params.put("bureauCode", bureauCode); 
		} 
		 return baseDao.selectListBySql(Constants.BASEDAO_GETTHROUGHLINENAME, bureauCode);
	}
	
	
	public List<DicRelaCrossPost> initDicRelaCrossPost(String bureau){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bureau", bureau);
		 return baseDao.selectListBySql(Constants.BASEDAO_INITDICRELACROSSPOST,map);
	}
	
	
	public List<OptionDto> getDepots(String bureauCode){
		Map<String, String> params = new HashMap<String, String>();  
		if(bureauCode != null){
			params.put("bureauCode", bureauCode); 
		}
		List<OptionDto> result =  baseDao.selectListBySql(Constants.BASEDAO_GET_DEPOT, params);
		return result;
	} 
	
	public List<OptionDto> getCrhTypes(){ 
		if(crhTypes.size() == 0){
			List<OptionDto> result =  baseDao.selectListBySql(Constants.BASEDAO_GET_CRHTYPE, null);
			crhTypes.addAll(result);
		}
		return crhTypes;
	}
	
	/**
	 * 保存表DIC_RELA_CROSS_POST中数据
	 * 
	 * @param cmdTrain
	 * @return
	 */
	public int insertDisRelaCrossPost(DicRelaCrossPost disRelaCrossPost) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERTDISRELACROSSPOST,
				disRelaCrossPost);
	}
	
	/**
	 * 保存表DIC_THROUGH_LINE中数据
	 * 
	 * @param cmdTrain
	 * @return
	 */
	public int insertDicThroughLine(DicThroughLine dicThroughLine) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERTDICTHROUGHLINE,
				dicThroughLine);
	}
	
	/**
	 * 根据throughLineName查询是否存在
	 * 
	 * @param cmdTrain
	 * @return
	 * @throws Exception 
	 */
	public List<DicThroughLine> isExistDicThroughLine(String throughLineName,String bureau) throws Exception {
		Map<String, String> params = new HashMap<String, String>();  
		params.put("throughLineName", throughLineName); 
		params.put("bureau", bureau); 
		return baseDao.selectListBySql(Constants.RUNPLANLKDAO_ISEXISTDICTHROUGHLINE,params);
	}

	public int deleteDicRelaCrossPost(Integer dicIds) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETEDICRELACROSSPOST,
				dicIds);
	}
	
	public int deleteDicThroughLine(String dicIds) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETEDICTHROUGHLINE,
				dicIds);
	}
	
	
	/**
	 * 更新
	 * @param dicIds
	 * @return
	 */
	public int updateRelaCrossPost(DicRelaCrossPost dicRelaCrossPost) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_UPDATERELACROSSPOST,
				dicRelaCrossPost);
	}
	
	public List<DicRelaCrossPost> getDicRelaCrossPostList(Map params){ 
		List<DicRelaCrossPost> result =  baseDao.selectListBySql(Constants.BASEDAO_GETDICRELACROSSPOSTLIST, params);
		return result;
	}
	
	public List<OptionDto> getAccs(String b){ 
		Map<String, String> params = new HashMap<String, String>();  
		if(b != null){
			params.put("bureauCode", b); 
		} 
		List<OptionDto> result =  baseDao.selectListBySql(Constants.BASEDAO_GET_ACC, params);
		return result;
	}
	/**
	 * 通过路局全称查询路基基本信息
	 */
	public Ljzd getLjInfo(String ljqc) {
		if(map.get(ljqc) != null){
			return map.get(ljqc);
		}
		
		map.clear();
		List<Ljzd> list = baseDao.selectListBySql(Constants.LJZDDAO_GET_FULL_STATION_INFO, null);
		for (Ljzd obj : list) {
			map.put(obj.getLjjc(), obj);
		}
		
//		Ljzd dto = (Ljzd)baseDao.selectOneBySql(Constants.LJZDDAO_GET_LJ_INFO, ljqc);
//		if (dto!=null && dto.getLjjc()!=null && !"".equals(dto.getLjjc().trim())) {
//			map.put(ljqc, dto);
//		}
		return map.get(ljqc);
	}

	/**
	 * 通过id查询列车类型信息
	 * @param id
	 * @return
	 */
	public  TrainType getTrainType(String id){
		TrainType trainType = (TrainType)baseDao.selectOneBySql(Constants.LJZDDAO_GET_TRAIN_TYPE, id);
		return trainType;
	}
	
	/**
    * 从基础数据库中获取18个路局的基本信息
    * @return
    */ 
    public List<Ljzd> getFullStationInfo(){
    	if (ljzdList == null || ljzdList.size()==0) {
    		ljzdList = baseDao.selectListBySql(Constants.LJZDDAO_GET_FULL_STATION_INFO, null);
    	}
    	return ljzdList;
    }
    
	/**
     * 获取站名
     * @return
     */ 
     public List<Node> getFullNodeInfo(){
     	if (nodeList == null || nodeList.size()==0) {
     		nodeList = baseDao.selectListBySql(Constants.LJZDDAO_GET_FULL_NODE_INFO, null);
     	}
     	return nodeList;
     }
     
 	/**
      * 获取站名对象
      * @return
      */ 
      public Node getNodeByName(String name){
    	  Node node  = null;
    	  List<Node> result = baseDao.selectListBySql(Constants.LJZDDAO_GET_NODE_BYNAME, name);
    	  if(result.size()>1){
    		for (Node n : result) {
				if(n.getMarks().contains("车场")){
					node = n;
				}
			}
    	  }
    	  else{
    		  node = result.get(0);
    	  }
      	return node;
      }
    
    /**
     * 从基础数据库中获取站名与站对象映射
     * @return
     */ 
     public Map<String, Node> getNodeNameMapping(){ 
    	 if(nodeNameMap.isEmpty()){
    		 List<Node> list = getFullNodeInfo();
	    	 for(Node node : list){
	    		 nodeNameMap.put(node.getName(), node);
	    	 }
    	 }
    	 return nodeNameMap;
     }
     
     /**
      * 从基础数据库中获取18个路局的基本信息
      * @return
      */ 
     public Map<String, String> getStationJCMapping(){ 
    	 if(jcMap.isEmpty()){
    		 List<Ljzd> list = getFullStationInfo();
    		 for(Ljzd ljzd : list){
    			 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
    		 }
    	 }
    	 return jcMap;
     }
     
     
     /**
      * 从基础数据库中获取18个路局的基本信息
      * @return
      */ 
      public String getStationJC(String py){ 
     	 if(jcMap.isEmpty()){
     		 List<Ljzd> list = getFullStationInfo();
 	    	 for(Ljzd ljzd : list){
 	    		 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
 	    	 }
     	 }
     	 return jcMap.get(py);
     	
      
      }
      
      /**
       * 从基础数据库中获取18个路局的基本信息
       * @return
       */ 
       public String getStationPy(String jc){ 
	      	 if(jcMap.isEmpty()){
	      		 List<Ljzd> list = getFullStationInfo();
	  	    	 for(Ljzd ljzd : list){
	  	    		 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
	  	    	 }
	      	 }
	      	for(String p : jcMap.keySet()){
	     		if(jc.equals(jcMap.get(p))){
	     			return p;
	     		}
	    	 }
	      	 return null;
       }
    
    
       /**
   	 * 组装发送到rabbit_MQ的字符串
   	 * @param listDto
   	 * @return
   	 */
   	public  String  combinationMessage(List<ParamDto> listDto) throws Exception{
   	
   		    JSONArray  jsonArray = new JSONArray();
               if(listDto != null && listDto.size() > 0){
   				
   				for(ParamDto dto : listDto){
   					
   					//TODO 判断表plan_train中字段CHECK_LEV1_TYPE，当CHECK_LEV1_TYPE=2时才能生成运行线
   					
   					//组装发往rabbit的报文
   					JSONObject  temp = new JSONObject();
   					temp.put("sourceEntityId", dto.getSourceEntityId());
   					temp.put("planTrainId", dto.getPlanTrainId());
   					temp.put("time", dto.getTime());
   					temp.put("source", dto.getSource());
   					temp.put("action", dto.getAction());
   					temp.put("msgReceiveUrl", dto.getMsgReceiveUrl());
   					jsonArray.add(temp);
   				}
   			}
             //组装发送报文
   			JSONObject  json = new JSONObject();
   			JSONObject head = new JSONObject();
   			head.put("event", "trainlineEvent");
   			head.put("requestId", UUID.randomUUID().toString());
   			head.put("batch", 1);
   			head.put("user", "test");
   			json.put("head", head);
   			json.put("param", jsonArray);
   			
   			return json.toString();
   	}

	/**
	 * 根据路局删除数据.
	 * 
	 * @param bureau
	 * @return
	 */
	public int deleteDicrelacrosspostByBureau(String bureau) {
		return baseDao.deleteBySql(Constants.DELETE_DICRELACROSSPOST_BY_BUREAU,bureau);
	}

	
	
	/**
	 * 修改dic_rela_cross_post.
	 * @param dicIds
	 * @return
	 */
	public int updateRelaCrossPostByMap(Map<String, Object> map) {
		return baseDao.deleteBySql(Constants.UPDATE_DICRELACROSSPOST_BY_MAP,map);
	}
	    
}
