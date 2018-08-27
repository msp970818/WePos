package net.msp.kaituo.wepos.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import net.msp.kaituo.wepos.R;
import net.msp.kaituo.wepos.WifiSupport;


public class WifiListContectActivity extends AppCompatActivity {
    private ProgressBar WifiLoading;
    private Boolean mHashPermission;

    /**
     1.进入页面。检测wifi开关是否打开，权限是否已经获取，如果没，动态申请权限，如果是请求wifi列表。
     2.获取扫描wifi列表，循环遍历转成自己的wifi对象和集合。更新适配器，刷新界面列表
     3.书写适配器，Recyclerview设置适配器。
     4.注册广播，监听wifi开关状态，监听wifi是否已经连接，监听wifi列表是否变化。
     5.点击wifi列表item连接。具体情况具体分析，已连接–>wifi详情；未连接–>连接；
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list_contect);
        //界面初始化
        initUI();
        setupEvent();
    }

    /**
     * 设置事件
     */
    private void setupEvent() {
        //先隐藏loading  进行权限检查
        hideProgressBar();
        mHashPermission = CheckPermission();
        //如果未授权，申请授权
        // 如果授权了，进行wifi检查
        if (!mHashPermission && WifiSupport.isOpenWifi(MainActivity.this)){
        }
    }

    private boolean CheckPermission() {
    }

    /**
     * 当加载完成是，隐藏ProgressBar
     */
    private void hideProgressBar() {
        WifiLoading.setVisibility(View.GONE);
    }

    /**
     * 界面初始化
     */
    private void initUI() {
        WifiLoading = (ProgressBar) findViewById(R.id.pb_wifi_loading);

    }
}
