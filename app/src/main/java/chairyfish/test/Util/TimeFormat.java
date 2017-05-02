package chairyfish.test.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chairyfish on 2017/2/16.
 */

public class TimeFormat {

    public static Date getOnlyDate(String date){
        Date d=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d=formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public  static Date getOnlyTime(String date){
        Date d=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss");
        try {
            d=formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date getDateAndTime(String date){
        Date d=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d=formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  d;
    }

    public static String DateTimetoStr(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
        }
}
