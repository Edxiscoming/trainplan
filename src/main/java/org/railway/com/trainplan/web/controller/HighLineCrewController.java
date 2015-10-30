package org.railway.com.trainplan.web.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.service.BaseDataService;
import org.railway.com.trainplan.service.HighLineCrewService;
import org.railway.com.trainplan.service.HighLineService;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Maps;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

/**
 * Created by speeder on 2014/6/27.
 */
@RestController
@RequestMapping(value = "/crew/highline")
public class HighLineCrewController {

	private static Log logger = LogFactory.getLog(HighLineCrewController.class);

	@Autowired
	private HighLineCrewService highLineCrewService;

	@Autowired
	private BaseDataService baseDataService;

	@Autowired
	private HighLineService highLineService;

	@Autowired
	private MessageService messageService;

	@ResponseBody
	@RequestMapping(value = "getHighLineCrew/{crewHighLineId}", method = RequestMethod.GET)
	public Result getHighLineCrew(
			@PathVariable("crewHighLineId") String crewHighLineId) {
		logger.debug("getHighLineCrew:::::::");
		Result result = new Result();
		try {
			Map<String, Object> params = Maps.newHashMap();
			params.put("crewHighlineId", crewHighLineId);
			HighLineCrewInfo highLineCrewInfo = highLineCrewService
					.findHighLineCrew(params);
			result.setData(highLineCrewInfo);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public Result getHighLineCrewList(@RequestBody Map<String, Object> reqMap) {
		logger.debug("getHighLineCrewList:::::::");
		Result result = new Result();
		try {

			String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
			// 将时间格式：yyyy-MM-dd转换成yyyyMMdd
			crewDate = DateUtil.getFormateDayShort(crewDate);
			String crewType = StringUtil.objToStr(reqMap.get("crewType"));

			List<HighLineCrewInfo> list = highLineCrewService.findListAll(
					crewDate, crewType);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Result addHighLineCrewInfo(
			@RequestBody HighLineCrewInfo highLineCrewInfo) {
		logger.debug("addHighLineCrewInfo:::::::");
		Result result = new Result();
		try {
			int maxSortId = highLineCrewService.getMaxSortId();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			highLineCrewInfo.setCrewHighlineId(UUID.randomUUID().toString());
			String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo
					.getCrewDate());
			highLineCrewInfo.setCrewDate(crewDate);
			// 所属局简称
			highLineCrewInfo.setCrewBureau(user.getBureauShortName());
			highLineCrewInfo.setRecordPeople(user.getName());
			highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
			highLineCrewInfo.setSortId(maxSortId + 1);
			highLineCrewService.addCrew(highLineCrewInfo);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;

	}

	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public Result updateHighLineCrewInfo(
			@RequestBody HighLineCrewInfo highLineCrewInfo) {
		logger.debug("updateHighLineCrewInfo:::::::"
				+ highLineCrewInfo.getCrewDate() + "|"
				+ highLineCrewInfo.getCrewCross());
		Result result = new Result();
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 所属局简称
			System.out.println(highLineCrewInfo.getCrewHighlineId());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("crewHighlineId", highLineCrewInfo.getCrewHighlineId());
			// HighLineCrewInfo highLineCrewInfo =
			// highLineCrewService.findHighLineCrew(params);
			HighLineCrewInfo highLineCrewInfo1 = highLineCrewService
					.findHighLineCrew(params);

			highLineCrewInfo.setCrewBureau(user.getBureauShortName());
			highLineCrewInfo.setRecordPeople(user.getName());
			highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
			String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo
					.getCrewDate());
			highLineCrewInfo.setCrewDate(crewDate);
			highLineCrewInfo.setSortId(highLineCrewInfo1.getSortId());
			highLineCrewService.update(highLineCrewInfo);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;

	}

	/**
	 * 批量删除highline_crew表中数据
	 *
	 * @param reqMap
	 * @return
	 */
	@RequestMapping(value = "deleteHighLineCrewInfo", method = RequestMethod.POST)
	public Result deleteHighLineCrewInfo(@RequestBody String reqStr) {
		logger.debug("deleteHighLineCrewInfo:::::::");
		Result result = new Result();
		logger.info("deleteWdCmd~~reqStr==" + reqStr);
		JSONObject reqObj = JSONObject.fromObject(reqStr);
		try {

			List<String> crewHighLineIds = null;
			crewHighLineIds = reqObj.getJSONArray("crewHighLineIds");
			//
			if (!crewHighLineIds.isEmpty()) {
				for (String crewHighLineId : crewHighLineIds) {
					highLineCrewService.delete(crewHighLineId);
				}
			}

		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 更新字段submitType字段的值为1
	 *
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "updateSubmitType", method = RequestMethod.POST)
    public Result updateSubmitType(@RequestBody Map<String, Object> reqMap) {
        Result result = new Result();
        try {
//            logger.debug("updateSubmitType~~~~~reqMap=" + reqMap);
            String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
            String id = StringUtil.objToStr(reqMap.get("a"));
            crewDate = DateUtil.getFormateDayShort(crewDate);
            String crewType = StringUtil.objToStr(reqMap.get("crewType"));
             String [] ids =  id.split(",");
            for (String string : ids) {
                 highLineCrewService.updateSubmitType(crewDate, crewType,string);
			}
            
            // send msg
            ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            // 消息内容：2015年6月3日太原客运段已填报普速车长乘务计划。/2015年6月3日太原机务段已填报普速司机乘务计划。
            String contents = "";
            // 消息类型：普速车长乘务/高铁车长乘务
            String typeCode = "";
            if(org.apache.commons.lang.StringUtils.equals(crewType, "1")){
            	// 高铁 - 填报车长
            	contents = DateUtil.formateDate3(crewDate) + user.getPostName() + "已填报高铁车长乘务计划";
            	typeCode = Constants.MSG_GT_CZ_CW;
            }else if(org.apache.commons.lang.StringUtils.equals(crewType, "11")){
            	// 普速 - 填报车长
            	contents = DateUtil.formateDate3(crewDate) + user.getPostName() + "已填报普速车长乘务计划";
            	typeCode = Constants.MSG_PS_CZ_CW;
            }else if(org.apache.commons.lang.StringUtils.equals(crewType, "2")){
            	// 高铁 - 填报司机
            	contents = DateUtil.formateDate3(crewDate) + user.getPostName() + "已填报高铁司机乘务计划";
            	typeCode = Constants.MSG_GT_SJ_CW;
            }else if(org.apache.commons.lang.StringUtils.equals(crewType, "12")){
            	// 普速 - 填报司机
            	contents = DateUtil.formateDate3(crewDate) + user.getPostName() + "已填报普速司机乘务计划";
            	typeCode = Constants.MSG_PS_SJ_CW;
            }else if(org.apache.commons.lang.StringUtils.equals(crewType, "3")){
            	// 高铁 - 填报机械师
            	// 消息内容：2015年6月3日太原动车所已填报高铁机械师乘务计划。
            	contents = DateUtil.formateDate3(crewDate) + user.getPostName() + "已填报高铁机械师乘务计划";
            	typeCode = Constants.MSG_GT_JXS_CW;
            }
            Integer msgCount = messageService.dealWithMsg(user, typeCode, contents,null,null);
			if(msgCount == 0){
				result.setCode(StaticCodeType.MSG_ERROR.getCode());
				result.setMessage(StaticCodeType.MSG_ERROR.getDescription());
			}
           
        } catch (Exception e) {
            logger.error(e);
            result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
            result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;
    }

	/**
	 * 获取运行线信息
	 *
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getRunLineListForRunDate", method = RequestMethod.POST)
	public Result getRunLineListForRunDate(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.debug("getRunLineListForRunDate~~~~~reqMap=" + reqMap);
			String runDate = StringUtil.objToStr(reqMap.get("runDate"));
			String flag = StringUtil.objToStr(reqMap.get("flag"));
			String currentBureau = StringUtil.objToStr(reqMap
					.get("currentBureau"));
			// 格式化时间
			runDate = DateUtil.getFormateDayShort(runDate);
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			String rownumstart = StringUtil.objToStr(reqMap.get("rownumstart"));
			String rownumend = StringUtil.objToStr(reqMap.get("rownumend"));
			String highlineFlag = StringUtil.objToStr(reqMap
					.get("highlineFlag"));
			// 仅查询本局相关的铁路线
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			System.err.println("Bureau" + user.getBureauShortName());
			PagingResult page = null;
			if (flag.equals("chaxun")) {
				QueryResult queryResult = highLineCrewService
						.getRunLineListForRunDate(runDate,
								"".equals(trainNbr) ? null : trainNbr,
								rownumstart, rownumend, highlineFlag,
								user.getBureauShortName());
				page = new PagingResult(queryResult.getTotal(),
						queryResult.getRows());
			} else {
				QueryResult queryResult = highLineCrewService
						.getRunLineListForCurrentBureau(runDate, rownumstart,
								rownumend, currentBureau, highlineFlag);
				page = new PagingResult(queryResult.getTotal(),
						queryResult.getRows());
			}

			result.setData(page);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据日期获取乘务计划列表信息
	 *
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHighlineCrewListForRunDate", method = RequestMethod.POST)
	public Result getHighlineCrewListForRunDate(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.debug("getHighlineCrewListForRunDate~~~~~reqMap=" + reqMap);
			String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
			String xwyName = StringUtil.objToStr(reqMap.get("xwyName"));
			String crewGroupName = StringUtil.objToStr(reqMap
					.get("crewGroupName"));
			// 格式化时间
			crewDate = DateUtil.getFormateDayShort(crewDate);
			String crewType = StringUtil.objToStr(reqMap.get("crewType"));
			String rownumstart = StringUtil.objToStr(reqMap.get("rownumstart"));
			String rownumend = StringUtil.objToStr(reqMap.get("rownumend"));
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			@SuppressWarnings("rawtypes")
			QueryResult queryResult = highLineCrewService
					.getHighlineCrewListForRunDate(crewGroupName, xwyName,
							crewDate, crewType, trainNbr, rownumstart,
							rownumend);
			PagingResult page = new PagingResult(queryResult.getTotal(),
					queryResult.getRows());
			result.setData(page);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据日期获取乘务计划列表信息(默认查询最近一周的信息)
	 *
	 * @param reqMap
	 * @return
	 */
	@RequestMapping(value = "getHighlineCrewListForWeek", method = RequestMethod.GET)
	public Result getHighlineCrewListForWeek() {
		Result result = new Result();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date data = new Date();
			String endData = sdf.format(data);
			Calendar calbegin = Calendar.getInstance();

			calbegin.add(Calendar.DAY_OF_MONTH, -6);
			String getDate = sdf.format(calbegin.getTime());
			System.out.println(getDate);

			List<HighLineCrewInfo> list = highLineCrewService
					.getHighlineCrewListForWeek(String.valueOf(getDate),
							String.valueOf(endData));
			result.setData(list);
		} catch (Exception e) {
			logger.error("getFullHighLineCrewInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;

	}

	/**
	 * 获取乘务组编号
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getFullHighLineCrewInfo", method = RequestMethod.GET)
	public Result getFullHighLineCrewInfo() {
		Result result = new Result();
		try {
			List<HighLineCrewInfo> list = highLineCrewService
					.getFullHighLineCrewInfo();
			result.setData(list);
		} catch (Exception e) {
			logger.error("getFullHighLineCrewInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 根据日期和车次获取乘务计划列表信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHighlineCrewForCrewDateAndTrainNbr", method = RequestMethod.POST)
	public Result getHighlineCrewForCrewDateAndTrainNbr(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			System.err
					.println("getHighlineCrewForCrewDateAndTrainNbr~~~~~reqMap="
							+ reqMap);
			logger.debug("getHighlineCrewForCrewDateAndTrainNbr~~~~~reqMap="
					+ reqMap);
			String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			// 提交状态（0：编辑，1：提交）
			String submitType = "1";
			List<HighLineCrewInfo> queryResult = highLineCrewService
					.getHighlineCrewForCrewDateAndTrainNbr(crewDate, trainNbr,
							submitType);
			result.setData(queryResult);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author denglj
	 */
	// exportExcelcrossserach
	@ResponseBody
	@RequestMapping(value = "/exportExcelcrossserach/{crewType}/{crewDate}/{trainNbr}/{highlineFlag}/{searchThroughLine}/{searchTokenVehDepot}/{searchAcc}/{bureaus}", method = RequestMethod.GET)
	public void exportExcelcrossserach(
			@PathVariable("crewType") String crewType,
			@PathVariable("crewDate") String crewDate,
			@PathVariable("trainNbr") String trainNbr,
			@PathVariable("highlineFlag") String highlineFlag1,
			@PathVariable("searchThroughLine") String searchThroughLine,
			@PathVariable("searchTokenVehDepot") String searchTokenVehDepot,
			@PathVariable("searchAcc") String searchAcc,
			@PathVariable("bureaus") String bureaus,
			HttpServletRequest request, HttpServletResponse response) {

		// searchThroughLine+"/"+searchTokenVehDepot+"/"+searchAcc
		// {tokenVehBureau=P, crossDate=20150202, crossBureau=P}
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String recordPeopleOrg = null;
		if (!"all".equals(crewType)) {
			recordPeopleOrg = user.getDeptName();
		}

		Map<String, String> pingyin = new HashMap<String, String>();
		List<Map<String, Object>> listmap = baseDataService.getBureauList();
		for (Map<String, Object> map : listmap) {
			pingyin.put(map.get("LJPYM").toString(), map.get("LJJC").toString());
		}

		// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
		// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}
		// System.out.println(crewDate+trainNbr
		// +searchThroughLine+searchTokenVehDepot+searchAcc+DateUtil.getFormateDayShort(crewDate));

		Map<String, Object> reqMap = new HashMap<String, Object>();
		// reqMap.put("crossBureau", user.getBureau());//仅查询本局

		reqMap.put("crossCheckType", 1);

		// // reqMap.put("crossBureau", user.getBureau());//仅查询本局
		// reqMap.put("crossCheckType", 1);
		// reqMap.put("vehicleSubType", 1);
		// reqMap.put("userPostId", user.getPostId());//用于填报权限控制

		reqMap.put("crossDate", DateUtil.getFormateDayShort(crewDate));// 仅查询本局
		if (!trainNbr.equals("-1"))
			reqMap.put("trainNbr", trainNbr);

		if (!bureaus.equals("-1"))
			reqMap.put("tokenVehBureau", bureaus);

		if (!searchThroughLine.equals("-1"))
			reqMap.put("throughLine", searchThroughLine);

		if (!searchTokenVehDepot.equals("-1"))
			reqMap.put("tokenVehDepot", searchTokenVehDepot);

		if (!searchAcc.equals("-1"))
			reqMap.put("postName", searchAcc);

		List<HighlineCrossInfo> list = highLineService
				.getHighlineCrossList(reqMap);
		// List<HighlineCrossInfo> list = new ArrayList<HighlineCrossInfo>();
		List<HighlineCrossInfo> returnList = new ArrayList<HighlineCrossInfo>();
		if (list != null && list.size() > 0) {/*
											 * for(HighlineCrossInfo crossInfo :
											 * list1){ HighlineCrossInfo temp =
											 * new HighlineCrossInfo(); Integer
											 * vehicleCheckType =
											 * crossInfo.getVehicleCheckType();
											 * try {
											 * BeanUtils.copyProperties(temp,
											 * crossInfo);
											 * 
											 * //车底计划审核状态（0:未审核1:审核），
											 * 已经审核的才能显示车底1和车底2
											 * if(vehicleCheckType != 1){
											 * temp.setVehicle1("");
											 * temp.setVehicle2(""); }
											 * list.add(crossInfo); } catch
											 * (IllegalAccessException e) { //
											 * TODO Auto-generated catch block
											 * e.printStackTrace(); } catch
											 * (InvocationTargetException e) {
											 * // TODO Auto-generated catch
											 * block e.printStackTrace(); } }
											 */
		}

		try {

			String _crewDate = DateUtil.getFormateDayShort(crewDate);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet("高铁车底_" + _crewDate);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4500);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4500);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 8000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4500);
			sheet.setColumnWidth((short) 13, (short) 4000);
			sheet.setColumnWidth((short) 14, (short) 4000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(crewDate + " " + "高铁车底");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 1);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("铁路线");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 2);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("首车日期");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 3);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("交路");

			// 备注
			HSSFCell chexing = row1.createCell((short) 4);
			chexing.setCellType(HSSFCell.CELL_TYPE_STRING);
			chexing.setCellStyle(styleTitle);
			chexing.setCellValue("车型");

			HSSFCell chedi1 = row1.createCell((short) 5);
			chedi1.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi1.setCellStyle(styleTitle);
			chedi1.setCellValue("车底1");

			HSSFCell chedi2 = row1.createCell((short) 6);
			chedi2.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi2.setCellStyle(styleTitle);
			chedi2.setCellValue("车底2");

			HSSFCell chukushifa = row1.createCell((short) 7);
			chukushifa.setCellType(HSSFCell.CELL_TYPE_STRING);
			chukushifa.setCellStyle(styleTitle);
			chukushifa.setCellValue("出库所/始发站");

			HSSFCell rukuzhongdao = row1.createCell((short) 8);
			rukuzhongdao.setCellType(HSSFCell.CELL_TYPE_STRING);
			rukuzhongdao.setCellStyle(styleTitle);
			rukuzhongdao.setCellValue("入库所/终到站");

			HSSFCell rebei = row1.createCell((short) 9);
			rebei.setCellType(HSSFCell.CELL_TYPE_STRING);
			rebei.setCellStyle(styleTitle);
			rebei.setCellValue("热备");

			HSSFCell dandang = row1.createCell((short) 10);
			dandang.setCellType(HSSFCell.CELL_TYPE_STRING);
			dandang.setCellStyle(styleTitle);
			dandang.setCellValue("担当局");

			HSSFCell dongchesuo = row1.createCell((short) 11);
			dongchesuo.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchesuo.setCellStyle(styleTitle);
			dongchesuo.setCellValue("动车所");

			HSSFCell dongchetai = row1.createCell((short) 12);
			dongchetai.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchetai.setCellStyle(styleTitle);
			dongchetai.setCellValue("管辖动车台");

			HSSFCell laiyuan = row1.createCell((short) 13);
			laiyuan.setCellType(HSSFCell.CELL_TYPE_STRING);
			laiyuan.setCellStyle(styleTitle);
			laiyuan.setCellValue("来源");

			// crossBureau
			HSSFCell jihuaju = row1.createCell((short) 13);
			jihuaju.setCellType(HSSFCell.CELL_TYPE_STRING);
			jihuaju.setCellStyle(styleTitle);
			jihuaju.setCellValue("计划局");
			// {tokenVehBureau=P, crossDate=20150202, tokenVehDepot=北京南动车所,
			// crossBureau=P}

			// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
			// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}

			// List<HighlineCrossInfo> list =
			// highLineService.getHighlineCrossList(reqMap);

			// 查询乘务上报信息
			// List<HighLineCrewInfo> list =
			// highLineCrewService.findList(_crewDate,
			// crewType,recordPeopleOrg);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighlineCrossInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(2 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);
					// 序号 铁路线 首车日期 交路 车型 车底1 车底2 出库所/始发站 入库所/终到站 热备 担当局 动车所
					// 管辖动车台 来源

					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 1);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getThroughLine());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 2);
					crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrossStartDate());
					// 经由铁路线 交路 车型 车底1 车底2

					HSSFCell throughLineCellFor = rowX.createCell((short) 3);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getCrossName());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 4);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getCrhType());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 5);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getVehicle1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 6);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getVehicle2());
					// 姓名2 出库所/始发站 入库所/终到站 热备
					HSSFCell name2CellFor = rowX.createCell((short) 7);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getCrossStartStn());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 8);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getCrossEndStn());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 9);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity2CellFor.setCellStyle(styleTitle);
					if ("1".equals(obj.getSpareFlag())) {
						identity2CellFor.setCellValue("否");
					} else {
						identity2CellFor.setCellValue("是");
					}

					// 备注 担当局
					// 动车所 管辖动车台 来源
					HSSFCell noteCellFor = rowX.createCell((short) 10);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(pingyin.get(obj
							.getTokenVehBureau()));

					HSSFCell noteCellFor1 = rowX.createCell((short) 11);
					noteCellFor1.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor1.setCellStyle(styleTitle);
					noteCellFor1.setCellValue(obj.getTokenVehDepot());

					HSSFCell noteCellFor2 = rowX.createCell((short) 12);
					noteCellFor2.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor2.setCellStyle(styleTitle);
					noteCellFor2.setCellValue(obj.getPostName());

					HSSFCell noteCellFor3 = rowX.createCell((short) 13);
					noteCellFor3.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor3.setCellStyle(styleTitle);
					noteCellFor3.setCellValue(obj.getCreateReason());

					HSSFCell jihuajuvalue = rowX.createCell((short) 13);
					jihuajuvalue.setCellType(HSSFCell.CELL_TYPE_STRING);
					jihuajuvalue.setCellStyle(styleTitle);
					jihuajuvalue.setCellValue(obj.getCrossBureau());

				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 14)); // 标题 合并1行11列
			/*
			 * sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
			 * (short) 0)); //序号 合并2行1列 sheet.addMergedRegion(new Region((short)
			 * 1, (short) 1, (short) 2, (short) 1)); //乘务交路 合并2行1列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
			 * (short) 2)); //车队组号 合并2行1列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 3, (short) 2, (short) 3)); //经由铁路线
			 * 合并2行1列 sheet.addMergedRegion(new Region((short) 1, (short) 4,
			 * (short) 1, (short) 6)); //司机1 合并1行3列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 7, (short) 1, (short) 9)); //司机2 合并1行3列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 10, (short)
			 * 2, (short) 10)); //备注 合并2行1列
			 */

			String filename = this.encodeFilename("高铁车底_" + _crewDate + ".xls",
					request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				// ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@RequestMapping(value = "/exportExcelcrossuse/{crewType}/{crewDate}/{trainNbr}/{highlineFlag}/{searchThroughLine}/{searchTokenVehDepot}/{searchAcc}/{bureaus}", method = RequestMethod.GET)
	public void exportExcelcrossuse(@PathVariable("crewType") String crewType,
			@PathVariable("crewDate") String crewDate,
			@PathVariable("trainNbr") String trainNbr,
			@PathVariable("highlineFlag") String highlineFlag1,
			@PathVariable("searchThroughLine") String searchThroughLine,
			@PathVariable("searchTokenVehDepot") String searchTokenVehDepot,
			@PathVariable("searchAcc") String searchAcc,
			@PathVariable("bureaus") String bureaus,
			HttpServletRequest request, HttpServletResponse response) {

		// searchThroughLine+"/"+searchTokenVehDepot+"/"+searchAcc
		// {tokenVehBureau=P, crossDate=20150202, crossBureau=P}
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String recordPeopleOrg = null;
		if (!"all".equals(crewType)) {
			recordPeopleOrg = user.getDeptName();
		}

		Map<String, String> pingyin = new HashMap<String, String>();
		List<Map<String, Object>> listmap = baseDataService.getBureauList();
		for (Map<String, Object> map : listmap) {
			pingyin.put(map.get("LJPYM").toString(), map.get("LJJC").toString());
		}

		// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
		// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}
		// System.out.println(crewDate+trainNbr
		// +searchThroughLine+searchTokenVehDepot+searchAcc+DateUtil.getFormateDayShort(crewDate));

		Map<String, Object> reqMap = new HashMap<String, Object>();
		// reqMap.put("crossBureau", user.getBureau());//仅查询本局

		reqMap.put("crossBureau", user.getBureau());// 仅查询本局
		reqMap.put("crossCheckType", 1);
		reqMap.put("vehicleSubType", 1);
		reqMap.put("userPostId", user.getPostId());// 用于填报权限控制

		reqMap.put("crossDate", DateUtil.getFormateDayShort(crewDate));// 仅查询本局
		if (!trainNbr.equals("-1"))
			reqMap.put("trainNbr", trainNbr);

		if (!bureaus.equals("-1") && !"undefined".equals(bureaus))
			reqMap.put("tokenVehBureau", bureaus);

		if (!searchThroughLine.equals("-1"))
			reqMap.put("throughLine", searchThroughLine);

		if (!searchTokenVehDepot.equals("-1"))
			reqMap.put("tokenVehDepot", searchTokenVehDepot);

		if (!searchAcc.equals("-1"))
			reqMap.put("postName", searchAcc);

		try {

			String _crewDate = DateUtil.getFormateDayShort(crewDate);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet("车底运用_" + _crewDate);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4500);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4500);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 8000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4500);
			sheet.setColumnWidth((short) 13, (short) 4000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(crewDate + " " + "车底运用");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 1);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("铁路线");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 2);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("首车日期");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 3);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("交路");

			// 备注
			HSSFCell chexing = row1.createCell((short) 4);
			chexing.setCellType(HSSFCell.CELL_TYPE_STRING);
			chexing.setCellStyle(styleTitle);
			chexing.setCellValue("车型");

			HSSFCell chedi1 = row1.createCell((short) 5);
			chedi1.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi1.setCellStyle(styleTitle);
			chedi1.setCellValue("车底1");

			HSSFCell chedi2 = row1.createCell((short) 6);
			chedi2.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi2.setCellStyle(styleTitle);
			chedi2.setCellValue("车底2");

			HSSFCell chukushifa = row1.createCell((short) 7);
			chukushifa.setCellType(HSSFCell.CELL_TYPE_STRING);
			chukushifa.setCellStyle(styleTitle);
			chukushifa.setCellValue("出库所/始发站");

			HSSFCell rukuzhongdao = row1.createCell((short) 8);
			rukuzhongdao.setCellType(HSSFCell.CELL_TYPE_STRING);
			rukuzhongdao.setCellStyle(styleTitle);
			rukuzhongdao.setCellValue("入库所/终到站");

			HSSFCell rebei = row1.createCell((short) 9);
			rebei.setCellType(HSSFCell.CELL_TYPE_STRING);
			rebei.setCellStyle(styleTitle);
			rebei.setCellValue("热备");

			HSSFCell dandang = row1.createCell((short) 10);
			dandang.setCellType(HSSFCell.CELL_TYPE_STRING);
			dandang.setCellStyle(styleTitle);
			dandang.setCellValue("担当局");

			HSSFCell dongchesuo = row1.createCell((short) 11);
			dongchesuo.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchesuo.setCellStyle(styleTitle);
			dongchesuo.setCellValue("动车所");

			HSSFCell dongchetai = row1.createCell((short) 12);
			dongchetai.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchetai.setCellStyle(styleTitle);
			dongchetai.setCellValue("管辖动车台");

			HSSFCell laiyuan = row1.createCell((short) 13);
			laiyuan.setCellType(HSSFCell.CELL_TYPE_STRING);
			laiyuan.setCellStyle(styleTitle);
			laiyuan.setCellValue("来源");
			// {tokenVehBureau=P, crossDate=20150202, tokenVehDepot=北京南动车所,
			// crossBureau=P}

			// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
			// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}

			List<HighlineCrossInfo> list = highLineService
					.getHighlineCrossList(reqMap);

			// 查询乘务上报信息
			// List<HighLineCrewInfo> list =
			// highLineCrewService.findList(_crewDate,
			// crewType,recordPeopleOrg);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighlineCrossInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(2 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);
					// 序号 铁路线 首车日期 交路 车型 车底1 车底2 出库所/始发站 入库所/终到站 热备 担当局 动车所
					// 管辖动车台 来源

					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 1);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getThroughLine());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 2);
					// crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrossStartDate());
					// 经由铁路线 交路 车型 车底1 车底2

					HSSFCell throughLineCellFor = rowX.createCell((short) 3);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getCrossName());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 4);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getCrhType());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 5);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getVehicle1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 6);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getVehicle2());
					// 姓名2 出库所/始发站 入库所/终到站 热备
					HSSFCell name2CellFor = rowX.createCell((short) 7);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getCrossStartStn());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 8);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getCrossEndStn());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 9);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity2CellFor.setCellStyle(styleTitle);
					// identity2CellFor.setCellValue(obj.getSpareFlag());
					if ("1".equals(obj.getSpareFlag())) {
						identity2CellFor.setCellValue("否");
					} else {
						identity2CellFor.setCellValue("是");
					}

					// 备注 担当局
					// 动车所 管辖动车台 来源
					HSSFCell noteCellFor = rowX.createCell((short) 10);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(pingyin.get(obj
							.getTokenVehBureau()));

					HSSFCell noteCellFor1 = rowX.createCell((short) 11);
					noteCellFor1.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor1.setCellStyle(styleTitle);
					noteCellFor1.setCellValue(obj.getTokenVehDepot());

					HSSFCell noteCellFor2 = rowX.createCell((short) 12);
					noteCellFor2.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor2.setCellStyle(styleTitle);
					noteCellFor2.setCellValue(obj.getPostName());

					HSSFCell noteCellFor3 = rowX.createCell((short) 13);
					noteCellFor3.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor3.setCellStyle(styleTitle);
					noteCellFor3.setCellValue(obj.getCreateReason());

				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 13)); // 标题 合并1行11列
			/*
			 * sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
			 * (short) 0)); //序号 合并2行1列 sheet.addMergedRegion(new Region((short)
			 * 1, (short) 1, (short) 2, (short) 1)); //乘务交路 合并2行1列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
			 * (short) 2)); //车队组号 合并2行1列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 3, (short) 2, (short) 3)); //经由铁路线
			 * 合并2行1列 sheet.addMergedRegion(new Region((short) 1, (short) 4,
			 * (short) 1, (short) 6)); //司机1 合并1行3列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 7, (short) 1, (short) 9)); //司机2 合并1行3列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 10, (short)
			 * 2, (short) 10)); //备注 合并2行1列
			 */

			String filename = this.encodeFilename("车底运用_" + _crewDate + ".xls",
					request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				// ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@RequestMapping(value = "/exportExcelcrosstianbao/{crewType}/{crewDate}/{trainNbr}/{highlineFlag}/{searchThroughLine}/{searchTokenVehDepot}/{searchAcc}/{bureaus}", method = RequestMethod.GET)
	public void exportExcelcrosstianbao(
			@PathVariable("crewType") String crewType,
			@PathVariable("crewDate") String crewDate,
			@PathVariable("trainNbr") String trainNbr,
			@PathVariable("highlineFlag") String highlineFlag1,
			@PathVariable("searchThroughLine") String searchThroughLine,
			@PathVariable("searchTokenVehDepot") String searchTokenVehDepot,
			@PathVariable("searchAcc") String searchAcc,
			@PathVariable("bureaus") String bureaus,
			HttpServletRequest request, HttpServletResponse response) {

		// searchThroughLine+"/"+searchTokenVehDepot+"/"+searchAcc
		// {tokenVehBureau=P, crossDate=20150202, crossBureau=P}
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String recordPeopleOrg = null;
		if (!"all".equals(crewType)) {
			recordPeopleOrg = user.getDeptName();
		}

		Map<String, String> pingyin = new HashMap<String, String>();
		List<Map<String, Object>> listmap = baseDataService.getBureauList();
		for (Map<String, Object> map : listmap) {
			pingyin.put(map.get("LJPYM").toString(), map.get("LJJC").toString());
		}

		// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
		// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}
		// System.out.println(crewDate+trainNbr
		// +searchThroughLine+searchTokenVehDepot+searchAcc+DateUtil.getFormateDayShort(crewDate));

		Map<String, Object> reqMap = new HashMap<String, Object>();
		// reqMap.put("crossBureau", user.getBureau());//仅查询本局

		reqMap.put("crossBureau", user.getBureau());// 仅查询本局
		reqMap.put("crossCheckType", 1);
		reqMap.put("userDeptName", user.getDeptName());// 用于填报权限控制

		reqMap.put("crossDate", DateUtil.getFormateDayShort(crewDate));// 仅查询本局
		if (!trainNbr.equals("-1"))
			reqMap.put("trainNbr", trainNbr);

		if (!bureaus.equals("-1") && !"undefined".equals(bureaus))
			reqMap.put("tokenVehBureau", bureaus);

		if (!searchThroughLine.equals("-1"))
			reqMap.put("throughLine", searchThroughLine);

		if (!searchTokenVehDepot.equals("-1"))
			reqMap.put("tokenVehDepot", searchTokenVehDepot);

		if (!searchAcc.equals("-1"))
			reqMap.put("postName", searchAcc);

		try {

			String _crewDate = DateUtil.getFormateDayShort(crewDate);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet("填报车底_" + _crewDate);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4500);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4500);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 8000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4500);
			sheet.setColumnWidth((short) 13, (short) 4000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(crewDate + " " + "填报车底");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 1);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("铁路线");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 2);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("首车日期");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 3);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("交路");

			// 备注
			HSSFCell chexing = row1.createCell((short) 4);
			chexing.setCellType(HSSFCell.CELL_TYPE_STRING);
			chexing.setCellStyle(styleTitle);
			chexing.setCellValue("车型");

			HSSFCell chedi1 = row1.createCell((short) 5);
			chedi1.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi1.setCellStyle(styleTitle);
			chedi1.setCellValue("车底1");

			HSSFCell chedi2 = row1.createCell((short) 6);
			chedi2.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi2.setCellStyle(styleTitle);
			chedi2.setCellValue("车底2");

			HSSFCell chukushifa = row1.createCell((short) 7);
			chukushifa.setCellType(HSSFCell.CELL_TYPE_STRING);
			chukushifa.setCellStyle(styleTitle);
			chukushifa.setCellValue("出库所/始发站");

			HSSFCell rukuzhongdao = row1.createCell((short) 8);
			rukuzhongdao.setCellType(HSSFCell.CELL_TYPE_STRING);
			rukuzhongdao.setCellStyle(styleTitle);
			rukuzhongdao.setCellValue("入库所/终到站");

			HSSFCell rebei = row1.createCell((short) 9);
			rebei.setCellType(HSSFCell.CELL_TYPE_STRING);
			rebei.setCellStyle(styleTitle);
			rebei.setCellValue("热备");

			HSSFCell dandang = row1.createCell((short) 10);
			dandang.setCellType(HSSFCell.CELL_TYPE_STRING);
			dandang.setCellStyle(styleTitle);
			dandang.setCellValue("担当局");

			HSSFCell dongchesuo = row1.createCell((short) 11);
			dongchesuo.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchesuo.setCellStyle(styleTitle);
			dongchesuo.setCellValue("动车所");

			HSSFCell dongchetai = row1.createCell((short) 12);
			dongchetai.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchetai.setCellStyle(styleTitle);
			dongchetai.setCellValue("管辖动车台");

			HSSFCell laiyuan = row1.createCell((short) 13);
			laiyuan.setCellType(HSSFCell.CELL_TYPE_STRING);
			laiyuan.setCellStyle(styleTitle);
			laiyuan.setCellValue("来源");
			// {tokenVehBureau=P, crossDate=20150202, tokenVehDepot=北京南动车所,
			// crossBureau=P}

			// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
			// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}

			List<HighlineCrossInfo> list = highLineService
					.getHighlineCrossList(reqMap);

			// 查询乘务上报信息
			// List<HighLineCrewInfo> list =
			// highLineCrewService.findList(_crewDate,
			// crewType,recordPeopleOrg);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighlineCrossInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(2 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);
					// 序号 铁路线 首车日期 交路 车型 车底1 车底2 出库所/始发站 入库所/终到站 热备 担当局 动车所
					// 管辖动车台 来源

					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 1);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getThroughLine());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 2);
					// crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrossStartDate());
					// 经由铁路线 交路 车型 车底1 车底2

					HSSFCell throughLineCellFor = rowX.createCell((short) 3);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getCrossName());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 4);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getCrhType());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 5);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getVehicle1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 6);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getVehicle2());
					// 姓名2 出库所/始发站 入库所/终到站 热备
					HSSFCell name2CellFor = rowX.createCell((short) 7);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getCrossStartStn());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 8);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getCrossEndStn());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 9);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity2CellFor.setCellStyle(styleTitle);
					// identity2CellFor.setCellValue(obj.getSpareFlag());

					if ("1".equals(obj.getSpareFlag())) {
						identity2CellFor.setCellValue("否");
					} else {
						identity2CellFor.setCellValue("是");
					}
					// 备注 担当局
					// 动车所 管辖动车台 来源
					HSSFCell noteCellFor = rowX.createCell((short) 10);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(pingyin.get(obj
							.getTokenVehBureau()));

					HSSFCell noteCellFor1 = rowX.createCell((short) 11);
					noteCellFor1.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor1.setCellStyle(styleTitle);
					noteCellFor1.setCellValue(obj.getTokenVehDepot());

					HSSFCell noteCellFor2 = rowX.createCell((short) 12);
					noteCellFor2.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor2.setCellStyle(styleTitle);
					noteCellFor2.setCellValue(obj.getPostName());

					HSSFCell noteCellFor3 = rowX.createCell((short) 13);
					noteCellFor3.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor3.setCellStyle(styleTitle);
					noteCellFor3.setCellValue(obj.getCreateReason());

				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 13)); // 标题 合并1行11列
			/*
			 * sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
			 * (short) 0)); //序号 合并2行1列 sheet.addMergedRegion(new Region((short)
			 * 1, (short) 1, (short) 2, (short) 1)); //乘务交路 合并2行1列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
			 * (short) 2)); //车队组号 合并2行1列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 3, (short) 2, (short) 3)); //经由铁路线
			 * 合并2行1列 sheet.addMergedRegion(new Region((short) 1, (short) 4,
			 * (short) 1, (short) 6)); //司机1 合并1行3列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 7, (short) 1, (short) 9)); //司机2 合并1行3列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 10, (short)
			 * 2, (short) 10)); //备注 合并2行1列
			 */

			String filename = this.encodeFilename("填报车底_" + _crewDate + ".xls",
					request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				// ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@RequestMapping(value = "/exportExcelcross/{crewType}/{crewDate}/{trainNbr}/{highlineFlag}/{searchThroughLine}/{searchTokenVehDepot}/{searchAcc}/{bureaus}", method = RequestMethod.GET)
	public void exportExcelcross(@PathVariable("crewType") String crewType,
			@PathVariable("crewDate") String crewDate,
			@PathVariable("trainNbr") String trainNbr,
			@PathVariable("highlineFlag") String highlineFlag1,
			@PathVariable("searchThroughLine") String searchThroughLine,
			@PathVariable("searchTokenVehDepot") String searchTokenVehDepot,
			@PathVariable("searchAcc") String searchAcc,
			@PathVariable("bureaus") String bureaus,
			HttpServletRequest request, HttpServletResponse response) {
		// searchThroughLine+"/"+searchTokenVehDepot+"/"+searchAcc
		// {tokenVehBureau=P, crossDate=20150202, crossBureau=P}
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String recordPeopleOrg = null;
		if (!"all".equals(crewType)) {
			recordPeopleOrg = user.getDeptName();
		}

		Map<String, String> pingyin = new HashMap<String, String>();
		List<Map<String, Object>> listmap = baseDataService.getBureauList();
		for (Map<String, Object> map : listmap) {
			pingyin.put(map.get("LJPYM").toString(), map.get("LJJC").toString());
		}

		// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
		// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}
		// System.out.println(crewDate+trainNbr
		// +searchThroughLine+searchTokenVehDepot+searchAcc+DateUtil.getFormateDayShort(crewDate));

		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("crossBureau", user.getBureau());// 仅查询本局

		reqMap.put("crossDate", DateUtil.getFormateDayShort(crewDate));// 仅查询本局
		if (!trainNbr.equals("-1"))
			reqMap.put("trainNbr", trainNbr);

		if (!bureaus.equals("-1") && !"undefined".equals(bureaus))
			reqMap.put("tokenVehBureau", bureaus);

		if (!searchThroughLine.equals("-1"))
			reqMap.put("throughLine", searchThroughLine);

		if (!searchTokenVehDepot.equals("-1"))
			reqMap.put("tokenVehDepot", searchTokenVehDepot);

		if (!searchAcc.equals("-1"))
			reqMap.put("postName", searchAcc);

		try {

			String _crewDate = DateUtil.getFormateDayShort(crewDate);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet("车底交路_" + _crewDate);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4500);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4500);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 8000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4500);
			sheet.setColumnWidth((short) 13, (short) 4000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(crewDate + " " + "车底交路");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 1);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("铁路线");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 2);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("首车日期");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 3);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("交路");

			// 备注
			HSSFCell chexing = row1.createCell((short) 4);
			chexing.setCellType(HSSFCell.CELL_TYPE_STRING);
			chexing.setCellStyle(styleTitle);
			chexing.setCellValue("车型");

			HSSFCell chedi1 = row1.createCell((short) 5);
			chedi1.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi1.setCellStyle(styleTitle);
			chedi1.setCellValue("车底1");

			HSSFCell chedi2 = row1.createCell((short) 6);
			chedi2.setCellType(HSSFCell.CELL_TYPE_STRING);
			chedi2.setCellStyle(styleTitle);
			chedi2.setCellValue("车底2");

			HSSFCell chukushifa = row1.createCell((short) 7);
			chukushifa.setCellType(HSSFCell.CELL_TYPE_STRING);
			chukushifa.setCellStyle(styleTitle);
			chukushifa.setCellValue("出库所/始发站");

			HSSFCell rukuzhongdao = row1.createCell((short) 8);
			rukuzhongdao.setCellType(HSSFCell.CELL_TYPE_STRING);
			rukuzhongdao.setCellStyle(styleTitle);
			rukuzhongdao.setCellValue("入库所/终到站");

			HSSFCell rebei = row1.createCell((short) 9);
			rebei.setCellType(HSSFCell.CELL_TYPE_STRING);
			rebei.setCellStyle(styleTitle);
			rebei.setCellValue("热备");

			HSSFCell dandang = row1.createCell((short) 10);
			dandang.setCellType(HSSFCell.CELL_TYPE_STRING);
			dandang.setCellStyle(styleTitle);
			dandang.setCellValue("担当局");

			HSSFCell dongchesuo = row1.createCell((short) 11);
			dongchesuo.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchesuo.setCellStyle(styleTitle);
			dongchesuo.setCellValue("动车所");

			HSSFCell dongchetai = row1.createCell((short) 12);
			dongchetai.setCellType(HSSFCell.CELL_TYPE_STRING);
			dongchetai.setCellStyle(styleTitle);
			dongchetai.setCellValue("管辖动车台");

			HSSFCell laiyuan = row1.createCell((short) 13);
			laiyuan.setCellType(HSSFCell.CELL_TYPE_STRING);
			laiyuan.setCellStyle(styleTitle);
			laiyuan.setCellValue("来源");
			// {tokenVehBureau=P, crossDate=20150202, tokenVehDepot=北京南动车所,
			// crossBureau=P}

			// {tokenVehBureau=B, crossDate=20150202, throughLine=京广高铁,
			// tokenVehDepot=北京南动车所, postName=京局动车调度一台, crossBureau=P}

			List<HighlineCrossInfo> list = highLineService
					.getHighlineCrossList(reqMap);

			// 查询乘务上报信息
			// List<HighLineCrewInfo> list =
			// highLineCrewService.findList(_crewDate,
			// crewType,recordPeopleOrg);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighlineCrossInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(2 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);
					// 序号 铁路线 首车日期 交路 车型 车底1 车底2 出库所/始发站 入库所/终到站 热备 担当局 动车所
					// 管辖动车台 来源
					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 1);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getThroughLine());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 2);
					// crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrossStartDate());
					// 经由铁路线 交路 车型 车底1 车底2

					HSSFCell throughLineCellFor = rowX.createCell((short) 3);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getCrossName());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 4);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getCrhType());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 5);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getVehicle1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 6);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getVehicle2());
					// 姓名2 出库所/始发站 入库所/终到站 热备
					HSSFCell name2CellFor = rowX.createCell((short) 7);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getCrossStartStn());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 8);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getCrossEndStn());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 9);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// identity2CellFor.setCellStyle(styleTitle);
					// identity2CellFor.setCellValue(obj.getSpareFlag());

					if ("1".equals(obj.getSpareFlag())) {
						identity2CellFor.setCellValue("否");
					} else {
						identity2CellFor.setCellValue("是");
					}
					// 备注 担当局
					// 动车所 管辖动车台 来源
					HSSFCell noteCellFor = rowX.createCell((short) 10);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(pingyin.get(obj
							.getTokenVehBureau()));

					HSSFCell noteCellFor1 = rowX.createCell((short) 11);
					noteCellFor1.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor1.setCellStyle(styleTitle);
					noteCellFor1.setCellValue(obj.getTokenVehDepot());

					HSSFCell noteCellFor2 = rowX.createCell((short) 12);
					noteCellFor2.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor2.setCellStyle(styleTitle);
					noteCellFor2.setCellValue(obj.getPostName());

					HSSFCell noteCellFor3 = rowX.createCell((short) 13);
					noteCellFor3.setCellType(HSSFCell.CELL_TYPE_STRING);
					// noteCellFor3.setCellStyle(styleTitle);
					noteCellFor3.setCellValue(obj.getCreateReason());

				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 13)); // 标题 合并1行11列
			/*
			 * sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
			 * (short) 0)); //序号 合并2行1列 sheet.addMergedRegion(new Region((short)
			 * 1, (short) 1, (short) 2, (short) 1)); //乘务交路 合并2行1列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
			 * (short) 2)); //车队组号 合并2行1列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 3, (short) 2, (short) 3)); //经由铁路线
			 * 合并2行1列 sheet.addMergedRegion(new Region((short) 1, (short) 4,
			 * (short) 1, (short) 6)); //司机1 合并1行3列 sheet.addMergedRegion(new
			 * Region((short) 1, (short) 7, (short) 1, (short) 9)); //司机2 合并1行3列
			 * sheet.addMergedRegion(new Region((short) 1, (short) 10, (short)
			 * 2, (short) 10)); //备注 合并2行1列
			 */

			String filename = this.encodeFilename("车底交路_" + _crewDate + ".xls",
					request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				// ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@RequestMapping(value = "/exportExcel/{crewType}/{crewDate}/{trainNbr}/{highlineFlag}", method = RequestMethod.GET)
	public void exportExcel(@PathVariable("crewType") String crewType,
			@PathVariable("crewDate") String crewDate,
			@PathVariable("trainNbr") String trainNbr,
			@PathVariable("highlineFlag") String highlineFlag1,
			HttpServletRequest request, HttpServletResponse response) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String recordPeopleOrg = null;
		if (!"all".equals(crewType)) {
			recordPeopleOrg = user.getDeptName();
		}
		try {
			String name = "";// 乘务类型（1车长、2司机、3机械师）
			if ("1".equals(crewType) || "11".equals(crewType)) {
				name = "车长";
			} else if ("2".equals(crewType) || "12".equals(crewType)) {
				name = "司机";
			} else if ("3".equals(crewType)) {
				name = "机械师";
			}

			// 格式化时间
			String _crewDate = DateUtil.getFormateDayShort(crewDate);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet(name + "乘务信息_" + _crewDate);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 4500);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4500);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 8000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(crewDate + " " + name + "乘务计划");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 1);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("乘务交路");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 2);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("乘务组编号");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 3);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("经由铁路线");
			// 司机1
			HSSFCell sj1Cell = row1.createCell((short) 4);
			sj1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			sj1Cell.setCellStyle(styleTitle);
			if (highlineFlag1.equals("1")) {
				sj1Cell.setCellValue(name + "1");
			} else {
				sj1Cell.setCellValue(name);// 乘务类型（1车长、2司机、3机械师）
			}
			HSSFCell blankCell = row1.createCell((short) 5);
			HSSFCell blankCell1 = row1.createCell((short) 6);
			// 司机2
			HSSFCell sj2Cell = row1.createCell((short) 7);
			sj2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			sj2Cell.setCellStyle(styleTitle);
			if (highlineFlag1.equals("1")) {
				sj2Cell.setCellValue(name + "2");
			} else {
				sj2Cell.setCellValue("业务员");// 乘务类型（1车长、2司机、3机械师）
			}
			HSSFCell blankCell8 = row1.createCell((short) 8);
			HSSFCell blankCell9 = row1.createCell((short) 9);

			// 备注
			HSSFCell noteCell = row1.createCell((short) 10);
			noteCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			noteCell.setCellStyle(styleTitle);
			noteCell.setCellValue("备注");

			HSSFRow row2 = sheet.createRow(2);// 创建一个行对象
			row2.setHeightInPoints(23);
			HSSFCell blankRowCell0 = row2.createCell((short) 0);
			HSSFCell blankRowCell1 = row2.createCell((short) 1);
			HSSFCell blankRowCell2 = row2.createCell((short) 2);
			HSSFCell blankRowCell3 = row2.createCell((short) 3);
			// 姓名1
			HSSFCell name1Cell = row2.createCell((short) 4);
			name1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			name1Cell.setCellStyle(styleTitle);
			name1Cell.setCellValue("姓名");
			// 电话1
			HSSFCell tel1Cell = row2.createCell((short) 5);
			tel1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			tel1Cell.setCellStyle(styleTitle);
			tel1Cell.setCellValue("电话");
			// 政治面貌1
			HSSFCell identity1Cell = row2.createCell((short) 6);
			identity1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			identity1Cell.setCellStyle(styleTitle);
			identity1Cell.setCellValue("政治面貌");
			// 姓名2
			HSSFCell name2Cell = row2.createCell((short) 7);
			name2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			name2Cell.setCellStyle(styleTitle);
			name2Cell.setCellValue("姓名");
			// 电话2
			HSSFCell tel2Cell = row2.createCell((short) 8);
			tel2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			tel2Cell.setCellStyle(styleTitle);
			tel2Cell.setCellValue("电话");
			// 政治面貌2
			HSSFCell identity2Cell = row2.createCell((short) 9);
			identity2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			identity2Cell.setCellStyle(styleTitle);
			identity2Cell.setCellValue("政治面貌");
			HSSFCell blankRowCell10 = row2.createCell((short) 10);

			// 查询乘务上报信息
			List<HighLineCrewInfo> list = highLineCrewService.findList(
					_crewDate, crewType, recordPeopleOrg);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighLineCrewInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(3 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);

					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 1);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getCrewCross());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 2);
					crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrewGroup());
					// 经由铁路线
					HSSFCell throughLineCellFor = rowX.createCell((short) 3);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getThroughLine());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 4);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getName1());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 5);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getTel1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 6);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getIdentity1());
					// 姓名2
					HSSFCell name2CellFor = rowX.createCell((short) 7);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getName2());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 8);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getTel2());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 9);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity2CellFor.setCellStyle(styleTitle);
					identity2CellFor.setCellValue(obj.getIdentity2());
					// 备注
					HSSFCell noteCellFor = rowX.createCell((short) 10);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(obj.getNote());
				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 10)); // 标题 合并1行11列
			sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
					(short) 0)); // 序号 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 1, (short) 2,
					(short) 1)); // 乘务交路 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
					(short) 2)); // 车队组号 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 3, (short) 2,
					(short) 3)); // 经由铁路线 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 4, (short) 1,
					(short) 6)); // 司机1 合并1行3列
			sheet.addMergedRegion(new Region((short) 1, (short) 7, (short) 1,
					(short) 9)); // 司机2 合并1行3列
			sheet.addMergedRegion(new Region((short) 1, (short) 10, (short) 2,
					(short) 10)); // 备注 合并2行1列

			String filename = this.encodeFilename(name + "乘务信息_" + _crewDate
					+ ".xls", request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				// ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置下载文件中文件的名称
	 *
	 * @param filename
	 * @param request
	 * @return
	 * @author denglj
	 */
	private String encodeFilename(String filename, HttpServletRequest request) {
		/**
		 * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
		 * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
		 * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
		 * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
		 */
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"),
							"ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
				// return MimeUtility.encodeText(filename, "UTF-8", "B");
				return new String(filename.getBytes("UTF-8"), "ISO8859-1");

			return filename;
		} catch (Exception ex) {
			ex.printStackTrace();
			return filename;
		}
	}

	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 导入乘务信息excel
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author denglj
	 */
	@ResponseBody
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result importExcel(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		Map<String, Object> rmap = new HashMap<String, Object>();
		// 创建 POI文件系统对象
		POIFSFileSystem ts = null;
		try {
			int successRec = 0; // 成功记录数
			int errorRec = 0; // 失败记录数

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String crewType = request.getParameter("crewType");
			String crewDate = DateUtil.getFormateDayShort(request
					.getParameter("crewDate"));
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 上传文件
				MultipartFile mf = entity.getValue();
				// 创建文件输入流对象
				ts = new POIFSFileSystem(mf.getInputStream());
				// 获取文档对象
				HSSFWorkbook wb = new HSSFWorkbook(ts);
				// 获取工作薄
				HSSFSheet sheet = wb.getSheetAt(0);
				// 声明行对象
				HSSFRow rowFirst = sheet.getRow(3);
				if (rowFirst == null || rowFirst.getCell((short) 10) == null) {
					result.setCode("101");
					result.setMessage("请按模板导入");
					return result;
				}

				// 目前界面只支持上传一个文件，故此写法没问题
				// 删除该日期内 所有上报类型
				highLineCrewService.deleteHighlineCrewForCrewDate(crewDate,
						crewType, user.getName());

				List<HighLineCrewInfo> list = new ArrayList<HighLineCrewInfo>();
				// 通过循环获取每一行 0\1\2行为标题
				for (int i = 3; sheet.getRow(i) != null; i++) {
					try {

						// 得到行
						HSSFRow row = sheet.getRow(i);

						String crewCross = String.valueOf(row
								.getCell((short) 1));
						if (crewCross == null || crewCross.equals("null")) {
							crewCross = "";
						} else {
							crewCross = String.valueOf(row.getCell((short) 1));
						}

						String crewGroup = String.valueOf(row
								.getCell((short) 2));
						if (crewGroup == null || crewGroup.equals("null")) {
							crewGroup = "";
						} else {
							crewGroup = String.valueOf(row.getCell((short) 2));
						}

						String throughLine = String.valueOf(row
								.getCell((short) 3));

						if (throughLine == null || throughLine.equals("null")) {
							throughLine = "";
						} else {
							throughLine = String
									.valueOf(row.getCell((short) 3));
						}

						String name11 = String.valueOf(row.getCell((short) 4));
						if (name11 == null || name11.equals("null")) {
							name11 = "";
						} else {
							name11 = String.valueOf(row.getCell((short) 4));
						}

						String tel1 = String.valueOf(row.getCell((short) 5));
						if (tel1 == null || tel1.equals("null")) {
							tel1 = "";
						} else {
							tel1 = String.valueOf(row.getCell((short) 5));
						}

						String identity1 = String.valueOf(row
								.getCell((short) 6));
						if (identity1 == null || identity1.equals("null")) {
							identity1 = "";
						} else {
							identity1 = String.valueOf(row.getCell((short) 6));
						}

						String name2 = String.valueOf(row.getCell((short) 7));
						if (name2 == null || name2.equals("null")) {
							name2 = "";
						} else {
							name2 = String.valueOf(row.getCell((short) 7));
						}

						String tel2 = String.valueOf(row.getCell((short) 8));
						if (tel2 == null || tel2.equals("null")) {
							tel2 = "";
						} else {
							tel2 = String.valueOf(row.getCell((short) 8));
						}

						String identity2 = String.valueOf(row
								.getCell((short) 9));
						if (identity2 == null || identity2.equals("null")) {
							identity2 = "";
						} else {
							identity2 = String.valueOf(row.getCell((short) 9));
						}

						String note = String.valueOf(row.getCell((short) 10));
						if (note == null || note.equals("null")) {
							note = "";
						} else {
							note = String.valueOf(row.getCell((short) 10));
						}

						HighLineCrewInfo addObj = new HighLineCrewInfo();
						addObj.setIndex(String.valueOf(row.getCell((short) 0)));
						addObj.setCrewDate(subZeroAndDot(crewDate));
						addObj.setCrewType(subZeroAndDot(crewType));// 乘务类型（1车长、2司机、3机械师）
						addObj.setCrewCross(subZeroAndDot(crewCross));// 乘务交路
						addObj.setCrewGroup(subZeroAndDot(crewGroup));// 车队组号
						addObj.setThroughLine(subZeroAndDot(throughLine));// 经由铁路线
						addObj.setName1(subZeroAndDot(name11));// 乘务员1姓名
						addObj.setTel1(subZeroAndDot(tel1));// 乘务员1电话
						addObj.setIdentity1(subZeroAndDot(identity1));// 乘务员1身份（党员、群众等）
						addObj.setName2(subZeroAndDot(name2));// 乘务员2姓名
						addObj.setTel2(subZeroAndDot(tel2));// 乘务员2电话
						addObj.setIdentity2(subZeroAndDot(identity2));// 乘务员2身份（党员、群众等）
						addObj.setNote(subZeroAndDot(note));// 备注
						list.add(addObj);
						// this.addHighLineCrewFromExcel(addObj);

						successRec++;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						errorRec++;
					}

				}
				if (list != null && list.size() > 0) {
					// ComparatorObj comparator=new ComparatorObj();
					Collections.reverse(list);
					for (int i = 0; i < list.size(); i++) {
						try {
							// 得到行
							HSSFRow row = sheet.getRow(i);
							HighLineCrewInfo addObj = list.get(i);
							int maxSortId = highLineCrewService.getMaxSortId();
							addObj.setCrewDate(addObj.getCrewDate());
							addObj.setCrewType(addObj.getCrewType());// 乘务类型（1车长、2司机、3机械师）
							addObj.setCrewCross(addObj.getCrewCross());// 乘务交路
							addObj.setCrewGroup(addObj.getCrewGroup());// 车队组号
							addObj.setThroughLine(addObj.getThroughLine());// 经由铁路线
							addObj.setName1(addObj.getName1());// 乘务员1姓名
							addObj.setTel1(addObj.getTel1());// 乘务员1电话
							addObj.setIdentity1(addObj.getIdentity1());// 乘务员1身份（党员、群众等）
							addObj.setName2(addObj.getName2());// 乘务员2姓名
							addObj.setTel2(addObj.getTel2());// 乘务员2电话
							addObj.setIdentity2(addObj.getIdentity2());// 乘务员2身份（党员、群众等）
							addObj.setNote(addObj.getNote());// 备注
							addObj.setSortId(maxSortId + 1);
							this.addHighLineCrewFromExcel(addObj);

							successRec++;
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							errorRec++;
						}
					}

				}

			}

			rmap.put("successRec", successRec);
			rmap.put("errorRec", errorRec);
			result.setData(rmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode("401");
			result.setData(rmap);
			result.setMessage("导入失败");
		}
		return result;
	}

	/**
	 * 获取表highline_crew中record_people_org的值列表
	 *
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getRecordPeopleOrgList", method = RequestMethod.GET)
	public Result getRecordPeopleOrgList() {
		Result result = new Result();
		try {
			result.setData(highLineCrewService.getRecordPeopleOrgList());
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 对highline_crew进行条件分页查询
	 *
	 * @param reqMap
	 *            主要有这些字段： crewStartDate;crewEndDate;crewType;
	 *            crewBureau;recordPeopleOrg;trainNbr;name;rownumstart;rownumend
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHighlineCrewBaseInfoForPage", method = RequestMethod.POST)
	public Result getHighlineCrewBaseInfoForPage(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.debug("getHighlineCrewBaseInfoForPag~~~reqMap==" + reqMap);
			String crewStartDate = StringUtil.objToStr(reqMap
					.get("crewStartDate"));
			String crewEndDate = StringUtil.objToStr(reqMap.get("crewEndDate"));
			if (crewStartDate != null && !"".equals(crewStartDate)) {
				crewStartDate = DateUtil.getFormateDayShort(crewStartDate);
				reqMap.put("crewStartDate", crewStartDate);
			}
			if (crewEndDate != null && !"".equals(crewEndDate)) {
				crewEndDate = DateUtil.getFormateDayShort(crewEndDate);
				reqMap.put("crewEndDate", crewEndDate);
			}
			if (reqMap.get("crewBureau") != null
					&& "".equals(reqMap.get("crewBureau").toString().trim())) {
				reqMap.remove("crewBureau");
			}

			QueryResult<HighLineCrewInfo> queryResult = highLineCrewService
					.getHighlineCrewBaseInfoForPage(reqMap);
			PagingResult page = new PagingResult(queryResult.getTotal(),
					queryResult.getRows());
			result.setData(page);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 保存乘务信息 被导入Excel方法调用
	 *
	 * @param highLineCrewInfo
	 * @author denglj
	 */
	private void addHighLineCrewFromExcel(HighLineCrewInfo highLineCrewInfo) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		highLineCrewInfo.setCrewHighlineId(UUID.randomUUID().toString());
		// 所属局简称
		highLineCrewInfo.setCrewBureau(user.getBureauShortName());
		highLineCrewInfo.setRecordPeople(user.getName());
		highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
		highLineCrewInfo.setSubmitType(0);
		highLineCrewService.addCrew(highLineCrewInfo);
	}

	/**
	 * 导出全部乘务计划信息excel
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author denglj
	 */
	@ResponseBody
	@RequestMapping(value = "/exportAllCrewTypeExcel", method = RequestMethod.GET)
	public void exportAllCrewTypeExcel(@PathParam("crewType") String crewType,
			@PathParam("trainNbr") String trainNbr,
			@PathParam("crewStartDate") String crewStartDate,
			@PathParam("crewEndDate") String crewEndDate,
			@PathParam("crewBureau") String crewBureau,
			@PathParam("recordPeopleOrg") String recordPeopleOrg,
			@PathParam("name") String name, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String dateTitleName = "";
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("crewType", crewType);
			if (trainNbr != null && !"".equals(trainNbr.trim())) {
				reqMap.put("crewCross", trainNbr.trim());
			}
			if (crewStartDate != null && !"".equals(crewStartDate.trim())) {
				dateTitleName += crewStartDate;
				reqMap.put("crewStartDate",
						DateUtil.getFormateDayShort(crewStartDate.trim()));
			}
			if (crewEndDate != null && !"".equals(crewEndDate.trim())) {
				reqMap.put("crewEndDate",
						DateUtil.getFormateDayShort(crewEndDate.trim()));
				if (!crewStartDate.equals(crewEndDate)) {
					dateTitleName += "~" + crewEndDate;
				}
			}
			if (crewBureau != null) {
				try {
					crewBureau = java.net.URLDecoder
							.decode(crewBureau, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			} else {
				crewBureau = "";
			}
			if (name != null) {
				try {
					name = java.net.URLDecoder.decode(name, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			} else {
				name = "";
			}

			reqMap.put("crewBureau", crewBureau);
			reqMap.put("recordPeopleOrg", recordPeopleOrg);
			if (name != null && !"".equals(name.trim())) {
				reqMap.put("name", name.trim());
			}

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFSheet sheet = workBook.createSheet("乘务信息_" + dateTitleName);// 创建一个工作薄对象

			sheet.setColumnWidth((short) 0, (short) 2000);
			sheet.setColumnWidth((short) 1, (short) 7000);
			sheet.setColumnWidth((short) 2, (short) 4000);
			sheet.setColumnWidth((short) 3, (short) 7000);
			sheet.setColumnWidth((short) 4, (short) 4000);
			sheet.setColumnWidth((short) 5, (short) 7000);
			sheet.setColumnWidth((short) 6, (short) 4000);
			sheet.setColumnWidth((short) 7, (short) 4000);
			sheet.setColumnWidth((short) 8, (short) 4000);
			sheet.setColumnWidth((short) 9, (short) 4000);
			sheet.setColumnWidth((short) 10, (short) 4000);
			sheet.setColumnWidth((short) 11, (short) 4000);
			sheet.setColumnWidth((short) 12, (short) 4000);
			sheet.setColumnWidth((short) 13, (short) 8000);
			sheet.setColumnWidth((short) 14, (short) 10000);

			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			HSSFDataFormat format = workBook.createDataFormat();
			style.setDataFormat(format.getFormat("@"));// 表文为普通文本
			style.setWrapText(true);
			// 设置表文字体
			HSSFFont tableFont = workBook.createFont();
			tableFont.setFontHeightInPoints((short) 12); // 设置字体大小
			tableFont.setFontName("宋体"); // 设置为黑体字
			style.setFont(tableFont);

			HSSFCellStyle dataStyle = workBook.createCellStyle();
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);// 顶部边框粗线
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
			dataStyle.setDataFormat(HSSFDataFormat
					.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle styleTitle = workBook.createCellStyle();
			// 设置字体
			HSSFFont font = workBook.createFont();// 创建字体对象
			font.setFontHeightInPoints((short) 12);// 设置字体大小
			font.setFontName("黑体");// 设置为黑体字
			styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			styleTitle.setFont(font);// 将字体加入到样式对象

			HSSFRow row0 = sheet.createRow(0);// 创建一个行对象
			row0.setHeightInPoints(23);
			// 标题
			HSSFCell titleCell = row0.createCell((short) 0);
			titleCell.setCellStyle(styleTitle);
			titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			titleCell.setCellValue(dateTitleName + "乘务计划");

			HSSFRow row1 = sheet.createRow(1);// 创建一个行对象
			row1.setHeightInPoints(23);
			// 序号
			HSSFCell indexCell = row1.createCell((short) 0);
			indexCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			indexCell.setCellStyle(styleTitle);
			indexCell.setCellValue("序号");
			// 乘务类型
			HSSFCell crewTypeCell = row1.createCell((short) 1);
			crewTypeCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewTypeCell.setCellStyle(styleTitle);
			crewTypeCell.setCellValue("乘务类型");
			// 日期
			HSSFCell crewDateCell = row1.createCell((short) 2);
			crewDateCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewDateCell.setCellStyle(styleTitle);
			crewDateCell.setCellValue("日期");
			// 乘务交路
			HSSFCell crewCrossCell = row1.createCell((short) 3);
			crewCrossCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewCrossCell.setCellStyle(styleTitle);
			crewCrossCell.setCellValue("乘务交路");
			// 车队组号
			HSSFCell crewGroupCell = row1.createCell((short) 4);
			crewGroupCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewGroupCell.setCellStyle(styleTitle);
			crewGroupCell.setCellValue("乘务组编号");
			// 经由铁路线
			HSSFCell throughLineCell = row1.createCell((short) 5);
			throughLineCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			throughLineCell.setCellStyle(styleTitle);
			throughLineCell.setCellValue("经由铁路线");
			// 司机1
			HSSFCell sj1Cell = row1.createCell((short) 6);
			sj1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			sj1Cell.setCellStyle(styleTitle);
			sj1Cell.setCellValue("乘务员1");// 乘务类型（1车长、2司机、3机械师）
			HSSFCell blankCell = row1.createCell((short) 7);
			HSSFCell blankCell1 = row1.createCell((short) 8);
			// 司机2
			HSSFCell sj2Cell = row1.createCell((short) 9);
			sj2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			sj2Cell.setCellStyle(styleTitle);
			sj2Cell.setCellValue("乘务员2");// 乘务类型（1车长、2司机、3机械师）
			HSSFCell blankCell8 = row1.createCell((short) 10);
			HSSFCell blankCell9 = row1.createCell((short) 11);

			// 路局
			HSSFCell crewBureauCell = row1.createCell((short) 12);
			crewBureauCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			crewBureauCell.setCellStyle(styleTitle);
			crewBureauCell.setCellValue("路局");
			// 填报部门
			HSSFCell recordPeopleOrgCell = row1.createCell((short) 13);
			recordPeopleOrgCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			recordPeopleOrgCell.setCellStyle(styleTitle);
			recordPeopleOrgCell.setCellValue("填报部门");
			// 备注
			HSSFCell noteCell = row1.createCell((short) 14);
			noteCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			noteCell.setCellStyle(styleTitle);
			noteCell.setCellValue("备注");

			HSSFRow row2 = sheet.createRow(2);// 创建一个行对象
			row2.setHeightInPoints(23);
			HSSFCell blankRowCell0 = row2.createCell((short) 0);
			HSSFCell blankRowCell1 = row2.createCell((short) 1);
			HSSFCell blankRowCell2 = row2.createCell((short) 2);
			HSSFCell blankRowCell3 = row2.createCell((short) 3);
			HSSFCell blankRowCell4 = row2.createCell((short) 4);
			HSSFCell blankRowCell5 = row2.createCell((short) 5);
			// 姓名1
			HSSFCell name1Cell = row2.createCell((short) 6);
			name1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			name1Cell.setCellStyle(styleTitle);
			name1Cell.setCellValue("姓名");
			// 电话1
			HSSFCell tel1Cell = row2.createCell((short) 7);
			tel1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			tel1Cell.setCellStyle(styleTitle);
			tel1Cell.setCellValue("电话");
			// 政治面貌1
			HSSFCell identity1Cell = row2.createCell((short) 8);
			identity1Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			identity1Cell.setCellStyle(styleTitle);
			identity1Cell.setCellValue("政治面貌");
			// 姓名2
			HSSFCell name2Cell = row2.createCell((short) 9);
			name2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			name2Cell.setCellStyle(styleTitle);
			name2Cell.setCellValue("姓名");
			// 电话2
			HSSFCell tel2Cell = row2.createCell((short) 10);
			tel2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			tel2Cell.setCellStyle(styleTitle);
			tel2Cell.setCellValue("电话");
			// 政治面貌2
			HSSFCell identity2Cell = row2.createCell((short) 11);
			identity2Cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			identity2Cell.setCellStyle(styleTitle);
			identity2Cell.setCellValue("政治面貌");
			HSSFCell blankRowCell10 = row2.createCell((short) 12);
			HSSFCell blankRowCell12 = row2.createCell((short) 13);
			HSSFCell blankRowCell13 = row2.createCell((short) 14);

			// 查询乘务上报信息
			List<HighLineCrewInfo> list = highLineCrewService
					.getHighlineCrewBaseInfo(reqMap);
			// 循环生成列表
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HighLineCrewInfo obj = list.get(i);
					HSSFRow rowX = sheet.createRow(3 + i);// 创建一个行对象
					rowX.setHeightInPoints(23);

					// 序号
					HSSFCell indexCellFor = rowX.createCell((short) 0);
					indexCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					indexCellFor.setCellStyle(styleTitle);
					indexCellFor.setCellValue(i + 1);

					// 乘务类型 1车长、2司机、3机械师）
					HSSFCell cerwTypeCellFor = rowX.createCell((short) 1);
					cerwTypeCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					cerwTypeCellFor.setCellStyle(styleTitle);
					if ("1".equals(obj.getCrewType())
							|| "11".equals(obj.getCrewType())) {
						cerwTypeCellFor.setCellValue("车长");
					} else if ("2".equals(obj.getCrewType())
							|| "12".equals(obj.getCrewType())) {
						cerwTypeCellFor.setCellValue("司机");
					} else if ("3".equals(obj.getCrewType())) {
						cerwTypeCellFor.setCellValue("机械师");
					}
					// 日期
					HSSFCell crewDateCellFor = rowX.createCell((short) 2);
					crewDateCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewDateCellFor.setCellStyle(styleTitle);
					crewDateCellFor.setCellValue(obj.getCrewDate());

					// 乘务交路
					HSSFCell crewCrossCellFor = rowX.createCell((short) 3);
					crewCrossCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewCrossCellFor.setCellStyle(styleTitle);
					crewCrossCellFor.setCellValue(obj.getCrewCross());
					// 车队组号
					HSSFCell crewGroupCellFor = rowX.createCell((short) 4);
					crewGroupCellFor.setCellStyle(styleTitle);
					crewGroupCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewGroupCellFor.setCellValue(obj.getCrewGroup());
					// 经由铁路线
					HSSFCell throughLineCellFor = rowX.createCell((short) 5);
					throughLineCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					throughLineCellFor.setCellStyle(styleTitle);
					throughLineCellFor.setCellValue(obj.getThroughLine());
					// 姓名1
					HSSFCell name1CellFor = rowX.createCell((short) 6);
					name1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name1CellFor.setCellStyle(styleTitle);
					name1CellFor.setCellValue(obj.getName1());
					// 电话1
					HSSFCell tel1CellFor = rowX.createCell((short) 7);
					tel1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel1CellFor.setCellStyle(styleTitle);
					tel1CellFor.setCellValue(obj.getTel1());
					// 政治面貌1
					HSSFCell identity1CellFor = rowX.createCell((short) 8);
					identity1CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity1CellFor.setCellStyle(styleTitle);
					identity1CellFor.setCellValue(obj.getIdentity1());
					// 姓名2
					HSSFCell name2CellFor = rowX.createCell((short) 9);
					name2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					name2CellFor.setCellStyle(styleTitle);
					name2CellFor.setCellValue(obj.getName2());
					// 电话2
					HSSFCell tel2CellFor = rowX.createCell((short) 10);
					tel2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					tel2CellFor.setCellStyle(styleTitle);
					tel2CellFor.setCellValue(obj.getTel2());
					// 政治面貌2
					HSSFCell identity2CellFor = rowX.createCell((short) 11);
					identity2CellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					identity2CellFor.setCellStyle(styleTitle);
					identity2CellFor.setCellValue(obj.getIdentity2());

					// 填报路局
					HSSFCell crewBureauCellFor = rowX.createCell((short) 12);
					crewBureauCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					crewBureauCellFor.setCellStyle(styleTitle);
					crewBureauCellFor.setCellValue(obj.getCrewBureau());
					// 填报部门
					HSSFCell recordPeopleOrgCellFor = rowX
							.createCell((short) 13);
					recordPeopleOrgCellFor
							.setCellType(HSSFCell.CELL_TYPE_STRING);
					recordPeopleOrgCellFor.setCellStyle(styleTitle);
					recordPeopleOrgCellFor.setCellValue(obj
							.getRecordPeopleOrg());

					// 备注
					HSSFCell noteCellFor = rowX.createCell((short) 14);
					noteCellFor.setCellType(HSSFCell.CELL_TYPE_STRING);
					noteCellFor.setCellStyle(styleTitle);
					noteCellFor.setCellValue(obj.getNote());
				}
			}

			// 行列合并 四个参数分别是：起始行，起始列，结束行，结束列
			sheet.addMergedRegion(new Region((short) 0, (short) 0, (short) 0,
					(short) 14)); // 标题 合并1行11列
			sheet.addMergedRegion(new Region((short) 1, (short) 0, (short) 2,
					(short) 0)); // 序号 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 1, (short) 2,
					(short) 1)); // 乘务类型 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 2, (short) 2,
					(short) 2)); // 日期 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 3, (short) 2,
					(short) 3)); // 乘务交路 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 4, (short) 2,
					(short) 4)); // 车队组号 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 5, (short) 2,
					(short) 5)); // 经由铁路线 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 6, (short) 1,
					(short) 8)); // 司机1 合并1行3列
			sheet.addMergedRegion(new Region((short) 1, (short) 9, (short) 1,
					(short) 11)); // 司机2 合并1行3列
			sheet.addMergedRegion(new Region((short) 1, (short) 12, (short) 2,
					(short) 12)); // 填报路局 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 13, (short) 2,
					(short) 13)); // 填报部门 合并2行1列
			sheet.addMergedRegion(new Region((short) 1, (short) 14, (short) 2,
					(short) 14)); // 备注 合并2行1列

			String filename = this.encodeFilename("乘务信息_" + dateTitleName
					+ ".xls", request);
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workBook.write(ouputStream);
				ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ouputStream != null) {
					ouputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
