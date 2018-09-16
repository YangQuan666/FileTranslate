package com.example.yangquan.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class recev_file_step1 extends AppCompatActivity {

    private Switch aSwitch;
    private ListView lv;
    private Button button,buttonInfo,startRev;

    private ArrayList<ScanResult> mList = new ArrayList<>();
    ArrayList<HashMap<String,String>> myArrayList=new ArrayList<>();
    WifiService wifiService;
    private Handler uiHandler ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recev_file_step1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setMessage("正在发送");
        uiHandler = new Handler(){

//            @Override
            public void handleMessage(Message msg){
                progressDialog.setMessage("正在发送第"+msg.arg1+"个文件");
                progressDialog.setProgress((int) msg.obj);
//                mToast = Toast.makeText(getApplicationContext(),
//                        "正在发送第"+msg.arg1+"个文件，已发送"+msg.obj+"%",
//                        Toast.LENGTH_SHORT);
//                mToast.show();

//            switch (msg.what){
//                case 1:
//                        alertDialog.setMessage("正在发送第"+msg.arg1+"个文件，已发送"+msg.obj+"%");
//                        alertDialog.show();
//                    break;
//                case 0:
//                    alertDialog.setMessage("发送完毕").show();
//                    break;
//            }
            }
        };
        wifiService = WifiService.getInstance(getApplicationContext());
        //wifi开关
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setChecked(wifiService.isWifiEnable());
        aSwitch.setText(wifiService.isWifiEnable()?"关闭WiFi":"打开WiFi");
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiService.isWifiEnable()){
                    wifiService.closeWiFi();
                    aSwitch.setText("打开WiFi");
                }

                else {
                    wifiService.openWifi();
                    aSwitch.setText("关闭WiFi");
                }
            }
        });
        //info
        buttonInfo = findViewById(R.id.wifiInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //打印 IP 地址
                Log.i("本机ip地址:",wifiService.getIp());
                Log.i("热点ip地址:",wifiService.getApIp());

            }
        });
        //开始接收
        startRev = findViewById(R.id.startRev);
        startRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //接收文件
                Thread Recevthread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            //接收文件

                            SocketClient client = new SocketClient(getApplicationContext(),wifiService.getApIp(),8989,uiHandler);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                Recevthread.start();
            }
        });
        //刷新按钮的功能
        button = findViewById(R.id.btn_refush);
        button.setOnClickListener(new View.OnClickListener() {
            //点击后可以刷新扫描内容
            @Override
            public void onClick(View v) {
                mList = (ArrayList<ScanResult>) wifiService.getScanResultList();
                 myArrayList.clear();
                for(int i=0;i<mList.size();i++){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemTitle", mList.get(i).SSID);
                    map.put("itemContent", "mac地址:"+mList.get(i).BSSID+",强度"+mList.get(i).level);
                    myArrayList.add(map);
                }

                SimpleAdapter sAdapter = (SimpleAdapter)lv.getAdapter();
                sAdapter.notifyDataSetChanged();
            }
        });


        //得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        lv = findViewById(R.id.lv);
        mList = (ArrayList<ScanResult>) wifiService.getScanResultList();
        //创建ArrayList对象 并添加数据

        for(int i=0;i<mList.size();i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemTitle", mList.get(i).SSID);
            map.put("itemContent", "mac地址:"+mList.get(i).BSSID+",强度"+mList.get(i).level);
            myArrayList.add(map);
        }

        //生成SimpleAdapter适配器对象
        SimpleAdapter mySimpleAdapter=new SimpleAdapter(this,
                myArrayList,//数据源
                android.R.layout.simple_list_item_activated_2,//ListView内部数据展示形式的布局文件listitem.xml
                new String[]{"itemTitle","itemContent"},//HashMap中的两个key值 itemTitle和itemContent
                new int[]{android.R.id.text1,android.R.id.text2});/*布局文件listitem.xml中组件的id布局文件的各组件分别映射到HashMap的各元素上，完成适配*/

        lv.setAdapter(mySimpleAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map=(HashMap<String,String>)lv.getItemAtPosition(position);
                String title=map.get("itemTitle");
                String content=map.get("itemContent");
                Toast.makeText(getApplicationContext(),
                        "正在连接"+title+"。。。",
                        Toast.LENGTH_SHORT).show();

                wifiService.connectWifi(title,"0123456789",3);



            }
        });







    }
}
