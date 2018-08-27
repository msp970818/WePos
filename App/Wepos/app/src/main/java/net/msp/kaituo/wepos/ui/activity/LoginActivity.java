package net.msp.kaituo.wepos.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.msp.kaituo.wepos.R;

public class LoginActivity extends AppCompatActivity {

    private Button btn_back;
    private AlertDialog alert = null;
    AlertDialog.Builder builder = null;
    private EditText edit_accout;
    private EditText edit_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化界面
        initUI();
        //控件事件设置
        setupEvents();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化界面
     */
    private void initUI() {

        btn_back = (Button) findViewById(R.id.title_btn_back);
        edit_accout = (EditText) findViewById(R.id.input_edit_account);
        edit_pwd = (EditText) findViewById(R.id.input_edit_password);

    }

    /**
     * 控件事件设置
     */
    private void setupEvents() {

        //登录见面左上角  退出按钮
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(LoginActivity.this);
                alert  = builder.setTitle("您确定退出吗？")
                        .setMessage("点击“确定”退出APP，点击“取消”退回。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(LoginActivity.this,"再见！欢迎下次使用！",Toast.LENGTH_SHORT).show();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                alert.show();
            }
        });
    }
}
