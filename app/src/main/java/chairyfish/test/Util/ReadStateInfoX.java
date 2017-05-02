package chairyfish.test.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chairyfish on 2017/4/26.
 */

public class ReadStateInfoX {
    private JSONObject stateInfo;
    private String a_name=null;
    private String c_name=null;
    private String code=null;
    private String date1=null;
    private String date2=null;
    private String state=null;

    public ReadStateInfoX(JSONObject stateInfo){
        this.stateInfo=stateInfo;
    }

    public String getA_name() {
        try {
            a_name=stateInfo.getString("aname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a_name;
    }
    public String getC_name() {
        try {
            c_name=stateInfo.getString("cname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c_name;
    }

    public String getCode() {
        try {
            code=stateInfo.getString("vcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getDate1() {
        try {
            date1=stateInfo.getString("timestamp");
            date1=date1.substring(0,10)+" "+date1.substring(11);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public String getDate2() {
        try {
            date2=stateInfo.getString("timestamp1");
            date2=date2.substring(0,10)+" "+date2.substring(11);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date2;
    }


    public Date getTime1() {
        if(date1==null){
            getDate1();
        }
       Date time1=TimeFormat.getDateAndTime(date1);
        return time1;
    }

    public Date getTime2() {
        if(date2==null){
            getDate2();
        }
        Date time2=TimeFormat.getDateAndTime(date2);
        return time2;
    }

    public String getState() {
        try {
            state=stateInfo.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }
}
