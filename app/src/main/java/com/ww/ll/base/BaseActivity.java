package com.ww.ll.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ww.ll.R;
import com.ww.ll.util.ActivityStackManager;
import com.ww.ll.util.LogUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Ww
 * @date 2018/5/10
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BaseActivity";
    /**
     * back fragment list
     */
    private ArrayList<BaseFragment> fragments;
    /**
     * current fragment
     */
    private BaseFragment fragment;
    /**
     * 界面里FrameLayout布局ID
     */
    private final int container = R.id.container;

    /**
     * context
     **/
    protected Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG, "--->onCreate()");
        initView();
        initData();
        ctx = this;
        ActivityStackManager.getActivityStackManager().pushActivity(this);
    }

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定事件
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * 跳转Activity
     * skip Another Activity
     *
     * @param activity
     * @param cls
     */
    public static void actionStart(Activity activity,
                                           Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 退出应用
     * called while exit app.
     */
    public void exitLogic() {
        //remove all activity.
        ActivityStackManager.getActivityStackManager().popAllActivity();
        //system exit.
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(TAG, "--->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "--->onDestroy()");
        ActivityStackManager.getActivityStackManager().popActivity(this);
    }

    /**
     * 提示操作
     * @param message
     * @param mills
     */
    public void showMessage(final String message, final int mills) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //进行UI操作
                Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
                showMyToast(toast,mills);
            }
        });
    }
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }
    /**
     * 添加fragment.
     *
     * @param fragment       the new fragment to shown.
     * @param addToBackStack if it can back.
     */
    public void addContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }


        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    /**
     * replace方式添加
     * @param fragment
     * @param addToBackStack
     */
    public void replaceContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    public void backTopFragment() {
        if (fragments != null && fragments.size() > 1) {
            removeContent();
            backTopFragment();
        }
    }

    /**
     * set current fragment.
     */
    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    /**
     * get the current fragment.
     *
     * @return current fragment
     */
    public BaseFragment getFirstFragment() {
        return fragment;
    }

    /**
     * get amount of fragment.
     *
     * @return amount of fragment
     */
    public int getFragmentNum() {
        return fragments != null ? fragments.size() : 0;
    }

    /**
     * clear fragment list
     */
    protected void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }

    /**
     * remove previous fragment
     */
    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }

    /**
     * init fragment list.
     */
    private void initFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    /**
     * remove current fragment and back to front fragment.
     */
    public void removeContent() {
        removePrevious();
        setFragment();

        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * remove all fragment from back stack.
     */
    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
