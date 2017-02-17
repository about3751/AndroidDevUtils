package com.example.injun.androiddevutils.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by injun on 2017/2/17.
 */


public class DateUtils
{

    /**
     * 制定转化的格式
     *
     * @return
     */
    public static String getDateFromLongFormat(long sstime)
    {

        Date date = new Date(sstime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd    HH:mm");
        return simpleDateFormat.format(date);

    }


    /**
     * 将毫秒值转化为时间
     *
     * @return
     */
    public static String getDateFromLong(long sstime, int type)
    {
        Date date = new Date(sstime);
        SimpleDateFormat sdf = null;
        if (0 == type)
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        return sdf.format(date);
    }


    /**
     * 方法说明:输入日期与今天的日期比较
     * <br/>String b_date 大日期
     * <br/>String s_date 小日期
     * <br/>返回日期文件名：20080611151112
     */
    public static String getDate_NowDate(String date)
    {
        String str = "yes";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            //System.out.println("输入日期："+formatter.parse(date));
            if (date.length() == 10)
            {
                date = date + " 00:00:00";
            }
            long date_long = formatter.parse(date).getTime();
            Date now_date = new Date();
            //System.out.println("今天日期："+now_date);
            long now_date_long = now_date.getTime();
            if (date_long > now_date_long)
            {
                str = "yes";
            } else
            {
                str = "no";
            }
        } catch (ParseException e)
        {
            System.out.println("类:DateControl 方法:getBDate_SDate 执行:输入日期与今天的日期比较 时发生:ParseException异常");
        }
        //System.out.println(str);
        return str;
    }

    /**
     * 方法说明:生成日期文件名
     * <br/>
     * <br/>返回日期文件名：20080611151112
     */
    public static String getDateFileName()
    {
        String FileName = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssS");
        Date date = new Date();
        FileName = formatter.format(date);
        return FileName;
    }

    /**
     * 方法说明:取得当前日期格式
     * <br/>参数:i=0,结果 yyyy-MM-dd HH:mm:ss
     * <br/>参数:i=1,结果 yyyy-MM-dd
     * <br/>参数:i=2,结果 yyyy年MM月dd日 HH:mm:ss
     * <br/>参数:i=3,结果 yyyy年MM月dd日
     * <br/>参数:i=4,结果 yyyy-MM
     * <br/>返回日期：
     */
    public static String getDateTime(int i)
    {
        String nowdate = null;
        String Type = "yyyy-MM-dd HH:mm:ss";
        if (i == 0)
        {
            Type = "yyyy-MM-dd HH:mm:ss";
        } else if (i == 1)
        {
            Type = "yyyy-MM-dd";
        } else if (i == 2)
        {
            Type = "yyyy年MM月dd日 HH:mm:ss";
        } else if (i == 3)
        {
            Type = "yyyy年MM月dd日";
        } else if (i == 4)
        {
            Type = "yyyy-MM";
        } else if (i == 5)
        {
            Type = "yyyyMMdd";
        } else if (i == 6)
        {
            Type = "yyyyMMddHHmmss";
        }


        SimpleDateFormat formatter = new SimpleDateFormat(Type);
        Date date = new Date();
        nowdate = formatter.format(date);
        return nowdate;
    }

    /**
     * 方法说明:格式化日期
     * <br/>参数:date
     * <br/>参数:i=0,结果 yyyy-MM-dd HH:mm:ss
     * <br/>参数:i=1,结果 yyyy-MM-dd
     * <br/>参数:i=2,结果 yyyy年MM月dd日 HH:mm:ss
     * <br/>参数:i=3,结果 yyyy年MM月dd日
     * <br/>参数:i=4,结果 yyyyMMdd
     * <br/>返回日期：
     */
    public static String getFormatDate(String date, int i)
    {
        String nowdate = null;
        String Type = "yyyy-MM-dd HH:mm:ss";
        if (i == 0)
        {
            Type = "yyyy-MM-dd HH:mm:ss";
        } else if (i == 1)
        {
            Type = "yyyy-MM-dd";
        } else if (i == 2)
        {
            Type = "yyyy年MM月dd日 HH:mm:ss";
        } else if (i == 3)
        {
            Type = "yyyy年MM月dd日";
        } else if (i == 4)
        {
            Type = "yyyyMMdd";
        } else if (i == 5)
        {
            Type = "MM月dd日";
        } else if (i == 6)
        {
            Type = "MM月dd号";
        }
        if (date == null) return "";
        try
        {
            Date date_gsh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(Type);
            nowdate = formatter.format(date_gsh);

        } catch (ParseException e)
        {
            System.out.println("类:DateControl 方法:getFormatDate 执行:转换日期格式 发生:ParseException异常");
        }

        return nowdate;
    }

