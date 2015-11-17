package org.railway.com.trainplan.entity;
/**
 * 线台
 * @author chengc
 *
 */
public class DicThroughLine {

	    //id
		private String throughLineLineId;
		//铁路线名
		private String  throughLineName;
		//所属局
		private String  bureau;
	    //备注
		private String note;
		private String highline_flag;
		public String getThroughLineLineId() {
			return throughLineLineId;
		}
		public void setThroughLineLineId(String throughLineLineId) {
			this.throughLineLineId = throughLineLineId;
		}
		public String getThroughLineName() {
			return throughLineName;
		}
		public void setThroughLineName(String throughLineName) {
			this.throughLineName = throughLineName;
		}
		public String getBureau() {
			return bureau;
		}
		public void setBureau(String bureau) {
			this.bureau = bureau;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getHighline_flag() {
			return highline_flag;
		}
		public void setHighline_flag(String highline_flag) {
			this.highline_flag = highline_flag;
		}
	
		
		
		
}
