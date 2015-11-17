package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;

public class PlanBureauStatisticsDto {
/**
 *   "bureauId": "990571dc-c10d-4066-a04c-36431922af0c",
      "bureauName": "西安铁路局",
     "bureauShortName": "西",
     "bureauCode": 18,
 */
	private String  bureauId;
	private String  bureauName;
	private String  bureauShortName;
	private String  bureauCode;
	private List<PlanBureauTsDto> listBureauDto = new ArrayList<PlanBureauTsDto>();
	public String getBureauId() {
		return bureauId;
	}
	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}
	public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
	public String getBureauCode() {
		return bureauCode;
	}
	public void setBureauCode(String bureauCode) {
		this.bureauCode = bureauCode;
	}
	public List<PlanBureauTsDto> getListBureauDto() {
		return listBureauDto;
	}
	public void setListBureauDto(List<PlanBureauTsDto> listBureauDto) {
		this.listBureauDto = listBureauDto;
	}
	
	
}
