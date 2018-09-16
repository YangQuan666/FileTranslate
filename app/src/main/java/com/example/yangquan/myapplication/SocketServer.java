package com.example.yangquan.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketServer {


    private static final int PORT = 8989;
    private List<Socket> mList = new ArrayList<>();
    ArrayList<File> files = new ArrayList<>();
    private ServerSocket server = null;
    private ExecutorService mExecutorService = null; //thread pool
    private Context context;

    public SocketServer(Context context, ArrayList<File> files){
        this.files = files;
        this.context = context;

        try {
            server = new ServerSocket(PORT);
            //创建线程池
            mExecutorService = Executors.newCachedThreadPool();
            InetAddress address = InetAddress.getLocalHost();

            Log.i("服务端：.","开始服务");
            Socket client = null;
            while(true)
            {
                client = server.accept();
                mList.add(client);
                mExecutorService.execute(new Service(client));
            }

        }catch(Exception e){e.printStackTrace();}
    }

    class Service implements Runnable {
        private Socket socket;
//        Handler uiHandler;
//        Message message;
        public Service(Socket socket) {
            this.socket = socket;
//            this.uiHandler = uiHandler;
//            this.message = new Message();
        }
        @Override
        public void run() {
            try{
                //发文件
                sendFile(files);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //发送文件
        void sendFile(ArrayList<File> files) {
            long totalSize = 0;
            byte buf[] = new byte[8192];
            int len;
            try {
                if (socket.isOutputShutdown()) {
                    return;
                }
                DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                dout.writeInt(files.size());


                for (int i = 0; i < files.size(); i++) {
                    dout.writeUTF(files.get(i).getName());
                    Log.d("新的 SendFile",files.get(i).getName());
                    dout.flush();
                    dout.writeLong(files.get(i).length());
                    Log.d("新的 SendFile","文件长度："+files.get(i).length());
                    dout.flush();
                    totalSize += files.get(i).length();
                }
                dout.writeLong(totalSize);
                Log.d("新的 SendFile","总长度："+totalSize);

                for (int i = 0; i < files.size(); i++) {
                    BufferedInputStream din = new BufferedInputStream(
                            new FileInputStream(files.get(i)));
                    while ((len = din.read(buf)) != -1) {

                        dout.write(buf, 0, len);
                    }
                }

                Log.d("新的 SendFile","文件传输完成");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("新的 SendFile","send file exception");
            }
            return;
        }

    }

    public int getClientNumber(){
        return mList.size();
    }
}

