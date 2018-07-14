package com.example.yangquan.myapplication;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

public class WifiService {

    private static WifiService wifiService = null;

    public static WifiManager wifiManager;
    List<ScanResult> mScanResultList = new ArrayList<>();
    List<WifiConfiguration> mWifiConfigurations;


    private WifiService(Context context){
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiService getInstance(Context context){
        if (wifiService==null)
            wifiService = new WifiService(context);
        return wifiService;
    }

    //判断WiFi状态
    public boolean isWifiEnable(){
        return wifiManager.isWifiEnabled() ? true : false;
    }

    //打开WiFi
    public void openWifi(){
        if (!isWifiEnable())
            wifiManager.setWifiEnabled(true);
    }
    //关闭WiFi
    public void closeWiFi(){
        if (isWifiEnable())
            wifiManager.setWifiEnabled(false);
    }

    /**
     * wifi扫描
     */

    public List<ScanResult> getScanResultList() {
        if (wifiManager.isWifiEnabled()){
            wifiManager.startScan();
            mScanResultList = wifiManager.getScanResults();
        }else return new ArrayList<>();
        return mScanResultList;
    }
//    public boolean ConnWiFi(String SSID, String Password, int Type){
//        WifiConfiguration wifiConfiguration = createWifiConfig(SSID,Password,Type);
//        wifiManager.addNetwork(wifiConfiguration);
//        wifiManager.disconnect();
//        wifiManager.enableNetwork(wifiConfiguration.networkId,true);
//        wifiManager.reconnect();
//        return true;
//    }
    //连接WiFi热点
//    public WifiConfiguration createWifiConfig(String SSID, String Password, int Type)
//    {
//        WifiConfiguration config = new WifiConfiguration();
//        config.allowedAuthAlgorithms.clear();
//        config.allowedGroupCiphers.clear();
//        config.allowedKeyManagement.clear();
//        config.allowedPairwiseCiphers.clear();
//        config.allowedProtocols.clear();
//        config.SSID = "\"" + SSID + "\"";
//
//
//
//        if(Type == 1) //WIFICIPHER_NOPASS
//        {
////            config.wepKeys[0] = "";
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
////            config.wepTxKeyIndex = 0;
//
//        }
//        if(Type == 2) //WIFICIPHER_WEP
//        {
//            config.hiddenSSID = true;
//            config.wepKeys[0]= "\""+Password+"\"";
//            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
//        }
//        if(Type == 3) //WIFICIPHER_WPA
//        {
//            config.preSharedKey = "\""+Password+"\"";
//            config.hiddenSSID = true;
//            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//            config.status = WifiConfiguration.Status.ENABLED;
//        }
//
//        return config;
//    }


    /**
     * 获取当前WifiInfo
     */
    public WifiInfo getWifiInfo(){
        WifiInfo mWifiInfo = wifiManager.getConnectionInfo();
        return mWifiInfo;
    }

    /**
     * 获取当前wifi的IP地址
     * @return
     */
    public String getIp(){
        int i = wifiService.getWifiInfo().getIpAddress();
        String ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
        return ip;
    }
    /**
     * 获取连接点WiFi的IP地址
     */
    public String getApIp(){
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        int i = dhcpInfo.serverAddress;
        //此处获取ip为整数类型，需要进行转换
        String strI = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 24) & 0xFF);
        return strI;
    }







    /**
     *
     * @param targetSsid
     * @param targetPsd
     * @param enc
     */

    //////////////
    public void connectWifi(String targetSsid, String targetPsd, int enc) {
        // 1、注意热点和密码均包含引号，此处需要需要转义引号
        String ssid = "\"" + targetSsid + "\"";
        String psd = "\"" + targetPsd + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        switch (enc) {
            case 1:
                // 加密类型为WEP
                conf.wepKeys[0] = psd;
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case 2:
                // 加密类型为WPA
                conf.preSharedKey = psd;
                break;
            case 3:
                //开放网络
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        //3、链接wifi
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals(ssid)) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
            }
        }
    }


    ///////////////////////////////////////////

}
