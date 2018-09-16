package com.example.yangquan.myapplication;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class send_file_step2 extends AppCompatActivity implements View.OnClickListener{

    ArrayList<File> selectedFiles = new ArrayList<>();

    private TextView countFile;
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView picContext;
    private TextView musicContext;
    private TextView videoContext;
    private Button checkOK;

//    private ListView musicListV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_file_step2);

        countFile = findViewById(R.id.countFile);

        viewPager = findViewById(R.id.viewPager);
        picContext = findViewById(R.id.pictureLayout);
        musicContext = findViewById(R.id.musicLayout);
        videoContext = findViewById(R.id.apkLayout);

        picContext.setOnClickListener(this);
        musicContext.setOnClickListener(this);
        videoContext.setOnClickListener(this);

        ListView picListview = new ListView(this);
        ListView musicListview = new ListView(this);
        ListView videoListview = new ListView(this);
        pageview =new ArrayList<>();

        //添加想要切换的界面
        pageview.add(picListview);
        pageview.add(musicListview);
        pageview.add(videoListview);

        //设置ViewPager的适配器
        viewPager.setAdapter(new CustomPagerAdapter(this, pageview));

//        files  = new FileUtil("/storage/emulated/0/12345").getMusicFiles();
//        ArrayList<File> picFile = FileManager.getInstance(getApplicationContext()).getPic();
//        String[] aString = (String[]) picFile.toArray();
//        System.out.println(Arrays.toString(aString));

        //这三句意思就是把File类型的转换为String类型的ArrayList ,下面的同理
        final ArrayList<File> picFile = FileManager.getInstance(getApplicationContext()).getPic();
        ArrayList<String> picString = new ArrayList<>();
        for (int i=0;i<picFile.size();i++){
            picString.add(picFile.get(i).getName());
        }
        //绑定适配器
        picListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, picString));


        final ArrayList<File> misFile = FileManager.getInstance(getApplicationContext()).getMusics();
        ArrayList<String> misString = new ArrayList<>();
        for (int i=0;i<misFile.size();i++){
            misString.add(misFile.get(i).getName());
        }
        musicListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, misString));


        final ArrayList<File> videoFile = FileManager.getInstance(getApplicationContext()).getVieos();
        ArrayList<String> videoString = new ArrayList<>();
        for (int i=0;i<videoFile.size();i++){
            videoString.add(videoFile.get(i).getName());
        }
        videoListview.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, videoString));


        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);



        //图片ListView监听器
        picListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView chkItem = view.findViewById(android.R.id.text1);
                chkItem.setChecked(!chkItem.isChecked());

                if (chkItem.isChecked()){
                    selectedFiles.add(picFile.get(position));
//                    Toast.makeText(getApplicationContext(), "选择了 "+picFile.get(position).getName()+" 图片\n", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedFiles.remove(picFile.get(position));
//                    Toast.makeText(getApplicationContext(), "取消选择了 "+picFile.get(position).getName()+" 图片\n", Toast.LENGTH_SHORT).show();
                }
                countFile.setText("已选择："+selectedFiles.size());
            }

        });

        //音乐ListView监听器
        musicListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView chkItem = view.findViewById(android.R.id.text1);
                chkItem.setChecked(!chkItem.isChecked());

                if (chkItem.isChecked()){
                    selectedFiles.add(misFile.get(position));
//                    Toast.makeText(getApplicationContext(), "选择了 "+misFile.get(position).getName()+" 音乐\n", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedFiles.remove(misFile.get(position));
//                    Toast.makeText(getApplicationContext(), "取消选择了 "+misFile.get(position).getName()+" 音乐\n", Toast.LENGTH_SHORT).show();
                }
                countFile.setText("已选择："+selectedFiles.size());
            }

        });
        //视频ListView监听器
        videoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView chkItem = view.findViewById(android.R.id.text1);
                chkItem.setChecked(!chkItem.isChecked());

                if (chkItem.isChecked()){
                    selectedFiles.add(videoFile.get(position));
//                    Toast.makeText(getApplicationContext(), "选择了 "+videoFile.get(position).getName()+" 图片\n", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedFiles.remove(videoFile.get(position));
//                    Toast.makeText(getApplicationContext(), "取消选择了 "+videoFile.get(position).getName()+" 图片\n", Toast.LENGTH_SHORT).show();
                }
                countFile.setText("已选择："+selectedFiles.size());
            }

        });


        //设置确定按钮点击事件
        checkOK = findViewById(R.id.checkOK);
        checkOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把数据发给上一个页面的file对象
                send_file_step1.files = selectedFiles;
                Intent intent = new Intent();
                intent.setClass(send_file_step2.this, send_file_step1.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pictureLayout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.musicLayout:
                viewPager.setCurrentItem(1);
                break;
            case R.id.apkLayout:
                viewPager.setCurrentItem(2);
                break;
        }

    }

}

