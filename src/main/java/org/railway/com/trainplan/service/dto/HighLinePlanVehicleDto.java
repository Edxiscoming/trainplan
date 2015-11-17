package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * 高铁车底信息dto
 * 包含车底、担当局信息及编组信息list
 * @author Think
 *
 */
public class HighLinePlanVehicleDto {
	private String tokenVehBureauCode;//车辆担当局（局码）;
	private String tokenVehBureauName;//车辆担当局（简称）
	private String tokenVehDept;//担当车辆段/动车段
	private String tokenVehDepot;//车辆/动车段     担当动车所（用于高铁）
	private String crossName;//交路名称
	private String crhType;//动车组车型（用于高铁）
	private String vehicle1;//车底1
	private String vehicle2;//车底2
	private String crossSection;//运行区段
	
	private List<VehicleDto> vehicleList;//车底编组详情list
	
	
	public String getTokenVehBureauCode() {
		return tokenVehBureauCode;
	}
	public void setTokenVehBureauCode(String tokenVehBureauCode) {
		this.tokenVehBureauCode = tokenVehBureauCode;
	}
	public String getTokenVehBureauName() {
		return tokenVehBureauName;
	}
	public void setTokenVehBureauName(String tokenVehBureauName) {
		this.tokenVehBureauName = tokenVehBureauName;
	}
	public String getTokenVehDept() {
		return tokenVehDept;
	}
	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}
	public String getTokenVehDepot() {
		return tokenVehDepot;
	}
	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}
	public String getVehicle1() {
		return vehicle1;
	}
	public void setVehicle1(String vehicle1) {
		this.vehicle1 = vehicle1;
	}
	public String getVehicle2() {
		return vehicle2;
	}
	public void setVehicle2(String vehicle2) {
		this.vehicle2 = vehicle2;
	}
	
	public String getCrossSection() {
		return crossSection;
	}
	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}
	public List<VehicleDto> getVehicleList() {
		if (vehicleList==null) {
			return new ArrayList<VehicleDto>();
		}
		return vehicleList;
	}
	public void setVehicleList(List<VehicleDto> vehicleList) {
		this.vehicleList = vehicleList;
	}
	
	
	
}