    /**
     * 方法说明：获得本周的 周一 日期
     * <br/>参数：Date date
     * <br/>返回值：本周的 周一 日期
     */
    public static String getMonday(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 方法说明：获得本周的 周日 日期
     * <br/>参数：Date date
     * <br/>返回值：本周的 周日 日期
     */
    public static String getSunday(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + 6);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 方法说明：把18.5小时转换成18小时30分钟
     * <br/>参数：小数时间
     * <br/>返回值：十分秒的时间
     */
    public static String Hms(float time)
    {
        String _time = "";
        float getTime = time * 60;
        try
        {
            DecimalFormat df = new DecimalFormat("00000");
            String itime = df.format(getTime);

            int ih = Integer.parseInt(itime) / 60;

            int is = Integer.parseInt(itime) % 60;

            _time = ih + "小时" + is + "分钟";

            if (ih < 1)
            {
                _time = is + "分钟";
            } else if (is < 1)
            {
                _time = ih + "小时";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return _time;
    }

    /**
     * 方法说明：把秒数转换成18小时30分钟
     * <br/>参数：小数时间
     * <br/>返回值：十分秒的时间
     */
    public static String Hm(float time)
    {
        String _time = "";
        //float getTime = time*60;
        try
        {
            DecimalFormat df = new DecimalFormat("00000");
            String itime = df.format(time);

            int ih = Integer.parseInt(itime) / 3600;

            int is = Integer.parseInt(itime) % 3600 / 60;

            _time = ih + "小时" + is + "分钟";

            if (ih < 1)
            {
                _time = is + "分钟";
            } else if (is < 1)
            {
                _time = ih + "小时";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return _time;
    }

    /**
     * 方法说明：计算两个时间差用秒数表示
     * <br/>参数：两个时间
     * <br/>返回值：十分秒的时间
     */
    public static long getMiaoShu(String s_date, String e_date)
    {
        long miaoshu = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (s_date.length() == 10)
        {
            s_date = s_date + " 00:00:00";
        }
        if (e_date.length() == 10)
        {
            e_date = e_date + " 23:59:59";
        }
        try
        {
            long s_date_long = formatter.parse(s_date).getTime();
            long e_date_long = formatter.parse(e_date).getTime();

            miaoshu = (e_date_long - s_date_long) / 1000;

            //如果有余数
            if (miaoshu % 2 == 1)
            {
                miaoshu = miaoshu + 1;
            }

        } catch (ParseException e)
        {
            System.out.println("类:DateControl 方法:getMiaoShu 执行:计算两个时间差用秒数表示 发生:ParseException异常");
        }
        return miaoshu;
    }

    /**
     * 得到日期的格式化字符串
     *
     * @param date      日期Date
     * @param formatStr 字符格式（yyyy）
     * @return
     */
    public static String getFormatDateStr(Date date, String formatStr)
    {

        if (date == null)
        {
            date = new Date();
        }
        String nowdate = null;

        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        nowdate = formatter.format(date);
        return nowdate;
    }

    public static void main(String[] args)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //c.add(Calendar.HOUR, 2);
        c.add(Calendar.DATE, 2);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(formatter.format(c.getTime()));
    }

    /**
     * 得到昨日
     */
    public static String getYesterday(String day) throws Exception
    {
        // 得到昨日
        String yesterday = "";
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = myFmt.parse(day);

        long msel = myDate.getTime() - 60 * 60 * 24 * 1000;

        myDate.setTime(msel);

        yesterday = myFmt.format(myDate);

        return yesterday;
    }
}


