package com.zjutkz;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.zjutkz.plug.SkinConfig;
import com.zjutkz.plug.SkinPackageManager;

/**
 * Created by kangzhe on 16/4/2.
 */
public abstract class AbsKnightDress {

    protected Object context;

    protected String dexPath;

    protected Resources mResource;

    public abstract void changeToNight();

    public abstract void changeToDay();

    public abstract void plugChangeToNight();

    public abstract void plugChangeToDay();

    public void setContext(Object context){
        this.context = context;

        if(context instanceof Fragment){
            dexPath = SkinPackageManager.getInstance(((Fragment) context).getActivity()).mDexPah;

            mResource = SkinPackageManager.getInstance(((Fragment) context).getActivity()).mResources;
        }else if(context instanceof Activity){
            dexPath = SkinPackageManager.getInstance((Activity)context).mDexPah;

            mResource = SkinPackageManager.getInstance((Activity)context).mResources;
        }else if(context instanceof View){
            dexPath = SkinPackageManager.getInstance(((View) context).getContext()).mDexPah;

            mResource = SkinPackageManager.getInstance(((View) context).getContext()).mResources;
        }
    }

    public void loadSkin(final boolean isNight) {
        if(context instanceof Fragment){
            SkinPackageManager.getInstance(((Fragment) context).getActivity()).loadSkinAsync(dexPath,
                    new SkinPackageManager.loadSkinCallBack() {

                        @Override
                        public void startLoadSkin() {
                            Log.d("TAG", "startloadSkin");
                        }

                        @Override
                        public void loadSkinSuccess() {
                            Log.d("TAG", "loadSkinSuccess");
                            if (isNight) {
                                plugChangeToNight();
                            } else {
                                plugChangeToDay();
                            }
                        }

                        @Override
                        public void loadSkinFail() {
                            Log.d("TAG", "loadSkinFail");
                        }
                    });
        }else if(context instanceof Activity){
            SkinPackageManager.getInstance((Activity)context).loadSkinAsync(dexPath,
                    new SkinPackageManager.loadSkinCallBack() {

                        @Override
                        public void startLoadSkin() {
                            Log.d("TAG", "startloadSkin");
                        }

                        @Override
                        public void loadSkinSuccess() {
                            Log.d("TAG", "loadSkinSuccess");
                            if (isNight) {
                                plugChangeToNight();
                            } else {
                                plugChangeToDay();
                            }
                        }

                        @Override
                        public void loadSkinFail() {
                            Log.d("TAG", "loadSkinFail");
                        }
                    });
        }else if(context instanceof View){
            SkinPackageManager.getInstance(((View)context).getContext()).loadSkinAsync(dexPath,
                    new SkinPackageManager.loadSkinCallBack() {

                        @Override
                        public void startLoadSkin() {
                            Log.d("TAG", "startloadSkin");
                        }

                        @Override
                        public void loadSkinSuccess() {
                            Log.d("TAG", "loadSkinSuccess");
                            if (isNight) {
                                plugChangeToNight();
                            } else {
                                plugChangeToDay();
                            }
                        }

                        @Override
                        public void loadSkinFail() {
                            Log.d("TAG", "loadSkinFail");
                        }
                    });
        }
    }
}
