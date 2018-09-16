package com.example.yangquan.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class SocketClient {

    private Socket socket = null;
    Context mContext;
    Handler handler;
    Message message;

    public SocketClient(Context context, String serverIP, int port, Handler uiHandler){
        mContext = context;
        this.handler = uiHandler;

        try {
            this.socket = new Socket(serverIP,port);
        } catch (IOException e) {
            Log.i("SOCKET 建立失败",serverIP);
            e.printStackTrace();
        }
        while (!socket.isConnected()){
        }

        receiveFile(socket);

    }

    /////////////////////////////////////////////
    void receiveFile(Socket socket) {
        String mFilePath = "/storage/emulated/0/123456/";
        File dirs = new File(mFilePath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        DataInputStream din = null;
        int fileNum = 0;
        long totalSize = 0;
        FileInfo[] fileinfos = null;
        try {
            din = new DataInputStream(new BufferedInputStream(
                    socket.getInputStream()));
            fileNum = din.readInt();
            fileinfos = new FileInfo[fileNum];
            for (int i = 0; i < fileNum; i++) {
                fileinfos[i] = new FileInfo();
                fileinfos[i].mFileName = din.readUTF();
                fileinfos[i].mFileSize = din.readLong();
            }
            totalSize = din.readLong();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG","读取文件数量异常--readInt Exception");
            System.exit(0);
        }
        Log.d("TAG","文件总数量："+fileNum);
        Log.d("TAG","总长度"+totalSize);

        for (FileInfo fileinfo : fileinfos) {
            Log.d("TAG","文件名："+fileinfo.mFileName);
            Log.d("TAG","文件大小："+fileinfo.mFileSize);

        }


        for (int i=0;i<fileNum;i++){
            long length = fileinfos[i].mFileSize;


            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(mFilePath
                        + fileinfos[i].mFileName));
                int bufferSize = 8192;//使用8192，是因为socket自带的缓冲区大小是这个，似乎大小匹配了，传输效率更高
                byte[] buf = new byte[bufferSize];
                while (true) {

                    if (length >= bufferSize) {
                        din.readFully(buf);
                        dos.write(buf, 0, bufferSize);
                        length -= bufferSize;
                    }
                    else {
                        int sm = (int)length;
//                        System.out.println(sm);
                        byte[] smallBuf = new byte[(int)length];
                        din.readFully(smallBuf);
                        dos.write(smallBuf);
                        break;
                    }
                    Message message = new Message();
                    message.arg1 = i+1; //第几个文件
                    message.obj =100-(int)(length*100/fileinfos[i].mFileSize);
                    handler.sendMessage(message);
                }
//                dos.flush();
                dos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        send_file_step1.showClient.setText(mList.size());
    }
    //////////////////////////////////////////////
    private class FileInfo {
        public String mFileName;
        public long mFileSize ;

    }

}
