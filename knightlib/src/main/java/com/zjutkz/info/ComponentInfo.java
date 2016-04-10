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

    private Map<String,String[]> plugResMap = new HashMap<>();

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

    public Map<String,String[]> getPlugResMap(){
        return plugResMap;
    }

    public void setRes(String resName,int nightRes,int dayRes){
        resMap.put(resName,new int[]{nightRes,dayRes});
    }

    public void setPlugRes(String resName,String nightRes,String dayRes,String valueName,String packageName){
        plugResMap.put(resName,new String[]{nightRes,dayRes,valueName,packageName});
    }
}
