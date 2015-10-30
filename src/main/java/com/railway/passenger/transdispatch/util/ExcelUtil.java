package com.railway.passenger.transdispatch.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

/**
 * 该类用于帮助解析和天聪EXCEL,目前没有分页的实现，所以数据量太大可能导致类存溢出
 * 使用凡是如下，
 * 先定义一个javabean
 * 然后做标题和bean的属性的map映射，
 *      LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		pm.put("crossIdForExcel", "序号");
		pm.put("crossName", "交路名");
		pm.put("crossSpareName", "备用交路名"); 
		pm.put("tokenVehBureau", "");  
 *    定义属性值得映射MAP
 *    Map<String,  Map<String, String>> valuesMap = new HashMap<String, Map<String, String>>();
 *    
 *    如tokenVehBureau 属性在EXCEL中保存的可能是路局的简称 如“京”，但是实际保存导数据库可鞥想装换成他的拼音码，就可以这样做:
 *    Map<String, String> tokenVehBureauMap = new  new HashMap<String, String>();
 *    tokenVehBureauMap.put("京"， "p");
 *    valuesMap。put("tokenVehBureau", tokenVehBureauMap);
 *    
 *    然后初始化一个帮助类
 *    ExcelUtil<CrossInfo> test = new ExcelUtil<CrossInfo>(pm, sheet, CrossInfo.class);
	       设  置 属性映射
	  test.setValueMapping(valuesMap);
	  getEntitiesHasNoHeader函数在你直接指导哪一行是数据行的开始，你可以直接闯入进行数据解析，但是这个时候pm的顺序一定要和标题行吻合
	      否者请使用getEntities(-1),它会自动找到合标题行吻合的行并从该行一下开始读取数据行
	  List<CrossInfo> list = test.getEntitiesHasNoHeader(4);  
 * @author Administrator
 *
 * @param <T>
 */
public class ExcelUtil<T> {
	private LinkedHashMap<String, String> propertyMapping;
	private HSSFSheet sheet;
	protected Class<T> entityClass;
	private Map<String, Short> map;
	private PropertyDescriptor[] propertyDescriptors;
	private DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
	private Map<String, ValueConvert> valueMapping = new HashMap<String, ValueConvert>();
	private String loginBureau;
	
	public ExcelUtil(LinkedHashMap<String, String> propertyMapping, HSSFSheet sheet, Class<T> c, String loginBureau){
		this.propertyMapping = propertyMapping;
		this.map = new HashMap<String, Short>();
		Short i = 0;
		for(String e : propertyMapping.keySet()){
			this.map.put(e, i++);
		}
		this.sheet = sheet;
		entityClass = c;
		this.loginBureau = loginBureau;
		try {
			BeanInfo beaninfo = Introspector.getBeanInfo(c);
			this.propertyDescriptors = beaninfo.getPropertyDescriptors();
		} catch (java.beans.IntrospectionException e1) {
			e1.printStackTrace();
		}
	}
	
	public List<T> getEntities(int headIndex){
		List<T> list = new LinkedList<T>();
		int rows = sheet.getPhysicalNumberOfRows();
		HSSFRow headRow = null;
		if(headIndex == -1){
			headIndex = getHeadIndex();
			if(headIndex != -1){
				headRow = sheet.getRow(headIndex);
			}
		}
		//System.out.println("sheet.getRow(headIndex)="+sheet.getRow(headIndex));
		Short[] st = getStatus(headRow);
		for(int i = headIndex + 1 ; i < rows; i++){
			list.add(getEntity(sheet.getRow(i), st));
		}
		return list;
	}
	
	public List<T> getEntitiesHasNoHeader(int startIndex){
		List<T> list = new ArrayList<T>();
		int rows = sheet.getPhysicalNumberOfRows();
	 
		//System.out.println("sheet.getRow(headIndex)="+sheet.getRow(headIndex));
		Short[] st = getStatus(null);
		for(int i = startIndex ; i < rows; i++){
			T t = getEntity(sheet.getRow(i), st);
			list.add(t);
		}
		return list;
	}
	
	private int getHeadIndex(){
		int rows = sheet.getPhysicalNumberOfRows();
		int like = 0;
		for(int i = 0; i < rows; i++){
			HSSFRow row = sheet.getRow(i);
			int cells = row.getPhysicalNumberOfCells();
			for(short j = 0; j < cells; j++){
				HSSFCell cell = row.getCell(j);
				//System.out.println("CellType:" + cell.getCellType());
				if(cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING && propertyMapping.values().contains(cell.getStringCellValue())){
					like++;
				}
				if(like > map.size()/2){
					return i;
				}			
			}
		}
		return -1;
	}
	
