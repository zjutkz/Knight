package com.zjutkz;

import android.content.Context;

/**
 * Created by kangzhe on 16/4/2.
 */
public abstract class AbsKnightDress {

    protected Object context;

    public abstract void changeToNight();

    public abstract void changeToDay();

    public void setContext(Object context){
        this.context = context;
    }
}
