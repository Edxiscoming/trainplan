package org.railway.com.trainplan.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * 路局Util，所有路局转换全部保存在这里； 铁路-路局之后会进行调整，到时候调整时，只需要将1.数据库数据更新、2.下面方法中的数据更新即可;
 * 为什么不查询数据库？ 1.因为我懒、2.我嫌这个系统目前查询个小数据都慢，我不想那么多次请求数据库.
 * 
 * 
 * @author zhangPengDong
 *
 *         2015年4月29日 上午10:43:47
 */
public class LjUtil {

	/**
	 * 根据名称后面带"局"的查询数据.
	 * 
	 * @param str
	 *            key值(例如:京局,哈局)
	 * @param i
	 *            返回那种类型(1:京局-京;2:京局-P;3.京局-北京铁路局)
	 * @return
	 */
	public static String getLjByNameJ(String str, Integer i) {
		if (i == 1) {
			return getLjByNameJ1(str.toUpperCase());
		} else if (i == 2) {
			return getLjByNameJ2(str.toUpperCase());
		} else if (i == 3) {
			return getLjByNameJ3(str.toUpperCase());
		}
		return "";
	}

	/**
	 * 根据路局简称查询数据.
	 * 
	 * @param str
	 *            key值(例如:京,哈)
	 * @param i
	 *            返回那种类型(1:京-京局;2:京-P;3.京-北京铁路局)
	 * @return
	 */
	public static String getLjByName(String str, Integer i) {
		if (i == 1) {
			return getLjByName1(str.toUpperCase().trim());
		} else if (i == 2) {
			return getLjByName2(str.toUpperCase().trim());
		} else if (i == 3) {
			return getLjByName3(str.toUpperCase().trim());
		}
		return "";
	}

	/**
	 * 根据路局简称查询数据.
	 * 
	 * @param str
	 *            key值(例如:P,V)
	 * @param i
	 *            返回那种类型(1:P-京局;2:P-京;3.P-北京铁路局)
	 * @return
	 */
	public static String getLjByNameBs(String str, Integer i) {
		if (i == 1) {
			return getLjByNameBs1(str.toUpperCase().trim());
		} else if (i == 2) {
			return getLjByNameBs2(str.toUpperCase().trim());
		} else if (i == 3) {
			return getLjByNameBs3(str.toUpperCase().trim());
		}
		return "";
	}

