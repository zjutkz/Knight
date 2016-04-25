package com.zjutkz.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zjutkz.AbsKnightDress;
import com.zjutkz.Constants.Config;
import com.zjutkz.Constants.ModeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kangzhe on 16/4/2.
 */
public class KnightUtil {

    public static Map<String,AbsKnightDress> MAP = new HashMap<>();


    public static void changeToNight(Context dressed){
        changeToNight(dressed, "", null);
    }

    public static void changeToNight(Object dressed,String clzName,Object inject){
        String newClzName = "";

        if(dressed instanceof Context){
            FileUtils.saveString((Context)dressed, ModeConstants.MODE_KEY, ModeConstants.MODE_NIGHT);
        }else if(dressed instanceof Fragment){
            Context fragContext = ((Fragment) dressed).getActivity();
            FileUtils.saveString(fragContext, ModeConstants.MODE_KEY, ModeConstants.MODE_NIGHT);
        }

        if(clzName.equals("")){
            clzName = dressed.getClass().getSimpleName() + Config.KNIGHT;
        }else{
            newClzName = clzName.replaceAll("\\$", Config.AdapterPlaceHolder) + Config.KNIGHT;
        }
        AbsKnightDress dress;
        if(newClzName.equals("")){
            dress = MAP.get(clzName);
        }else{
            dress = MAP.get(newClzName);
        }

        if(dress == null) {
            if(newClzName.equals("")){
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
            }else{
                try {
                    Class dressedClz = Class.forName(newClzName);

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

        }

        dress.setContext(dressed);

        if(inject != null){
            dress.setInject(inject);
        }

        dress.changeToNight();

        dress.loadSkin(true);
    }

    public static void changeToDay(Context dressed){
        changeToDay(dressed, "", null);
    }

    public static void changeToDay(Object dressed,String clzName,Object inject){
        String newClzName = "";

        if(dressed instanceof Context){
            FileUtils.saveString((Context)dressed, ModeConstants.MODE_KEY, ModeConstants.MODE_DAY);
        }else if(dressed instanceof Fragment){
            Context fragContext = ((Fragment) dressed).getActivity();
            FileUtils.saveString(fragContext, ModeConstants.MODE_KEY, ModeConstants.MODE_DAY);
        }

        if(clzName.equals("")){
            clzName = dressed.getClass().getSimpleName() + Config.KNIGHT;
        }else{
            newClzName = clzName.replaceAll("\\$", Config.AdapterPlaceHolder) + Config.KNIGHT;
        }

        AbsKnightDress dress;
        if(newClzName.equals("")){
            dress = MAP.get(clzName);
        }else{
            dress = MAP.get(newClzName);
        }

        if(dress == null) {
            if(newClzName.equals("")){
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
            }else{
                try {
                    Class dressedClz = Class.forName(newClzName);

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

        }

        dress.setContext(dressed);

        if(inject != null){
            dress.setInject(inject);
        }

        dress.changeToDay();

        dress.loadSkin(false);
    }

    public static void prepareForChange(Object context){
        prepareForChange(context,"",null);
    }

    public static void prepareForChange(Object context,String clzName,Object inject){
        if(context instanceof Context){
            String mode = FileUtils.loadString((Context)context, ModeConstants.MODE_KEY);
            if(TextUtils.isEmpty(mode)){
                FileUtils.saveString((Context)context,ModeConstants.MODE_KEY,ModeConstants.MODE_DAY);
                mode = ModeConstants.MODE_DAY;
            }

            if(mode.equals(ModeConstants.MODE_DAY)){
                changeToDay(context,clzName,inject);
            }else{
                changeToNight(context,clzName,inject);
            }
        }else if(context instanceof Fragment){
            Context fragContext = ((Fragment) context).getActivity();
            String mode = FileUtils.loadString(fragContext, ModeConstants.MODE_KEY);
            if(TextUtils.isEmpty(mode)){
                FileUtils.saveString(fragContext,ModeConstants.MODE_KEY,ModeConstants.MODE_DAY);
                mode = ModeConstants.MODE_DAY;
            }

            if(mode.equals(ModeConstants.MODE_DAY)){
                changeToDay(context,clzName,inject);
            }else{
                changeToNight(context,clzName,inject);
            }
        }

    }

    public static boolean isNight(Context context){
        return FileUtils.loadString(context,ModeConstants.MODE_KEY).equals(ModeConstants.MODE_NIGHT);
    }
}
