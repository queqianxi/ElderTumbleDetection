package com.ww.ll.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理类
 * @author Ww
 * @date 2018/5/10
 */
public class ActivityStackManager {
    private static Stack<Activity> activityStack;
    private static ActivityStackManager instance;

    private ActivityStackManager() {
    }

    public synchronized static ActivityStackManager getActivityStackManager() {
        if (instance == null) {
            instance = new ActivityStackManager();
        }
        return instance;
    }

    /**
     * 关闭activity
     * finish the activity and remove it from stack.
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获取当前的Activity
     * get the current activity.
     *
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取最后一个的Activity
     * get the first activity in the stack.
     *
     */
    public Activity firstActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.firstElement();
        return activity;
    }


    /**
     * 添加activity到Stack
     * add the activity to the stack.
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除所有Activity
     * remove all activity.
     */
    public void popAllActivity() {
        if (activityStack == null) {
            return;
        }
        while (true) {
            if (activityStack.empty()) {
                break;
            }
            Activity activity = currentActivity();
            popActivity(activity);
        }
    }

    /**
     * 移除所有Activity但保持目前的Activity
     * remove all activity but keep the current activity.
     */
    public void popAllActivityWithOutCurrent() {
        Activity activity = currentActivity();
        while (true) {
            if (activityStack.size() == 1) {
                break;
            }
            if (firstActivity() == activity) {
                break;
            } else {
                popActivity(firstActivity());
            }
        }
    }
}
