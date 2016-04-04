package zjutkz.com.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjutkz.annotation.Dress;
import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

public class MainActivity extends AppCompatActivity {

    @Knight(resName = "background,textColor",dayResId = {R.color.btn_day,R.color.text_color_day},
            nightResId = {R.color.btn_night,R.color.text_color_night})
    Button change;

    @Knight(resName = "background,textColor",dayResId = {R.color.tv_night,R.color.text_color_day},
            nightResId = {R.color.tv_day,R.color.text_color_night})
    TextView tv;

    @Knight(resName = "src",dayResId = R.drawable.ironman,nightResId = R.drawable.cap)
    ImageView iv;

    @Dress
    BorderView bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        change = (Button)findViewById(R.id.change);
        tv = (TextView)findViewById(R.id.tv);
        iv = (ImageView)findViewById(R.id.iv);
        bv = (BorderView)findViewById(R.id.bv);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MyApp.isNight){
            KnightUtil.changeToNight(this);
        }
    }

    public void change(View view){
        if(MyApp.isNight){
            MyApp.isNight = false;

            KnightUtil.changeToDay(this);
        }else {
            MyApp.isNight = true;

            KnightUtil.changeToNight(this);
        }
    }
}
