package com.example.filedownload;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private int ThreadNumber;
    private long TotalProgress=0 ;
    private long EveryThreadDownload;
    private List<Thread>ThreadList=new ArrayList<>();
    ProgressListener progressListener=new ProgressListener() {
        @Override
        public void setProgress(long Progress) {
            TotalProgress+=Progress;
            publishProgress((int)(TotalProgress/ThreadNumber));
        }
    };

    private DownloadListener listener;

    private boolean isCanceled = false;

    private boolean isPaused = false;
    private int lastProgress;
    private SquareProgressBar msquareProgressBar;
    public DownloadTask(DownloadListener listener, int ThreadNumber, SquareProgressBar squareProgressBar) {
        this.listener = listener;
        this.ThreadNumber=ThreadNumber;
        msquareProgressBar=squareProgressBar;

    }

    @Override
    protected Integer doInBackground(String... params) {
        File file = null;
        try {
            long downloadedLength = 0; // 记录已下载的文件长度
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
               publishProgress(100);
               return TYPE_SUCCESS;
            }
            EveryThreadDownload=contentLength/ThreadNumber;
           //假装断点续传
              if(ThreadList.size()!=0)
            {
                System.out.println("暂停唤醒" );
                for (int i=0;i<ThreadList.size();i++)
                {
                    ThreadList.get(i).notify();
                }
            }
            else if (file.exists()) {
                for (int i = 0; i < ThreadNumber; i++) {
                    long star =downloadedLength+ i * EveryThreadDownload;
                    long end = downloadedLength+(i + 1) * EveryThreadDownload - 1;
                    DownloadThread downloadThread = new DownloadThread(downloadUrl, star, end, file, progressListener);
                    downloadThread.start();
                    ThreadList.add(downloadThread);
                }}
                else if(ThreadList.size()!=0)
            {
                for (int i=0;i<ThreadList.size();i++)
                {
                    ThreadList.get(i).notify();
                }
            }
                else if (file.length()!=contentLength) {
                    for (int i = 0; i < ThreadNumber; i++) {
                        long star = i * EveryThreadDownload;
                        long end = (i + 1) * EveryThreadDownload - 1;
                        DownloadThread downloadThread = new DownloadThread(downloadUrl, star, end, file, progressListener);
                        downloadThread.start();
                        ThreadList.add(downloadThread);
                    }
                }
                if(isPaused||isCanceled){
                for (int i=0;i<ThreadList.size();i++)
                {
                    ThreadList.get(i).wait();
                }
                }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }

        if(msquareProgressBar!=null)
        {
            msquareProgressBar.setProgress(lastProgress);
            listener.onProgress(lastProgress);
            if(lastProgress==100)listener.onSuccess();
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }
    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

}