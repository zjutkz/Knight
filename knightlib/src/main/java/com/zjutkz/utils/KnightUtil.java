package com.zjutkz.utils;

import android.content.Context;
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

    public static void changeToNight(Context dressed,String clzName,Object inject){
        String newClzName = "";

        FileUtils.saveString(dressed, ModeConstants.MODE_KEY, ModeConstants.MODE_NIGHT);

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
        changeToDay(dressed, "",null);
    }

    public static void changeToDay(Context dressed,String clzName,Object inject){
        String newClzName = "";

        FileUtils.saveString(dressed, ModeConstants.MODE_KEY, ModeConstants.MODE_DAY);

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

    public static void prepareForChange(Context context){
        prepareForChange(context,"",null);
    }

    public static void prepareForChange(Context context,String clzName,Object inject){
        String mode = FileUtils.loadString(context, ModeConstants.MODE_KEY);
        if(TextUtils.isEmpty(mode)){
            FileUtils.saveString(context,ModeConstants.MODE_KEY,ModeConstants.MODE_DAY);
            mode = ModeConstants.MODE_DAY;
        }

        if(mode.equals(ModeConstants.MODE_DAY)){
            changeToDay(context,clzName,inject);
        }else{
            changeToNight(context,clzName,inject);
        }
    }

    public static boolean isNight(Context context){
        return FileUtils.loadString(context,ModeConstants.MODE_KEY).equals(ModeConstants.MODE_NIGHT);
    }
}
