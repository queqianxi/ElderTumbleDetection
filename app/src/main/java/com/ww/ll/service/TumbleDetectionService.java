package com.ww.ll.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSON;
import com.suke.widget.SwitchButton;
import com.ww.ll.FallReceiver;
import com.ww.ll.MainActivity;
import com.ww.ll.R;
import com.ww.ll.util.LogUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**摔倒检测服务
 * @author Ww
 */
public class TumbleDetectionService extends Service {
    private final static String TAG = "TumbleDetectionService";
    private final String APIkey = "yC3nDz8qwN5jcYCu5ZeahhXk0gw=";
    private final String Host = "http://api.heclouds.com/devices/" + 24483119 + "/datastreams/";
    private final int FELL = 0;
    private Double svm = 0.0;
    boolean highLow;
    private Double height = 0.0;
    private FallReceiver receiver;
    private boolean pushthread = false;
    public TumbleDetectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"onCreate executed启动了");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.FELL");
        receiver = new FallReceiver();
        this.registerReceiver(receiver, intentFilter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG,"onStartCommand executed启动了");
//        DetectThread detectThread = new DetectThread();
//        detectThread.start();
        getPushThread();
        return super.onStartCommand(intent, flags, startId);
    }

    //循环请求的线程
    public void getPushThread() {
        pushthread = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pushthread) {
                    pushthread = false;
                    Message msg = handler.obtainMessage();
                    msg.what = FELL;
                    handler.sendMessage(msg);
                    stopSelf();
//                  这里面判断数据食肉达到阈值
//                    try {
//                        if (svm <15) {
//                            Thread.sleep(500);
//                            getSvm();
//                            getHighLow();
//                            getHeight();
//                        }else if (svm>15 && svm <20){
//                            if (height < 1.5 && highLow){
//                                Thread.sleep(500);
//                                getSvm();
//                                getHeight();
//                                getHighLow();
//                            }
//                        }else {
//                    达到的话，发送摔倒信息
//                            pushthread = false;
//                            Message msg = handler.obtainMessage();
//                            msg.what = FELL;
//                            handler.sendMessage(msg);
//                            LogUtil.d(TAG, "svm=" + svm);
//
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();
    }

    /**
     * 获取svm值
     */
    private void getSvm() {
        sendOkHttpRequestGet(Host + "svm",APIkey, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("data");
                    Object aaa = map.get("current_value");
                    String bbb = String.valueOf(aaa);
                    svm = Double.valueOf(bbb);
                }
            }
        });
    }
    /**
     * 获取高低电平值
     */
    private void getHighLow() {
        sendOkHttpRequestGet(Host + "highLow",APIkey, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("data");
                    Object aaa = map.get("current_value");
                    int bbb = Integer.parseInt(String.valueOf(aaa));
                    if (bbb == 0){
                        highLow = true;
                    }else {
                        highLow = false;
                    }
                }
            }
        });
    }
    /**
     * 获取高度差值
     */
    private void getHeight() {
        sendOkHttpRequestGet(Host + "height",APIkey, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("data");
                    Object aaa = map.get("current_value");
                    String bbb = String.valueOf(aaa);
                    height = Double.valueOf(bbb);
                }
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case FELL:
                    //报警
//                    showAlertDialog();
                    Intent intent = new Intent("com.broadcast.FELL");
                    sendBroadcast(intent);
                    LogUtil.d(TAG,"发出了广播");
                    break;
                default:
                    break;
            }

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        LogUtil.d(TAG,"onDestroy executed启动了");
    }
    public  void sendOkHttpRequestGet(String url, String APIKey,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("api-key", APIKey)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
