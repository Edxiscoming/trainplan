package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.Ljzd;

 
public interface LjzdMybatisDao {

	/**
	 * 根据路局简称获取路局基本信息
	 * @param ljqc 路局简称
	 * @return
	 */
	public Ljzd getLjInfo(String ljqc);
}
