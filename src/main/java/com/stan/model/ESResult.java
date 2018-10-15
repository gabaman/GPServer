package com.stan.model;

import java.util.ArrayList;
import java.util.Map;

public class ESResult {

    public ArrayList<Map<String, Object>> list;
    public String totalTime;
    public String totalHits;

    public ArrayList<Map<String, Object>> getList() {
        return list;
    }

    public void setList(ArrayList<Map<String, Object>> list) {
        this.list = list;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(String totalHits) {
        this.totalHits = totalHits;
    }

}
