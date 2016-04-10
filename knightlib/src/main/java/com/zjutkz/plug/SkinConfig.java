package com.zjutkz.plug;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhe on 16/4/10.
 */
public class SkinConfig {
    private static SkinConfig mInstance;
    private Context mContext;


    public SkinConfig(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public static SkinConfig getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new SkinConfig(mContext);
        }
        return mInstance;
    }


    public void setSkinResourcePath(String skinPath) {
        SharedPreferences sp = mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("skinPath",skinPath);
        editor.commit();
    }


    public String getSkinResourcePath() {
        SharedPreferences sp = mContext.getSharedPreferences("skin_sharePref", mContext.MODE_PRIVATE);
        return sp.getString("skinPath", "");
    }
}

