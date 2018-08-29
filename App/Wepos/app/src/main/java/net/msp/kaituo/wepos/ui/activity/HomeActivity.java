package net.msp.kaituo.wepos.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import net.msp.kaituo.wepos.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationBar mBottomNavigationBar;
    private int index; //点击的fragment的下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        setupEvent();

    }

    /**
     * 设置事件
     */
    private void setupEvent() {

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        index = 0;
                        break;
                    case 1:
                        index = 1;
                        break;
                    case 2:
                        index = 2;
                        break;
                }
                Intent intent = null;
                if (index == 0){
                    startActivity(intent = new Intent(HomeActivity.this,PandianActivity.class));
                }else if (index == 1){
                    startActivity(intent = new Intent(HomeActivity.this,HelpActivity.class));
                }else if (index == 2){
                    startActivity(intent = new Intent(HomeActivity.this,SettingActivity.class));
                    return;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.white)//选中颜色 图标和文字
//                .setInActiveColor("#8e8e8e")//默认未选择颜色
                .setBarBackgroundColor(R.color.orange_deep);//默认背景色
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.pandian_white,"盘点")
                .setInactiveIcon(ContextCompat.getDrawable(HomeActivity.this,R.drawable.pandian)))

                .addItem(new BottomNavigationItem(R.drawable.help_white,"帮助")
                .setInactiveIcon(ContextCompat.getDrawable(HomeActivity.this,R.drawable.help)))

                .addItem(new BottomNavigationItem(R.drawable.setting_white,"设置")
                .setInactiveIcon(ContextCompat.getDrawable(HomeActivity.this,R.drawable.setting)))
//                .setFirstSelectedPosition(0)//设置默认选择的按钮督查
                .initialise();//所有的设置需在调用该方法前完成
    }

    /**
     * 初始化控件
     */
    private void initUI() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }


}
