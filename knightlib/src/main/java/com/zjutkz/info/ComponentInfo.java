package com.zjutkz.info;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kangzhe on 16/4/2.
 */
public class ComponentInfo {

    private String name;

    private String type;

    private Map<String,int[]> resMap = new HashMap<>();

    public ComponentInfo(String name,String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Map<String, int[]> getResMap() {
        return resMap;
    }

    public void setRes(String resName,int nightRes,int dayRes){
        resMap.put(resName,new int[]{nightRes,dayRes});
    }
}
