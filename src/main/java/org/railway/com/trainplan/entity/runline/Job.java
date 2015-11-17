package org.railway.com.trainplan.entity.runline;

public class Job {
	private String code; //作业内容，每一项内容都须由‘<’和‘>’包含，作业内容间无字符, "code": "<经由><上水>"
	private String[] items; //作业内容，只读,"items": ["经由" ,"上水"]
	private String itemsText; //显示内容，只读,"itemsText": "经由、上水"
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	public String getItemsText() {
		return itemsText;
	}
	public void setItemsText(String itemsText) {
		this.itemsText = itemsText;
	}
	
	
	
}
