package org.railway.com.trainplan.service.dto;

import java.util.List;

/**
 * 高铁查询页面数据封装对象
 * @author Think
 *
 */
public class HighLinePlanPageDto {
	List<HighLinePlanTrainDto> trainDatas;	//列车列表数据
	List<HighLinePlanTreeDto> trainTypeTreeNodes;//列车类型树data
	List<HighLinePlanTreeDto> stnNameTreeNodes;//发到站及分界口树data
	
	public List<HighLinePlanTrainDto> getTrainDatas() {
		return trainDatas;
	}
	public void setTrainDatas(List<HighLinePlanTrainDto> trainDatas) {
		this.trainDatas = trainDatas;
	}
	public List<HighLinePlanTreeDto> getTrainTypeTreeNodes() {
		return trainTypeTreeNodes;
	}
	public void setTrainTypeTreeNodes(List<HighLinePlanTreeDto> trainTypeTreeNodes) {
		this.trainTypeTreeNodes = trainTypeTreeNodes;
	}
	public List<HighLinePlanTreeDto> getStnNameTreeNodes() {
		return stnNameTreeNodes;
	}
	public void setStnNameTreeNodes(List<HighLinePlanTreeDto> stnNameTreeNodes) {
		this.stnNameTreeNodes = stnNameTreeNodes;
	}
	
}
