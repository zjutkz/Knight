package zjutkz.com.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kangzhe on 16/4/4.
 */
public class ContentFragment extends Fragment{

    @Knight(resName = "backgroundColor",nightResId = {R.color.content_night},dayResId = {R.color.content_day})
    @Bind(R.id.fragment_content)
    FrameLayout content;

    @Knight(resName = "textColor",nightResId = {R.color.desc_night},dayResId = {R.color.desc_day})
    @Bind(R.id.content_desc)
    TextView desc;

    LocalBroadcastManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,container,false);

        ButterKnife.bind(this,view);

        manager = LocalBroadcastManager.getInstance(this.getActivity());

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApp.CHANGE_SKIN);
        manager.registerReceiver(new ChangeSkinBroadcastReceiver(), filter) ;

        return view;
    }

    public void setDesc(String text){
        desc.setText(text);
    }

    private class ChangeSkinBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyApp.CHANGE_SKIN)){
                if(intent.getBooleanExtra(MyApp.ACTION_IS_NIGHT,false)){
                    KnightUtil.changeToNight(ContentFragment.this);
                }else {
                    KnightUtil.changeToDay(ContentFragment.this);
                }
            }
        }
    }
}
