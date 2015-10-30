package com.railway.basicmap.original.repository;

import java.util.List;

import com.railway.basicmap.original.entity.BureauDic;


public interface BureauDicMapper {
	/**
	 * 查询路局数据字典
	 * @Description: TODO
	 * @param @return   
	 * @return List<BureauDic>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	public List<BureauDic> getBureauDictMap();
}
