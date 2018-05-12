package user.zc.utils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 时间转换工具
 *
 * @author:Administrator
 * @create 2018-01-31 15:46
 */
public class DateUtils {

    // G 年代标志符
    //  y 年
    //  M 月
    //  d 日
    //  h 时 在上午或下午 (1~12)
    //  H 时 在一天中 (0~23)
    //  m 分
    //  s 秒
    //  S 毫秒
    //  E 星期
    //  D 一年中的第几天
    //  F 一月中第几个星期几
    //  w 一年中第几个星期
    //  W 一月中第几个星期
    //  a 上午 / 下午 标记符
    //  k 时 在一天中 (1~24)
    //  K 时 在上午或下午 (0~11)
    //  z 时区
    public static String formatMdW(Date date ){
        return format(date,"MM月dd日（E）");
    }

    public static String formatYMd(Date date ){
        return format(date,"yyyy年MM月dd日");
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     * @return String
     */
    public static String getCurrTime() {
        Date date  = new Date();
        return format(date,"yyyyMMddHHmmss");
    }

    public static String format(Date date ,String format){
        //用给定的模式和默认语言环境的日期格式符号构造 SimpleDateFormat
        SimpleDateFormat date1  = new SimpleDateFormat(format);
        String str =date1.format(date);
        return str;
    }
    public static String formatByNum(int num){
        return fullNum(num/60)+":"+fullNum(num%60);
    }
    private static String fullNum(int num){
        return (num<10?"0":"")+num;
    }

    public static Boolean isWorkDay(String date){
        Gson gson = new Gson();
        HashMap map = gson.fromJson(date,HashMap.class);
        System.out.println(map);
        Date  now  =  new Date();
        Object year = map.get((now.getYear()+1900)+"");
        if(year!=null){
            Object month = ((LinkedTreeMap)year).get((now.getMonth()+1)+"");
            if(month!=null){
                Object day = ((LinkedTreeMap)month).get(now.getDate()+"");
                if(day!=null)return true;
            }
        }
        return false;
    }

    public static Long compare(Date start ,Date end){
        return (start.getTime()-end.getTime())/1000;
    }

    public static Date getDateFromString(String string){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
