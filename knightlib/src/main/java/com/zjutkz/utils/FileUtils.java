package com.zjutkz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by kangzhe on 16/4/20.
 */
public class FileUtils {

    private static final String NAME = "Knight";

    public static void saveString(Context context, String name, String value) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * 从本地加载字符串，默认返回空串
     *
     * @param context
     * @param key
     * @return
     */
    public static String loadString(Context context, String key) {

        SharedPreferences sp = getSharedPreferences(context);
        String result = sp.getString(key, "");
        return result;
    }

    /**
     * 将inputStream转换成byte数组
     * @param in
     * @return
     */
    public static byte[] convertStreamToByte(InputStream in) {

        if (in == null) {
            return null;
        }

        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2 * 1024];
            int read = -1;

            while ((read = in.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }

            return bos.toByteArray();
        } catch (Exception e) {
            Log.e("error:", e.toString());
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e2) {
                }
            }
        }

        return null;
    }

    /**
     * 清除某一Preference
     *
     * @param key
     */
    public static void clearPreference(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences(Context mContext) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}


