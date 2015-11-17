package com.railway.passenger.transdispatch.dayplan.service;

import org.railway.com.trainplan.web.dto.Result;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;

public interface IDayplanService {
	/**
	 * 根据原始对数表信息生成逻辑交路及其所有相关信息
	 * @param cross
	 * @return
	 */
	public Result generateDayPlan(TCmPhyCross cross);
}
