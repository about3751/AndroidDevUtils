package com.example.injun.androiddevutils.utils;

/**
 * Created by injun on 2017/2/17.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

/**
 * app手机系统自带的公共方法
 * Created by tarena on 2016/6/1.
 */
public class AppUtils
{


    /**
     * 读取assets资源文件夹下的图片
     *
     * @param mContext 上下文 实例化AssetsManage
     * @param fileName 图片名称
     * @return
     */
    public static Bitmap getImageFromAssets(Context mContext, String fileName)
    {

        Bitmap bitmap = null;
        // Ass管理器
        AssetManager manager = mContext.getAssets();
        InputStream is = null;
        try
        {
            is = manager.open("images/" + fileName);
            // 将is转换成bitmap
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 读取assets资源文件夹下的图片
     *
     * @param activity 上下文 实例化AssetsManage
     * @param fileName 图片名称
     * @return
     */
    public static Bitmap getImageFromAssets(Activity activity, String fileName)
    {
        Bitmap bitmap = null;
        // Ass管理器
        AssetManager manager = activity.getResources().getAssets();
        InputStream is = null;
        try
        {
            is = manager.open("images/" + fileName);
            // 将is转换成bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 调用手机联系人
     *
     * @param context
     */
    public static void callContact(Fragment context)
    {
        //联系人选择
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        context.startActivityForResult(intent, PICK_CONTACT);
    }

    /**
     * 调用手机联系人
     *
     * @param context
     */
    public static void callContact(Activity context)
    {
        //联系人选择
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        context.startActivityForResult(intent, PICK_CONTACT);
    }

    /**
     * 检查是否开启GPS
     *
     * @param context
     * @return
     */
    public static boolean checkIsGPSOpen(Context context)
    {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 解析选中的联系人
     *
     * @param act
     * @param data
     * @return 0:联系人；1：联系人手机号
     */
    public static String[] analyContactData(Activity act, Intent data)
    {

        String[] contactDataArray = new String[2];

        ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info : infos)
        {
            if (!"com.yzb.card.king".equals(info.processName))
                continue;

            if (act.checkPermission(Manifest.permission.READ_CONTACTS, info.pid, info.uid) == PackageManager.PERMISSION_GRANTED)
            {
                Intent query = new Intent(Intent.ACTION_MAIN);
                query.addCategory("android.intent.category.LAUNCHER");

                Uri contactData = data.getData();
                Cursor cursor = act.managedQuery(contactData, null, null, null, null);
                int contactIdIndex = 0;
                if (cursor.getCount() > 0)
                {
                    contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                }

                if (cursor.moveToFirst())
                {
                    String contactId = cursor.getString(contactIdIndex);
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String phoneNumber = null;
                    if (hasPhone.equalsIgnoreCase("1"))
                    {
                        Cursor phones = act.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phones.moveToNext())
                        {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber = phoneNumber.replaceAll("-", "");
                            phoneNumber = phoneNumber.replaceAll(" ", "");

                            if (phoneNumber.length() > 11)
                            {
                                ToastCustom.sendDialog(act, act.getWindow().peekDecorView(), "手机号有误", 140);
                                return contactDataArray;
                            }

                            String linkMan = name;

                            String linkPhone = phoneNumber;

                            contactDataArray[0] = linkMan;

                            contactDataArray[1] = linkPhone;

                            break;
                        }
                        phones.close();
                        return contactDataArray;
                    }
                }
            } else
            {
                ToastCustom.sendDialog(act, act.getWindow().peekDecorView(), "此应用没有配置读取联系人权限", 140);
            }
        }

        return contactDataArray;
    }

    /**
     * 对金额处理
     *
     * @param l
     * @return
     */
    public static String handleNumberStr(double l)
    {

        StringBuffer sb = new StringBuffer();
        if (l >= 0 && l < 10000)
        {

            BigDecimal b = new BigDecimal(l);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            String lTemp = String.valueOf(f1);
            ;

            String[] str = lTemp.split("\\.");

            if (str.length == 2)
            {


                int xiaoshu = Integer.parseInt(str[1]);

                if (xiaoshu > 0)
                {
                    sb.append(lTemp);
                } else
                {
                    sb.append((int) l + "");
                }

            }


        } else if (l >= 10000 && l < 100000000)
        {

            double a = l / 10000;

            BigDecimal b = new BigDecimal(a);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            String lTemp = String.valueOf(f1);
            ;
            String[] str = lTemp.split("\\.");

            if (str.length == 2)
            {
                int xiaoshu = Integer.parseInt(str[1]);

                if (xiaoshu > 0)
                {

                    sb.append(lTemp + "万");

                } else
                {
                    sb.append(str[0] + "万");
                }
            }


        } else if (l >= 100000000)
        {


            double a = l / 100000000;


            BigDecimal b = new BigDecimal(a);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            String lTemp = String.valueOf(f1);
            ;

            String[] str = lTemp.split("\\.");

            if (str.length == 2)
            {
                int xiaoshu = Integer.parseInt(str[1]);
                if (xiaoshu > 0)
                {

                    sb.append(lTemp + "亿");

                } else
                {
                    sb.append(str[0] + "亿");
                }
            }

        }

        return sb.toString();
    }

    /**
     * 隐藏软键盘显示光标
     *
     * @param activity
     * @param et
     */
    public static void hideSoftShowCursor(Activity activity, EditText et)
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT <= 10)
        {
            // 点击EditText，屏蔽默认输入法
            et.setInputType(InputType.TYPE_NULL); // editText是声明的输入文本框。
        } else
        {
            // 点击EditText，隐藏系统输入法
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try
            {
                Class<EditText> cls = EditText.class;
                Method method = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);// 4.0的是setShowSoftInputOnFocus，4.2的是setSoftInputShownOnFocus
                method.setAccessible(false);
                method.invoke(et, false); // editText是声明的输入文本框。
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    LogUtil.LCi(" 当前网络链接状态 ");
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        LogUtil.LCi("  当前网络链接失败状态 ");
        return false;
    }


    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context)
    {
        try
        {

            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex)
        {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
        // return null;
    }

    /**
     * 隐藏键盘
     *
     * @param act
     * @param edit
     */
    public static void hideSoftWindow(Activity act, EditText edit)
    {

        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * popupwindow弹出后背景明暗设置
     *
     * @param context
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}

