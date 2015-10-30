package org.railway.com.trainplan.service.dto;


/**
 * 车底dto
 * @author Think
 *
 */
public class VehicleDto {
	private String vehicleSort;//编号
	private String vehicleType;//车种
	private String vehicleCapacity;//定员
	
	public VehicleDto() {

	}
	
	public VehicleDto(String vehicleSort, String vehicleType, String vehicleCapacity) {
		this.vehicleSort = vehicleSort;
		this.vehicleType = vehicleType;
		this.vehicleCapacity = vehicleCapacity;
	}
	
	public String getVehicleSort() {
		return vehicleSort;
	}
	public void setVehicleSort(String vehicleSort) {
		this.vehicleSort = vehicleSort;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVehicleCapacity() {
		return vehicleCapacity;
	}
	public void setVehicleCapacity(String vehicleCapacity) {
		this.vehicleCapacity = vehicleCapacity;
	}
	
	
}
