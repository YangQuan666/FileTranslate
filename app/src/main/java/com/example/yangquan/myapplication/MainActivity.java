package com.example.yangquan.myapplication;

import android.content.Intent;
import android.os.Bundle;

//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转页面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, send_file_step1.class);
                startActivity(intent);


            }
        });

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*这里写实现接收文件的代码
                TODO
                */
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, recev_file_step1.class);//指明要跳转的Activity类
                startActivity(intent);

            }
        });

    }

}
