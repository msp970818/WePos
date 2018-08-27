package net.msp.kaituo.wepos.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import net.msp.kaituo.wepos.R;

public class SlpashActivity extends AppCompatActivity {

    private TextView slpash_tv_versionName;
    private final int SPLASH_DISPLAY_LENGHT = 3000;  //延迟3秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);
        initUI();
        initData();

    }

    private void initData() {
        //1.获取当前版本的版本名称
        slpash_tv_versionName.setText(getVersionName());
        //2.检测是否有更新,如果有更新，提示用户下载
        //3.app开启展示logo
        countNum();



    }

    private void countNum() {//数秒
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SlpashActivity.this, LoginActivity.class);
                SlpashActivity.this.startActivity(intent);
                SlpashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    private String getVersionName() {
        //获取包的管理者
        PackageManager pm = getPackageManager();
        //通过包的管理者获取  版本名称和版本号,flags 传入0 代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initUI() {
        slpash_tv_versionName = (TextView) findViewById(R.id.slpash_tv_versionName);
    }


}
