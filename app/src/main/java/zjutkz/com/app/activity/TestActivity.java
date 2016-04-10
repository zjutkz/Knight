package zjutkz.com.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import zjutkz.com.app.R;

/**
 * Created by kangzhe on 16/4/8.
 */
public class TestActivity extends AppCompatActivity{

    @Knight(resName = "backgroundColor",nightResId = {R.color.night_background},dayResId = {R.color.day_background})
    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.change)
    Button btn;

    @Knight(resName = "background,textColor",nightResId = {R.color.night_background,R.color.night_text_color},dayResId = {R.color.day_background,R.color.day_text_color})
    @Bind(R.id.tv)
    TextView tv;

    @Knight(resName = "src",nightResId = {R.drawable.nav_header_night},dayResId = {R.drawable.nav_header_day})
    @Bind(R.id.iv)
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);
    }

    public void change(View view){
        //KnightUtil.changeToDay(this);
        KnightUtil.changeToNight(this);
    }
}
