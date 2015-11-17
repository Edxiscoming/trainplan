package org.railway.com.trainplan.common.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.OriginalCross;

public class CompareUtil {
	private static Log logger = LogFactory.getLog(CompareUtil.class.getName());
	/**原始对数表不用比对的字段*/
	private static final List<String> ORIGINALCROSS_UNCOMPARE_FIELDS = new ArrayList<String>(); 
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	static{
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("originalCrossId");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("createPeople");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("createPeopleOrg");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("checkPeople");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("checkTime");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("checkFlag");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("checkPeopleOrg");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("note");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("createTime");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("chartId");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("chartName");
		ORIGINALCROSS_UNCOMPARE_FIELDS.add("creatCrossTime");
	}
	
	/**
	 * 根据字段来比较对象
	 * @param org 比较的源对象
	 * @param tg 比较的目标对象
	 * @param clz 比较的对象类模板
	 * @param uncompareList 无须比对的字段
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean compareObjectByFields(Object org, Object tg, Class clz, List<String> uncompareList){
		try{
			logger.debug("===========================开始比对==========================="); 
			for(Field field : clz.getDeclaredFields()){
				String name = field.getName();
				if(!uncompareList.contains(name)){
					field.setAccessible(true);
					Object org_value = field.get(org);
					Object tg_value = field.get(tg);
					logger.debug("fieldName=" + name + " | org_value=" + org_value + " | tg_value=" + tg_value);
					if(field.getType() == String.class 
							&& !(org_value+"").equals(tg_value+"")){
						logger.debug("field " + name + " 校验失败!");
						return false;
					}
					if(field.getType().getName().equals("int") 
							&& Integer.parseInt(org_value+"") != Integer.parseInt(tg_value+"")){
						logger.debug("field " + name + " 校验失败!");
						return false;
					}
					if(field.getType() == Date.class 
							&& !FORMAT.format(org_value).equals(FORMAT.format(tg_value))){
						logger.debug("field " + name + " 校验失败!");
						return false;
					}
					/*若有其他类型的字段需要比对 这里添加其比对规则*/
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		logger.debug("===========================比对结束===========================");
		return true;
	}
	
	/**
	 * 对比OriginalCross，其中目前需要比对的项目全部为String类型或者int类型
	 * @param org
	 * @param tg
	 * @return
	 */
	public static boolean compareOriginalCross(OriginalCross org, OriginalCross tg){
		return compareObjectByFields(org, tg, OriginalCross.class, ORIGINALCROSS_UNCOMPARE_FIELDS);
	}
	
	public static void main(String[] args) throws Exception{
//		OriginalCross org = new OriginalCross();
//		org.setOriginalCrossId("asdasdasd123");
//		org.setChartId("chart_org");
//		org.setCrossName("a-b-cd");
//		org.setCrossSection("成广京");
//		org.setCutOld(1);
//		org.setHighlineRule("测试rule");
//		org.setCreateTime(FORMAT.parse("2015-09-09"));
//		
//		OriginalCross tg = new OriginalCross();
//		tg.setOriginalCrossId("asdasdasd456");
//		tg.setChartId("chart_org");
//		tg.setCrossName("a-b-cd");
//		tg.setCrossSection("成广京");
//		tg.setCutOld(1);
//		tg.setHighlineRule("测试rule");
//		tg.setCreateTime(FORMAT.parse("2015-09-09"));
//		
//		Object obj = new Date();
//		System.out.println(FORMAT.format(obj));
//		
//		System.out.println(compareObjectByFields(org, tg, OriginalCross.class, ORIGINALCROSS_UNCOMPARE_FIELDS));
	}
}
