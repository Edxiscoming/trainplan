package org.railway.com.trainplan.service.dto;

/**
 * tree
 * @author Think
 *
 */

public class HighLinePlanTreeDto {
	private String id;//ID;
	private String pId;//
	private String name;//树节点显示名称
	private String treeType;//树类型	trainTypeTree：列车类型数	stnNameTree：发到站及分界口树
	private String key;//树节点点击事情须传递的关键属性
	private String keyType;//关键字类型
	private int count;//统计数字
	private boolean open;
	
	public HighLinePlanTreeDto(String id, String pId, String name,
			String treeType, String key, String keyType, int count,boolean open) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.treeType = treeType;
		this.key = key;
		this.keyType = keyType;
		this.count = count;
		this.open = open;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTreeType() {
		return treeType;
	}
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKeyType() {
		return keyType;
	}
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	
	
	
}