	private Short[] getStatus(HSSFRow row){
		row = null;
		Short[] status = new Short[map.size()];
		//System.out.println("map.size():" + map.size());
		if(row == null){
			for(String key : map.keySet()){
				status[map.get(key)] = map.get(key); 
			}
		}else{
			int cells = row.getPhysicalNumberOfCells();
			for(short j = 0; j < cells; j++){
				//System.out.println("row.getCell[" + j + "]=" + row.getCell(j));
				if(row.getCell(j) != null){
					//System.out.println("row.getCell(" + j + ").getCellType=" + row.getCell(j).getCellType());
					//System.out.println("HSSFCell.CELL_TYPE_STRING=" + HSSFCell.CELL_TYPE_STRING);
					switch (row.getCell(j).getCellType()){
					case HSSFCell.CELL_TYPE_STRING :
						for(String key : propertyMapping.keySet()){
							//System.out.println("propertyMapping.get(" + key + ")=" +propertyMapping.get(key));
							//System.out.println("row.getCell("+j+").getStringCellValue()"+row.getCell(j).getStringCellValue());
							if(propertyMapping.get(key).equals(row.getCell(j).getStringCellValue())){
								status[map.get(key)] = j;
								/*for (int i = 0; i < status.length; i++){
									System.out.println("status["+i+"]="+status[i]);
								}*/
							}
						}
						break;
					default:
						break;		
					}
				}
			}
		}
		return status;
	}
	
