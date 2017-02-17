package com.example.injun.androiddevutils.utils;

/**
 * Created by injun on 2017/2/17.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * AppActivity管理类
 * Created by about on 2016-12-11.
 */

public class AppManager
{

    private AppManager()
    {
    }

    private static AppManager mAppmanager;

    private static Stack<Activity> mActivityStack;

    public static AppManager getAppManager()
    {
        if (mAppmanager == null)
        {
            synchronized (AppManager.class)
            {
                if (mAppmanager == null)
                {
                    mAppmanager = new AppManager();
                }
            }
        }
        return mAppmanager;
    }

    public void addActivity(Activity activity)
    {
        if (mActivityStack == null)
        {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity()
    {
        return mActivityStack.lastElement();
    }

    /**
     * 结束当前activity
     */
    public void finishActivity()
    {
        finishActivity(mActivityStack.lastElement());
    }

    /**
     * 结束指定Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity)
    {
        if (activity != null)
        {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束制定类名的Activity
     *
     * @param activity
     */
    public void finishActivity(Class<?> activity)
    {
        for (Activity a : mActivityStack)
        {
            if (a.getClass().equals(activity))
            {
                finishActivity(a);
            }
        }
    }

    public void finishActivity(boolean isFinishAll)
    {
        if (isFinishAll)
        {
            for (Activity activity : mActivityStack)
            {
                activity.finish();
            }
            mActivityStack.clear();
        }
    }

    public void Exit(Context context)
    {
        try
        {

            finishActivity(true);
            ActivityManager activityManagerCompat = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManagerCompat.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e)
        {
            Log.i("LCZ", e.getMessage());
        }
    }

}
