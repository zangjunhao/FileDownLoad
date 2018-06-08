package com.example.filedownload;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ThreadFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 67698 on 2018/6/8.
 */

public class DownloadThread extends Thread {
    private String url;
    private long  star;
    private long end;
    private File file;
    private ProgressListener progressListener;
    public DownloadThread(String url,long star,long end,File file,ProgressListener progressListener)
    {
        Log.d("newThead", "新线程开始下载 ");
        this.url=url;
        this.star=star;
        this.end=end;
        this.file=file;
        this.progressListener=progressListener;
    }

    @Override
    public void run() {
        InputStream is;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                // 断点下载，指定从哪个字节开始下载
                .addHeader("RANGE", "bytes=" + star + "-"+end)
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                RandomAccessFile savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(star);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) !=-1) {
                        total += len;
                        savedFile.write(b, 0, len);
                        progressListener.setProgress(total/(end-star)*100);
                }
                //System.out.println( total/(end-star));
                response.body().close();
                is.close();
                savedFile.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        }
}
