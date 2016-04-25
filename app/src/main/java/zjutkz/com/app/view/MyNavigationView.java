package zjutkz.com.app.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;

import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

import zjutkz.com.app.MyApp;

/**
 * Created by kangzhe on 16/4/4.
 */
public class MyNavigationView extends NavigationView{

    LocalBroadcastManager manager;

    public MyNavigationView(Context context) {
        super(context);
    }

    public MyNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        manager = LocalBroadcastManager.getInstance(this.getContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApp.CHANGE_SKIN);
        manager.registerReceiver(new ChangeSkinBroadcastReceiver(), filter) ;
    }

    @Knight(nightResId = {Color.WHITE},dayResId = {Color.BLACK})
    public void setItemTextColor(int color){
        ColorStateList colors = new ColorStateList(
                new int[][] {{ android.R.attr.state_pressed, android.R.attr.state_focused},{0}},
                new int[] { color , color});
        super.setItemTextColor(colors);
    }

    private class ChangeSkinBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyApp.CHANGE_SKIN)){
                if(intent.getBooleanExtra(MyApp.ACTION_IS_NIGHT,false)){
                    KnightUtil.changeToNight(MyNavigationView.this.getContext());
                }else {
                    KnightUtil.changeToDay(MyNavigationView.this.getContext());
                }
            }
        }
    }
}
