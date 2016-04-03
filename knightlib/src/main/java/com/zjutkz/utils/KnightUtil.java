package com.zjutkz.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zjutkz.AbsKnightDress;
import com.zjutkz.Constants.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kangzhe on 16/4/2.
 */
public class KnightUtil {

    public static Map<String,AbsKnightDress> MAP = new HashMap<>();


    public static void changeToNight(Object dressed){

        String clzName = dressed.getClass().getSimpleName() + Config.KNIGHT;

        AbsKnightDress dress = MAP.get(clzName);

        if(dress == null) {
            try {
                Class dressedClz = Class.forName(dressed.getClass().getName() + Config.KNIGHT);

                dress = (AbsKnightDress)dressedClz.newInstance();

                MAP.put(clzName,dress);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(dressed instanceof Activity){
            dress.setContext((Activity)dressed);
        }else if(dressed instanceof View){
            dress.setContext(((View) dressed).getContext());
        }else if(dressed instanceof Fragment){
            dress.setContext(((Fragment) dressed).getActivity());
        }

        dress.changeToNight();
    }

    public static void changeToDay(Object dressed){

        String clzName = dressed.getClass().getSimpleName() + "$$KNIGHT";

        AbsKnightDress dress = MAP.get(clzName);

        if(dress == null) {
            try {
                Class dressedClz = Class.forName(dressed.getClass().getName() + Config.KNIGHT);

                dress = (AbsKnightDress)dressedClz.newInstance();

                MAP.put(clzName,dress);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(dressed instanceof Activity){
            dress.setContext((Activity)dressed);
        }else if(dressed instanceof View){
            dress.setContext(((View) dressed).getContext());
        }else if(dressed instanceof Fragment){
            dress.setContext(((Fragment) dressed).getActivity());
        }

        dress.changeToDay();
    }
}
