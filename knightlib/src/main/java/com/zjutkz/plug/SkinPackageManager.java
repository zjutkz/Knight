package com.zjutkz.plug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kangzhe on 16/4/10.
 */
public class SkinPackageManager {
    private static SkinPackageManager mInstance;
    private Context mContext;

    /**
     * 当前资源包名
     */
    public String mPackageName;

    public String mDexPah;

    /**
     * 皮肤资源
     */
    public Resources mResources;

    public SkinPackageManager(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 获取单例
     *
     * @param mContext
     * @return
     */
    public static SkinPackageManager getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new SkinPackageManager(mContext);
        }
        return mInstance;
    }

    /**
     * 从网络中加载
     *
     * @param downloadPath
     * @param path
     * @return
     */
    public boolean copyApkFromNet(String downloadPath,String path) {
        boolean copyIsFinish = false;

        mDexPah = path;

        try {
            File file = new File(path);

            URL url = new URL(downloadPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, size);
            }

            fos.flush();
            fos.close();
            bis.close();
            copyIsFinish = true;
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copyIsFinish;
    }

    /**
     * 从assets中复制apk到sd中
     *
     * @param context
     * @param filename
     * @param path
     * @return
     */
    public boolean copyApkFromAssets(Context context, String filename,
                                     String path) {
        boolean copyIsFinish = false;

        mDexPah = path;

        try {
            // 打开assets的输入流
            InputStream is = context.getAssets().open(filename);
            File file = new File(path);
            // 创建一个新的文件
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i); // 写入到文件
            }

            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copyIsFinish;

    }

    /**
     * 异步加载皮肤资源
     *
     * @param dexPath
     *            需要加载的皮肤资源
     * @param callback
     *            回调接口
     */
    public void loadSkinAsync(String dexPath, final loadSkinCallBack callback) {
        new AsyncTask<String, Void, Resources>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (callback != null) {
                    callback.startLoadSkin();
                }
            }

            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        //
                        String dexPath_tmp = params[0];
                        // 得到包管理器
                        PackageManager mpm = mContext.getPackageManager();
                        // 得到包信息
                        PackageInfo mInfo = mpm.getPackageArchiveInfo(
                                dexPath_tmp, PackageManager.GET_ACTIVITIES);
                        mPackageName = mInfo.packageName;

                        // AssetManager实例
                        AssetManager assetManager = AssetManager.class
                                .newInstance();
                        // 通过反射调用addAssetPath方法
                        Method addAssetPath = assetManager.getClass()
                                .getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, dexPath_tmp);

                        // 得到资源实例
                        Resources superRes = mContext.getResources();
                        // 实例化皮肤资源
                        Resources skinResource = new Resources(assetManager,
                                superRes.getDisplayMetrics(),
                                superRes.getConfiguration());
                        // 保存资源路径
                        SkinConfig.getInstance(mContext).setSkinResourcePath(
                                dexPath_tmp);
                        return skinResource;
                    }
                } catch (Exception e) {
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Resources result) {
                super.onPostExecute(result);
                mResources = result;

                // 这里执行回调方法
                if (callback != null) {
                    if (mResources != null) {
                        callback.loadSkinSuccess();
                    } else {
                        callback.loadSkinFail();
                    }
                }
            }

        }.execute(dexPath);
    }

    public interface loadSkinCallBack {
        void startLoadSkin();

        void loadSkinSuccess();

        void loadSkinFail();
    }

}
