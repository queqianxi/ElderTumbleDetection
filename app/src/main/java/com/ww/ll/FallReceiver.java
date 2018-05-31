package com.ww.ll;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ww.ll.util.LogUtil;

/**
 * @author Ww
 */
public class FallReceiver extends BroadcastReceiver {
    private final static String TAG = "FallReceiver";

    public FallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG,"广播运行了");
        Intent intent1 = new Intent(context,WarningActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }



}
