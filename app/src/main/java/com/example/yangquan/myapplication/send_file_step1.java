package com.example.yangquan.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class send_file_step1 extends AppCompatActivity {

    Button button;
    Button startSend;
    SocketServer s;
    Context context;
    public static ArrayList<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_file_step1);
        context = this;
//        showClient = findViewById(R.id.showClient);

//        //刷新客户端数量
//        refresh= findViewById(R.id.refresh);
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showClient.setText("当前已连上的客户端数:"+s.getClientNumber());
//            }
//        });

        //打开WiFi热点
        button = findViewById(R.id.OpeAp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "正在开启WiFi热点。。。",
                        Toast.LENGTH_SHORT).show();

                APService apService= APService.getInstance(getApplicationContext());
                apService.openAp();

            }
        });


        //进入选择文件界面
        button = findViewById(R.id.sendFile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳到选择文件的界面，选择文件后又会返回此界面
                Intent intent = new Intent();
                intent.setClass(send_file_step1.this, send_file_step2.class);
                startActivity(intent);


            }
        });

        //点击后开启服务器发送文件
        startSend = findViewById(R.id.startSend);
        startSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 //开启新线程来传输文件
                 Thread SendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                try  {
                    s = new SocketServer(context,files);

                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                });
                 SendThread.start();

            }
        });

    }
}
