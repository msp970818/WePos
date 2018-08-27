package net.msp.kaituo.wepos;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Wepos
 *
 * @author kaituo:马世鹏
 * @create 2018/8/27 17:35
 * @since
 **/
public class WifiSupport {


    public static boolean isOpenWifi(Context context) {
        //
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean b = wifiManager.isWifiEnabled();
        return b;
    }
}
