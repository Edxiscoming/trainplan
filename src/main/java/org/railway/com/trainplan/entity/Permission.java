package org.railway.com.trainplan.entity;

/**
 * Created by star on 5/15/14.
 */
public class Permission {

    private int id;
    
    private String key;	//权限缩写

    private String name;

    private String desc;

    public int getId() {
        return id;
    }

    
    public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
