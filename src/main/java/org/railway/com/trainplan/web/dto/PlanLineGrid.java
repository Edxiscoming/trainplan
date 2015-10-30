package org.railway.com.trainplan.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 5/14/14.
 */
public class PlanLineGrid {
    private List<PlanLineGridX> days = new ArrayList<PlanLineGridX>();

    private List<PlanLineGridY> crossStns = new ArrayList<PlanLineGridY>();

    public PlanLineGrid(List<PlanLineGridX> days, List<PlanLineGridY> crossStns) {
        this.days = days;
        this.crossStns = crossStns;
    }

    public List<PlanLineGridX> getDays() {
        return days;
    }

    public void setDays(List<PlanLineGridX> days) {
        this.days = days;
    }

    public List<PlanLineGridY> getCrossStns() {
        return crossStns;
    }

    public void setCrossStns(List<PlanLineGridY> crossStns) {
        this.crossStns = crossStns;
    }
}
