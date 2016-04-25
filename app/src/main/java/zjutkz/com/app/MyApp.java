package zjutkz.com.app;

import android.app.Application;
import android.text.TextUtils;

import com.zjutkz.plug.SkinConfig;
import com.zjutkz.plug.SkinPackageManager;


/**
 * Created by kangzhe on 16/4/4.
 */
public class MyApp extends Application{

    public static final String CHANGE_SKIN = "change_skin";

    public static final String ACTION_IS_NIGHT = "action_is_night";

    @Override
    public void onCreate() {
        super.onCreate();

        String skinPath = SkinConfig.getInstance(this).getSkinResourcePath();
        if (!TextUtils.isEmpty(skinPath)) {
            // 如果已经换皮肤，那么第二次进来时，需要加载该皮肤
            SkinPackageManager.getInstance(this).loadSkinAsync(skinPath, null);
        }
    }
}
