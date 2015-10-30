package org.railway.com.trainplan.entity;

/**
 * 落成记录,对应plan_sent.
 * 
 * @author Ad
 *
 */
public class PlanSent {

	/**
	 * id.
	 */
	private String plan_sent_id;
	/**
	 * 交路id.
	 */
	private String plan_cross_id;
	/**
	 * 列车id.
	 */
	private String plan_train_id;
	/**
	 * 交路名.
	 */
	private String cross_name;
	/**
	 * 开行日期.
	 */
	private String run_date;
	/**
	 * 车次.
	 */
	private String train_nbr;
	/**
	 * 前续列车id.
	 */
	private String pre_train_id;
	/**
	 * 后续列车ID.
	 */
	private String next_train_id;
	/**
	 * 落成时间.
	 */
	private String sent_time;
	/**
	 * 落成人姓名.
	 */
	private String sent_people;
	/**
	 * 落成人所属单位.
	 */
	private String sent_people_org;
	/**
	 * 落成人所属路局(简称).
	 */
	private String sent_people_bureau;

	public String getPlan_sent_id() {
		return plan_sent_id;
	}

	public void setPlan_sent_id(String plan_sent_id) {
		this.plan_sent_id = plan_sent_id;
	}

	public String getPlan_cross_id() {
		return plan_cross_id;
	}

	public void setPlan_cross_id(String plan_cross_id) {
		this.plan_cross_id = plan_cross_id;
	}

	public String getPlan_train_id() {
		return plan_train_id;
	}

	public void setPlan_train_id(String plan_train_id) {
		this.plan_train_id = plan_train_id;
	}

	public String getCross_name() {
		return cross_name;
	}

	public void setCross_name(String cross_name) {
		this.cross_name = cross_name;
	}

	public String getRun_date() {
		return run_date;
	}

	public void setRun_date(String run_date) {
		this.run_date = run_date;
	}

	public String getTrain_nbr() {
		return train_nbr;
	}

	public void setTrain_nbr(String train_nbr) {
		this.train_nbr = train_nbr;
	}

	public String getPre_train_id() {
		return pre_train_id;
	}

	public void setPre_train_id(String pre_train_id) {
		this.pre_train_id = pre_train_id;
	}

	public String getNext_train_id() {
		return next_train_id;
	}

	public void setNext_train_id(String next_train_id) {
		this.next_train_id = next_train_id;
	}

	public String getSent_time() {
		return sent_time;
	}

	public void setSent_time(String sent_time) {
		this.sent_time = sent_time;
	}

	public String getSent_people() {
		return sent_people;
	}

	public void setSent_people(String sent_people) {
		this.sent_people = sent_people;
	}

	public String getSent_people_org() {
		return sent_people_org;
	}

	public void setSent_people_org(String sent_people_org) {
		this.sent_people_org = sent_people_org;
	}

	public String getSent_people_bureau() {
		return sent_people_bureau;
	}

	public void setSent_people_bureau(String sent_people_bureau) {
		this.sent_people_bureau = sent_people_bureau;
	}

}
