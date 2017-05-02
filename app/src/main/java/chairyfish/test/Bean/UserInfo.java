package chairyfish.test.Bean;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chairyfish on 2017/2/8.
 */

public class UserInfo {
    private static String USERNAME=null;
    private static String PASSWORD=null;
    private static JSONArray CHECKSTATE=new JSONArray();

    UserInfo(){};

    public static String getUSERNAME(Context context){
        if(USERNAME==null){
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
            USERNAME = sharedPreferences.getString("username", null);
        }
        return USERNAME;
    }

    public static String getUSERNAME() {
        if(USERNAME==null){
        }
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setUSERNAME(String USERNAME) {
        UserInfo.USERNAME = USERNAME;
    }

    public static JSONArray getCHECKSTATE() {
        return CHECKSTATE;
    }

    public static void setCHECKSTATE(JSONArray CHECKSTATE) {
        UserInfo.CHECKSTATE = CHECKSTATE;
    }

    public static void setPASSWORD(String PASSWORD) {
        UserInfo.PASSWORD = PASSWORD;

    }
}
