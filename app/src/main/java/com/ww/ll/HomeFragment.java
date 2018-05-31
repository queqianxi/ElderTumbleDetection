package com.ww.ll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.suke.widget.SwitchButton;
import com.ww.ll.base.BaseFragment;
import com.ww.ll.service.TumbleDetectionService;

import org.litepal.LitePalApplication;

/**
 *
 * @author Ww
 * @date 2018/5/16
 */
public class HomeFragment extends BaseFragment{
    private LocationClient mClient;
    private MyLocationListener myLocationListener = new MyLocationListener();
    String address;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        startLocation();
        SwitchButton switchButton = view.findViewById(R.id.switchButton);
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(LitePalApplication.getContext());
        Button button = view.findViewById(R.id.home_button);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(view.isChecked()){
                    Intent startIntent = new Intent(getContext(), TumbleDetectionService.class);
                    getContext().startService(startIntent);
                }else{
                    Intent stopIntent = new Intent(getContext(), TumbleDetectionService.class);
                    getContext().stopService(stopIntent);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSMS(address);
            }
        });
        return view;
    }

    @Override
    protected int setView() {
        return 0;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
    private void sendSMS(String address){
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();

        String name = sharedPreferences.getString("pre_key_name", null);
        String phoneNum = sharedPreferences.getString("pre_key_phone", null);
        String smsContent = name + "在" + address + "走失了！请及时救助！";
        smsManager.sendTextMessage(phoneNum, null, smsContent ,null, null);
        Toast.makeText(LitePalApplication.getContext(), "短信已经发出", Toast.LENGTH_SHORT).show();
    }
    private void startLocation(){
        mClient = new LocationClient(LitePalApplication.getContext());
        LocationClientOption mOption = new LocationClientOption();
        mOption.setScanSpan(3000);
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setIsNeedLocationDescribe(true);
        mClient.setLocOption(mOption);
        mClient.registerLocationListener(myLocationListener);
        mClient.start();
    }
    class  MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuffer sb = new StringBuffer(256);
            // 定位类型
            sb.append(bdLocation.getLocationDescribe());
            address = sb.toString();
        }
    }
}
