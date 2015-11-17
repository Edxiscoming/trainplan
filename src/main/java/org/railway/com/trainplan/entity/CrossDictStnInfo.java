package org.railway.com.trainplan.entity;

/**
 * 交路经由字典
 * 
 * @author denglj
 * 
 */
public class CrossDictStnInfo {

	private String drawGrapStnId;// 本表ID（绘制交路图中经由站ID）
	private String drawGrapId;// 绘制交路图ID（对应DRAW_GRAPH主表中DRAW_GRAPH_ID）
	private String drawGraphStnId;// 本表ID（绘制交路图中经由站ID）
	private String drawGraphId;// 绘制交路图ID（对应DRAW_GRAPH主表中DRAW_GRAPH_ID）
	private int stnSort;// 站序
	private String stnName;// 车站名
	private String heightDetail;// 绘详图高度（距离第一个站的高度）
	private String heightSimple;//绘简图高度（距离第一个站的高度）
	private String stnType;// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
	//所属局简称
    private String bureau;
 
    
	public String getBureau() {
		return bureau;
	}

	public void setBureau(String bureau) {
		this.bureau = bureau;
	}

	public String getDrawGrapStnId() {
		return drawGrapStnId;
	}

	public void setDrawGrapStnId(String drawGrapStnId) {
		this.drawGrapStnId = drawGrapStnId;
	}

	public String getDrawGrapId() {
		return drawGrapId;
	}

	public void setDrawGrapId(String drawGrapId) {
		this.drawGrapId = drawGrapId;
	}

	

	public int getStnSort() {
		return stnSort;
	}

	public void setStnSort(int stnSort) {
		this.stnSort = stnSort;
	}

	public String getStnName() {
		return stnName;
	}

	public void setStnName(String stnName) {
		this.stnName = stnName;
	}

	

	public String getHeightDetail() {
		return heightDetail;
	}

	public void setHeightDetail(String heightDetail) {
		this.heightDetail = heightDetail;
	}

	public String getHeightSimple() {
		return heightSimple;
	}

	public void setHeightSimple(String heightSimple) {
		this.heightSimple = heightSimple;
	}

	public String getStnType() {
		return stnType;
	}

	public void setStnType(String stnType) {
		this.stnType = stnType;
	}

	public String getDrawGraphStnId() {
		return drawGraphStnId;
	}

	public void setDrawGraphStnId(String drawGraphStnId) {
		this.drawGraphStnId = drawGraphStnId;
	}

	public String getDrawGraphId() {
		return drawGraphId;
	}

	public void setDrawGraphId(String drawGraphId) {
		this.drawGraphId = drawGraphId;
	}

}
