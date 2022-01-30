package com.service.microjc.Activity.App.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.service.microjc.Activity.Jw.RoomActivity;
import com.service.microjc.stType.TimeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CustomUtils {
    private static Timer timer = null;
    private static TimerTask task = null;

    /**
     * 公共函数，获取app版本号
     * */
    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取课程表源码，存为本地txt文件
     * */

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String str, String filePath, String fileName) {
        deleteSingleFile(filePath+fileName);
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = str + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(File file) {
        String content = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream inStream = new FileInputStream(file);
                    InputStreamReader inputReader
                            = new InputStreamReader(inStream, StandardCharsets.UTF_8);
                    BufferedReader buffReader = new BufferedReader(inputReader);
                    String line;
                    //分行读取
                    while ((line = buffReader.readLine()) != null) {
                        content += line + "\n";
                    }
                    inStream.close();//关闭输入流
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return content;
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
//                Toast.makeText("test", "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
//            Toast.makeText(MyApplication.getContext(), "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 指定延迟运行时间
     * */
    public static void runDelayed(Runnable runnable, long time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }, time);
    }


    public static void runTime(Boolean stop){
        final int[] i = {0};

        //防止多次点击开启计时器
        if(timer == null){
            timer = new Timer();
        }
        if (task == null){

            task = new TimerTask() {
                @Override
                public void run() {
                    i[0]++;
                    //每次需要执行的代码放到这里面。
                    if (1<=i[0] && i[0]<=3){
                        WaitDialog.show("正在查询中...("+i[0]+"s)");
                    }else if (3<i[0] && i[0]<=5){
                        WaitDialog.show("第一次查询耗时较长("+i[0]+"s)");
                    }else if (5<i[0] && i[0]<=8){
                        WaitDialog.show("请耐心等待("+i[0]+"s)");
                    }else if (8<i[0] && i[0]<=12){
                        WaitDialog.show("如果出错，我会提醒你("+i[0]+"s)");
                    }else if (12<i[0] && i[0]<=30){
                        WaitDialog.show("这可能需要15秒左右("+i[0]+"s)");
                    }
                }
            };

            timer.schedule(task, 0, 1000);


        }else if (stop){
            timer.cancel();
            timer = null;
            task = null;
            WaitDialog.dismiss();
        }

//以下是几种调度task的方法：
//time为Date类型：在指定时间执行一次。
        //timer.schedule(task, time);
//firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
        //timer.schedule(task, 0, 1000);    //较为常用 ,上面的示例就是这个方法
//delay 为long类型：从现在起过delay毫秒执行一次。
        //timer.schedule(task, delay);
    }


    /**
     * 将Long或Date类型时间戳转化成 2021-08-10 String格式
     * */
    public static TimeInfo LongToString(Object o){
        TimeInfo timeInfo = new TimeInfo();

        String ymd;//20210824
        String y_m_d;//2021-08-24
        String ymdString;//2021年08月24日
        String Week = null;//周二
        String y;//年
        String m;//月
        String d;//日
        String h;//时
        String min;//分
        String s;//秒
        String hm;//时分 0943
        String hmString;//时：分 09：43
        //将long转换成Calendar类型，获取int 型的 年 月 日，显示在UI
        //Date也可以获取，但是工具提示已经过时
        Calendar calendar = Calendar.getInstance();

        if (o instanceof Long){
            calendar.setTimeInMillis((Long) o);
        }else if (o instanceof Date){
            calendar.setTime((Date) o);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        //年
        y = String.valueOf(year);
        //月
        if (month < 9){
            m =  "0"+(month+1);
        }else {
            m = String.valueOf(month+1);
        }
        //日
        if (day < 10){
            d = "0"+day;
        }else {
            d = String.valueOf(day);
        }
        //时
        if (hour <10){
            h = "0"+hour;
        }else {
            h = String.valueOf(hour);
        }
        //分
        if (minute <10){
            min = "0"+minute;
        }else {
            min = String.valueOf(minute);
        }
        //秒
        if (second <10){
            s = "0"+second;
        }else {
            s = String.valueOf(second);
        }

        switch (week-1){
            case 1:
                Week = "周一";
                break;
            case 2:
                Week = "周二";
                break;
            case 3:
                Week = "周三";
                break;
            case 4:
                Week = "周四";
                break;
            case 5:
                Week = "周五";
                break;
            case 6:
                Week = "周六";
                break;
            case 7:
                Week = "周日";
                break;
        }

        ymd = y+m+d;
        y_m_d = y+"-"+m+"-"+d;
        ymdString = y+"年"+m+"月"+d+"日";
        hm = h+min;
        hmString = h+":"+min;

        timeInfo.setY(y);
        timeInfo.setM(String.valueOf(month+1));
        timeInfo.setMm(m);
        timeInfo.setD(String.valueOf(day));
        timeInfo.setDd(d);
        timeInfo.setH(h);
        timeInfo.setMin(min);
        timeInfo.setS(s);
        timeInfo.setYmd(ymd);
        timeInfo.setY_m_d(y_m_d);
        timeInfo.setYmdString(ymdString);
        timeInfo.setHm(hm);
        timeInfo.setHmString(hmString);
        timeInfo.setWeek(Week);



        return timeInfo;

    }




}
