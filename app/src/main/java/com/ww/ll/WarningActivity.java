package com.ww.ll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ww.ll.base.BaseActivity;
import com.ww.ll.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**报警活动
 * @author Ww
 */
public class WarningActivity extends BaseActivity {
    private final static String TAG = "WarningActivity";
    private TextView countingView;
    private Dialog dialog;
    private Timer timer;
    private SharedPreferences sharedPreferences;
    //振动器
    private Vibrator vibrator;
    private boolean isVibrate;
    //音视频播放
    private MediaPlayer mediaPlayer;

    private LocationClient mClient;
//    private MyLocationListener myLocationListener = new MyLocationListener();
    String address;
    private final String APIkey = "yC3nDz8qwN5jcYCu5ZeahhXk0gw=";
    private final String Host = "http://api.heclouds.com/devices/" + 24483119 + "/datastreams/";
    private String Lon;
    private String Lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warnning);
        LogUtil.d(TAG,"onCreate executed");
        getLatitude();
        getLongitude();
//        startLocation();
        getCity(Lon,Lat);
        //sendSMS(address);
        showAlertDialog();

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(WarningActivity.this);
        isVibrate = sharedPreferences.getBoolean("pre_key_vibrate", true);
        if(isVibrate){
            startVibrate();
        }
        startAlarm();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    /**
     * 弹窗报警
     */
    private void showAlertDialog() {
        countingView = new TextView(WarningActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                WarningActivity.this);
        builder.setTitle("跌倒警报");
        builder.setView(countingView);
        builder.setMessage("检测到跌倒发生，是否发出警报？");
        builder.setIcon(R.drawable.ic_warning);
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
                dialog.dismiss();
                if(isVibrate){
                    stopVibrate();
                }
                stopAlarm();
                finish();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        countDown();
        dialog.show();
    }

    /**
     *倒计时
     */
    private void countDown() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int countTime = 10;
            @Override
            public void run() {
                if(countTime > 0){
                    countTime --;
                }
                Message msgTime = handler.obtainMessage();
                msgTime.arg1 = countTime;
                handler.sendMessage(msgTime);
            }
        };
        timer.schedule(timerTask, 50, 1000);
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 > 0){
                //动态显示倒计时
                countingView.setText("                         "
                        + msg.arg1 + "秒后自动报警");
            }else{
                //倒计时结束自动关闭
                if(dialog != null){
                    dialog.dismiss();
                    if(isVibrate){
                        stopVibrate();
                    }
                    stopAlarm();
                    sendSMS(address);
                }
                timer.cancel();
                finish();
            }
        }
    };


    /**
     *开始震动
     */
    private void startVibrate(){
        vibrator = (Vibrator) WarningActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 500, 100, 500};
        vibrator.vibrate(pattern, 2);
    }
    /**
     *停止震动
     */
    private void stopVibrate(){
        vibrator.cancel();
    }

    /**
     *开始播放铃声
     */
    private void startAlarm(){
        String ringtone = sharedPreferences.getString("pre_key_alarm" , null);
        Uri ringtoneUri = Uri.parse(ringtone);

        mediaPlayer = MediaPlayer.create(WarningActivity.this, ringtoneUri);
        //设置循环
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    /**
     *停止播放铃声
     */
    private void stopAlarm(){
        mediaPlayer.stop();
    }

    private void sendSMS(String address){
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();

        String name = sharedPreferences.getString("pre_key_name", null);
        String phoneNum = sharedPreferences.getString("pre_key_phone", null);
        String smsContent = name + "在" + address + "摔倒了！请及时救助！";
        smsManager.sendTextMessage(phoneNum, null, smsContent ,null, null);
        Toast.makeText(WarningActivity.this, "短信已经发出", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVibrate();
        stopAlarm();
        finish();
    }
//    private void startLocation(){
//        mClient = new LocationClient(WarningActivity.this);
//        LocationClientOption mOption = new LocationClientOption();
//        mOption.setScanSpan(3000);
//        mOption.setCoorType("bd09ll");
//        mOption.setIsNeedAddress(true);
//        mOption.setOpenGps(true);
//        mOption.setIsNeedLocationDescribe(true);
//        mClient.setLocOption(mOption);
//        mClient.registerLocationListener(myLocationListener);
//        mClient.start();
//    }
//    class  MyLocationListener extends BDAbstractLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            StringBuffer sb = new StringBuffer(256);
//            // 定位类型
//            sb.append(bdLocation.getLocationDescribe());
//            address = sb.toString();
//        }
//    }

    /**
     * 获取经度
     */
    private void getLongitude() {
        sendOkHttpRequestGet(Host + "Longitude",APIkey, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("data");
                    Object aaa = map.get("current_value");
                    Lon = String.valueOf(aaa);
                }
            }
        });
    }

    /**
     * 获取纬度
     */
    private void getLatitude() {
        sendOkHttpRequestGet(Host + "Latitude",APIkey, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("data");
                    Object aaa = map.get("current_value");
                    Lat = String.valueOf(aaa);

                }
            }
        });
    }

    /**
     * 获取定位信息
     * @param longitude
     * @param latitude
     */
    public void getCity(String longitude,String latitude) {
        sendOkHttpRequestGet2("http://api.map.baidu.com/geocoder?output=json&location="+latitude+",%20"+longitude+"","yC3nDz8qwN5jcYCu5ZeahhXk0gw=", new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Map<String,Object> map=( Map<String,Object>) JSON.parseObject(result).get("result");
                    Object aaa = map.get("formatted_address");
                    address = String.valueOf(aaa);

                }
            }
        });
    }

    /**
     * 网络请求方法
     * @param url
     * @param key
     * @param callback
     */
    public  void sendOkHttpRequestGet2(String url, String key,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .header("key", key)
                .build();
        client.newCall(request).enqueue(callback);
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
