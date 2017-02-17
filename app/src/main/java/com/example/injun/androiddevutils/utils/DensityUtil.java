package com.example.injun.androiddevutils.utils;

/**
 * Created by injun on 2017/2/17.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;

/**
 * Created by dev on 2015/12/2.
 */
public class DensityUtil
{
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context)
    {
        if (context != null)
        {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        return 0;
    }

    public static int getScreenHeight(Context context)
    {
        if (context != null)
        {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    public static int[] getAreaOne(Activity activity)
    {
        int[] result = new int[2];

        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        result[0] = outP.x;
        result[1] = outP.y;

        return result;
    }

    public static int[] getAreaTwo(Activity activity)
    {
        int[] result = new int[2];

        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        System.out.println("top:" + outRect.top + " ; left: " + outRect.left);
        result[0] = outRect.width();
        result[1] = outRect.height();

        return result;
    }

    public static int[] getAreaThree(Activity activity)
    {
        int[] result = new int[2];
        // 用户绘制区域
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        result[0] = outRect.width();
        result[1] = outRect.height();
        // end
        return result;
    }

    /**
     * 获取应用区域高度
     *
     * @author ysg
     * created at 2016/7/8 16:25
     */
    public static int getDisplayHeight(Context context)
    {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = getStatusBarHeight(context);
        //得到屏幕的整个高度
        int mFullDisplayHeight = ((Activity) context).getWindowManager().getDefaultDisplay()
                .getHeight();

//        int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentViewTop - statusBarHeight;

        //得到可显示屏幕高度
        int mDisplayHeight = mFullDisplayHeight - statusBarHeight;
        // Log.d("TEST", "status bar height:" + statusBarHeight);
        // Log.d("TEST", "mFullDisplayHeight====" + mFullDisplayHeight);
        return mDisplayHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @author ysg
     * created at 2016/7/8 16:26
     */
    public static int getStatusBarHeight(Context context)
    {

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
