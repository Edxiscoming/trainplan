package org.railway.com.trainplan.entity;

import java.sql.Date;

/**
 * Created by star on 5/22/14.
 */
public class LevelCheck {

    private String id;

    private String people;

    private Date date;

    private String dept;

    private String bureau;

    private int checkType;

    private String planId;

    private String lineId;

    public LevelCheck(String id, String people, Date date, String dept, String bureau, int checkType, String planId, String lineId) {
        this.id = id;
        this.people = people;
        this.date = date;
        this.dept = dept;
        this.bureau = bureau;
        this.checkType = checkType;
        this.planId = planId;
        this.lineId = lineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBureau() {
        return bureau;
    }

    public void setBureau(String bureau) {
        this.bureau = bureau;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
}
