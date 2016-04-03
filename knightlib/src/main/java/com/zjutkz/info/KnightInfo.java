package com.zjutkz.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhe on 16/4/2.
 */
public class KnightInfo {

    private String packageName;

    private String className;

    private String knightClassName;

    private List<ComponentInfo> componentInfoList = new ArrayList<>();

    public KnightInfo(String packageName,String className){
        this.packageName = packageName;
        this.className = className;
        this.knightClassName = className + "$$KNIGHT";
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getKnightClassName(){
        return knightClassName;
    }

    public void setComponent(ComponentInfo info){
        componentInfoList.add(info);
    }

    public List<ComponentInfo> getComponentInfoList() {
        return componentInfoList;
    }
}
