package com.hjl.commonlib.utils;

import android.os.Looper;

import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        long millis = System.currentTimeMillis();
        return sdf.format(millis);
    }



    public static String getDateFromMill(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(millis);
    }

    public static String getHttpRequestTime(long millis){
        Looper.prepare();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault());
        return sdf.format(millis);
    }

    public static String getTimeFromMill(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(millis);
    }

    public static String getMusicTime(int millis){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return sdf.format(millis);
    }

    public static boolean isNeedUpdateUrl(String date){
        long targetDateMill = date2Mill(date);
        long currentDateMill = System.currentTimeMillis();
        long updateInterval = 8 * 60 * 60 * 1000 ; // 暂时设定8小时 8 * 60 * 60 * 1000

        return currentDateMill - targetDateMill > updateInterval;



    }

    public static long date2Mill(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;

    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param timeStamp 需要格式化的时间 时间戳 1482993869868  如"2014-07-14 19:01:45"
     * @param //        输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *                  <p/>如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(long timeStamp) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (timeStamp > 0) {
            try {
                Date tDate = new Date(timeStamp);//new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy", Locale.CHINA);
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                SimpleDateFormat halfDf = new SimpleDateFormat(BaseApplication.getApplication().getString(R.string.mm_mnth_dd_dy_hh_mm), Locale.CHINA);
                long dTime = today.getTime() - tDate.getTime();
                if (tDate.before(thisYear)) {
                    display = new SimpleDateFormat(BaseApplication.getApplication().getString(R.string.yyyy_yr_mm_mnth_dd_), Locale.CHINA).format(tDate);
                } else {
                    if (dTime < tMin) {
                        display = BaseApplication.getApplication().getString(R.string.just);
//                        display = new SimpleDateFormat("HH:mm", Locale.CHINA).format(tDate);
                    } else if (dTime < tHour) {
                        display = (int) Math.ceil(dTime / tMin) + BaseApplication.getApplication().getString(R.string.minutes_ago);
//                        display = new SimpleDateFormat("HH:mm", Locale.CHINA).format(tDate);
                    } else if (dTime < tDay && tDate.after(yesterday)) {
                        display = (int) Math.ceil(dTime / tHour) + BaseApplication.getApplication().getString(R.string.hours_ago);
//                        display = new SimpleDateFormat("HH:mm", Locale.CHINA).format(tDate);
                    } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                        display = "昨天 " + getStringTime(timeStamp, "HH:mm"); //+ new SimpleDateFormat("HH:mm").format(tDate);
                    } else {
                        display = halfDf.format(tDate);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    public static String getStringTime(long time, String type) {
        return new SimpleDateFormat(type, Locale.CHINA).format(new Date(time));
    }

}
