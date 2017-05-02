package chairyfish.test.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chairyfish on 2017/2/16.
 */

public class ReadStateInfo {
    private JSONObject stateInfo;
    private String a_name=null;
    private String code=null;
    private String date=null;
    private String date1=null;
    private String state=null;
    private String u_name=null;
    private String name=null;

    private Date time;

    public ReadStateInfo(JSONObject stateInfo){
        this.stateInfo=stateInfo;
    }

    public String getA_name() {
        try {
            a_name=stateInfo.getString("a_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a_name;
    }

    public String getCode() {
        try {
            code=stateInfo.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getDate() {
        try {
            date=stateInfo.getString("date");
            date=date.substring(0,10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getDate1() {
        try {
            date1=stateInfo.getString("date1");
            date1=date1.substring(11);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public String getState() {
        try {
            state=stateInfo.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

    public String getU_name() {
        try {
            u_name=stateInfo.getString("u_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u_name;
    }

    public Date getTime(){
        if(date==null||date1==null){
            getDate();
            getDate1();
        }
        String TIME=date+" "+date1;
        time=TimeFormat.getDateAndTime(TIME);
        return time;
    }
    public String getName() {
        try {
            name=stateInfo.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
}
