package zjutkz.com.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zjutkz.annotation.Dress;
import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.drawer)
    DrawerLayout drawer;

    @Dress
    @Knight(resName = "backgroundColor",nightResId = {R.color.nav_background_night},dayResId = {R.color.nav_background_day})
    @Bind(R.id.navigation)
    MyNavigationView navigationView;

    @Bind(R.id.content)
    FrameLayout content;

    @Nullable
    @Bind(R.id.nav_header)
    FrameLayout nav_header;

    @Nullable
    @Knight(resName = "src", nightResId = {R.drawable.nav_header_night}, dayResId = {R.drawable.nav_header_day})
    @Bind(R.id.nav_header_img)
    ImageView nav_header_img;

    ContentFragment fragment;

    LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // TODO: 16/4/4 how to use butterKnife with navigationView header?!?!
        View headerView = navigationView.getHeaderView(0);
        nav_header_img = (ImageView)headerView.findViewById(R.id.nav_header_img);

        fragment = new ContentFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction().add(R.id.content, fragment);
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                fragment.setDesc(item.getTitle().toString());

                drawer.closeDrawers();
                return false;
            }
        });

        manager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApp.CHANGE_SKIN);
        manager.registerReceiver(new ChangeSkinBroadcastReceiver(),filter) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.change:
                if(MyApp.isNight){
                    MyApp.isNight = false;
                    
                    Intent intent = new Intent();
                    intent.setAction(MyApp.CHANGE_SKIN);
                    intent.putExtra(MyApp.ACTION_IS_NIGHT, MyApp.isNight);
                    
                    manager.sendBroadcast(intent);
                }else{
                    MyApp.isNight = true;

                    Intent intent = new Intent();
                    intent.setAction(MyApp.CHANGE_SKIN);
                    intent.putExtra(MyApp.ACTION_IS_NIGHT, MyApp.isNight);

                    manager.sendBroadcast(intent);
                }
                break;
        }
        return true;
    }
    
    private class ChangeSkinBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyApp.CHANGE_SKIN)){
                if(intent.getBooleanExtra(MyApp.ACTION_IS_NIGHT,false)){
                    KnightUtil.changeToNight(MainActivity.this);
                }else {
                    KnightUtil.changeToDay(MainActivity.this);
                }
            }
        }
    }
}