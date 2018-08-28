package net.msp.kaituo.wepos;

import android.support.annotation.NonNull;

/**
 * Wepos
 *
 * @author kaituo:马世鹏
 * @create 2018/8/28 9:50
 * @since
 **/
public class WifiBean implements Comparable<WifiBean> {
    /**
     * ScanResult的属性如下：
     *  public String SSID;
     public String capabilities;
     public int centerFreq0;
     public int centerFreq1;
     public int channelWidth;
     public int frequency;
     public int level;
     public CharSequence operatorFriendlyName;
     public long timestamp;
     public CharSequence venueName;
     */

    private String wifiName;
    private String level;
    private String state;   //已连接  正在连接  未连接 三种状态
    private String capabilities;    //加密方式

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }


    @Override
    public int compareTo(WifiBean o) {
        int level1 = Integer.parseInt(this.getLevel());
        int level2 = Integer.parseInt(o.getLevel());
        return level1 - level2;
    }
}
