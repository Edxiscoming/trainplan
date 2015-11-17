package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
public class BureauDTO {
    private String code;
    private String name;
    private String fullName;
    private String sort;

    public BureauDTO(Map<String, Object> map) {
        this.code = MapUtils.getString(map, "LJPYM", "");
        this.name = MapUtils.getString(map, "LJJC", "");
        this.fullName = MapUtils.getString(map, "LJQC", "");
        this.sort = MapUtils.getString(map, "LJDM", "");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