	private static String getLjByNameBs1(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("B", "哈");
		bureuaMap.put("T", "沈");
		bureuaMap.put("P", "京");
		bureuaMap.put("V", "太");
		bureuaMap.put("C", "呼");
		bureuaMap.put("F", "郑");
		bureuaMap.put("N", "武");
		bureuaMap.put("Y", "西");
		bureuaMap.put("K", "济");
		bureuaMap.put("H", "上");
		bureuaMap.put("G", "南");
		bureuaMap.put("Q", "广");
		bureuaMap.put("Z", "宁");
		bureuaMap.put("W", "成");
		bureuaMap.put("M", "昆");
		bureuaMap.put("J", "兰");
		bureuaMap.put("R", "乌");
		bureuaMap.put("O", "青");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByNameBs2(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("B", "哈");
		bureuaMap.put("T", "沈");
		bureuaMap.put("P", "京");
		bureuaMap.put("V", "太");
		bureuaMap.put("C", "呼");
		bureuaMap.put("F", "郑");
		bureuaMap.put("N", "武");
		bureuaMap.put("Y", "西");
		bureuaMap.put("K", "济");
		bureuaMap.put("H", "上");
		bureuaMap.put("G", "南");
		bureuaMap.put("Q", "广");
		bureuaMap.put("Z", "宁");
		bureuaMap.put("W", "成");
		bureuaMap.put("M", "昆");
		bureuaMap.put("J", "兰");
		bureuaMap.put("R", "乌");
		bureuaMap.put("O", "青");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByNameBs3(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("B", "哈尔滨铁路局        ");
		bureuaMap.put("T", "沈阳铁路局          ");
		bureuaMap.put("P", "北京铁路局          ");
		bureuaMap.put("V", "太原铁路局          ");
		bureuaMap.put("C", "呼和浩特铁路局      ");
		bureuaMap.put("F", "郑州铁路局          ");
		bureuaMap.put("N", "武汉铁路局          ");
		bureuaMap.put("Y", "西安铁路局          ");
		bureuaMap.put("K", "济南铁路局          ");
		bureuaMap.put("H", "上海铁路局          ");
		bureuaMap.put("G", "南昌铁路局          ");
		bureuaMap.put("Q", "广州铁路（集团）公司");
		bureuaMap.put("Z", "南宁铁路局          ");
		bureuaMap.put("W", "成都铁路局          ");
		bureuaMap.put("M", "昆明铁路局          ");
		bureuaMap.put("J", "兰州铁路局          ");
		bureuaMap.put("R", "乌鲁木齐铁路局      ");
		bureuaMap.put("O", "青藏铁路公司        ");

		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByName1(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈", "哈局");
		bureuaMap.put("沈", "沈局");
		bureuaMap.put("京", "京局");
		bureuaMap.put("太", "太局");
		bureuaMap.put("呼", "呼局");
		bureuaMap.put("郑", "郑局");
		bureuaMap.put("武", "武局");
		bureuaMap.put("西", "西局");
		bureuaMap.put("济", "济局");
		bureuaMap.put("上", "上局");
		bureuaMap.put("南", "南局");
		bureuaMap.put("广", "广铁");
		bureuaMap.put("宁", "宁局");
		bureuaMap.put("成", "成局");
		bureuaMap.put("昆", "昆局");
		bureuaMap.put("兰", "兰局");
		bureuaMap.put("乌", "乌局");
		bureuaMap.put("青", "青藏");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByName2(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈", "B");
		bureuaMap.put("沈", "T");
		bureuaMap.put("京", "P");
		bureuaMap.put("太", "V");
		bureuaMap.put("呼", "C");
		bureuaMap.put("郑", "F");
		bureuaMap.put("武", "N");
		bureuaMap.put("西", "Y");
		bureuaMap.put("济", "K");
		bureuaMap.put("上", "H");
		bureuaMap.put("南", "G");
		bureuaMap.put("广", "Q");
		bureuaMap.put("宁", "Z");
		bureuaMap.put("成", "W");
		bureuaMap.put("昆", "M");
		bureuaMap.put("兰", "J");
		bureuaMap.put("乌", "R");
		bureuaMap.put("青", "O");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByName3(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈", "哈尔滨铁路局        ");
		bureuaMap.put("沈", "沈阳铁路局          ");
		bureuaMap.put("京", "北京铁路局          ");
		bureuaMap.put("太", "太原铁路局          ");
		bureuaMap.put("呼", "呼和浩特铁路局      ");
		bureuaMap.put("郑", "郑州铁路局          ");
		bureuaMap.put("武", "武汉铁路局          ");
		bureuaMap.put("西", "西安铁路局          ");
		bureuaMap.put("济", "济南铁路局          ");
		bureuaMap.put("上", "上海铁路局          ");
		bureuaMap.put("南", "南昌铁路局          ");
		bureuaMap.put("广", "广州铁路（集团）公司");
		bureuaMap.put("宁", "南宁铁路局          ");
		bureuaMap.put("成", "成都铁路局          ");
		bureuaMap.put("昆", "昆明铁路局          ");
		bureuaMap.put("兰", "兰州铁路局          ");
		bureuaMap.put("乌", "乌鲁木齐铁路局      ");
		bureuaMap.put("青", "青藏铁路公司        ");

		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByNameJ1(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈局", "哈");
		bureuaMap.put("沈局", "沈");
		bureuaMap.put("京局", "京");
		bureuaMap.put("太局", "太");
		bureuaMap.put("呼局", "呼");
		bureuaMap.put("郑局", "郑");
		bureuaMap.put("武局", "武");
		bureuaMap.put("西局", "西");
		bureuaMap.put("济局", "济");
		bureuaMap.put("上局", "上");
		bureuaMap.put("南局", "南");
		bureuaMap.put("广铁", "广");
		bureuaMap.put("宁局", "宁");
		bureuaMap.put("成局", "成");
		bureuaMap.put("昆局", "昆");
		bureuaMap.put("兰局", "兰");
		bureuaMap.put("乌局", "乌");
		bureuaMap.put("青藏", "青");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByNameJ2(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈局", "B");
		bureuaMap.put("沈局", "T");
		bureuaMap.put("京局", "P");
		bureuaMap.put("太局", "V");
		bureuaMap.put("呼局", "C");
		bureuaMap.put("郑局", "F");
		bureuaMap.put("武局", "N");
		bureuaMap.put("西局", "Y");
		bureuaMap.put("济局", "K");
		bureuaMap.put("上局", "H");
		bureuaMap.put("南局", "G");
		bureuaMap.put("广铁", "Q");
		bureuaMap.put("宁局", "Z");
		bureuaMap.put("成局", "W");
		bureuaMap.put("昆局", "M");
		bureuaMap.put("兰局", "J");
		bureuaMap.put("乌局", "R");
		bureuaMap.put("青藏", "O");
		return MapUtils.getString(bureuaMap, str);
	}

	private static String getLjByNameJ3(String str) {
		Map<String, String> bureuaMap = new HashMap<String, String>();
		bureuaMap.put("哈局", "哈尔滨铁路局        ");
		bureuaMap.put("沈局", "沈阳铁路局          ");
		bureuaMap.put("京局", "北京铁路局          ");
		bureuaMap.put("太局", "太原铁路局          ");
		bureuaMap.put("呼局", "呼和浩特铁路局      ");
		bureuaMap.put("郑局", "郑州铁路局          ");
		bureuaMap.put("武局", "武汉铁路局          ");
		bureuaMap.put("西局", "西安铁路局          ");
		bureuaMap.put("济局", "济南铁路局          ");
		bureuaMap.put("上局", "上海铁路局          ");
		bureuaMap.put("南局", "南昌铁路局          ");
		bureuaMap.put("广铁", "广州铁路（集团）公司");
		bureuaMap.put("宁局", "南宁铁路局          ");
		bureuaMap.put("成局", "成都铁路局          ");
		bureuaMap.put("昆局", "昆明铁路局          ");
		bureuaMap.put("兰局", "兰州铁路局          ");
		bureuaMap.put("乌局", "乌鲁木齐铁路局      ");
		bureuaMap.put("青藏", "青藏铁路公司        ");

		return MapUtils.getString(bureuaMap, str);
	}

	public static String isNull(String str) {
		if (null != str && !"".equals(str)) {
			return str.trim();
		} else {
			return "";
		}
	}

}
