package com.example.yangquan.myapplication;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

import java.lang.reflect.Method;

//服务端用来打开wifi热点

public class APService {

    private static APService instance = null;
    private WifiManager wifiManager = null;
    private static String ApName = "yangquan";
    //私有构造方法
    private APService(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static APService getInstance(Context context) {
        if (instance==null)
            instance = new APService(context);
        return instance;
    }

    /**判断热点开启状态*/
    public boolean isApEnabled() {
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
//            Method method = wifiManager.getClass().getDeclaredMethod("cancelLocalOnlyHotspotRequest");
//            method.invoke(wifiManager);
        }
        catch (Throwable throwable) {}
        return false;
    }

    /*关闭WiFi热点*/
    public void disableAp() {
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, null, false);
        } catch (Throwable ignored) {

        }
    }

    /*开启wifi热点*/
    public boolean openAp() {

        WifiConfiguration config = null;
        try {

            ///wifi和热点不能同时打开，先判断wifi是否打开，打开则关闭
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
            if(isApEnabled()) {
                return true;
            }
            //反射
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            //配置热点信息
            config = new WifiConfiguration();
            config.SSID = ApName;
//            config.preSharedKey = mPasswd;
            config.hiddenSSID = true;
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//            config.allowedKeyManagement.set(6);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            method.invoke(wifiManager, config, true);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
