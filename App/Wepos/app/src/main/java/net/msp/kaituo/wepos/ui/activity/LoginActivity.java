package net.msp.kaituo.wepos.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private Button btn_back,btn_login;
    private AlertDialog alert = null;
    AlertDialog.Builder builder = null;
    private EditText edit_accout;
    private EditText edit_pwd;
    private String TAG = "LoginActivity";
    private String account;
    private String pwd;


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
        btn_login = (Button) findViewById(R.id.login_button);

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
        //登录按钮
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkaccountAndpwd();
            }
        });
    }

    private void checkaccountAndpwd() {
        account = edit_accout.getText().toString();
        pwd = edit_pwd.getText().toString();
        Intent intent = null;
        if ((account.equals("admin")) && (pwd.equals("admin"))){
            intent = new Intent(LoginActivity.this,HomeActivity.class);
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

            startActivity(intent);
        }else {
            Toast.makeText(LoginActivity.this,"账户或者密码错误！确认后再试！",Toast.LENGTH_SHORT).show();
        }
    }
}











//    /**
//     * 读取数据库文件（.sql），并执行sql语句
//     * @param db
//     * @param dbfilepath assets下的*.sql文件路径，比如 acupointsdb/acupoints.sql
//     */
//    public void executeAssetsSQL(SQLiteDatabase db,String dbfilepath){
//        BufferedReader bufferedReader = null;
//        try {
//            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(dbfilepath)));
//            Log.d(TAG, "路径" + dbfilepath);
//            String line;
//            String buffer = "";
//            //开启事物
//            db.beginTransaction();
//            while ((line = bufferedReader.readLine()) != null){
//                buffer += line;
//                if (line.trim().endsWith(";")){
//                    db.execSQL(buffer.replace(";",""));
//                    buffer = "";
//                }
//            }
//            //设置事物标识为成功，当结束事物时会提交事务
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("db-error",e.toString());
//        }finally {
//            //事务结束
//            db.endTransaction();
//            if (bufferedReader != null){
//                try {
//                    bufferedReader.close();
//                } catch (Exception e) {
//                    Log.e("db-error",e.toString());
//                }
//            }
//        }
//    }

