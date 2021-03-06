package com.example.filedownload;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import ch.halcyon.squareprogressbar.SquareProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };
    public int isLoad=0;
    String[] ImageUrl= {
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528023919417&di=6653f6f735682b3e49666d044265d63f&imgtype=0&src=http%3A%2F%2Fnewsimg.5054399.com%2Fuploads%2Fuserup%2F1805%2F1Q54F441W.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528023919635&di=44992f6e25919064af743e6592e68a26&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180520%2Febe503f8236247159f7dfbaf07306d22.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528023919636&di=4ab5676cd080ee1b69745463c5f8636a&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fphoto%2Fm%2Fpublic%2Fp2511041499.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528023919594&di=bd6159b8fb7cb152e86420a2f06a9c90&imgtype=0&src=http%3A%2F%2Fpic2.zhimg.com%2F50%2Fv2-7b77af1913b2a1fb4fb9e1c8e5384fe1_hd.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528024103654&di=a10a23c1335810e90ee1d43a28e5e5dc&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2F874662fc04181e42837cb45459d871648e0ca427.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SquareProgressBar squareProgressBar1=findViewById(R.id.sprogressbar1);
        SquareProgressBar squareProgressBar2=findViewById(R.id.sprogressbar2);
        SquareProgressBar squareProgressBar3=findViewById(R.id.sprogressbar3);
        SquareProgressBar squareProgressBar4=findViewById(R.id.sprogressbar4);
        SquareProgressBar squareProgressBar5=findViewById(R.id.sprogressbar5);
        SquareProgressBar squareProgressBar6=findViewById(R.id.sprogressbar6);
        Button redownload=(Button)findViewById(R.id.redownload);
        Button pruse=(Button)findViewById(R.id.pruse);
        squareProgressBar1.setImage(R.drawable.one);
        squareProgressBar2.setImage(R.drawable.two);
        squareProgressBar3.setImage(R.drawable.three);
        squareProgressBar4.setImage(R.drawable.four);
        squareProgressBar5.setImage(R.drawable.five);
        squareProgressBar6.setImage(R.drawable.six);
        squareProgressBar1.setOnClickListener(this);
        squareProgressBar2.setOnClickListener(this);
        squareProgressBar3.setOnClickListener(this);
        squareProgressBar4.setOnClickListener(this);
        squareProgressBar5.setOnClickListener(this);
        squareProgressBar6.setOnClickListener(this);
        redownload.setOnClickListener(this);
        pruse.setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent); // 启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝就无法下载到本地", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sprogressbar1:
                SquareProgressBar squareProgressBar1 = findViewById(R.id.sprogressbar1);
                    squareProgressBar1.showProgress(true);
                    downloadBinder.getProgressUi(squareProgressBar1);
                    downloadBinder.startDownload(ImageUrl[0]);

                    Toast.makeText(this, "开始下载第一个", Toast.LENGTH_SHORT).show();

                    break;
            case R.id.sprogressbar2:
                SquareProgressBar squareProgressBar2 = findViewById(R.id.sprogressbar2);
                   squareProgressBar2.showProgress(true);
                downloadBinder.getProgressUi(squareProgressBar2);
                   downloadBinder.startDownload(ImageUrl[1]);
                   Toast.makeText(this, "开始下载第二个", Toast.LENGTH_SHORT).show();

               break;
            case R.id.sprogressbar3:
                SquareProgressBar squareProgressBar3 = findViewById(R.id.sprogressbar3);

                    squareProgressBar3.showProgress(true);
                downloadBinder.getProgressUi(squareProgressBar3);
                    downloadBinder.startDownload(ImageUrl[2]);
                    Toast.makeText(this, "开始下载第三个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sprogressbar4:
                SquareProgressBar squareProgressBar4 = findViewById(R.id.sprogressbar4);
                    squareProgressBar4.showProgress(true);
                downloadBinder.getProgressUi(squareProgressBar4);
                    downloadBinder.startDownload(ImageUrl[3]);
                    Toast.makeText(this, "开始下载第四个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sprogressbar5:
                SquareProgressBar squareProgressBar5 = findViewById(R.id.sprogressbar5);
                    squareProgressBar5.showProgress(true);
                downloadBinder.getProgressUi(squareProgressBar5);
                    downloadBinder.startDownload(ImageUrl[4]);
                    Toast.makeText(this, "开始下载第五个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sprogressbar6:
                Toast.makeText(this, "萱姐姐天下第一", Toast.LENGTH_SHORT).show();
                break;
            case R.id.redownload:
                downloadBinder.cancelDownload();
                break;
            case R.id.pruse:
                downloadBinder.pauseDownload();
                break;
        }
    }
}
