package com.example.yangquan.myapplication;

import android.app.AlertDialog;
import android.content.Context;
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

    public SocketClient(Context context,String serverIP,int port){
        mContext = context;
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


//        Toast.makeText(mContext, "接收进度", Toast.LENGTH_SHORT).show();
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

    private void ReceiveFile(Socket socket){
        try{

            InputStream nameStream = socket.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);

            String length = br.readLine();
            int len = Integer.parseInt(length);

            Log.i("RECEV File:", "文件数量:" + len);

            InputStream dataStream = null;
            FileOutputStream file = null;
            for (int i=0;i<len;i++){
            String fileName = br.readLine();
                Log.i("RECEV File:", "正在接收:" + fileName);

                dataStream = socket.getInputStream();
                String savePath = "/storage/emulated/0/123456" + "/" + fileName;
                file = new FileOutputStream(savePath, false);
                byte[] buffer = new byte[1024];
                int size = -1;
                while ((size = dataStream.read(buffer)) != -1){
                    file.write(buffer, 0 ,size);
                }
                Log.i("RECEV File:", fileName + "接收完成");

                file.flush();
            }
            br.close();
            streamReader.close();
            nameStream.close();
            file.close();
            dataStream.close();
            socket.close();

        }catch(Exception e){
            Log.i("RECEV File:", "接收错误:\n" + e.getMessage());
        }
    }

}
