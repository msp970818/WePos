package net.msp.kaituo.wepos.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.msp.kaituo.wepos.Adapter.WifiListAdapter;
import net.msp.kaituo.wepos.AppContants;

import net.msp.kaituo.wepos.R;
import net.msp.kaituo.wepos.WifiBean;
import net.msp.kaituo.wepos.WifiSupport;
import net.msp.kaituo.wepos.ui.WifiLinkDialog;
import net.msp.kaituo.wepos.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WifiListContectActivity extends AppCompatActivity {
    ProgressBar WifiLoading;
    private Boolean mHashPermission;
    private RecyclerView recyclerWifiList;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;

    List<WifiBean> realWifiList = new ArrayList<>();

    private WifiListAdapter adapter;
    private static final String TAG = "WifiListContectActivity";
    private WifiBroadcastReceiver wifiReceiver;

    private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）

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
        WifiLoading = (ProgressBar) findViewById(R.id.pb_wifi_loading);
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
        if (!mHashPermission && WifiSupport.isOpenWifi(WifiListContectActivity.this)){
            requestPermission();
        }else if (mHashPermission && WifiSupport.isOpenWifi(WifiListContectActivity.this)){
            initRecycler();
        }else {
            Toast.makeText(WifiListContectActivity.this,"wifi处于关闭状态",Toast.LENGTH_SHORT).show();
        }


    }

    private void hideProgressBar() {
        WifiLoading.setVisibility(View.GONE);
    }

    private void initRecycler() {

        recyclerWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
        adapter = new WifiListAdapter(this,realWifiList);
        recyclerWifiList.setLayoutManager(new LinearLayoutManager(this));
        recyclerWifiList.setAdapter(adapter);

        if(WifiSupport.isOpenWifi(WifiListContectActivity.this) && mHashPermission){
            sortScaResult();
        }else{
            Toast.makeText(WifiListContectActivity.this,"WIFI处于关闭状态或权限获取失败22222",Toast.LENGTH_SHORT).show();
        }

        adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Object o) {
                WifiBean wifiBean = realWifiList.get(postion);
                if(wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)){
                    String capabilities = realWifiList.get(postion).getCapabilities();
                    if(WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                        WifiConfiguration tempConfig  = WifiSupport.isExsits(wifiBean.getWifiName(),WifiListContectActivity.this);
                        if(tempConfig == null){
                            WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                            WifiSupport.addNetWork(exsits, WifiListContectActivity.this);
                        }else{
                            WifiSupport.addNetWork(tempConfig, WifiListContectActivity.this);
                        }
                    }else{   //需要密码，弹出输入密码dialog
                        noConfigurationWifi(postion);
                    }
                }
            }
        });
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,NEEDED_PERMISSIONS,PERMISSION_REQUEST_CODE);
    }

    /**
     * 授权回调
     * @param requestCode 请求码
     * @param permissions
     * @param grantResults  授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hashAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE){
            for (int i : grantResults){
                if (i != PackageManager.PERMISSION_GRANTED){
                    hashAllPermission = true;       //判断用户是否同意获取权限
                    break;
                }
            }

            //如果同意权限
            if (hashAllPermission){
                mHashPermission = true;
                if (mHashPermission && WifiSupport.isOpenWifi(WifiListContectActivity.this)){       //如果wifi开关是开 并且 已经获取权限
                    sortScaResult();
                }else {
                    Toast.makeText(this,"WIFI处于关闭或者权限获取失败.1",Toast.LENGTH_SHORT).show();
                }
            }else {//用户不同意权限
                mHashPermission = false;
                Toast.makeText(WifiListContectActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    private void sortScaResult() {
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(this));
        realWifiList.clear();
        if (!CollectionUtils.isNullOrEmpty(scanResults)){
            for (int i =0 ;i < scanResults.size() ;i++){
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);
                Log.d(TAG, "sortScaResult: " + wifiBean);

                //排序
                Collections.sort(realWifiList);
                adapter.notifyDataSetChanged();     //?
            }
        }

    }

    /**
     * 检查是否权限请求完成  false代表未请求权限
     * @return  true代表已经请求权限
     */
    private boolean CheckPermission() {
        for (String permission : NEEDED_PERMISSIONS){
            if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifiwifi连接状态广播
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        this.registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(wifiReceiver);
    }

    //监听wifi的状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
                switch (state){
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */

                    case WifiManager.WIFI_STATE_DISABLED:{
                        Log.d(TAG,"已经关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        Log.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        Log.d(TAG,"已经打开");
                        sortScaResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        Log.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        Log.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    Log.d(TAG, "wifi没有连接上");
                    hideProgressBar();
                    for (int i = 0; i < realWifiList.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
                    }
                    adapter.notifyDataSetChanged();
                }else if (NetworkInfo.State.CONNECTED == info.getState()){
                    Log.d(TAG, "wifi连接上了");
                    hideProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiListContectActivity.this);

                    //连接成功 跳转界面 传递ip地址
                    Toast.makeText(WifiListContectActivity.this,"wifi连接上了",Toast.LENGTH_SHORT).show();

                    connectType = 1;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType);
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    Log.d(TAG,"wifi正在连接");
                    showProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiListContectActivity.this);
                    connectType = 2;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType );
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.d(TAG, "网络列表变化了");
                wifiListChange();
            }
        }
    }

        /**
         * //网络状态发生改变 调用此方法！
         */
        public void wifiListChange(){
            sortScaResult();
            WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiListContectActivity.this);
            if(connectedWifiInfo != null){
                wifiListSet(connectedWifiInfo.getSSID(),connectType);
            }
        }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     * @param wifiName
     * @param type
     */
    public void wifiListSet(String wifiName , int type){
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if(CollectionUtils.isNullOrEmpty(realWifiList)){
            return;
        }
        for(int i = 0;i < realWifiList.size();i++){
            realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for(int i = 0;i < realWifiList.size();i++){
            WifiBean wifiBean = realWifiList.get(i);
            if(index == -1 && ("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)){
                index = i;
                wifiInfo.setLevel(wifiBean.getLevel());
                wifiInfo.setWifiName(wifiBean.getWifiName());
                wifiInfo.setCapabilities(wifiBean.getCapabilities());
                if(type == 1){
                    wifiInfo.setState(AppContants.WIFI_STATE_CONNECT);
                }else{
                    wifiInfo.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                }
            }
        }
        if(index != -1){
            realWifiList.remove(index);
            realWifiList.add(0, wifiInfo);
            adapter.notifyDataSetChanged();
        }
    }

    private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
        WifiLinkDialog linkDialog = new WifiLinkDialog(this,R.style.dialog_download,realWifiList.get(position).getWifiName(), realWifiList.get(position).getCapabilities());
        if(!linkDialog.isShowing()){
            linkDialog.show();
        }
    }

    public void showProgressBar() {
        WifiLoading.setVisibility(View.VISIBLE);
    }
}