	private T getEntity(HSSFRow row, Short[] status){ 
		T t = null;
			try {
				t = entityClass.newInstance();
				for(int i = 0; i < propertyDescriptors.length; i++){
					try{
						PropertyDescriptor pd = propertyDescriptors[i];
						String propertyName = pd.getName();
						if(propertyMapping.keySet().contains(propertyName)){
							Method method = pd.getWriteMethod();
							HSSFCell cell = row.getCell(status[map.get(propertyName)]);
							Object value = null;
							if(cell != null){
								switch(cell.getCellType()){
								case HSSFCell.CELL_TYPE_NUMERIC : 
//									value = cell.getNumericCellValue();
									BigDecimal result = new BigDecimal(cell.getNumericCellValue());
									if(pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class){
										value = result.intValue();
									}else if(pd.getPropertyType() == Float.class || pd.getPropertyType() == float.class){
										value = result.floatValue();
									}else if(pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class){
										value = cell.getNumericCellValue();
									}else if(pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class){
										value = result.longValue();
									}else if(pd.getPropertyType() == Short.class || pd.getPropertyType() == short.class){
										value = result.shortValue();
									}else if(pd.getPropertyType() == String.class){
										value = result.toString();
									} 
									break;
								case HSSFCell.CELL_TYPE_STRING :
									String resultStr = cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim();
									if(pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class){
										value = Integer.parseInt(resultStr);
									}else if(pd.getPropertyType() == Float.class || pd.getPropertyType() == float.class){
										value = Float.parseFloat(resultStr);
									}else if(pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class){
										value = Double.parseDouble(resultStr);
									}else if(pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class){
										value = Long.parseLong(resultStr);
									}else if(pd.getPropertyType() == Short.class || pd.getPropertyType() == short.class){
										value = Short.parseShort(resultStr);
									}else if(pd.getPropertyType() == String.class){
										 value = resultStr;
									} 
									break;
								default :
									break;
								}	
								if("excelId".equals(propertyName)){//为合并单元格后的数据添加统一标识符
									value = isCombineCell(getMergeCell(), cell, row, status[map.get(propertyName)]);
								}
							}
							
							String type = pd.getPropertyType().getName();
							if(type.endsWith("Date") && value != null){	 
									value = format.parseObject(String.valueOf(value));							
							}
							if(valueMapping != null && valueMapping.containsKey(propertyName)){
								ValueConvert vc = valueMapping.get(propertyName);  
								value = vc.convert(value);
							}		
							if(value != null){ 
								method.invoke(t, value);	
							} 
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		return t;				
	}
	
	/**
	 * 获取合并的单元格
	 * @Description: TODO
	 * @param @return   
	 * @return List<Region>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月29日
	 */
	private List<Region> getMergeCell(){
		List<Region> list = new ArrayList<Region>();
		int sheetmergerCount = sheet.getNumMergedRegions();
	    //遍历合并单元格
	    for(int j = 0; j<sheetmergerCount;j++) 
	    {
	      //获得合并单元格加入list中
	    	Region ca = sheet.getMergedRegionAt(j);
	    	list.add(ca);
	    }
		return list;
	}
	
	/**
	   * 判断单元格是否为合并单元格，是的话则将单元格的值返回
	   * @param listCombineCell 存放合并单元格的list
	   * @param cell 需要判断的单元格
	   * @param sheet sheet
	   * @return
	   */
	   private Object isCombineCell(List<Region> listCombineCell, HSSFCell cell, HSSFRow row, int columnIndex) throws Exception
	   {
	     int firstC = 0;
	     int lastC = 0;
	     int firstR = 0;
	     int lastR = 0;
	     Object cellValue = null;
	     for(Region ca :listCombineCell)
	     {
	       //获得合并单元格的起始行, 结束行, 起始列, 结束列
	       firstC = ca.getColumnFrom();
	       lastC = ca.getColumnTo();
	       firstR = ca.getRowFrom();
	       lastR = ca.getRowTo();
	       if(row.getRowNum() >= firstR && row.getRowNum() <= lastR) 
	       {
	         if(columnIndex >= firstC && columnIndex <= lastC) 
	         {
	        	HSSFRow fRow = sheet.getRow(firstR);
	        	HSSFCell fCell = fRow.getCell(Short.parseShort(String.valueOf(firstC)));
	        	if(fCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ){
	        		BigDecimal result = new BigDecimal(fCell.getNumericCellValue());
	        		cellValue = result.toString();
	        	}else if(fCell.getCellType() == HSSFCell.CELL_TYPE_STRING ){
	        		String resultStr = fCell.getStringCellValue() == null ? "" : fCell.getStringCellValue().trim();
	        		cellValue = resultStr;
	        	}
	           return cellValue;
	         }
	       }
	       else
	       {
	         cellValue = "";
	       }
	     }
	     return cellValue;
	   }

	public LinkedHashMap<String, String> getPropertyMapping() {
		return propertyMapping;
	}

	public void setPropertyMapping(LinkedHashMap<String, String> propertyMapping) {
		this.propertyMapping = propertyMapping;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public Map<String, Short> getMap() {
		return map;
	}

	public void setMap(Map<String, Short> map) {
		this.map = map;
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public void setPropertyDescriptors(PropertyDescriptor[] propertyDescriptors) {
		this.propertyDescriptors = propertyDescriptors;
	}

	public DateFormat getFormat() {
		return format;
	}

	public void setFormat(DateFormat format) {
		this.format = format;
	}  
	
	static class ValueConvert{
		
		private String reg = null;
		
		private String loginBureau;
		
		private Map<String, String> valueMap = new HashMap<String, String>();  
		
		public ValueConvert(Map<String, String> valueMap, String loginBureau){
			StringBuffer regStr = new StringBuffer("("); 
			for(String key : valueMap.keySet()){
				if(regStr.length() > 1){
					regStr.append("|");
				}
				regStr.append(valueMap.get(key));
				this.valueMap.put(valueMap.get(key) ,key);
			}
			regStr.append(")");
			
			this.reg = regStr.toString(); 
			
			this.loginBureau = loginBureau;
		} 
		public Object convert(Object value) {
			// TODO Auto-generated method stub 
			if(this.reg == null){
				return value;
			}
			Pattern p = Pattern.compile(this.reg);
			Matcher m = p.matcher(String.valueOf(value));
			StringBuffer result = new StringBuffer(); 
			if(m.find()){
				String v = valueMap.get(m.group(1)); 
				if(value != null){
					m.appendReplacement(result, v);
				}
				m.appendTail(result);
			}else{
				result.append(this.loginBureau);
			}
			
			return result.toString();
		}
		
	}
	
	public Map<String, ValueConvert> getValueMapping() {
		return valueMapping;
	}

	public void setValueMapping(Map<String,  Map<String, String>> valueMapping) {
		for(String key : valueMapping.keySet()){
			this.valueMapping.put(key, new ValueConvert(valueMapping.get(key), this.loginBureau));
		} 
	}
}
