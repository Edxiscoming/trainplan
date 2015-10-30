package org.railway.com.trainplan.entity;

import java.io.Serializable;

/**
 * Created by star on 5/15/14.
 */
public class Role  implements Serializable{

    private int id;

    private String name;

    private String desc;

    public int getId() {
        return id;
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
